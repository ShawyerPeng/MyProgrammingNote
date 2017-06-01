# 开始使用数据库MongoDB

1. 给数据库命名：
```python
client = pymongo.MongoClient('localhost',27017)
walden = client['walden']   #左边的是在python中使用的对象，右边的是在数据库中同步建立的对象
```

2. 在文件下创建表单：
```python
sheet_tab = walden['sheet_tab']
```

3. 往数据库中写入数据：
```python  
path = 'c:/Users/asus-pc/Desktop/walden.txt'    #打开一个本地文件开始读取数据
with open(path,'r') as f:
        lines = f.readlines()
        for index,line in enumerate(lines):
            data = {
                'index':index,
                'line':line,
                'words':len(line.split())
            }
            sheet_tab.insert_one(data)  #关键是这一步
```

4. 展示数据库中的数据：
```python
for item in sheet_tab.find():
        print(item)
```

5. 数据库的操作：
```python
for item in sheet_tab.find({'words':0}): #筛选出所有words值=0的数据并打印
        print(item)
for item in sheet_tab.find():  #打印所有line属性数据
        print(item['line'])
# $lt/$lte/$gt/$gte/$ne，依次等价于</<=/>/>=/!=。（l表示less g表示greater e表示equal n表示not）
for item in sheet_tab.find({'words':{'$lt':5}}):    #筛选出所有words值小于的数据并打印
        print(item)
```

# 完整代码
```python
import pymongo
client = pymongo.MongoClient('localhost',27017)
walden = client['walden']   #左边的是在python中使用的对象，右边的是在数据库中同步建立的对象

path = 'c:/Users/asus-pc/Desktop/walden.txt'    #打开一个本地文件开始读取数据
with open(path,'r') as f:
        lines = f.readlines()
        for index,line in enumerate(lines):
            data = {
                'index':index,
                'line':line,
                'words':len(line.split())
            }
            sheet_tab.insert_one(data)  #关键是这一步

for item in sheet_tab.find():
        print(item)

for item in sheet_tab.find({'words':0}): #筛选出所有words值=0的数据并打印
        print(item)
for item in sheet_tab.find():  #打印所有line属性数据
        print(item['line'])
# $lt/$lte/$gt/$gte/$ne，依次等价于</<=/>/>=/!=。（l表示less g表示greater e表示equal n表示not）
for item in sheet_tab.find({'words':{'$lt':5}}):    #筛选出所有words值小于的数据并打印
        print(item)
```
