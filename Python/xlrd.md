### 1. 打开excel文件
`workbook = xlrd.open_workbook(r'excel.xls',encoding='cp1252')`  
### 2. 获取sheet
工作表的号码：`print("The number of worksheets is",workbook.nsheets)`  
获取所有sheet：`print(workbook.sheet_names())`  
获取某个sheet：`sh_name = workbook.sheet_names()[0]`  

`nsheets`属性是一个整数，包含工作簿sheet的数量。与`sheet_by_index`方法结合起来是获取独立sheet最常用的方法。  
`sheet_names`方法返回包含工作簿中所有sheet名字的unicode列表。  
单独的sheet可以通过`sheet_by_name`方法使用这些名字获取。  
`sheets`方法的结果是迭代获取工作簿中的每个sheet。  
```python
for sheet_index in range(workbook.nsheets):  
    print(workbook.sheet_by_index(sheet_index))  
```
```python
for sheet_name in workbook.sheet_names():  
    print(workbook.sheet_by_name(sheet_name))
```
```python
for sheet in workbook.sheets():  
    print(sheet)
```
 xlrd.Book对象有与工作簿内容相关的其它属性，但很少用到：`codepage` `countries` `user_name`



### 3. 根据sheet索引或者名称获取sheet内容
根据sheet索引获取sheet内容：`sh = workbook.sheet_by_index(0)`  
根据sheet名称获取sheet内容：`sh = workbook.sheet_by_name('sheet2')`  
这是第三种方法获取sheet内容：`sh = workbook.sheets()[0]`
```python
book = open_workbook('odd.xls')  
sheet = book.sheet_by_index(0)  

for row_index in range(sheet.nrows):  
    for col_index in range(sheet.ncols):  
        print(cellname(row_index,col_index),'-',)  
        print(sheet.cell(row_index,col_index).value)  
```
### 4. sheet的名称，行数，列数
`sh.name,sh.nrows,sh.ncols`  
xlrd.sheet.Sheet对象有其他一些与worksheet内容相关的属性，但很少使用：
`col_label_ranges` `row_label_ranges` `visibility`
### 5. 获取整行和整列的值（数组）
获取第四行内容：`rows = sh.row_values(3)`  
获取第三列内容：`cols = sh.col_values(2)`
### 6. 获取单元格内容
`print(sh.cell(1, 0).value.encode('utf-8'))`  
`print(sh.cell_value(1, 0).encode('utf-8'))`  
`print(sh.row(1)[0].value.encode('utf-8'))`  
### 操作行列和单元格
Sheet对象的cell方法能用来返回特定单元格的内容。  
`print("Cell D30 is", sh.cell_value(rowx=29, colx=3))`  
`print("Cell D30 is", sh.cell(29,3).value)`  
```python
book = open_workbook('odd.xls')  
sheet = book.sheet_by_index(1)  

cell = sheet.cell(0,0)  
print(cell)  #value包含了单元格的真实值，ctype包含了单元格的类型
print(cell.value)
print(cell.ctype==XL_CELL_TEXT)  

for i in range(sheet.ncols):  
    print(sheet.cell_type(1,i),sheet.cell_value(1,i))
```

### 7. 获取单元格内容的数据类型
`print(sh.cell(1, 0).ctype)`
ctype : 0 empty,1 string, 2 number, 3 date, 4 boolean, 5 error
###  迭代
`for rx in range(sh.nrows):
    print(sh.row(rx))`


### 迭代sheet的内容
`row`和`col`方法分别返回一整行（列）的Cell对象。  
`row_slice`和`col_slice`方法分别返回一行（列）中以开始索引和一个可选的结束索引为边界的Cell对象列表。  

`row_types`和`col_types`方法分别返回一行（列）中以开始索引和一个可选的结束索引为边界的表示单元格类型的整数列表。  
`row_values`和`col_values`方法分别返回一行（列）中以开始索引和一个可选的结束索引为边界的表示单元格值的对象列表。  

```python
book = open_workbook('odd.xls')  
sheet0 = book.sheet_by_index(0)  
sheet1 = book.sheet_by_index(1)  

print sheet0.row(0)  
print sheet0.col(0)  
print  
print sheet0.row_slice(0,1)  
print sheet0.row_slice(0,1,2)  
print sheet0.row_values(0,1)  
print sheet0.row_values(0,1,2)  
print sheet0.row_types(0,1)  
print sheet0.row_types(0,1,2)  
print  
print sheet1.col_slice(0,1)  
print sheet0.col_slice(0,1,2)  
print sheet1.col_values(0,1)  
print sheet0.col_values(0,1,2)  
print sheet1.col_types(0,1)  
print sheet0.col_types(0,1,2)  
```

### 实用方法
当围绕workbook进行操作的时候，把行和列转换成用户习惯看到的Excel单元格引用（如：(0,0)转换成A1），这是很有用的。下面提供的方法帮助我们实现它：  
`cellname`方法把一对行和列索引转换为一个对应的Excel单元格引用。  
`cellnameabs`方法把一对行和列索引转换为一个绝对的Excel单元格引用（如：$A$1）。  
`colname`方法把一个列索引转换为Excel列名。  
```python
from xlrd import cellname, cellnameabs, colname  

print cellname(0,0),cellname(10,10),cellname(100,100)  
print cellnameabs(3,1),cellnameabs(41,59),cellnameabs(265,358)  
print colname(0),colname(10),colname(100)  
```

### xlwt
```python
xlwt Quick Start
import xlwt
from datetime import datetime

style0 = xlwt.easyxf('font: name Times New Roman, color-index red, bold on',
    num_format_str='#,##0.00')
style1 = xlwt.easyxf(num_format_str='D-MMM-YY')

wb = xlwt.Workbook()
ws = wb.add_sheet('A Test Sheet')

ws.write(0, 0, 1234.56, style0)
ws.write(1, 0, datetime.now(), style1)
ws.write(2, 0, 1)
ws.write(2, 1, 1)
ws.write(2, 2, xlwt.Formula("A3+B3"))

wb.save('example.xls')
```

# 参考资料
1. [Python Excel Tutorial 指南](http://huaxia524151.iteye.com/blog/1173828)
2. [python中使用xlrd、xlwt操作excel表格详解](http://www.jb51.net/article/60510.htm)
3. [python excel 的相关操作](https://my.oschina.net/yixiusztx/blog/70437)
4. [Python操作Excel](https://yangwenbo.com/articles/python-excel-lib.html)
5. [Reading Excel with Python xlrd](https://blogs.harvard.edu/rprasad/2014/06/16/reading-excel-with-python-xlrd/)
