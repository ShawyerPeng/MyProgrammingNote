## 1. while 语句
当 conditional 为真时，执行 code。  
语法中 do 或 : 可以省略不写。但若要在一行内写出 while 式，则必须以 do 或 : 隔开条件式或程式区块。

    while conditional [do]
       code
    end
```ruby
$i = 0
$num = 5

while $i < $num  do
   puts("在循环语句中 i = #$i" )
   $i +=1
end
```

## 2. while 修饰符
当 conditional 为真时，执行 code。  
如果 while 修饰符跟在一个没有 rescue 或 ensure 子句的 begin 语句后面，code 会在 conditional 判断之前执行一次。

    code while condition

    或者

    begin
      code
    end while conditional
```ruby
$i = 0
$num = 5
begin
   puts("在循环语句中 i = #$i" )
   $i +=1
end while $i < $num
```

## 3. until 语句
当 conditional 为假时，执行 code。  
语法中 do 可以省略不写。但若要在一行内写出 until 式，则必须以 do 隔开条件式或程式区块。

    until conditional [do]
       code
    end
```ruby
$i = 0
$num = 5

until $i > $num  do
   puts("在循环语句中 i = #$i" )
   $i +=1;
end
```

## 4. until 修饰符
当 conditional 为 false 时，执行 code。  
如果 until 修饰符跟在一个没有 rescue 或 ensure 子句的 begin 语句后面，code 会在 conditional 判断之前执行一次。

    code until conditional

    或者

    begin
       code
    end until conditional
```ruby
$i = 0
$num = 5
begin
   puts("在循环语句中 i = #$i" )
   $i +=1;
end until $i > $num
```

## 5. for 语句
先计算表达式得到一个对象，然后针对 expression 中的每个元素分别执行一次 code。

    for variable [, variable ...] in expression [do]
       code
    end
```ruby
for i in 0..5
   puts "局部变量的值为 #{i}"
end
```
for...in 循环几乎是完全等价于：

    (expression).each do |variable[, variable...]| code end

但是，for 循环不会为局部变量创建一个新的作用域。  
语法中 do 可以省略不写。但若要在一行内写出 for 式，则必须以 do 隔开条件式或程式区块。
```ruby
(0..5).each do |i|
   puts "局部变量的值为 #{i}"
end
```

## 6. break 语句
```ruby
for i in 0..5
   if i > 2 then
      break
   end
   puts "局部变量的值为 #{i}"
end
```

## 7. next 语句
```ruby
for i in 0..5
   if i < 2 then
      next
   end
   puts "局部变量的值为 #{i}"
end
```

## 8. redo 语句
重新开始最内部循环的该次迭代，不检查循环条件。如果在块内调用，则重新开始 yield 或 call。
```ruby
for i in 0..5
   if i < 2 then
      puts "局部变量的值为 #{i}"
      redo
   end
end
```

# 参考资料
[Ruby 循环](http://www.runoob.com/ruby/ruby-loop.html)
