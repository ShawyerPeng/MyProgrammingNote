# 云计算概述
云计算是一种按使用量付费的模式，这种模式提供可用的、便捷的、按需的网络访问，进入可配置的计算资源共享池(资源包括网络，服务器，存储，应用软件，服务)，这些资源能够被快速提供，只需投入很少的管理工作，或与服务供应商进行很少的交互。
* 按需自服务
* 广泛的网络访问
* 资源共享
* 快速弹性
* 服务可度量

# 云计算部署模型
公有云：由第三方云提供者拥有的可公共访问的云环境
社区云：类似于公有云，但是访问被限制于特定的云用户社区
私有云：有一家组织单独拥有的
混合云：有两个或以上组成



### 1. IaaS: Infrastructure-as-a-Service（基础设施即服务）
第一层叫做IaaS，有时候也叫做Hardware-as-a-Service，几年前如果你想在办公室或者公司的网站上运行一些企业应用，你需要去买服务器，或者别的高昂的硬件来控制本地应用，让你的业务运行起来。但是现在有IaaS，你可以将硬件外包到别的地方去。IaaS公司会提供场外服务器，存储和网络硬件，你可以租用。节省了维护成本和办公场地，公司可以在任何时候利用这些硬件来运行其应用。一些大的IaaS公司包括Amazon, Microsoft, VMWare, Rackspace和Red Hat.不过这些公司又都有自己的专长，比如Amazon和微软给你提供的不只是IaaS，他们还会将其计算能力出租给你来host你的网站。

### 2. PaaS: Platform-as-a-Service（平台即服务）
第二层就是所谓的PaaS，某些时候也叫做中间件。你公司所有的开发都可以在这一层进行，节省了时间和资源。PaaS公司在网上提供各种开发和分发应用的解决方案，比如虚拟服务器和操作系统。这节省了你在硬件上的费用，也让分散的工作室之间的合作变得更加容易。网页应用管理，应用设计，应用虚拟主机，存储，安全以及应用开发协作工具等。一些大的PaaS提供者有Google App Engine,Microsoft Azure，Force.com,Heroku，Engine Yard。最近兴起的公司有AppFog, Mendix 和 Standing Cloud









程服务器上的应用都可以通过网络来运行，就是SaaS了。你消费的服务完全是从网页如Netflix, MOG, Google Apps, Box.NET, Dropbox或者苹果的iCloud那里进入这些分类。尽管这些网页服务是用作商务和娱乐或者两者都有，但这也算是云技术的一部分。一些用作商务的SaaS应用包括Citrix的GoToMeeting，Cisco的WebEx，Salesforce的CRM，ADP，Workday和SuccessFactors。



# 优势
* 模块松耦合
* 组件配置灵活
* 二次开发容易


# 三大核心组件
Nova:Compute
Neutron:Networking
Swift:Storage



# 一、安装和配置KeyStone
### 1. 为KeyStone配置数据库
1. 使用数据库客户端，以root用户连接到数据库中：`mysql -u root -p`
2. 创建KeyStone数据库：`CREATE DATABASE keystone;`
3. 为KeyStone用户授权：  
`GRANT ALL PRIVILLEGES ON keystone.* TO 'keystone'@'localhost' IDENTIFIED BY 'KEYSTONE_DBPASS';`  
`GRANT ALL PRIVILLEGES ON keystone.* TO 'keystone'@'%' IDENTIFIED BY 'KEYSTONE_DBPASS';`

### 2. 安装KeyStone
1. KeyStone服务的监听端口是5000和35357，配置Apache HTTP服务监听这两个端口，为了避免端口冲突，在Ubuntu上禁止KeyStone开机自启动：`echo "manual" > /etc/init/keystone.override`
2. 安装与Keystone相关的软件包：  
`apt-get install keystone python-openstackclient apache2 libapache2-mod-wsgi memcached python-memcache`

### 3. 修改KeyStone的配置文件keystone.conf
修改/etc/keystone/keystone.conf  
小技巧：将原文件备份，使用命令`cat backup_file | grep -v '^#' > new_file`可以生成新文件，并去掉注释
1. 随机生成一个16进制的token：`openssl rand -hex 10`
2. 修改[default]部分，配置初始的令牌管理：`admin_token = ADMIN_TOKEN`
ADMIN_TOKEN是刚才所生成的随机值
3. 修改[database]部分，配置数据库的连接：`connection = mysql://keystone:KEYSTONE_DBPASS@controller/keystone`
4. 修改[memcache]，配置Memcache 服务:`servers = localhost:11211`  
11211是memecahced服务器的默认端口号
5. 修改[token]部分，配置UUID令牌的提供者和memcached的持久化驱动：  
`provider = keystone.token.providers.uuid.Provider`   
`driver = keystone.token.persistence.backends.memcache.Token`
6. 修改[revoke] 部分, 配置SQL的撤回驱动：`driver = keystone.contrib.revoke.backends.sql.Revoke`  
可选：为了方便做问题诊断，在[DEFAULT]部分，配置详细的日志输出：`verbose = True`
7. 为keystone数据库填充数据：`su -s /bin/sh -c "keystone-manage db_sync" keystone`

### 4. 配置 Apache HTTP server
1. 编辑/etc/apache2/apache2.conf，配置ServerName选项为控制节点hostname：`ServerName controller`
2. 创建/etc/apache2/sites-available/wsgi-keystone.conf文件，添加如下内容：
    ```xml
    Listen 5000
    Listen 35357
    
    <VirtualHost *:5000>
        WSGIDaemonProcess keystone-public processes=5 threads=1 user=keystone display-name=%{GROUP}
        WSGIProcessGroup keystone-public
        WSGIScriptAlias / /var/www/cgi-bin/keystone/main
        WSGIApplicationGroup %{GLOBAL}
        WSGIPassAuthorization On
        <IfVersion >= 2.4>
        ErrorLogFormat "%{cu}t %M"
        </IfVersion>
        LogLevel info
        ErrorLog /var/log/apache2/keystone-error.log
        CustomLog /var/log/apache2/keystone-access.log combined
    </VirtualHost>
    
    <VirtualHost *:35357>
        WSGIDaemonProcess keystone-admin processes=5 threads=1 user=keystone display-name=%{GROUP}
        WSGIProcessGroup keystone-admin
        WSGIScriptAlias / /var/www/cgi-bin/keystone/admin
        WSGIApplicationGroup %{GLOBAL}
        WSGIPassAuthorization On
        <IfVersion >= 2.4>
        ErrorLogFormat "%{cu}t %M"
        </IfVersion>
        LogLevel info
        ErrorLog /var/log/apache2/keystone-error.log
        CustomLog /var/log/apache2/keystone-access.log combined
    </VirtualHost>
    ```
3. 启用身份认证服务的虚拟主机：  
`ln -s /etc/apache2/sites-available/wsgi-keystone.conf /etc/apache2/sites-enabled`
4. 为WSGI组件创建目录结构：`mkdir -p /var/www/cgi-bin/keystone`
5. 拷贝WSGI组件到当前创建好的目录/var/www/cgi-bin/keystone下：  
`curl http://git.openstack.org/cgit/openstack/keystone/plain/httpd/keystone.py?h=stable/kilo | tee /var/www/cgi-bin/keystone/main /var/www/cgi-bin/keystone/admin`
6. 设置目录和文件的权限：  
`chown -R keystone:keystone /var/www/cgi-bin/keystone`  
`chmod 755 /var/www/cgi-bin/keystone/*`
7. 重启apache http server：`service apache2 restart`
8. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/keystone/keystone.sqlite`

# 二、配置服务实体与API端点
### 1. 配置认证服务的服务实体
1. 设置操作系统临时环境变量：校验令牌：`export OS_TOKEN=ADMIN_TOKEN`
2. 设置操作系统临时环境变量：端点URL：`export OS_URL=http://controller:35357/v2.0`
3. 为认证服务创建服务实体：  
`openstack service create --name keystone --description "OpenStack Identity" identity`
4. 配置认证服务的API端点：  
`openstack endpoint create --publicurl http://controller:5000/v2.0 --internalurl http://controller:5000/v2.0 --adminurl http://controller:35357/v2.0 --region RegionOne identity`

# 三、创建项目（租户）、用户和角色
1. 创建admin租户：`openstack project create --description "Admin Project" admin`
2. 创建admin用户：`openstack user create --password-prompt admin`
3. 创建admin角色：`openstack role create admin`
4. 添加admin角色到admin租户和用户：`openstack role add --project admin --user admin admin`

# 四、创建服务项目
1. 位其他的OpenStack服务创建服务项目：`openstack project create --description "Service Project" service`

# 五、创建普通项目和用户
1. 创建demo项目：`openstack project create --description "Demo Project" demo`
2. 创建demo用户：`openstack user create --password-prompt demo`
3. 创建demo角色：`openstack role create user`
4. 添加user角色到demo租户和用户：`openstack role add --project demo --user demo user`

# 六、校验安装
### 1. 校验安装前的准备
1. 基于安全的原因，先临时禁止校验令牌的机制。编辑/etc/keystone/keystone-paste.ini：  
移除admin_token_auth从[pipeline:public_api], [pipeline:admin_api], [pipeline:api_v3]部分
2. 取消设置的操作系统环境变量：`unset OS_TOKEN OS_URL`

### 2. 使用admin用户校验安装
1. 以admin用户，从2.0版本的认证API申请一个校验令牌：  
`openstack --os-auth-url http://controller:35357 --os-project-name admin --os-username admin --os-auth-type password token issue`  
3.0版本的认证API支持域名，以admin用户申请一个校验令牌：  
`openstack --os-auth-url http://controller:35357 --os-project-domain-id default --os-user-domain-id default --os-project-name admin --os-username admin --os-auth-type password token issue`
2. 以admin用户，校验admin用户是否有权限云查看认证服务中所包含的项目：  
`openstack --os-auth-url http://controller:35357 --os-project-name admin --os-username admin --os-auth-type password project list`
3. 以admin用户，查看认证服务中的用户是否创建成功：   
`openstack --os-auth-url http://controller:35357 --os-project-name admin --os-username admin --os-auth-type password user list`
4. 以admin用户，查看认证服务中的角色是否创建成功：  
`openstack --os-auth-url http://controller:35357 --os-project-name admin --os-username admin --os-auth-type password role list`

### 3. 使用demo用户校验安装
1. 以demo用户，从v3版本的认证API中申请校验令牌：  
`openstack --os-auth-url http://controller:5000 --os-project-domain-id default --os-user-domain-id default --os-project-name demo --os-username demo --os-auth-type password token issue`
2. 以demo用户，尝试能否执行只有管理用户才能执行的查看用户的操作：  
`openstack --os-auth-url http://controller:5000 --os-project-domain-id default --os-user-domain-id default --os-project-name demo --os-username demo --os-auth-type password user list`

# 七、创建OpenStack客户端脚本
### 1. 创建admin用户的脚本
创建和编辑文件admin-openrc.sh，加入如下内容：
```
export OS_PROJECT_DOMAIN_ID=default
export OS_USER_DOMAIN_ID=default
export OS_PROJECT_NAME=admin
export OS_TENANT_NAME=admin
export OS_USERNAME=admin
export OS_PASSWORD=ADMIN_PASS
export OS_AUTH_URL=http://controller:35357/v3
```

### 2. 创建demo用户的脚本
创建和编辑文件demo-openrc.sh，加入如下内容：
```
export OS_PROJECT_DOMAIN_ID=default
export OS_USER_DOMAIN_ID=default
export OS_PROJECT_NAME=demo
export OS_TENANT_NAME=demo
export OS_USERNAME=demo
export OS_PASSWORD=DEMO_PASS
export OS_AUTH_URL=http://controller:5000/v3
```
### 3. 测试脚本
1. 加载脚本：`source admin-openrc.sh`
2. 测试脚本：`openstack token issue`






# Glance

# 一、安装Glance前的准备
### 1. 为Glance创建数据库
1. 使用数据库客户端，以root用户身份连接到数据库中：`mysql -u root -p`
2. 创建Glance数据库：`CREATE DATABASE glance;`
3. 为Glance用户授予数据库权限：  
`GRANT ALL PRIVILEGES ON glance.* TO 'glance'@'localhost' IDENTIFIED BY 'GLANCE_DBPASS';`  
`GRANT ALL PRIVILEGES ON glance.* TO 'glance'@'%' IDENTIFIED BY 'GLANCE_DBPASS';`

### 2. 创建Glance的身份认证证书
1. 加载admin用户的客户端脚本：`source admin-openrc.sh`
2. 创建glance用户：`openstack user create --password-prompt glance`
3. 将admin角色添加给glance用户和service项目：`openstack role add --project service --user glance admin`
4. 创建glance的服务实体：  
`openstack service create --name glance --description "OpenStack Image service" image`
5. 创建镜像服务的API endpoint：  
`openstack endpoint creat --publicurl http://controller:9292 --internalurl http://controller:9292 --adminurl http://controller:9292 --region RegionOne image`

# 二、安装和配置Glance
### 1. 安装Glance
在Controller节点上进行安装
1. 安装Glance：`apt-get install -y glance python-glanceclient`

### 2. 配置Glance-api
编辑文件/etc/glance/glance-api.conf  
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`
`connection = mysql://glance:GLANCE_DBPASS@controller/glance`
1. 修改[database]部分，配置数据库的连接：`connection = mysql://glance:GLANCE_DBPASS@controller/glance`
2. 修改[keystone_authtoken]和[paste_deploy]两部分, 配置身份认证服务访问：
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = glance
    password = GLANCE_PASS
    ```
    ```
    flavor = keystone
    ```

3. 修改[keystone_store]部分,配置镜像存储采用文件的形式，并且指定存储的路径：
    ```
    default_store = file
    filesystem_store_datadir = /var/lib/glance/images/
    ```

4. 在[DEFAULT]部分，配置noop禁用通知驱动，因为这是为telemetry测量服务保留的：
    ```
    notification_driver = noop
    ```

5. 在[DEFAULT]部分启用日志详细信息记录：
    ```
    verbose = True
    ```

### 3. 配置Glance-registry
编辑文件/etc/glance/glance-registry.conf  
1. 修改[database]部分，配置数据库的连接：`connection = mysql://glance:GLANCE_DBPASS@controller/glance`
2. 修改[keystone_authtoken]和[paste_deploy]两部分, 配置身份认证服务访问:
```
auth_uri = http://controller:5000
auth_url = http://controller:35357
auth_plugin = password
project_domain_id = default
user_domain_id = default
project_name = service
username = glance
password = GLANCE_PASS
```

3. 在[DEFAULT]部分，配置noop禁用通知驱动，因为这是为telemetry测量服务保留的：
```
notification_driver = noop
```

4. 在[DEFAULT]部分启用日志详细信息记录：
```
verbose = True
```

### 4. 配置Glance数据库
1. 为镜像服务数据库添加数据：`su -s /bin/sh -c "glance-manage db_sync" glance`
2. 重启镜像服务Glance：`service glance-registry restart` `service glance-api restart`
3. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/glance/glance.sqlite`

# 三、校验Glance安装
使用CirrOS镜像做测试，因为CirrOS非常小，所以常被用来做测试
1. 编辑admin和demo的脚本文件，添加新的操作系统环境变量：  
`echo "export OS_IMAGE_API_VERSION=2" | tee -a admin-openrc.sh demo-openrc.sh`
2. 加载admin客户端脚本：`source admin-openrc.sh`
3. 创建临时目录：`mkdir /tmp/images`
4. 下载测试镜像到该目录：  
`wget -P /tmp/images http://download.cirros-cloud.net/0.3.4/cirros-0.3.4-x86_64-disk.img`
5. 使用QCOW2的磁盘格式和bare的容器格式将镜像上传到glance镜像服务中，并且设置为对所有的项目可见：  
`glance image-create --name "cirros-0.3.4-x86_64" --file /tmp/images/cirros-0.3.4-x86_64-disk.img --disk-format qcow2 --container-format bare --visibility public --progress`
6. 验证镜像是否上传成功以及查看属性：`glance image-list`
7. 可选：删除测试镜像目录：`rm -r /tmp/images`















# Nova

# 一、安装 Nova 前的准备
### 1. 为KeyStone配置数据库
提示：以下操作在控制节点完成，为计算服务创建数据库、服务认证和API端点
1. 使用数据库客户端，以root用户连接到数据库中：`mysql -u root -p`
2. 创建Nova数据库：`CREATE DATABASE nova;`
3. 为Nova用户授予数据库权限：  
`GRANT ALL PRIVILLEGES ON nova.* TO 'nova'@'localhost' IDENTIFIED BY 'NOVA_DBPASS';`  
`GRANT ALL PRIVILLEGES ON nova.* TO 'nova'@'%' IDENTIFIED BY 'NOVA_DBPASS';`

### 2. 创建Nova的身份认证证书
1. 加载admin用户的客户端脚本：`source admin-openrc.sh`
2. 创建nova用户：`openstack user create --password-prompt nova`
3. 将admin角色添加给nova用户：`openstack role add --project service --user nova admin`
4. 创建nova的服务实体：  
`openstack service create --name nova --description "OpenStack Compute" compute`
5. 创建计算服务的API endpoint：  
 `openstack endpoint create --publicurl http://controller:8774/v2/%\(tenant_id\)s --internalurl http://controller:8774/v2/%\(tenant_id\)s --adminurl http://controller:8774/v2/%\(tenant_id\)s --region RegionOne compute`

# 二、在控制节点上安装和配置Nova
### 1. 安装Nova
1. 安装Nova：`apt-get install -y nova-api nova-cert nova-conductor nova-consoleauth nova-novncproxy nova-scheduler python-novaclient`

### 2. 配置Nova
编辑文件/etc/nova/nova.conf  
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`
1. 修改[database]部分，配置数据库的连接：`connection = mysql://nova:NOVA_DBPASS@controller/nova`  
记得密码替换为自己设置密码,这是mysql的密码，并非nova用户的密码
2. 修改[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

3. 修改[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = nova
    password = NOVA_PASS
    ```

4. 修改[DEFAULT]部分的my_ip参数，配置控制节点的管理IP地址：
    ```
    my_ip = 10.0.0.11
    ```

5. 修改[DEFAULT]部分，配置VNC代理以使用控制节点的管理IP地址：
    ```
    vncserver_listen = 10.0.0.11
    vncserver_proxyclient_address = 10.0.0.11
    ```

6. 修改[glance]部分，配置镜像服务的位置：
    ```
    host = controller
    ```

7. 修改[oslo_concurrency]部分，配置锁路径：
    ```
    lock_path = /var/lock/nova
    ```

8. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 3. 配置Nova数据库
1. 为计算服务数据库添加数据：`su -s /bin/sh -c "nova-manage db_sync" nova`
2. 重启计算服务Nova：
    ```
    service nova-api restart
    service nova-cert restart
    service nova-consoleauth restart
    service nova-scheduler restart
    service nova-conductor restart
    service nova-novncproxy restart
    ```
3. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/nova/nova.sqlite`



# 三、在计算节点上安装和配置Nova
### 1. 安装Nova
1. 安装Nova：`apt-get install -y nova-compute sysfsutils`

### 2. 配置Nova
编辑文件/etc/nova/nova.conf  
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`
1. 修改[database]部分，配置数据库的连接：`connection = mysql://nova:NOVA_DBPASS@controller/nova`  
记得密码替换为自己设置密码,这是mysql的密码，并非nova用户的密码
2. 修改[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

3. 修改[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = nova
    password = NOVA_PASS
    ```

4. 修改[DEFAULT]部分的my_ip参数，配置控制节点的管理IP地址：
    ```
    my_ip = MANAGEMENT_INTERFACE_IP_ADDRESS
    ```

5. 修改[DEFAULT]部分，配置VNC代理以启用远程终端的访问：
    ```
    vnc_enabled = True
    vncserver_listen = 0.0.0.0
    vncserver_proxyclient_address = MANAGEMENT_INTERFACE_IP_ADDRESS
    novncproxy_base_url = http://controller:6080/vnc_auto.html
    ```

6. 修改[glance]部分，配置镜像服务的位置：
    ```
    host = controller
    ```

7. 修改[oslo_concurrency]部分，配置锁路径：
    ```
    lock_path = /var/lock/nova
    ```

8. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 3. 完成在计算节点上安装和配置Nova
1. 检查计算节点是否支持虚拟机的硬件加速：`egrep -c '(vmx|svm)' /proc/cpuinfo`  
如果输出值是1或则比这更大，则不需要额外配置  
如果是0，计算节点不支持硬件加速，你必须配置libvirt为QEMU，代替KVM  
2. 修改文件/etc/nova/nova-compute.conf下的[libvirt]部分：
    ```
    virt_type = qemu
    ```
3. 重启计算服务nova：`service nova-compute restart`
4. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/nova/nova.sqlite`

# 四、校验安装
提示：在控制节点上进行校验
1. 加载admin客户端脚本：`source admin-openrc.sh`
2. 检查计算服务的服务组件是否成功地启动和注册：`nova service-list`
```
+----+------------------+------------+----------+---------+-------+----------------------------+-----------------+
| Id | Binary           | Host       | Zone     | Status  | State | Updated_at                 | Disabled Reason |
+----+------------------+------------+----------+---------+-------+----------------------------+-----------------+
| 1  | nova-conductor   | controller | internal | enabled | up    | 2014-09-16T23:54:02.000000 | -               |
| 2  | nova-consoleauth | controller | internal | enabled | up    | 2014-09-16T23:54:04.000000 | -               |
| 3  | nova-scheduler   | controller | internal | enabled | up    | 2014-09-16T23:54:07.000000 | -               |
| 4  | nova-cert        | controller | internal | enabled | up    | 2014-09-16T23:54:00.000000 | -               |
| 5  | nova-compute     | compute1   | nova     | enabled | up    | 2014-09-16T23:54:06.000000 | -               |
+----+------------------+------------+----------+---------+-------+----------------------------+-----------------+
```
3. 在身份认证服务中查看API端点信息以验证是否能够连接到OpenStack的认证服务：`nova endpoints`
4. 通过在镜像服务中查看镜像信息以验证是否能够连接镜像服务：`nova image-list`












# DashBoard

# 一、安装和配置DashBoard
### 1. 安装DashBoard
注意：在控制节点上进行操作
1. 安装dashboard：`apt-get install openstack-dashboard`

### 2. 配置DashBoard
1. 编辑/etc/openstack-dashboard/local_settings.py：  
`sudo nano  /etc/openstack-dashboard/local_settings.py`
2. 配置dashboard使用控制节点上的OpenStack服务：`OPENSTACK_HOST = "controller"`
3. 允许任何主机访问dashboard：`ALLOWED_HOSTS = '*'`
4. 配置memcached会话存储服务，注意把其他的会话存储服务都注释掉
    ```
    CACHES = {
    'default': {
        'BACKEND': 'django.core.cache.backends.memcached.MemcachedCache',
        'LOCATION': '127.0.0.1:11211',
    }
    }
    ```

5. 配置在DashBoard中创建的用户默认所使用的角色，建议使用user角色：  
`OPENSTACK_KEYSTONE_DEFAULT_ROLE = "user"`
6. 重新加载web服务器的配置：`service apache2 reload`

### 3. 校验DashBoard的安装
1. 通过谷歌浏览器访问DashBoard：`http://controller/horizon`或`http://10.0.0.11/horizon`
2. 使用admin或则demo用户登录：


# 二、DashBoard的使用
### 1. 会话缓存
Horizon可以使用不同的方式来缓存会话信息：`Local Memory Cache` `Memcached` `Database`

### 2. 自定义DashBoard
更改logo：
![](http://p1.bpimg.com/567571/0054a8a055330d83.png)

CSS样式：
![](http://p1.bpimg.com/567571/4148725bd3f7130c.png)

HTML title：
![](http://p1.bpimg.com/567571/530015abe47757cd.png)

站点条幅的链接：
![](http://i1.piimg.com/567571/3de717de7f6f218b.png)

帮助链接：
![](http://p1.bpimg.com/567571/12eaca585b8baf94.png)

# 三、创建第一个实例
### 1. 创建密钥对
注意：在控制节点上完成操作
镜像的校验一般采用的是公钥校验的方式而不是用户名和密码的方式
1. 加载demo用户的证书：`source demo-openrc.sh`
2. 产生密钥对：`nova keypair-add demo-key`
3. 检查密钥对是否添加成功：`nova keypair-list`

### 2. 创建实例
注意：创建一个实例，必须要指定实例类型、镜像名、网络、安全组、密钥和实例名
1. 查看可用的实例类型：`nova flavor-list`
2. 查看可用的镜像：`nova image-list`
3. 查看可用的网络：`neutron net-list`
4. 查看可用的安全组：`nova secgroup-list`
5. 创建实例：  
`nova boot --flavor m1.tiny --image cirros-0.3.4-x86_64 --nic net-id=DEMO_NET_ID --security-group default --key-name demo-key demo-instance1`
6. 查看实例状态：`nova list`
7. 查看访问实例的VNC会话URL：`nova get-vnc-console demo-instance1 novnc`
8. 在浏览器中访问实例：把`http://controller:6080/vnc_auto.html?token=xxx` 输入浏览器即可看到实例












# Neutron

# 一、安装Neutron前的准备
### 1. 为Neutron配置数据库
1. 使用数据库客户端，以root用户连接到数据库中：`mysql -u root -p`
2. 创建Neutron数据库：`CREATE DATABASE neutron;`
3. 为Neutron用户授予数据库权限：  
`GRANT ALL PRIVILEGES ON neutron.* TO 'neutron'@'localhost' IDENTIFIED BY 'NEUTRON_DBPASS';`  
`GRANT ALL PRIVILEGES ON neutron.* TO 'neutron'@'%' IDENTIFIED BY 'NEUTRON_DBPASS';`

### 2. 创建Neutron的身份认证证书
1. 加载admin用户的客户端脚本：`source admin-openrc.sh`
2. 创建neutron用户：`openstack user create --password-prompt neutron`
3. 将admin角色添加给neutron用户：`openstack role add --project service --user neutron admin`
4. 创建neutron的服务实体：  
`openstack service create --name neutron --description "OpenStack Networking" network`
5. 创建网络服务的API endpoint：  
`openstack endpoint create --publicurl http://controller:9696 --adminurl http://controller:9696 --internalurl http://controller:9696 --region regionOne network`

# 二、在控制节点上安装和配置Neutron
### 1. 安装Neutron
1. 安装Neutron：`apt-get install neutron-server neutron-plugin-ml2 python-neutronclient`

### 2. 配置Neutron
需要配置：数据库、校验机制、消息队列、拓扑更改通知、插件

编辑Neutron的配置文件：/etc/neutron/neutron.conf  
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`

1. 修改[database]部分，配置数据库的连接：`connection = mysql://neutron:NEUTRON_DBPASS@controller/neutron`  
记得密码替换为自己设置密码,这是mysql的密码，并非nova用户的密码
2. 修改[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

3. 修改[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = nova
    password = NOVA_PASS
    ```

4. 修改[DEFAULT]部分，启用ML2插件、路由服务和IP地址重叠：
    ```
    core_plugin = ml2
    service_plugins = router
    allow_overlapping_ips = True
    ```

5. 修改[DEFAULT]和[nova]部分，配置网络拓扑结构更改的通知：
    ```
    notify_nova_on_port_status_changes = True
    notify_nova_on_port_data_changes = True
    nova_url = http://controller:8774/v2
    nova_admin_auth_url = http://controller:35357/v2.0
    nova_region_name = regionOne
    nova_admin_username = nova
    nova_admin_tenant_id = SERVICE_TENANT_ID
    nova_admin_password = NOVA_PASS
    ```
    ```
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    region_name = RegionOne
    project_name = service
    username = nova
    password = NOVA_PASS
    ```

6. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 3. 配置ML2插件
编辑配置文件：/etc/neutron/plugins/ml2/ml2_conf.ini
1. 在[ml2]部分，配置启用flat和generic routing encapsulation(GRE)网络类型驱动，GRE租户网络类型和OVS机制驱动：
    ```
    type_drivers = flat,vlan,gre,vxlan
    tenant_network_types = gre
    mechanism_drivers = openvswitch
    ```
2. 在[ml2_type_gre]部分,配置tunnel identifier(id)范围：
    ```
    tunnel_id_ranges = 1:1000
    ```
3. 在[securitygroup]部分，启用安全组、ipset和配置OVS iptables的防火墙驱动：
    ```
    enable_security_group = True
    enable_ipset = True
    firewall_driver = neutron.agent.linux.iptables_firewall.OVSHybridIptablesFirewallDriver
    ```

### 4. 配置计算服务的网络类型
默认情况下openstack配置计算服务使用传统网络类型(legacy networking)，所以需要重新配置计算服务使用neutron来管理网络服务
注意：在控制节点上进行配置
1. 在[DEFAULT]部分，配置APIs和驱动：
    ```
    network_api_class = nova.network.neutronv2.api.API
    security_group_api = neutron
    linuxnet_interface_driver = nova.network.linux_net.LinuxOVSInterfaceDriver
    firewall_driver = nova.virt.firewall.NoopFirewallDriver
    ```

2. 在[neutron]部分，配置访问参数：
    ```
    url = http://controller:9696
    auth_strategy = keystone
    admin_auth_url = http://controller:35357/v2.0
    admin_tenant_name = service
    admin_username = neutron
    admin_password = NEUTRON_PASS
    ```

### 5. 完成安装 
1. 产生数据库：  
`su -s /bin/sh -c "neutron-db-manage --config-file /etc/neutron/neutron.conf --config-file /etc/neutron/plugins/ml2/ml2_conf.ini upgrade head" neutron`
2. 重启计算服务：  
`service nova-api restart`    
`service nova-scheduler restart`    
`service nova-conductor restart`    
3. 重启网络服务：`service nova-server restart`

### 6. 校验安装
提示：在控制节点上进行校验
1. 加载admin客户端脚本：`source admin-openrc.sh`
2. 查看加载的扩展以校验是否可以成功地发起neutron-server进程：`neutron ext-list`




# 三、在网络节点上安装和配置Neutron
### 1. 调整网络节点的网络调优参数
1. 编辑/etc/sysctl.conf文件，编辑一些网络调优参数：
    ```
    net.ipv4.ip_forward=1
    net.ipv4.conf.all.rp_filter=0
    net.ipv4.conf.default.rp_filter=0
    ```
2. 使更改立刻生效：`sysctl -p`

### 2. 安装Neutron
1. 在网络节点上安装Neutron：  
`apt-get install neutron-plugin-ml2 neutron-plugin-openvswitch-agent neutron-l3-agent neutron-dhcp-agent neutron-metadata-agent`

### 3. 配置网络服务的通用组件
网络服务的通用组件包括：校验机制、消息队列和插件
1. 编辑文件/etc/neutron/neutron.conf进行配置
2. 在[database]部分，注释掉所有连接数据库的参数，因为网络节点不能直接访问数据库
3. 在[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

4. 在[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = neutron
    password = NEUTRON_PASS
    ```

5. 在[DEFAULT]部分，配置ML2插件、路由服务和IP地址的重叠：
    ```
    core_plugin = ml2
    service_plugins = router
    allow_overlapping_ips = True
    ```

6. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 4. 配置ML2插件
修改文件：/etc/neutron/plugins/ml2/ml2_conf.ini
1. 在[ml2]部分，配置启用flat,VLAN,GRE和VXLAN网络类型驱动，GRE租户网络类型和OVS机制驱动：
    ```
    type_drivers = flat,vlan,gre,vxlan
    tenant_network_types = gre
    mechanism_drivers = openvswitch
    ```

2. 在[ml2_type_flat]部分,配置外部的flat网络：
    ```
    flat_networks = external
    ```

3. 在[ml2_type_gre]部分,配置tunnel identifier(id)范围：
    ```
    tunnel_id_ranges = 1:1000
    ```

4. 在[securitygroup]部分，启用安全组、ipset和配置OVS iptables的防火墙驱动：
    ```
    enable_security_group = True
    enable_ipset = True
    firewall_driver = neutron.agent.linux.iptables_firewall.OVSHybridIptablesFirewallDriver
    ```

5. 在[ovs]部分，启用隧道，配置本地隧道端点和将外部的flat网络映射到br-ex外部网络桥接：
    ```
    local_ip = INSTANCE_TUNNELS_INTERFACE_IP_ADDRESS(网络节点上隧道网络的IP地址10.0.1.21)
    enable_tunneling = True
    bridge_mappings = external:br-ex
    ```

6. 在[agent]部分，启用GRE隧道：
    ```
    tunnel_types = gre
    ```

### 5. 配置L3代理
编辑文件：/etc/neutron/l3_agent.ini
1. 在[DEFAULT]部分，配置接口驱动，配置外部网络桥接和失效路由命名空间的清除：
    ```
    interface_driver = neutron.agent.linux.interface.OVSInterfaceDriver
    external_network_bridge =        #这部分为空，目的是在一个代理上启用多个外部网络
    dhcp_driver = neutron.agent.linux.dhcp.Dnsmasq
    use_namespaces = True
    dhcp_delete_namespaces = True
    ```

### 6. 配置DHCP代理
编辑文件：/etc/neutron/dhcp_agent.ini
1. 在[DEFAULT]部分，配置接口和DHCP驱动，启用命名空间和启用删除废弃的命名空间:
    ```
    interface_driver = neutron.agent.linux.interface.OVSInterfaceDriver
    dhcp_driver = neutron.agent.linux.dhcp.Dnsmasq
    use_namespaces = True
    dhcp_delete_namespaces = True
    ```

### 7. 配置metadata代理
主要配置实验的连接认证信息  
编辑文件：/etc/neutron/metadata_agent.ini
1. 在[DEFAULT]部分，配置metadata的主机为控制节点：
    ```
    nova_metadata_ip = controller
    ```

2. 在[DEFAULT]部分，配置metadata代理共享密码：
    ```
    metadata_proxy_shared_secret = METADATA_SECRET
    ```

3. 在[DEFAULT] 部分，配置访问参数：
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_region = regionOne
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = neutron
    password = NEUTRON_PASS
    ```

### 8. 配置metadata代理(注意！！！在控制节点上完成)
编辑文件：/etc/nova/nova.conf
1. 在[neutron]部分，启动metadata代理和配置密码：
    ```
    service_metadata_proxy = True
    metadata_proxy_shared_secret = METADATA_SECRET
    ```
2. 在控制节点重启api服务：
    ```
    service nova-api restart
    ```

### 9. 配置Open vSwitch(OVS)服务
1. 重启OVS服务：`service openvswitch-switch restart`
2. 添加额外的桥接：`ovs-vsctl add-br br-ex`
3. 添加一个外部网桥的端口用于连接外部网络的物理网卡：`ovs-vsctl add-port br-ex eth2`

### 10. 完成安装
1. 重启服务：
    ```
    service neutron-plugin-openvswitch-agent restart
    service neutron-l3-agent restart
    service neutron-dhcp-agent restart
    service neutron-metadata-agent restart
    ```

### 11. 校验安装
1. 加载admin客户端脚本（在控制节点上！）：`source admin-openrc.sh`
2. 查看neytron的代理，以确定是否可以正确地加载这些代理，应当有四个代理（在控制节点上！）：`neutron agent-list`
    ```
    +--------------------------------------+--------------------+---------+-------+----------------+---------------------------+
    | id                                   | agent_type         | host    | alive | admin_state_up | binary                    |
    +--------------------------------------+--------------------+---------+-------+----------------+---------------------------+
    | 051543d3-4be3-45b9-a9a2-5bbd3e89a47c | Open vSwitch agent | network | :-)   | True           | neutron-openvswitch-agent |
    | 16bd7da4-b76e-4fbd-9e5f-92b52a8c70a5 | DHCP agent         | network | :-)   | True           | neutron-dhcp-agent        |
    | 2882756c-e965-4070-a105-9b408ef1cebc | L3 agent           | network | :-)   | True           | neutron-l3-agent          |
    | 5c4678e6-4096-44fe-95ad-5c4c26f2cc43 | Metadata agent     | network | :-)   | True           | neutron-metadata-agent    |
    +--------------------------------------+--------------------+---------+-------+----------------+---------------------------+
    ```









# 四、在计算节点上安装和配置Neutron
### 1. 调整计算节点的网络调优参数
1. 编辑/etc/sysctl.conf文件，编辑一些网络调优参数：
    ```
    net.ipv4.conf.all.rp_filter=0
    net.ipv4.conf.default.rp_filter=0
    ```
2. 使更改立刻生效：`sysctl -p`

### 2. 在计算节点上安装Neutron
1. 在网络节点上安装Neutron：  
`apt-get install neutron-plugin-ml2 neutron-plugin-openvswitch-agent neutron-l3-agent neutron-dhcp-agent neutron-metadata-agent`

### 3. 配置网络服务的通用组件
网络服务的通用组件包括：校验机制、消息队列和插件
1. 编辑文件/etc/neutron/neutron.conf进行配置
2. 在[database]部分，注释掉所有连接数据库的参数，因为网络节点不能直接访问数据库
3. 在[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

4. 在[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = neutron
    password = NEUTRON_PASS
    ```

5. 在[DEFAULT]部分，配置ML2插件、路由服务和IP地址的重叠：
    ```
    core_plugin = ml2
    service_plugins = router
    allow_overlapping_ips = True
    ```

6. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 4. 配置ML2插件
修改文件：/etc/neutron/plugins/ml2/ml2_conf.ini
1. 在[ml2]部分，配置启用flat,VLAN,GRE和VXLAN网络类型驱动，GRE租户网络类型和OVS机制驱动：
    ```
    type_drivers = flat,vlan,gre,vxlan
    tenant_network_types = gre
    mechanism_drivers = openvswitch
    ```

2. 在[ml2_type_gre]部分,配置tunnel identifier(id)范围：
    ```
    tunnel_id_ranges = 1:1000
    ```

3. 在[securitygroup]部分，启用安全组、ipset和配置OVS iptables的防火墙驱动：
    ```
    enable_security_group = True
    enable_ipset = True
    firewall_driver = neutron.agent.linux.iptables_firewall.OVSHybridIptablesFirewallDriver
    ```

4. 在[ovs]部分，启用隧道，配置本地隧道端点和将外部的flat网络映射到br-ex外部网络桥接：
    ```
    local_ip = INSTANCE_TUNNELS_INTERFACE_IP_ADDRESS(计算节点上隧道网络的IP地址10.0.1.31)
    enable_tunneling = True
    bridge_mappings = external:br-ex
    ```

5. 在[agent]部分，启用GRE隧道：
    ```
    tunnel_types = gre
    ```



### 5. 配置Open vSwitch(OVS)服务
1. 重启OVS服务：`service openvswitch-switch restart`

### 6. 配置计算服务使用网络服务
默认情况下openstack配置计算服务使用传统网络类型(legacy networking)，所以需重新配置计算服务使用neutron来管理网络服务  
编辑文件：/etc/nova/nova.conf  
1. 在[DEFAULT]部分，配置APIs和驱动：
    ```
    network_api_class = nova.network.neutronv2.api.API
    security_group_api = neutron
    linuxnet_interface_driver = nova.network.linux_net.LinuxOVSInterfaceDriver
    firewall_driver = nova.virt.firewall.NoopFirewallDriver
    ```

2. 在[neutron] 部分，配置访问参数
    ```
    url = http://controller:9696
    auth_strategy = keystone
    admin_auth_url = http://controller:35357/v2.0
    admin_tenant_name = service
    admin_username = neutron
    admin_password = NEUTRON_PASS
    ```

### 7. 完成安装
1. 重启计算服务：`service nova-compute restart`
2. 重启Open vSwitch(OVS)代理：`service neutron-plugin-openvswitch-agent restart`

### 8. 校验安装
注意：在控制节点上进行校验
1. 加载admin客户端脚本：`source admin-openrc.sh`
2. 查看neutron的代理，以确定是否可以正确地加载这些代理，输出中应当有四个代理在网络节点中，在计算节点上有一个代理：
  `neutron agent-list`


# 五、创建第一个网络
### 1. 创建外部网络（在控制节点上）
创建一个外部网络，用于外网访问实例
1. 加载admin客户端脚本：`source admin-openrc.sh`
2. 创建外部网络：  
`neutron net-create ext-net --router:external True --provider:physical_network external --provider:network_type flat`
3. 在外部网络上创建一个子网：  
`neutron subnet-create ext-net --name ext-subnet --allocation-pool start=FLOATING_IP_START,end=FLOATING_IP_END --disable-dhcp --gateway EXTERNAL_NETWORK_GATEWAY EXTERNAL_NETWORK_CIDR`

### 2. 创建租户网络（在控制节点上）
1. 在租户网络上创建子网：  
`neutron subnet-create demo-net --name demo-subnet --gateway TENANT_NETWORK_GATEWAY TENANT_NETWORK_CIDR`

### 3. 在租户网络上创建路由
在租户网络上创建路由，并将外部网络和租户网络添加到路由上
1. 创建路由：`neutron router-create demo-router`
2. 添加路由到demo租户的子网：`neutron router-interface-add demo-router demo-subnet`
3. 添加路由到外部网络：`neutron router-gateway-set demo-router ext-net`

### 4. 校验安装
用外部网络的一台主机，测试是否能ping通租户的路由网关：`ping -c 4 203.0.113.101`












# Swift
# 一、安装和配置对象存储服务Swift
在控制节点完成
### 1. 创建Swift的身份认证证书
1. 加载admin客户端脚本：`source admin-openrc.sh`
2. 创建swift用户：`openstack user create --password-prompt swift`
3. 将admin角色添加给swift用户：`openstack role add --project service --user swift admin`

### 2. 创建Swift的服务实体
1. 创建swift的服务实体：  
`openstack service create --name swift --description "OpenStack Object Storage" object-store`

### 3. 创建Swift的API端点
1. 创建swift的API endpoint：  
`openstack endpoint create --publicurl 'http://controller:8080/v1/AUTH_%(tenant_id)s' --internalurl 'http://controller:8080/v1/AUTH_%(tenant_id)s' --adminurl 'http://controller:8080' --region RegionOne object-store`


# 二、在控制节点上安装和配置Swift
### 1. 安装Swift
1. 安装Swift：  
`apt-get install swift swift-proxy python-swiftclient python-keystoneclient python-keystonemiddleware memcached`

### 2. 配置Swift
1. 创建/etc/swift目录：`mkdir /etc/swift`
2. 进入该目录：`cd /etc/swift`
3. 从对象存储服务源库获取代理服务配置文件：  
`curl -o /etc/swift/proxy-server.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/proxy-server.conf-sample?h=stable/kilo`
4. 编辑文件：/etc/swift/proxy-server.conf
5. 修改[DEFAULT]部分，配置绑定端口，用户和配置文件存放的路径：
    ```
    bind_port = 8080
    user = swift
    swift_dir = /etc/swift
    ```
6. 在[pipeline:main]部分，启用相应的模块：
    ```
    pipeline = catch_errors gatekeeper healthcheck proxy-logging cache container_sync bulk ratelimit authtoken keystoneauth container-quotas account-quotas slo dlo proxy-logging proxy-server
    ```
7. 在[app:proxy-server]部分，启用账户自动创建的功能：
    ```
    account_autocreate = true
    ```
8. 在[filter:keystoneauth]部分，配置操作的角色
    ```
    use = egg:swift#keystoneauth
    operator_roles = admin,user
    ```
9. 在[filter:authtoken]部分，配置身份认证服务的访问，注意要注释掉其他的内容：
    ```
    paste.filter_factory = keystonemiddleware.auth_token:filter_factory
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = swift
    password = SWIFT_PASS
    delay_auth_decision = true
    ```
10. 在[filter:cache]部分，配置memcached位置：
    ```
    memcache_servers = 127.0.0.1:11211
    ```




# 三、在对象存储节点上安装和配置Swift
### 1. 配置对象存储的操作系统环境
1. 配置管理接口：待补充...........................................
2. 配置主机名：待补充...........................................
3. 将两台主机的/etc/hosts内容同步到其他所有节点：
4. 为两台主机分别添加新的磁盘，并创建分区：`fdisk /dev/sdb` `fdisk /dev/sdc`
5. 在两个节点上分别安装工具包：`apt-get install xfsprogs rsync`
6. 格式化/dev/sdb1和/dev/sdc1位XFS：`mkfs.xfs /dev/sdb1` `mkfs.xfs /dev/sdc1`
7. 创建目录挂载点：`mkdir -p /srv/node/sdb1` `mkdir -p /srv/node/sdc1`
8. 编辑/etc/fstab，添加这两个内容：  
`/dev/sdb1 /srv/node/sdb1 xfs noatime,nodiratime,nobarrier,logbufs=8 0 2`
`/dev/sdc1 /srv/node/sdc1 xfs noatime,nodiratime,nobarrier,logbufs=8 0 2`
9. 挂载设备：`mount /srv/node/sdb1` `mount /srv/node/sdc1`
10. 编辑/etc/rsyncd.conf，添加如下内容：
    ```
    uid = swift
    gid = swift
    log file = /var/log/rsyncd.log
    pid file = /var/run/rsyncd.pid
    address = MANAGEMENT_INTERFACE_IP_ADDRESS

    [account]
    max connections = 2
    path = /srv/node/
    read only = false
    lock file = /var/lock/account.lock

    [container]
    max connections = 2
    path = /srv/node/
    read only = false
    lock file = /var/lock/container.lock

    [object]
    max connections = 2
    path = /srv/node/
    read only = false
    lock file = /var/lock/object.lock
    ```

11. 编辑/etc/default/rsync文件，，添加下面内容，启用rsync服务：
    ```
    RSYNC_ENABLE=true
    ```

12. 启动rsync服务：`service rsync start`

### 2. 安装Swift（在两个对象存储节点上安装）
1. 安装Swift：`apt-get install swift swift-account swift-container swift-object`

### 3. 获取Swift的配置文件
1. 获取accounting, container, object, container-reconciler和object-expirer service服务的配置文件  
`curl -o /etc/swift/account-server.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/account-server.conf-sample?h=stable/kilo`
`curl -o /etc/swift/container-server.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/container-server.conf-sample?h=stable/kilo`
`curl -o /etc/swift/object-expirer.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/object-expirer.conf-sample?h=stable/kilo`
`curl -o /etc/swift/object-expirer.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/object-expirer.conf-sample?h=stable/kilo`
`curl -o /etc/swift/object-expirer.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/object-expirer.conf-sample?h=stable/kilo`

2. 编辑配置文件/etc/swift/account-server.conf：
    在[DEFAULT]部分，配置绑定ip地址，端口，用户，配置文件路径，和挂载点的路径：
    ```
    bind_ip = MANAGEMENT_INTERFACE_IP_ADDRESS
    bind_port = 6002
    user = swift
    swift_dir = /etc/swift
    devices = /srv/node
    ```
    在[pipeline:main]部分，启用appropriate模块：
    ```
    pipeline = healthcheck recon account-server
    ```
    在[filter:recon]部分，配置缓存目录：
    ```
    recon_cache_path = /var/cache/swift
    ```

3. 编辑配置文件/etc/swift/container-server.conf：
    在[DEFAULT]部分，配置绑定ip地址，端口，用户，配置文件路径，和挂载点的路径：
    ```
    bind_ip = MANAGEMENT_INTERFACE_IP_ADDRESS
    bind_port = 6001
    user = swift
    swift_dir = /etc/swift
    devices = /srv/node
    ```
    在[pipeline:main]部分，启用appropriate模块：
    ```
    pipeline = healthcheck recon container-server
    ```
    在[filter:recon]部分，配置缓存目录：
    ```
    recon_cache_path = /var/cache/swift
    ```

4. 编辑配置文件/etc/swift/object-server.conf：
    在[DEFAULT]部分，配置绑定ip地址，端口，用户，配置文件路径，和挂载点的路径：
    ```
    bind_ip = MANAGEMENT_INTERFACE_IP_ADDRESS
    bind_port = 6001
    user = swift
    swift_dir = /etc/swift
    devices = /srv/node
    ```
    在[pipeline:main]部分，启用appropriate模块：
    ```
    pipeline = healthcheck recon object-server
    ```
    在[filter:recon]部分，配置缓存目录和lock目录：
    ```
    recon_cache_path = /var/cache/swift
    recon_lock_path = /var/lock
    ```

5. 修改挂载点的权限：`chown -R swift:swift /srv/node`
6. 创建recon目录，并修改目录权限：`mkdir -p /var/cache/swift` `chown -R swift:swift /var/cache/swift`




# 四、创建初始的rings
### 1. 创建Account ring（在控制节点上）
1. 进入/etc/swift目录：`cd /etc/swift`
2. 创建基本的account.builder文件：`swift-ring-builder account.builder create 10 3 1`
3. 添加所有的存储节点到ring中：  
`swift-ring-builder account.builder add r1z1-10.0.0.51:6002/sdb1 100`  
`swift-ring-builder account.builder add r1z2-10.0.0.51:6002/sdc1 100`  
`swift-ring-builder account.builder add r1z3-10.0.0.52:6002/sdb1 100`  
`swift-ring-builder account.builder add r1z4-10.0.0.52:6002/sdc1 100`  

### 2. 校验Account ring
1. 校验Account ring的内容：`swift-ring-builder account.builder`
2. 重新分布ring：`swift-ring-builder account.builder rebalance`

### 3. 创建Container ring（在控制节点上）
1. 进入/etc/swift目录：`cd /etc/swift`
2. 创建基本的container.builder文件：`swift-ring-builder container.builder create 10 3 1`
3. 添加所有的存储节点到ring中：  
`swift-ring-builder container.builder add r1z1-10.0.0.51:6001/sdb1 100`  
`swift-ring-builder container.builder add r1z2-10.0.0.51:6001/sdc1 100`  
`swift-ring-builder container.builder add r1z3-10.0.0.52:6001/sdb1 100`  
`swift-ring-builder container.builder add r1z4-10.0.0.52:6001/sdc1 100`  

### 4. 校验Container ring
1. 校验Container ring的内容：`swift-ring-builder container.builder`
2. 重新分布ring：`swift-ring-builder container.builder rebalance`

### 5. 创建Object ring（在控制节点上）
1. 进入/etc/swift目录：`cd /etc/swift`
2. 创建基本的object.builder文件：`swift-ring-builder object.builder create 10 3 1`
3. 添加所有的存储节点到ring中：  
`swift-ring-builder object.builder add r1z1-10.0.0.51:6000/sdb1 100`  
`swift-ring-builder object.builder add r1z2-10.0.0.51:6000/sdc1 100`  
`swift-ring-builder object.builder add r1z3-10.0.0.52:6000/sdb1 100`  
`swift-ring-builder object.builder add r1z4-10.0.0.52:6000/sdc1 100`  

### 6. 校验Object ring
1. 校验Object ring的内容：`swift-ring-builder object.builder`
2. 重新分布ring：`swift-ring-builder object.builder rebalance`

### 7. 分发配置文件
如果有其他的对象存储节点，请将account.ring.gz, container.ring.gz和object.ring.gz三个文件拷贝到其他节点的/etc/swift目录

### 8. 完成安装
1. 从对象存储资源库中获取/etc/swift/swift.conf文件：  
`curl -o /etc/swift/swift.conf https://git.openstack.org/cgit/openstack/swift/plain/etc/swift.conf-sample?h=stable/kilo`
2. 编辑/etc/swift/swift.conf文件：
    在[swift-hash]部分，为环境配置哈希路径的前缀和后缀：
    ```
    swift_hash_path_suffix = HASH_PATH_PREFIX(改成唯一值)
    swift_hash_path_prefix = HASH_PATH_SUFFIX(改成唯一值)
    ```
    在[storage-policy:0] 部分，配置默认存储策略：
    ```
    name = Policy-0
    default = yes
    ```

3. 复制文件swift.conf到每个存储节点响应的/etc/swift目录下：
    ```
    scp swift.conf  aboutyun@10.0.0.51:~/
    scp swift.conf  aboutyun@10.0.0.52:~/

    sudo cp swift.conf /etc/swift
    ```

4. 在所有的存储节点上，设置目录的权限：`chown -R swift:swift /etc/swift`
5. 在控制节点和其他存储节点上重启服务：`service memcached restart` `service swift-proxy restart`
6. 在所有的存储节点上重启服务：`swift-init all start`







# 五、校验Swift的安装（在控制节点上）
1. 检查对象存储服务启动的服务组件：`swift -V 3 stat`
2. 上传一个测试文件：`swift -V 3 upload demo-container1 FILE`
3. 查看容器：`swift -V 3 list`
4. 下载测试文件：`swift -V 3 download demo-container1 FILE`


























# Cinder

# 一、安装Cinder前的准备
### 1. 为Cinder创建数据库（在控制节点）
提示：以下操作在控制节点完成，为块存储服务创建数据库、服务认证和API端点
1. 使用数据库客户端，以root用户连接到数据库中：`mysql -u root -p`
2. 创建Cinder数据库：`CREATE DATABASE cinder;`
3. 为Cinder用户授予数据库权限：  
`GRANT ALL PRIVILLEGES ON cinder.* TO 'cinder'@'localhost' IDENTIFIED BY 'CINDER_DBPASS';`  
`GRANT ALL PRIVILLEGES ON cinder.* TO 'cinder'@'%' IDENTIFIED BY 'CINDER_DBPASS';`  

### 2. 创建Cinder的身份认证证书
1. 加载admin用户的客户端脚本：`source admin-openrc.sh`
2. 创建cinder用户：`openstack user create --password-prompt cinder`
3. 将admin角色添加给cinder用户：`openstack role add --project service --user cinder admin`
4. 创建cinder的服务实体(volume和volumev2两个服务实体)：  
`openstack service create --name cinder --description "OpenStack Block Storage" volume`    
`openstack service create --name cinderv2 --description "OpenStack Block Storage" volumev2`
5. 创建计算服务的API endpoint(volume和volumev2两个端点：  
`openstack endpoint create --publicurl http://controller:8776/v2/%\(tenant_id\)s --internalurl http://controller:8776/v2/%\(tenant_id\)s --adminurl http://controller:8776/v2/%\(tenant_id\)s --region RegionOne volume`    
`openstack endpoint create --publicurl http://controller:8776/v2/%\(tenant_id\)s --internalurl http://controller:8776/v2/%\(tenant_id\)s --adminurl http://controller:8776/v2/%\(tenant_id\)s --region RegionOne volumev2`

# 二、在控制节点上安装和配置Cinder
### 1. 安装Cinder
1. 安装Cinder：`apt-get install cinder-api cinder-scheduler python-cinderclient`

### 2. 配置Cinder
编辑文件：/etc/cinder/cinder.conf  
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`
1. 修改[database]部分，配置数据库的连接：`connection = mysql://cinder:CINDER_DBPASS@controller/cinder`  
记得密码替换为自己设置密码,这是mysql的密码，并非cinder用户的密码
2. 修改[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

3. 在[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = cinder
    password = CINDER_PASS
    ```

4. 修改[DEFAULT]部分的my_ip参数，配置控制节点的管理IP地址：
    ```
    my_ip = 10.0.0.11
    ```

5. 修改[oslo_concurrency]部分，配置锁路径：
    ```
    lock_path = /var/lock/cinder
    ```

6. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 3. 配置Cinder数据库
1. 为块存储服务数据库添加数据：`su -s /bin/sh -c "cinder-manage db_sync" cinder`

### 4. 完成在控制节点上安装和配置Cinder
1. 重启块存储服务Cinder：`service cinder-scheduler restart` `service cinder-api restart`
2. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/cinder/cinder.sqlite`


# 三、在块存储节点上安装和配置Cinder
### 1. 配置块存储的操作系统环境
编辑文件：/etc/hosts
1. 配置管理接口：IP地址：10.0.0.41和10.0.2.41
2. 配置主机名：block1
3. 将blcok主机的/etc/hosts内容同步到其他所有节点：``
4. 为主机添加新的磁盘，并创建分区：`fdisk /dev/sdb`
5. 安装LVM包：`apt-get install lvm2`
6. 配置LVM的物理卷：`pvcreate /dev/sdb1`
7. 创建LVM的卷组cinder-volumes：`vgcreate cinder-volumes /dev/sdb1`
8. 配置LVM仅仅扫描含有cinder-volume的卷组，编辑/etc/lvm/lvm.conf：
在devices部分，添加一个筛选器，仅仅允许/dev/sdb设备并拒绝所有其他设备：
    ```
    devices {
    ...
    filter = [ "a/sdb/", "r/.*/"]
    ```

9. 检查过滤器是否起作用：`vgs -vvvv`

### 2. 安装Cinder（在block1节点上）
1. 安装Cinder：`apt-get install -y cinder-volume python-mysqldb`

### 3. 配置Cinder
编辑Cinder的配置文件：/etc/cinder/cinder.conf  
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`
1. 在[database]部分，配置数据库访问：
    ```
    connection = mysql://cinder:CINDER_DBPASS@controller/cinder
    ```

2. 在[DEFAULT]和[oslo_messaging_rabbit]两部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```
    ```
    rabbit_host = controller
    rabbit_userid = openstack
    rabbit_password = RABBIT_PASS
    ```

3. 在[DEFAULT]和[keystone_authtoken]两部分，配置身份认证服务的访问：
    ```
    auth_strategy = keystone
    ```
    注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数
    ```
    auth_uri = http://controller:5000
    auth_url = http://controller:35357
    auth_plugin = password
    project_domain_id = default
    user_domain_id = default
    project_name = service
    username = cinder
    password = CINDER_PASS
    ```

4. 修改[DEFAULT]部分的my_ip参数，配置块存储节点的管理IP地址：
    ```
    my_ip = MANAGEMENT_INTERFACE_IP_ADDRESS(10.0.0.41)
    ```

5. 在[lvm]部分，配置LVM驱动，卷组，iscsi的协议和helper：
    ```
    volume_driver = cinder.volume.drivers.lvm.LVMVolumeDriver
    volume_group = cinder-volumes
    iscsi_protocol = iscsi
    iscsi_helper = tgtadm
    ```

6. 在[DEFAULT]部分，启用LVM作为后端存储系统：
    ```
    enabled_backends = lvm
    ```

7. 在[DEFAULT]部分，配置镜像服务运行的节点：
    ```
    glance_host = controller
    ```

8. 在[oslo_concurrency]部分，配置锁路径：
    ```
    lock_path = /var/lock/cinder
    ```

9. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 4. 完成安装和配置Cinder
1. 重启块存储服务Cinder：`service tgt restart` `service cinder-volume restart`
2. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/cinder/cinder.sqlite`



# 四、校验Cinder的安装
### 1. 校验Cinder的安装
提示：在控制节点上进行校验，尝试创建一个卷
1. 在admin-openrc.sh和demo-openrc.sh两个脚本里添加块存储的客户端使用API v2.0：  
`echo "export OS_VOLUME_API_VERSION=2" | tee -a admin-openrc.sh demo-openrc.sh`
2. 加载admin客户端脚本：`source admin-openrc.sh`
3. 检查块存储服务启动的服务组件：`cinder service-list`  
应该有两个服务：controller和block1@lvm
4. 加载demo脚本：`source demo-openrc.sh`
5. 创建1GB的一个卷：`cinder create --name demo-volume1 1`
6. 创建卷是否创建成功，检查状态是否为可用，否则查看日志文件/var/log/cinder：`cinder list`

### 2. 将卷添加到实例中
1. 查看现有的实例，注意查看实例名和确保实例状态是active：`nova list`
2. 查看可用的卷，注意查看卷ID：`nova volume-list`
3. 查看连接实例的VNC URL：`nova get-vnc-console demo-instance1 novnc`
4. 在浏览器中连接实例
5. 查看实例中的现有卷：`sudo fdisk -l`
6. 将查看到的卷添加到实例中：`nova volume-attach demo-instance1 158bea89-07db-4ac2-8115-66c0d6a4bb48`
7. 查看卷的状态，status应该是in-use：`nova volume-list`
8. 在浏览器中连接实例
9. 在实例中验证卷是否添加到实例中：`sudo fdisk -l`


















# Heat

# 一、在控制节点上安装和配置部署编排服务Heat
### 1. 为Heat创建数据库（在控制节点）
提示：以下操作在控制节点完成，为部署编排服务创建数据库、服务认证和API端点
1. 使用数据库客户端，以root用户连接到数据库中：`mysql -u root -p`
2. 创建Heat数据库：`CREATE DATABASE heat;`
3. 为Heat用户授予数据库权限：  
`GRANT ALL PRIVILLEGES ON heat.* TO 'heat'@'localhost' IDENTIFIED BY 'HEAT_DBPASS';`  
`GRANT ALL PRIVILLEGES ON heat.* TO 'heat'@'%' IDENTIFIED BY 'HEAT_DBPASS';`

### 2. 创建Heat的身份认证证书
1. 加载admin用户的客户端脚本：`source admin-openrc.sh`
2. 创建Heat用户：`openstack user create --password-prompt heat`
3. 将admin角色添加给Heat用户：`openstack role add --project service --user heat admin`
4. 创建heat_stack_owner角色：`openstack role create heat_stack_owner`
5. 将heat_stack_owner角色添加给demo用户：`openstack role add --project demo --user demo heat_stack_owner`
6. 创建heat_stack_user角色：`openstack role create heat_stack_user`
4. 创建Heat的服务实体(heat和heat-cfn两个服务实体)：  
`openstack service create --name heat --description "Orchestration" orchestration`  
`openstack service create --name heat-cfn --description "Orchestration" cloudformation`  
5. 创建计算服务的API endpoint(两个端点)：
`openstack endpoint create --publicurl http://controller:8004/v1/%\(tenant_id\)s --internalurl http://controller:8004/v1/%\(tenant_id\)s --adminurl http://controller:8004/v1/%\(tenant_id\)s --region RegionOne orchestration`    
`openstack endpoint create --publicurl http://controller:8000/v1/%\(tenant_id\)s --internalurl http://controller:8000/v1/%\(tenant_id\)s --adminurl http://controller:8000/v1/%\(tenant_id\)s --region RegionOne cloudformation`  

### 3.安装Heat组件（在控制节点上）
1. 安装Heat组件：`apt-get install -y heat-api heat-api-cfn heat-engine python-heatclient` 


### 4. 配置Heat
编辑文件：/etc/heat/heat.conf    
小助手：删除#和空格的命令：`cat file | grep -v '^#' | grep -v '^$' > newfile`
1. 修改[database]部分，配置数据库的连接：`connection = mysql://heat:HEAT_DBPASS@controller/heat`  
记得密码替换为自己设置密码,这是mysql的密码，并非heat用户的密码
2. 修改[DEFAULT]部分, 配置RabbitMQ消息队列的访问：
    ```
    rpc_backend = rabbit
    ```

3. 在[keystone_authtoken]和[ec2authtoken]两部分，配置身份认证服务的访问：  
注意：修改[keystone_authtoken]部分要注释掉本部分其他的参数  
注释掉任何auth_host,auth_port和auth_protocol的选项，因为identity_uri会代替这些选项
    ```
    auth_uri = http://controller:5000/v2.0
    identity_uri = http://controller:35357
    admin_tenant_name = service
    admin_user = heat
    admin_password = HEAT_PASS
    ```
    ```
    auth_uri = http://controller:5000/v2.0
    ```

4. 在[DEFAULT]部分，配置元数据和URL：
    ```
    heat_metadata_server_url = http://controller:8000
    heat_waitcondition_server_url = http://controller:8000/v1/waitcondition
    ```

5. 在[DEFAULT]部分，配置关于heat认证实体服务的域：
    ```
    stack_domain_admin = heat_domain_admin
    stack_domain_admin_password = HEAT_DOMAIN_PASS
    stack_user_domain_name = heat_user_domain
    ```

6. 可选：在[DEFAULT]部分启用日志信息详细记录：
    ```
    verbose = True
    ```

### 5. 创建Heat域
1. 加载admin-openrc.sh脚本：`source admin-openrc.sh`
2. 创建heat域：  
`heat-keystone-setup-domain --stack-user-domain-name heat_user_domain --stack-domain-admin heat_domain_admin --stack-domain-admin-password HEAT_DOMAIN_PASS`

### 6. 配置Heat数据库
1. 为部署编排服务数据库添加数据：`su -s /bin/sh -c "heat-manage db_sync" heat`

### 7. 完成在控制节点上安装和配置Heat
1. 重启块存储服务Heat：`service heat-api restart` `service heat-api-cfn restart`
2. 删除ubuntu默认创建的SQLite数据库：`rm -f /var/lib/heat/heat.sqlite`


# 二、校验Heat的安装（在控制节点）
1. 加载admin脚本：`source admin-openrc.sh`
2. 创建测试模板test-stack.yml
3. 使用stack-create命令从模板中创建一个stack：  
`NET_ID = $(nova net-list | awk '/demo-net/{print $2}')`  
`heat stack-create -f test-stack.yml -P "ImageID=cirros-0.3.3-x86_64;NetID=$NET_ID" testStack`
4. 使用stack-list命令查看刚才创建的stack：`heat stack-list`
