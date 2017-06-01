# 小试牛刀
## 简介
* shell脚本通常以`#!/bin/bash`起始，该起始行被称作"shebang"，/bin/bash时Bash的路径
* 运行脚本：`sh script.sh`（将脚本作为sh的命令行参数）或`/path/to/script.sh`（使用脚本的完整路径）
* 赋予脚本可执行权限：`chmod a+x script.sh`使脚本能以下列方式执行：`./script.sh`
* 每个命令通过`分号`或`换行符`分隔
* `#`表明注释的开始

## 终端打印
1. echo
`echo`：用于终端打印，默认在每次调用后添加一个换行符，可不带双引号，也可用单引号
双引号：希望打印文本中的！，就要用转义字符将之转义
单引号：不会对单引号中的变量求值，而是原样显示
不带引号：没法在所要显示的文本中显示，因为在bash shell中被用作命令定界符
`echo "Welcome to Bash"`
`echo Welcome to Bash`

2. printf
要手动添加换行符，可格式化字符串

补充内容：
* 在echo中转义换行符：`echo -e "1\t2\t3"`
* 彩色打印输出：`echo -e "\e[1;31m This is red text \e[0m"`
（`\e[1;31m`将颜色设为红色，`\e[0m`将颜色重新置回）

## 玩转变量和环境变量
* 查看某个进程运行时的环境变量：`cat /proc/$PID/environ`
* 获得进程的ID：`pgrep gedit`
* 格式化环境变量输出：`cat /proc/12501/environ  | tr '\0' '\n'`
* `var=value`：赋值，如果value不包含任何空白字符，则不需要引号  
`var = value`：相等
* 打印变量的内容：`echo $var`或`echo ${var}`
* 查看PATH变量：`echo $PATH`，一般定义在`/etc/environment` or `/etc/profile` or `~/.bashrc`
* 设置环境变量：`export PATH="$PATH:/home/user/bin"`
* 获得字符串长度：`echo ${#var}`
* 识别当前的shell版本：`echo $SHELL`或`echo $0`
* 检查是否为超级用户：`echo $UID`
* 修改bash提示字符串：`cat ~/.bashrc | grep PS1`

## 通过shell进行数学运算
1. `let`可以执行最基本的算数操作
自加操作：`let no1++`
自减操作：`let no1--`
缩写：`let no += 6`

2. []：`echo $[no1 + no2]`

3. 也可以使用(())，但使用时，变量名之前需要加上$

4. expr也可以用于算数操作：`echo ～expr 3 + 4～`（～指代反引号）或`echo $(expr 3 + 4)`

5. `bc`：可执行浮点运算等高级数学运算操作  
`echo "4 * 0.56" | bc`
scale设定小数精度：`echo "scale=2;3/8" | bc`
obase进制转换：`echo "obase=2;100" | bc`
平方根：`echo "sqrt(100)" | bc`
平方：`echo "10^10" | bc`

## 玩转文件描述符和重定向
文件描述符：
`stdin`：0，标准输入  
`stdout`：1，标准输出  
`stderr`：2，标准错误  

* 文本重定向或保存到文件中（会先清空文件）：`echo "This is a sample text 1" > temp.txt`
* 文本重定向或保存到文件中（只追加到尾部）：`echo "This is a sample text 1" >> temp.txt`

* 查看文件内容：`cat temp.txt`

* 将stderr文本打印到屏幕，生成空文件：`ls + > out.txt`
* 有效：`ls + 2> out.txt`
* 将stderr单独重定向到一个文件，将stdout重定向到另一个文件：`cmd 2>stderr.txt 1>stdout.txt`
* 将stderr和stdout都被重定向到同一个文件：`cmd 2>&1 output.txt`或`cmd &> output.txt`
* 将命令的stderr的输出丢到/dev/null中：`cmd 2> /dev/null`

* 将文件重定向到命令：`cmd < file`
* 重定向脚本内部的文本块：
* 自定义文件描述符：

## 数组和关联数组
普通数组只能使用整数作为索引，关联数字可以使用字符串作为索引。
* 在单行中使用一列值来定义一个数组：`array_var=(1 2 3 4 5 6)`
* 将数组定义成一组索引-值：
    ```
    array_var[0]="test1"
    array_var[1]="test2"
    array_var[2]="test3"
    array_var[3]="test4"
    array_var[4]="test5"
    array_var[5]="test6"
    ```

* 打印出特定索引的数组元素内容：`echo ${array_var[0]}`
* 以清单形式打印数组中的所有值：`echo ${array_var[*]}`或`echo ${array_var[@]}`
* 打印数组长度：`echo ${#array_var[*]}`

* 声明关联数组：`declare -A ass_array`
    1. 利用内嵌索引-值列表法，提供一个索引-值列表：`ass_array=([index1]=val1 [index2]=val2)`
    2. 使用独立的索引-值进行赋值：`ass_array[index1]=val1`
* 列出数组索引：`echo ${!array_var[*]}`或`echo ${!array_var[@]`

## 使用别名
* 创建别名（暂时）：`alias new_command='command sequence'`
* 创建别名（永久）：`echo 'alias cmd="command seq"' >> ~/.bashrc`
* 删除别名：`unalias command'`
* 对别名进行转义：`\command`

## 获取终端信息
* 获取终端的行数：`tput cols`
* 获取终端的列数：`tput lines`
* 打印当前终端名：`tput longname`
* 将光标移动到方位(100,100)处：`tput cup 100 100`
* 设置终端背景色（n为0-7）：`tputsetb n`
* 将文本前景色设为白色：`tputsetf n`
* 设置文本样式为粗体：`tput bold`
* 设置下划线的起止：`tput smul` `tput rmul`
* 删除当前光标位置到行尾的所有内容：`tputed`

## 获取、设置日期和延时
* 读取日期：`date`
* 打印纪元时（POSIX时间）：`date +%s`
* 将日期串转换成纪元时：`date --date "Thu Nov 18 08:07:21 IST 2010" +%s`
* 使用任意的日期格式化选项打印输出：`date --date "Jan 20 2001" +%A`
* 用格式串结合+作为参数，打印出对应格式的日期：`date "+%d %B %Y"`
* 设置日期和时间：`date -s "21 June 2009 11:01:22"`

日期格式字符串列表：
| Date component | Format |
| --- | --- |
| Weekday| `%a` (for example, Sat) <br> `%A` (for example, Saturday) |
| Month| `%b` (for example, Nov) <br> `%B` (for example, November) | 
| Day | `%d` (for example, 31) |
| Date in format (mm/dd/yy) | `%D` (for example, 10/18/10) |
| Year | `%y` (for example, 10) <br> `%Y` (for example, 2010) |
| Hour | `%I` or `%H` (For example, 08) |
| Minute | `%M` (for example, 33) |
| Second | `%S` (for example, 10) |
| Nano second | `%N` (for example, 695208515) |
| Epoch Unix time in seconds | `%s` (for example, 1290049486) |

## 调试脚本
-x启动跟踪调试shell脚本：`bash -x script.sh`

* `set -x`：在执行时显示参数和命令
* `set +x`：禁止调试
* `set -v`：当命令进行读取时显示输入
* `set +v`：禁止打印输入、

## 函数和参数
* 定义函数：
    ```
    function fname()
    {
        echo $1, $2;    # 访问参数1和参数2
        echo "$@";      # 列表打印所有参数
        echo "$*";      # 类似于$@, 但是参数被作为单个实体
        return 0;       # 返回值
    }
    Or alternately,
    fname()
    {
        statements;
    }
    ```
* 只需使用函数名就可调用某个函数：`$ fname`
* 参数可以传递给函数，并由脚本进行访问：`fname arg1 arg2`
* 导出函数：函数的作用域扩展到子进程中：`export -f fname`
* 读取命令返回值（状态）：`echo $?`
* 向命令传递参数：  
`command -p -v -k 1 file`  
`command -pv -k 1 file`  
`command -vpk 1 file`  
`command file -pvk 1`  

## 读取命令序列输出
两种方法读取命令序列的输出：
* 子shell
* 反引用

## 以不按回车键的方式读取字符“n”
从输入中读取n个字符并存入变量：`read -n number_of_chars variable_name`
用不回显的方式读取密码：`read -s var`
显示提示信息：`read -p "Enter input:"  var`
在特定时限内读取输入：`read -t timeout var`
用定界符结束输入行：`read -d delim_char var

## 运行命令直到成功
```
repeat()
{
  while true
  do
    $@ && return
  done
}
```

更快的方式：`repeat() { while :; do $@ && return; done }`

## 字段分隔符和迭代器
字段分隔符：IFS，在处理文本数据时相当有用
```bash
#!/bin/bash
data="name,sex,rollno,location"
oldIFS=$IFS
IFS=,
for item in $data;
do
    echo Item: $item
done
IFS=$oldIFS
```

* for
    ```bash
    for((i=0;i<10;i++))
    {
        commands; # Use $i
    }
    ```

* while
    ```bash
    x=0;
    while x>5
    do
        let x++; echo $x;
    done
    ```

* until
    ```bash
    x=0;
    until [ $x -eq 9 ];
    do
        let x++; echo $x;
    done
    ```

## 比较与测试
* if
    ```bash
    if [condition] or [condition]; 
    then
        commands;
    else if condition; then
        commands;
    else
        commands;
    fi
    ```

* 算数比较
`-gt`大于，`-lt`小于，`-ge`大于等于，`-le`小于等于，`-eq`等于，`-ne`不等于

* 文件系统相关测试：
    * `[ -f $file_var ]`: 如果给定的变量包含正常的文件路径或文件名，则返回真
    * `[ -x $var ]`: 如果给定的变量包含的文件可执行，则返回真
    * `[ -d $var ]`: 如果给定的变量包含的时目录，则返回真
    * `[ -e $var ]`: 如果给定的变量包含的文件存在，则返回真
    * `[ -c $var ]`: 如果给定的变量包含的时一个字符设备文件的路径，则返回真
    * `[ -b $var ]`: 如果给定的变量包含的时一个块设备文件的路径，则返回真
    * `[ -w $var ]`: 如果给定的变量包含的文件可写，则返回真
    * `[ -r $var ]`: 如果给定的变量包含的文件可读，则返回真
    * `[ -L $var ]`: 如果给定的变量包含的时一个符号链接，则返回真
    ```bash
    fpath="/etc/passwd"
    if [ -e $fpath ]; then
        echo File exists; 
    else
        echo Does not exist; 
    fi
    ```

* 字符串比较
    * `[[ $str1 = $str2 ]]`: 当str1等于str2时，则返回真
    * `[[ $str1 == $str2 ]]`: 同上
    * `[[ $str1 != $str2 ]]`: 如果str1和str2不相同，则返回真
    * `[[ $str1 > $str2 ]]`: 如果str1的字母序比str2大，则返回真
    * `[[ $str1 < $str2 ]]`: 如果str1的字母序比str2小，则返回真
    * `[[ -z $str1 ]]`: 如果str1包含的是空字符串，则返回真
    * `[[ -n $str1 ]]`: 如果str1包含的是非空字符串，则返回真




# 命令之乐
## 用cat进行拼接
* 优秀的工具：grep, awk, sed, find
* 读取拼接后的文件内容：`cat file1 file2 file3`
* 压缩空白行（将多个空行压缩成单个）:`cat -s file`
* 将制表符显示为`^|`：`cat -T file.py`
* 显示行号：`cat -n lines.txt`

## 录制与回放终端会话
开始录制终端会话：`script -t 2> timing.log -a output.session`

## 文件查找与文件列表
1. 根据文件名或正则表达式匹配搜索
    * `find base_path`：列出当前目录及子目录下所有文件和文件夹
    * `find . -print`：打印文件和目录的列表
    * `find /home/slynux -name "*.txt" -print`：根据文件名或正则表达式匹配搜索
    * `find . -iname "example*" -print`：忽略大小写
    * `find . \( -name "*.txt" -o -name "*.pdf" \) -print`：匹配多个条件中的一个
    * `find /home/users -path "*/slynux/*" -print`：-path参数可使用通配符来匹配（将文件路径作为一个整体）
    * `find . -regex ".*\(\.py\|\.sh\)$"`：-regex参数基于正则表达式来匹配
    * `find . -iregex ".*\(\.py\|\.sh\)$"`：-iregex参数忽略正则表达式的大小写

2. 否定参数
    * `find . ! -name "*.txt" -print`：否定参数，匹配不以.txt结尾的文件

3. 基于目录深度的搜索
    * `find . -maxdepth 1 -name "f*" -print`：-maxdepth参数限制向下遍历的最大深度为1
    * `find . -mindepth 2 -name "f*" -print`：打印出深度距离当前目录至少两个子目录的所有文件

4. 根据文件类型搜索
    * `find . -type d -print`：只列出所有的目录
    * `find . -type f -print`：只列出普通文件
    * `find . -type l -print`：只列出符号链接

| File type | Type argument |
| --- | --- |
| Regular file | f |
| Symbolic link | l |
| Directory | d |
| Character special device | c |
| Block device | b |
| Socket | s |
| FIFO | p |

5. 根据文件时间进行搜索  
    * `-atime`：访问时间（以天为单位）
    * `-mtime`：修改时间（以天为单位）
    * `-ctime`：变化时间（以天为单位）
    * `-amin`：访问时间（以分钟为单位）
    * `-mmin`：修改时间（以分钟为单位）
    * `-cmin`：变化时间（以分钟为单位）

    * `find . -type f -atime -7 -print`：打印在最近七天内被访问过的文件
    * `find . -type f -atime 7 -print`：打印在恰好在七天前被访问过的文件
    * `find . -type f -atime +7 -print`：打印访问时间超过七天的文件
    * `find . -type f -newer file.txt -print`：打印比file.txt修改时间更早的文件

6. 基于文件大小的搜索  
`find . -type f -size +2k`：打印文件大小大于2KB的文件
    * `b`: 块，512 byte blocks
    * `c`: 字节，Bytes
    * `w`: 字，Two-byte words
    * `k`: 千字节，Kilobyte (1024 bytes)
    * `M`: 兆字节，Megabyte (1024 kilobytes)
    * `G`: 吉字节，Gigabyte (1024 megabytes)

7. 删除匹配的文件  
`find . -type f -name "*.swp" -delete`：删除匹配的文件

8. 基于文件权限和所有权的匹配  
    * `find . -type f -perm 644 -print`：给予文件权限和所有权的匹配，打印权限为644的文件
    * `find . -type f -name "*.php" ! -perm 644 -print`：没有设置好执行权限的PHP文件
    * `find . -type f -user slynux -print`：根据文件的所有权进行搜索，打印slynux用户拥有的所有文件

9. 结合find执行命令或动作  
    * `find . -type f -user root -exec chown slynux {} \;`：
    * `find . -type f -name "*.c" -exec cat {} \;>all_c_files.txt`：
    * `find . -type f -mtime +10 -name "*.txt" -exec cp {} OLD  \;`：
    * `find . -type f -name "*.txt" -exec printf "Text file: %s\n" {} \;`：

10. 让find跳过特定的目录  
`find devel/source_path  \( -name ".git" -prune \) -o \( -type f -print \)`：打印不包括在.git目录下的所有文件

## 玩转xargs


## 用tr进行转换
tr可以对来自标准输入的字符进行替换、删除及压缩。还可以将一组字符变成另一组字符，通常被称为转换命令。

* 将大写转换成小写：`echo "HELLO WHO IS THIS" | tr 'A-Z' 'a-z'`
* 加密：`echo 12345 | tr '0-9' '9876543210'`  
解密：`echo 87654 | tr '9876543210' '0-9'`
* ROT13加密：`echo "tr came, tr saw, tr conquered." | tr 'a-zA-Z' 'n-za-mN-ZA-M'`
解密：`echo ge pnzr, ge fnj, ge pbadhrerq. | tr 'a-zA-Z' 'n-za-mN-ZA-M'`
* 将制表符转换成空格：`tr '\t' ' ' < file.txt`
* 用tr删除字符（-d指定要被删除的字符集合）：`cat "Hello 123 world 456" | tr -d  '0-9'`
* 字符集补集（-c指定不在该集合内的字符全部删除）：`echo hello 1 char 2 next 4 | tr -d -c '0-9 \n'`
* 压缩字符：`echo "GNU is       not     UNIX. Recursive   right ?" | tr -s ' '`
* 字符类：`tr [:class:] [:class:]` 
alnum: 字母和数字
alpha: 字母
cntrl: 控制（非打印）字符
print: 可打印字符
digit: 数字
graph: 图形字符
lower: 小写字母
upper: 大写字母
punct: 标点符号
space: 空白字符
xdigit: 十六进制字符

## 校验与核实
* 计算文件的md5sum值（32个字符的十六进制串）：`md5sum filename`
* 校验多个文件：`md5sum file1 file2 file3`
* 将输出的校验和重定向到文件：`md5sum filename > file_sum.md5`
* 用生成的文件核实数据完整性：`md5sum -c file_sum.md5`
* 用所有的.md5信息来检查所有的文件：`md5sum -c *.md5`
* SHA1与md5sum类似，只需把md5sum改成sha1sum，把.md5改成.sha1就行了
* 对目录进行校验：`md5deep -rl directory_path > directory.md5`

## 加密工具和hashes
常用工具：crypt, gpg, base64, md5sum, sha1sum, openssl
1. crypt
加密：`crypt <input_file >output_file`
提供passphrase加密：`crypt PASSPHRASE <input_file >encrypted_file`
解密：`crypt PASSPHRASE -d <encrypted_file >output_fil`

2. gpg
加密：`gpg -c filename`
解密：`gpg filename.gpg`

3. Base64
加密：`base64 filename > outputfile`或`cat file | base64 > outputfile`
解密：`base64 -d file > outputfile`或`cat base64_file | base64 -d > outputfile`

4. md5sum
`md5sum file`

5. sha1sum
`sha1sum file`

6. Shadow-like hash (salted hash)
用户密码存储在`/etc/shadow`  
`opensslpasswd -1 -salt SALT_STRING PASSWORD`

## 排序、单一与重复
1. sort：排序
    * 排序一组文件：`sort file1.txt file2.txt > sorted.txt`或`sort file1.txt file2.txt -o sorted.txt`
    * 数字排序：`sort -n file.txt`
    * 反序：`sort -r file.txt`
    * 月份排序：`sort -M months.txt`
    * 合并两个排序文件：`sort -m sorted1 sorted2`
    * 从已排序文件中寻找unique的一行：`sort file1.txt file2.txt | uniq`
    * 根据第一列反向排序（-k指定列号）：`sort -nrk 1 data.txt`
    * 通过第二列排序（-k指定列号）：`sort -k 2 data.txt`

2. uniq：消除重复内容
* 消除文件中重复的行：`uniq sorted.txt`
* 只显示唯一的行，不显示重复的行：`uniq -u sorted.txt`
* 统计各行在文件中出现的次数：`sort unsorted.txt | uniq -c`
* 找出文件中重复的行：`sort unsorted.txt  | uniq -d`
* -s指定可以跳过前N个字符，-w指定用于比较的最大字符数

## 临时文件命名与随机数
临时文件名：
* `echo "$(tempfile)"`（tempfile）
* `echo "file-$RANDOM"`（随机数）
* `echo "$$"`（当前运行脚本的进程ID）
* `echo "～mktemp～"`（～指代反引号），真正创建了文件
* `echo "～mktemp～ -d"`（～指代反引号），真正创建了文件夹
* `echo "～mktemp～ -u"`（～指代反引号），没有真正创建文件
* `mktemp test.XXX`

## 分割文件和数据
1. split
* 将文件分割成多个文件，每个大小10KB，并且以字母为后缀：`split -b 10k data.file`
* 将文件分割成多个文件，每个大小10KB，并且以数字为后缀：`split -b 10k data.file -d`
* 将文件分割成多个文件，每个大小10KB，并且以数字为后缀，长度为4：`split -b 10k data.file -d -a 4`
* 加前缀split_file：`split -b 10k data.file -d -a 4 split_file`
* 根据行数分割文件(-l)：`split -l 10 data.file`

2. csplit

## 根据扩展名切分文件名
* %操作符提取名称：
    ```
    file_jpg="sample.jpg"
    echo ${file_jpg%.*}
    ```
* #操作符提取扩展名：
    ```
    file_jpg="sample.jpg"
    echo ${file_jpg#*.}
    ```
* %使用.*从右向左执行非贪婪匹配：
```
VAR=hack.fun.book.txt
echo ${VAR%.*}
```
结果是hack.fun.book
* %%使用.*从右向左执行贪婪匹配：
```
VAR=hack.fun.book.txt
echo ${VAR%%.*}
```
结果是hack
* #使用*.从左向右执行非贪婪匹配：
```
VAR=hack.fun.book.txt
echo ${VAR#*.}
```
结果是fun.book.txt
* ##使用*.从左向右执行贪婪匹配：
```
VAR=hack.fun.book.txt
echo ${VAR##*.}
```
结果是txt
* 综上，因为文件名中可能包含多个.字符，所以##更适合从文件名中提取出扩展名

## 批量重命名和移动
* 用特定的格式重命名当前目录下的图像文件
```bash
count=1;
for img in `find . -iname '*.png' -o -iname '*.jpg' -type f -maxdepth 1`
do
  new=image-$count.${img##*.}

  echo "Renaming $img to $new"
  mv "$img" "$new"
  let count++
done 
```
新格式为：image-1.jpg，image-2.png...
* *.JPG改成*.jpg：`rename *.JPG *.jpg`
* 将文件名中的空格替换成_：`rename 's/ /_/g' *`
* 转换文件名的大小写：`rename 'y/A-Z/a-z/' *`
* 将所有的.mp3文件移入指定目录：`find path -type f -name "*.mp3" -exec mv {} target_dir \;`
* 将所有文件名中的空格替换为字符_：`find path -type f -exec rename 's/ /_/g' {} \;`

## 拼写检查与词典操作
`aspell`：进行拼写检查
`ls /usr/share/dict/`目录下

检查给定的单词是否是词典中的单词：
```bash
#!/bin/bash
#Filename: checkword.sh
word=$1
grep "^$1$" /usr/share/dict/british-english -q 
if [ $? -eq 0 ]; then
  echo $word is a dictionary word;
else
  echo $word is not a dictionary word;
fi
```
使用：`./checkword.sh ful`

## 交互输入
expect
```bash
#!/usr/bin/expect 
#Filename: automate_expect.sh
spawn ./interactive .sh 
expect "Enter number:" 
send "1\n" 
expect "Enter name:" 
send "hello\n" 
expect eof 
```

## 并行线程使命令运行更快
```bash
#/bin/bash
#filename: generate_checksums.sh
PIDARRAY=()
for file in File1.iso File2.iso
do
  md5sum $file &
  PIDARRAY+=("$!")
done
wait ${PIDARRAY[@]}
```
使用：`./generate_checksums.sh`



# 以文件之名
## 生成任意大小的文件
创建一个1MB大小的文件junk.data（if-输入文件，of-输出文件）：`dd if=/dev/zero of=junk.data bs=1M count=1`

## 文本文件的交集与差集
交集：两个文件共有的行
求差：指定文件所包含的且不相同的那些行
差集：包含在文件A中，不包含在其他指定文件中的那些行

comm必须使用排过序的文件作为输入
`sort A.txt -o A.txt ; sort B.txt -o B.txt`
`comm A.txt B.txt`
输出的第一列包含只在A.txt中出现的行，第二列包含只在B.txt中出现的行，第三列包含了两个文件相同的行
参数：-1（从输出中删除第一列）、-2（从输出中删除第二列）、-3（从输出中删除第三列）
* 删除行首的\t：`comm A.txt B.txt  -3 | sed 's/^\t//'`

## 查找并删除重复文件
```bash
#!/bin/bash
#Filename: remove_duplicates.sh
ls -lS --time-style=long-iso | awk 'BEGIN { 
  getline; getline; 
  name1=$8; size=$5 
} 
{
  name2=$8; 
  if (size==$5) 
  { 
    "md5sum "name1 | getline; csum1=$1;
    "md5sum "name2 | getline; csum2=$1;
    if ( csum1==csum2 ) 
    {
      print name1; print name2
    }
  };

  size=$5; name1=name2; 
}' | sort -u > duplicate_files 

cat duplicate_files | xargs -I {} md5sum {} | sort | uniq -w 32 | awk '{ print "^"$2"$" }' | sort -u >  duplicate_sample

echo Removing..
comm duplicate_files duplicate_sample  -2 -3 | tee /dev/stderr | xargs rm
echo Removed duplicates files successfully.
```

## 文件权限、所有权和粘滞位
* 查看文件权限：`ls -l`  
第一列明确了后面的输出
`-` – 普通文件
`d` – 目录
`c` – 字符设备
`b` – 块设备
`l` – 符号链接
`s` – 套接字
`p` – 管道

`u`：用户权限
`g`：用户组权限
`o`：其他用户权限

`r`：读权限，指明是否允许读取目录中的文件和子目录
`w`：写权限，指明是否允许创建或删除文件和目录
`x`：执行权限，指明是否可以访问目录中的文件和子目录

`S`：出线在x的位置，只有拥有者具有该权限
`t`或`T`：粘滞位，出线在x的位置。当一个目录设置了粘滞位，只有创建该目录的用户才能删除目录中的文件，即使用户组和其他用户也有写权限

第一组的三个字符对应用户权限（所有者），第二组的三个字符对应用户组权限，第三组的三个字符对应其他用户权限

* 设置权限：`chmod u=rwx g=rw o=r filename`
* 更改所有权：`chown user.group filename`
* 设置粘滞位：`chmod a+t directory_name`
* 以递归的方式设置权限：`chmod 777 . -R`
* 以递归的方式设置所有权：`chown user.group . -R`
* 以不同的用户运行可执行文件：`chown root.root executable_file ; chmod +s executable_file ; ./executable_file`
* setuid的使用有限制，为了安全，它只能应用在Linux ELF格式二进制文件上，而不能用于脚本文件

## 创建长路径目录
* 创建目录：``


## 创建不可修改文件(immutable)
* 将文件设置为不可修改：`chattr +i file`
* 移除不可修改属性：`chattr -i file`

## 批量生成空白文件
* 创建文件：`touch filename`
* 批量生成名字不同的空白文件：
    ```bash
    for name in {1..100}.txt
    do
    touch $name
    done
    ```
* 如果文件已存在，touch命令会将该文件的时间戳修改为当前时间，`touch -a`只更改访问时间，`touch -m`只更改修改时间
* 为时间戳指定时间日期：`touch -d "Fri Jun 25 20:50:14 IST 1999" filename`  
-d使用的日期串不一定总以同样的格式呈现，可以接受任何标准日期格式

## 查找符号链接及其指向目标
符号链接类似Windows中的快捷方式，删除符号链接不会影响原始文件
* 创建符号链接：`ln -s target symbolic_link_name`
* 打印当前目录下的符号链接：`ls -l | grep "^l"`或`find . -type l -print`
* 打印符号链接指向的目标：`readlink web`

## 列举文件类型统计信息
* 打印文件类型信息：`file filename`
* 打印不包括文件名在内的文件类型信息：`file -b filename`

## 环回文件与挂载
* 创建一个1GB大小的文件：`dd if=/dev/zero of=loobackfile.img bs=1G count=1`
* 将该文件格式化成ext4文件系统：`mkfs.ext4 loopbackfile.img`
* 挂载环回文件：`mkdir /mnt/loopback ; mount -o loop loopbackfile.img /mnt/loopback`

## 生成ISO文件及混合ISO

## 查找文件差异并进行修补
* 查找两文件的差异（非一体化输出）：`diff version1.txt version2.txt`
* 查找两文件的差异（一体化输出，可读性更好）：`diff -u version1.txt version2.txt`
参数：-N（将所有缺失的文件视为空文件）、-a（将所有文件视为文本文件）、-r（遍历目录下的所有文件）
* 将diff的输出重定向到文件：`diff -u version1.txt version2.txt > version.patch`
* 修补文件：`patch -p1 version1.txt < version.patch`
* 撤销修补：`patch -p1 version1.txt < version.patch `

## head与tail
* 打印文件的前十行：`head file`或
* 从stdin读取数据：`cat text | head`
* 指定打印前几行：`head -n 4 file`
* 打印除了最后5行之外所有的行：`head -n -5 file`

* 打印文件的最后十行：`tail file`
* 从stdin读取数据：`cat text | tail`
* 指定打印最后几行：`tail -n 5 file`
* 打印除了5前行之外所有的行：`head -n +6 file`
* 监视不断增加内容的文件内容：`tail -f growing_file`或`dmesg | tail -f`

## 只列出目录的其他方法
* `ls -d */`
* `ls -F | grep "/$"`
* `ls -l | grep "^d"`
* `find . -type d -maxdepth 1 -print`

## 在命令行中用pushd和popd快速定位
* 将路径压入栈：`pushd /var/www`
* 再次压入栈：`pushd /usr/src`
* 查看栈内容：`dirs`
* 切换到列表中任意一个路径：`pushd +3`
* 从栈中删除路径（最近压入栈的路径）：`popd`
* 只涉及两个位置：`cd -`

## 统计文件的行数、单词数和字符数
* 统计行数：`wc -l file`或`cat file | wc -l`
* 统计单词数：`wc -w file`
* 统计字符数：`wc -c file`
* 打印三者：`wc file`

## 打印目录树
* 打印目录树：`tree ~/unixfs`
* 重点标记出匹配某种样式的文件：`tree PATH -P PATTERN`
* 重点标记出除了符合某种样式之外的文件：`tree PATH -I PATTERN`
* 同时打印文件和目录的大小：`tree -h`
* HTML输出（当前目录用`.`）：`tree PATH -H http://localhost -o out.html`




# 让文本飞
## 正则表达式入门
http://opensourceforu.com/2011/04/sed-explained-part-1/  
http://www.regexper.com

## 用grep在文件中搜索文本
* 搜索文件：`grep pattern filename`
* 对多个文件进行搜索：`grep "match_text" file1 file2 file3`
* 从stdin读取：`echo -e "this is a word\nnext line" | grep word`
* 重点标记匹配到的单词：`grep word filename --color=auto`
* 正则匹配：`grep -E "[a-z]+" filename`或`egrep "[a-z]+" filename`
* 只输出文件中匹配到的部分：`echo this is a line. | egrep -o "[a-z]+\."`
* 打印除包含match_pattern的行之外的所有行：`grep -v match_pattern file`
* 统计文件或文本中包含匹配字符串的行数：`grep -c "text" filename`
* 递归搜索文件：`grep "text" . -R -n`
* 忽略样式中的大小写：`echo hello world | grep -i "HELLO"`
* 匹配多个样式：`grep -e "pattern1" -e "pattern"`
* 在搜索中包括或排除文件：  
`grep "main()" . -r  --include *.{c,cpp}`  
`grep "main()" . -r --exclude "README"`  
* 使用0值字节后缀的grep与xargs：`grep "test" file* -lZ | xargs -0 rm`
* 静默输出：`grep -q "match_text" filename`
* 打印出匹配文本之前或之后的行（-A：结果之后，-B：结果之前，-C：结果之前及之后）：
`seq 10 | grep 5 -A 3`
`seq 10 | grep 5 -B 3`
`seq 10 | grep 5 -C 3`

## 用cut按列切分文件





# 一团乱麻？没这回事
## 网站下载
* 下载网页或远程文件：`wget URL`
* 指定重试次数（t=0则不断重试）：`wget -t 5 URL`
* 下载限速（k/m指定）：`wget --limit-rate 20k URL`
* 指定最大下载配额：`wget -Q 100m URL`
* 断点续传：`wget -c URL`
* 用cURL下载（并不将下载数据写入文件）：`curl URL > index.html`
* 复制或镜像整个网站：`wget --mirror --convert-links URL`或`wget -r -N -l -k DEPTH URL`
* 访问需要认证的HTTP/FTP页面：`wget --user username --password pass URL`

## 以格式化纯文本形式下载网页
* 将网页以ASCII字符的形式下载到文件：`lynx -dump URL > webpage_as_text.txt`

## cURL入门
* 基本使用：`curl URL`
* 不显示进度信息：`curl URL --silent`
* 下载数据写入文件：`curl URL -O filename`
* 在下载过程中显示形如#的进度条：`curl URL --progress`
* 断点续传：`curl URL/file -C offset`或`curl -C - URL`（自动计算偏移量）
* 设置参照页字符串：`curl --referer Referer_URL target_URL`
* 设置cookie：`curl URL --cookie "user=slynux;pass=hack"`
* 将cookie存为一个文件：`curl URL --cookie-jar cookie_file`
* 设置用户代理字符串：  
`curl URL --user-agent "Mozilla/5.0"`  
`curl -H "Host: www.slynux.org" -H "Accept-language: en" URL`（HTTP头部信息）
* 限定可占用的带宽：`curl URL --limit-rate 20k`
* 指定最大下载量：`curl URL --max-filesize bytes`
* 用cURL进行认证：`curl -u user:pass http://test_auth.com`
* 只打印响应头部信息×不包括数据部分）：`curl -I URL`

## 从命令行访问Gmail

## 解析网站数据

## 制作图片抓取器及下载工具

## 网页相册生成器

## Twitter命令行客户端

## 基于Web后端的定义查询工具

## 查找网站中的无效链接

## 跟踪网站变更

## 以POST方式发送网页并读取响应
* curl：`curl URL -d "key1=val1&key2=val2"`
例如：`curl 115.159.188.200:8001/do_login/ -d "name=admin&pwd=9876543210"`
* wget：`wget URL -post-data "key1=val1&key2=val2" -o output.html`



# Backup计划
## 用tar归档

`-c`：创建文件
`-f`：指定文件名
`-r`：向归档文件追加文件
`-x`：从归档文件提取文件或文件夹
`-C`：指定目录
`-a`：根据扩展名自动进行压缩
`-A`：合并多个tar文件
`-t`：
`-u`：检查时间戳来指明只有比归档文件中的同名文件更新的时候才进行添加
`-d`：打印归档文件中的文件与文件系统中的同名文件是否有差别
`-v`：归档过程中获取更多细节
`--delete`：从归档文件中删除文件
`--exclude`：排除文件
`--exclude-vcs`：排除版本控制目录


`-j`：指定.tar.bz2（bunzip2）
`-z`：指定.tar.gz（gzip）
`--lzma`：指定.tar.lzma格式（lzma）



* `tar -cf output.tar [SOURCES]`


## 用cpio归档
* 归档：`echo file1 file2 file3 | cpio -ov > archive.cpio`
* 列出归档文件内容：`cpio -it < archive.cpio`
* 提取：`cpio -id < archive.cpio`

`-o`：指定输出目录
`-v`：打印归档文件列表
`-i`：指定输入
`-t`：列出归档文件中的内容
`-d`：提取内容

## 用gunzip或gzip压缩
* 压缩（删除原文件）：`gzip filename`
* 解压（删除原文件）：`gunzip filename.gz`
* 指定压缩级别：`--fast`最低压缩比、`--best`最高压缩比
* 压缩归档文件：  
`tar -czvvf archive.tar.gz [FILES]`  
`tar -cavvf archive.tar.gz [FILES]`（-a指定从文件扩展名自动判断压缩格式）  


## 用bunzip或bzip压缩

## 用lzma压缩

## 用zip归档和压缩

## 超高压缩率的squashfs文件系统

## 加密工具与散列

## 用rsync备份系统快照
* 将源目录复制到目的端（创建镜像）：`rsync -av source_path destination_path`
* 将数据备份到服务器：`rsync -av source_dir username@host:PATH`
* 将远程主机上的数据恢复到本地主机：`rsync -av username@host:PATH destination`
* 使用数据压缩：`rsync -avz source destination`
* 如果路径有/结尾，则只复制目录下的内容，如果没有/结尾，则会将目录本身也复制到目的端
* 排除部分文件：`rsync -avz source_path destination_path --exclude "*.txt"`
* 在更新备份时，删除不存在的文件：`rsync -avz SOURCE DESTINATION --delete`
* 定期调度备份：`crontab -ev`

## 用Git备份版本控制

## 用dd克隆磁盘



# 无网不利
## 联网知识入门
`ifconfig`：用于显示网络接口、子网掩码等详细信息

* 打印网络接口列表：`ifconfig | cut -c-10 | tr -d ' ' | tr -s '\n'`
* IP地址的分配与显示：`ifconfig iface_name`  
HWaddr：硬件地址（MAC地址）  
inet addr：IP地址  
Bcast：广播地址  
Mask：子网掩码  
* 提取IP地址：`ifconfig wlan0 | egrep -o "inet addr:[^ ]*" | grep -o "[0-9.]*"`
* 设置网络接口的IP地址（root身份）：`ifconfig wlan0 192.168.0.80`
* 设置IP地址的子网掩码：`ifconfig wlan0 192.168.0.80 netmask 255.255.255.0`
* 硬件地址（MAC地址）欺骗：`ifconfig eth0 hw ether 00:1c:bf:87:25:d5`
* 查看分配给当前系统的名字服务器：`cat /etc/resolv.conf`
* 手动添加名字服务器：`echo nameserver IP_ADDRESS >> /etc/resolv.conf`
* 获取域名对应的IP地址：`ping google.com`
* DNS查找：`host google.com`
* DNS资源记录：`nslookup google.com`
* 添加hosts：`echo IP_ADDRESS symbolic_name >> /etc/hosts`
* 显示路由表：`route`或`route -n`
* 设置默认网关：`route add default gw IP_ADDRESS INTERFACE_NAME`
* traceroute：`traceroute google.com`

## 使用ping
* 检查网络上两点的连通性：`ping ADDRESS`
    返回结果：
    ```
    7 packets transmitted, 7 received, 0% packet loss, time 6004ms
    rtt min/avg/max/mdev = 50.822/53.150/56.984/1.873 ms
    ```
    最小RTT为50.822ms，平均RTT为53.150ms，最大RTT为56.984ms，mdev代表平均偏差为1.873 ms

* 限制发送的分组数量：`ping ADDRESS -c 2`
* ping命令的返回状态：
    ```bash
    $ ping domain -c2
    if [ $? -eq 0 ];
    then
    echo Successful ;
    else
    echo Failure
    fi
    ```

## 列出网络上所有的活动主机
```bash
#!/bin/bash
#Filename: ping.sh
# Change base address 192.168.0 according to your network.

for ip in 192.168.0.{1..255} ;
do
  ping $ip -c 2 &> /dev/null ;
  
  if [ $? -eq 0 ];
  then
    echo $ip is alive
  fi
done
```

或者使用已有的工具查询网络上的主机状态：`fping -a 192.160.1/24 -g 2> /dev/null`

## 传输文件
1. FTP
* FTP连接：`lftp username@ftphost`
* 下载文件：`get filename`
* 上传文件：`put filename`
* `lcd`改变本机目录，`cd`改变远程服务器目录
* 断开连接：`quit`
* 

2. SFTP (Secure FTP)
* SFTP连接：`sftp user@domainname`
* 指定端口连接：`sftp -oPort=422 user@domainname`

3. rsync

4. SCP (secure copy program)
* 复制文件到远程服务器：`scp filename user@remotehost:/home/path`
* 从远程服务器下载文件：`scp user@remotehost:/home/path/filename filename`
* 递归复制：`scp -r /home/slynux user@remotehost:/home/backups`
* 如果SSH没有运行在22端口，使用`-oPort`，并采用和sftp相同的语法
* `-p`选项能在复制文件的同时保留文件的权限和模式

5. SSH
* SSH连接：`ssh username@remote_host`或`ssh -l username remote_host`
* 指定端口连接：`ssh user@remote_host -p 422`
* 执行命令：`ssh user@host 'command1 ; command2 ; command3'`
* 对所有数据请求压缩：`ssh -C user@hostname COMMANDS`
* 打开调试模式：`ssh -v username@remote_host`
* X11 forwarding：`ssh -X user@host "command1; command2"`

## 用脚本设置以太网与无线LAN
```bash
# iwlist scan
wlan0     Scan completed : 
          Cell 01 - Address: 00:12:17:7B:1C:65
                    Channel:11
                    Frequency:2.462 GHz (Channel 11) 
                    Quality=33/70  Signal level=-77 dBm
                    Encryption key:on
                    ESSID:"model-2" 
```

## 用SSH实现无密码自动登录
1. 创建SSH密钥，这需要登录到远程主机：`ssh-keygen -t rsa`
2. 将生成的公钥传输到远程主机，并将其加入文件`~/.ssh/authorized_keys`中：
`ssh USER@REMOTE_HOST "cat >> ~/.ssh/authorized_keys" < ~/.ssh/id_rsa.pub`

## 用SSH在远程主机上运行命令

## 在本地挂载点上挂载远程驱动器
`sshfs`允许将远程文件系统挂载到本地挂载点上
1. `sshfs -o allow_other user@remotehost:/home/path /mnt/mountpoint`
现在位于远程主机/home/path中的数据就可以通过本地挂载点/mnt/mountpoint来访问了
2. 卸载：`umount /mnt/mountpoint`

## 在网络上发送多播式窗口消息

## 网络流量与端口分析
* 列出系统中的开放端口以及运行在端口上的服务的详细信息：`lsof -i`
* 列出本地主机当前的开放端口：`lsof -i | grep ":[0-9]\+->" -o | grep "[0-9]\+" -o  | sort | uniq`
* 用netstat查看开放端口与服务：`netstat -tnp`

## Creating arbitrary sockets
`netcat`
1. Set up the listening socket：`nc -l 1234`
2. Connect to the socket ：`nc HOST 1234`

Quickly copying files over the network：  
1. On the receiver machine：`nc -l 1234 > destination_filename`
2. On the sender machine：`nc HOST 1234 < source_filename`

## 分享网络连接
使用iptables设置Network Address Translation (NAT)
```bash
#!/bin/bash
#filename: netsharing.sh
echo 1 > /proc/sys/net/ipv4/ip_forward
iptables -A FORWARD -i $1 -o $2 -s 10.99.0.0/16 -m conntrack --ctstate NEW -j ACCEPT
iptables -A FORWARD -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT
iptables -A POSTROUTING -t nat -j MASQUERADE
```
使用：`./netsharing.sh eth0 wlan0`

## Basic firewall using iptables
Block traffic to a specific IP address：`#iptables -A OUTPUT -d 8.8.8.8 -j DROP`
清除改变：`#iptables --flush`



# 当个好管家
## 统计磁盘的使用情况
* 找出文件占用的磁盘空间（以字节为单位）：`du FILENAME1 FILENAME2`
* 获得目录中所有文件的磁盘使用情况，并在每一行显示各文件的占用详情：`du -a DIRECTORY`
* 以KB、MB或块为单位显示：`du -h FILENAME`
* 显示磁盘使用总计：`du -c FILENAME1 FILENAME`
* 只输出合计数据：`du -s FILES(s)`或`du -sh DIRECTORY`
* 用特定的单位打印文件：`-b`字节，`-k`KB，`-m`MB，`-B`块
* 从磁盘使用统计中排除部分文件：`du --exclude "*.txt" FILES(s)`
* 指定遍历的最大深度：`du --max-depth 2 DIRECTORY`
* 找出指定目录中最大的10个文件（包含目录）：`du -ak SOURCE_DIR | sort -nrk 1 | head`  
只含有文件：`find . -type f -exec du -k {} \; | sort -nrk 1 | head`
* 磁盘可用空间信息：`df -h`

## 计算命令执行时间
* 显示执行某命令的时间：`time COMMAND`

## 与当前登录用户、启动日志及启动故障的相关信息
* 获取当前登录用户的相关信息：`who`或`w`
* 列出去当前登录主机的用户列表（如果打开多个伪终端将显示多个同一用户）：`users`
排除重复用户：`users | tr ' ' '\n' | sort | uniq`
* 查看系统通电运行时间：`uptime`
* 获取前一次的启动及用户登录会话信息：`last`
* 获取单个用户登录会话信息：`last USER`
* 重启信息：`last reboot`
* 获取失败的用户登录会话信息：`lastb`

## 打印出10条最常使用的命令
命令历史列表存储在`~/.bash_history`

## 列出1小时内占用CPU最多的10个进程

## 用watch监视命令输出
* 以固定间隔监视命令输出（默认2秒1次）：`watch 'COMMAND'`
* 设置间隔：`watch -n 5 'COMMAND'`
* 高亮差异：`watch -d 'COMMANDS'`

## 对文件及目录访问进行记录
安装inotify-tools
`inotifywait -m -r -e create,move,delete PATH -q`

| Event | Description |
| --- | --- |
| access | When a read happens to a file. |
| modify | When file contents are modified. |
| attrib | When metadata is changed. |
| move | When a file undergoes a move operation. |
| create | When a new file is created. |
| open | When a file undergoes an open operation. |
| close | When a file undergoes a close operation. |
| delete | When a file is removed. |

## 用logrotate管理日志文件

## 用syslog记录日志

## 通过监视登录找出入侵者

## 监视远程磁盘的健康情况

## 找出系统中用户的活动时段




# 管理重任
## 收集进程信息
1. ps
* 收集进程信息：`ps`
* 显示包含更多信息的更多列：`ps -f`
* 获取运行在系统中的每一个进程的信息：`ps -e`或`ps -ax`
* 运行`ps -e`或`ps -ef`，要么是`ps -ax`或`ps -axf`
* -o过滤想要显示的列：`ps [OPTIONS] -o parameter1,parameter2,parameter3`
Parameter | Description
--- | ---
pcpu | Percentage of CPU
pid | Process ID
ppid | Parent Process ID
pmem | Percentage of memory
comm | Executable filename
cmd | Simple command
user | The user who started the process
nice | The priority (niceness)
time | Cumulative CPU time
etime | Elapsed time since the process started
tty | The associated TTY device
euid | The effective user
stat | Process state
* 根据参数对ps输出进行排序：`ps [OPTIONS] --sort -paramter1,+parameter2,parameter3`
* 找出给定命令名对应的进程ID：`ps -C COMMAND_NAME`或`ps -C COMMAND_NAME -o pid=`（移除每一列头部）

2. top
* 收集进程信息：`top`

3. pgrep
* 收集进程信息：`pgrep COMMAND`
* 指定输出定界符：`pgrep COMMAND -d ":"`
* 指定进程的用户（拥有者）列表：`pgrep -u root,slynux COMMAND`
* 返回所匹配的进程数量：`pgrep -c COMMAND`
* 根据真实用户或ID以及有效用户或ID过滤ps输出：`ps -u root -U root -o user,pcpu`
* 用TTY过滤ps输出：`ps -t TTY1, TTY2`
* 进程线程的相关信息：`ps -eLf`或`ps -eLf --sort -nlwp | head`
* 指定输出宽度以及所要显示的列：  
-f ps -ef  
u ps -e u  
ps ps -e w (w stands for wide output)  
* 显示进程的环境变量：`ps -eo cmd e`

## 杀死进程以及发送或响应信号
* 列出所有可用的信号：`kill -l`
* 终止进程：`kill PROCESS_ID_LIST`
* 通过命令向进程发送指定的信号：`kill -s SIGNAL PID`  
SIGHUP 1: 对控制进程或终端进行挂起检测  
SIGINT 2: 当按下Ctrl+C时发送该信号  
SIGKILL 9: 用于强行杀死进程  
SIGTERM 15: 默认用于终止进程  
SIGTSTP 20: 当按下Ctrl+Z时发送该信号  
* 强制杀死进程：`kill -s SIGKILL PROCESS_ID`或`kill -9 PROCESS_ID`
* 杀死一组命令：`killall process_name`

## which、whereis、file、whatis与平衡负载

## 向用户终端发送消息
* 向终端中所有的当前登录用户发送广播消息：`cat message | wall`或`wall< message`
* 允许写入消息：`mesg y`，禁止写入消息：`mesg n`

## 收集系统信息
* 打印当前系统的主机名：`hostname`或`uname -n`
* 打印Linux内核版本、硬件架构等信息：`uname -a`
* 打印内核发行版本：`uname -r`
* 打印主机类型：`uname -m`
* 打印CPU的相关信息：`cat /proc/cpuinfo`
* 获取处理器名称：`cat /proc/cpuinfo | sed -n 5p`
* 打印内存的详细信息：`cat /proc/meminfo`
* 打印系统可用内存总量：`cat /proc/meminfo  | head -1`
* 列出系统的分区信息：`cat /proc/partitions`或`fdisk -l`
* 获取系统的详细信息（推荐root身份查看）：`lshw`

## 用`/proc`收集信息

## 用cron进行调度
crontab

## 从Bash中读写MySQL数据库
1. 建表
    ```bash
    #!/bin/bash
    #Filename: create_db.sh
    #Description: Create MySQL database and table

    USER="user"
    PASS="user"

    mysql -u $USER -p$PASS <<EOF 2> /dev/null
    CREATE DATABASE students;
    EOF

    [ $? -eq 0 ] && echo Created DB || echo DB already exist 
    mysql -u $USER -p$PASS students <<EOF 2> /dev/null
    CREATE TABLE students(
    id int,
    name varchar(100),
    mark int,
    dept varchar(4)
    );
    EOF

    [ $? -eq 0 ] && echo Created table students || echo Table students already exist 

    mysql -u $USER -p$PASS students <<EOF
    DELETE FROM students;
    EOF
    ```

2. 插入
    ```bash
    #!/bin/bash
    #Filename: write_to_db.sh
    #Description: Read from CSV and write to MySQLdb

    USER="user"
    PASS="user"

    if [ $# -ne 1 ];
    then
    echo $0 DATAFILE
    echo
    exit 2
    fi

    data=$1

    while read line;
    do

    oldIFS=$IFS
    IFS=,
    values=($line)
    values[1]="\"`echo ${values[1]} | tr ' ' '#' `\""
    values[3]="\"`echo ${values[3]}`\""


    query=`echo ${values[@]} | tr ' #' ', ' `
    IFS=$oldIFS

    mysql -u $USER -p$PASS students <<EOF
    INSERT INTO students VALUES($query);
    EOF

    done< $data
    echo Wrote data into DB
    ```

3. 查询
    ```bash
    #!/bin/bash
    #Filename: read_db.sh
    #Description: Read from the database

    USER="user"
    PASS="user"

    depts=`mysql -u $USER -p$PASS students <<EOF | tail -n +2
    SELECT DISTINCT dept FROM students;
    EOF`

    for d in $depts;
    do

    echo Department : $d

    result="`mysql -u $USER -p$PASS students <<EOF
    SET @i:=0;
    SELECT @i:=@i+1 as rank,name,mark FROM students WHERE dept="$d" ORDER BY mark DESC;
    EOF`"

    echo "$result"
    echo

    done
    ```

4. `studentdata.csv`
    ```
    1,Navin M,98,CS
    2,Kavya N,70,CS
    3,Nawaz O,80,CS
    4,Hari S,80,EC
    5,Alex M,50,EC
    6,Neenu J,70,EC
    7,Bob A,30,EC
    8,Anu M,90,AE
    9,Sruthi,89,AE
    10,Andrew,89,AE
    ```

## 用户管理脚本

## 图像文件的批量缩放及格式转换