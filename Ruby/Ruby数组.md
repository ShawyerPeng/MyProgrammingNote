## 创建数组  
```ruby
    names = Array.new
    names = Array.new(20) #names.size或names.length返回数组的大小
    names = Array.new(4, "mac") #给数组中的每个元素赋值。puts "#{names}"得到["mac", "mac", "mac", "mac"]
    nums = Array.new(10) { |e| e = e * 2 } #使用带有 new 的块，每个元素使用块中的计算结果来填充
    nums = Array.[](1, 2, 3, 4,5)
    nums = Array[1, 2, 3, 4,5]
    digits = Array(0..9) #只接收单个参数的 Array 方法，该方法使用一个范围作为参数来创建一个数字数组
```

## 数组内建方法  
创建 Array 对象实例的方式：`Array.[](...) [or] Array[...] [or] [...]`  
这将返回一个使用给定对象进行填充的新数组。
```ruby
digits = Array[0,1,2]
digits2 = Array[1,2,3]
puts "#{digits&digits2}" #取交集，无重复
puts "#{digits|digits2}" #取并集，无重复
puts "#{digits+digits2}" #连接两个数组，有重复
puts "#{digits-digits2}" #从初始数组digits中移除了在digits2中出现的项的副本，有重复

puts "#{digits2<<4<<5}" #把给定的对象附加到数组的末尾。该表达式返回数组本身，所以几个附加可以连在一起。

puts "#{digits<=>digits2}" #如果数组digits小于、等于或大于digits2，则返回一个整数（-1、 0 或 +1）(从第一个元素开始比较)
puts "#{digits==digits2}" #如果两个数组包含相同的元素个数，且每个元素与另一个数组中相对应的元素相等（根据 Object.==），那么这两个数组相等




a = Array[1,2,3,4,5]
b = Array[2,3,4,5,6]
puts "#{a[0]}"  #a[start, length]
puts "#{a[0..2]}"  #a.slice(index)
puts "#{a.slice(0,2)}"  #a.slice(range)

a[0] = 5 #obj
array[0,1] =  #obj or an_array or nil
array[0..2] =  #obj or an_array or nil


a.at(0) #返回索引为0的元素
a.clear #从数组中移除所有的元素
array.collect { |item| block } [or] array.map { |item| block } #为 self 中的每个元素调用一次 block。创建一个新的数组，包含 block 返回的值
array.collect! { |item| block } [or] array.map! { |item| block } #为 self 中的每个元素调用一次 block，把元素替换为 block 返回的值


a.abbrev(pattern = nil) #为 self 中的字符串计算明确的缩写集合。如果传递一个模式或一个字符串，只考虑当字符串匹配模式或者以该字符串开始时的情况。
a.assoc(obj) #搜索一个数组，其元素也是数组，使用 obj.== 把 obj 与每个包含的数组的第一个元素进行比较。如果匹配则返回第一个包含的数组，如果未找到匹配则返回 nil。
```

## 参考资料
[Ruby 数组（Array）](http://www.runoob.com/ruby/ruby-array.html)
