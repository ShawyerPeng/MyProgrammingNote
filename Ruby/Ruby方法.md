方法名应以小写字母开头。
## 语法
```ruby
def method_name [( [arg [= default]]...[, * arg [, &expr ]])]
   expr..
end
```

1. 一个简单的方法
```ruby
def method_name
       expr..
end```

2. 一个接受参数的方法
```ruby
def method_name (var1, var2)
       expr..
end```

3. 为参数设置默认值
```ruby
def method_name (var1=value1, var2=value2)
       expr..
end
```

当您要调用方法时，只需要使用方法名即可：`method_name`  
当您调用带参数的方法时，您在写方法名时还要带上参数：`method_name 25, 30`

## 从方法返回值 + return 语句
Ruby 中的每个方法默认都会返回一个值。这个返回的值是最后一个语句的值

return 语句用于从 Ruby 方法中返回一个或多个值。  
如果给出超过两个的表达式，包含这些值的数组将是返回值。如果未给出表达式，nil 将是返回值。  
`return [expr[',' expr...]]`

## 可变数量的参数
```ruby
def sample (*test)
   puts "参数个数为 #{test.length}"
   for i in 0...test.length
      puts "参数值为 #{test[i]}"
   end
end
sample "Zara", "6", "F"
sample "Mac", "36", "M", "MCA"
```

## 类方法
当方法定义在类的外部，方法默认标记为 private。另一方面，如果方法定义在类中的，则默认标记为 public。  
方法默认的可见性和 private 标记可通过模块（Module）的 public 或 private 改变。
当你想要访问类的方法时，您首先需要实例化类。然后，使用对象，您可以访问类的任何成员。  
Ruby 提供了一种不用实例化即可访问方法的方式。让我们看看如何声明并访问类方法：  
```ruby
class Accounts
   def reading_charge
   end
   def Accounts.return_date
   end
end
```
我们已经知道方法 return_date 是如何声明的。它是通过在类名后跟着一个点号，点号后跟着方法名来声明的。您可以直接访问类方法：`Accounts.return_date`

## alias 语句
这个语句用于为方法或全局变量起别名。别名不能在方法主体内定义。即使方法被重写，方法的别名也保持方法的当前定义。  
为编号的全局变量（$1, $2,...）起别名是被禁止的。重写内置的全局变量可能会导致严重的问题。  
语法：
`alias 方法名 方法名`
`alias 全局变量 全局变量`

## undef 语句
这个`undef 方法名`语句用于取消方法定义。undef 不能出现在方法主体内。  
通过使用 undef 和 alias，类的接口可以从父类独立修改，但请注意，在自身内部方法调用时，它可能会破坏程序。
