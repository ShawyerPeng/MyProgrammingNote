# 一、在路径中传参数
```java
@Controller
@RequestMapping("/json/test")
public class JsonController {
    @RequestMapping(value="{name}", method = RequestMethod.GET)
    public @ResponseBody
    User getUserInJSON(@PathVariable String name) {
        User user = new User();
        user.setUsername(name);
        user.setPassword("xiaoye");
        return user;
    }
}
```
![](http://p1.bpimg.com/567571/0b51f37bc5dbf35f.png)

