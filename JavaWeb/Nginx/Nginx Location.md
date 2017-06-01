## URI匹配
`location [=|~|~*|^~|@] /uri/ {...}``
* `=`：精确匹配
* `~`：大小写敏感的正则表达式匹配
* `~*`：大小写不敏感的正则表达式匹配
* `^~`：非正则表达式，匹配以xxx开头的
* `@`：
* `uri`

```
location = / {
    # 完全匹配  =
    # 大小写敏感 ~
    # 忽略大小写 ~*
}
location ^~ /images/ {
    # 前半部分匹配 ^~
    # 可以使用正则，如：
    # location ~* \.(gif|jpg|png)$ { }
}
location / {
    # 如果以上都未匹配，会进入这里
}
```

### location匹配规则及优先级
1. `=` ：严格匹配这个查询。如果找到，停止搜索。
2. `^~`：匹配路径的前缀，如果找到，停止搜索。
3. `~` ：区分大小写的正则匹配
4. `~*`：不区分大小写的正则匹配

优先级： `=`, `^~`, `~/~*`, `无`

### 具体
1. `=`
请求的URI必须精确匹配指定的pattern，pattern只能是文本字符串，而不能时正则表达式
```
server {
    server_name website.com;
    location = /abcd {
    }
}
```
* Applies to `http://website.com/abcd` (exact match)
* May apply to `http://website.com/ABCD` (it is only case-sensitive if your operating system uses a case-sensitive filesystem)
* Applies to `http://website.com/abcd?param1&param2` (regardless of the query string arguments)
* Does not apply to `http://website.com/abcd/` (trailing slash)
* Does not apply to `http://website.com/abcde` (extra characters after the specified pattern)

2. No modifier
请求的URI必须以指定的pattern开头，不能使用正则表达式
```
server {
    server_name website.com;
    location /abcd {
    }
}
```
* Applies to `http://website.com/abcd` (exact match)
* May apply to `http://website.com/ABCD` (it is only case-sensitive if your operating system uses a case-sensitive filesystem)
* Applies to `http://website.com/abcd?param1&param2` (regardless of the query string arguments)
* Applies to `http://website.com/abcd/` (trailing slash)
* Applies to `http://website.com/abcde` (extra characters after the specified pattern)

3. `~`
请求的URI必须`大小写敏感`匹配指定的正则表达式，以`^`开头，`$`结尾
```
server {
    server_name website.com;
    location ~ ^/abcd$ {
    }
}
```
指定以`/`为前缀，`d`结尾

* Applies to `http://website.com/abcd` (exact match)
* Does not apply to `http://website.com/ABCD` (case-sensitive)
* Applies to `http://website.com/abcd?param1&param2` (regardless of the query string arguments)
* Does not apply to `http://website.com/abcd/` (trailing slash) due to the specified regular expression
* Does not apply to `http://website.com/abcde` (extra characters) due to the specified regular expression


4. `~*`
请求的URI必须`大小写不敏感`匹配指定的正则表达式，以`^`开头，`$`结尾
```
server {
    server_name website.com;
    location ~* ^/abcd$ {
    }
}
```
* Applies to `http://website.com/abcd` (exact match)
* Applies to `http://website.com/ABCD` (case-insensitive)
* Applies to `http://website.com/abcd?param1&param2` (regardless of the query string arguments)
* Does not apply to `http://website.com/abcd/` (trailing slash) due to the specified regular expression
* Does not apply to `http://website.com/abcde` (extra characters) due to the specified regular expression


5. `^~`
请求的URI必须以指定的pattern开头，如果pattern匹配到了，将会停止搜索其他的pattern

6. `@`
定义named location block，这些块不能被客户端访问，只能被try_files、error_page等内部指令的请求访问

