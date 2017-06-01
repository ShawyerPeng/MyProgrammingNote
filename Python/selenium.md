`from selenium import webdriver`  
`from selenium.webdriver.common.keys import Keys`  
`from selenium.common.exceptions import NoSuchElementException`
`from selenium.webdriver.common.action_chains import ActionChains`
#### 1. 选择浏览器：
`driver = webdriver.Firefox()`
#### 2. 打开url(访问一个页面):
`driver.get("http://www.baidu.com")`
#### 3. 等待：  
`time.sleep(3)` -> 强制等待.
`driver.implicitly_wait(30)` -> 隐性等待.对整个driver的周期都起作用，所以只要设置一次即可   
`driver.set_page_load_timeout(30)`  
`driver.set_script_timeout(30)`  
#### 4. 关闭浏览器：
`driver.quit()/driver.close()`
#### 5. 前进/后退：
`driver.forward()/driver.back()`
#### 6. 刷新：
`driver.refresh()`
#### 7. 返回对象
`driver.title` -> 返回当前页面标题    
`driver.current_url` -> 返回当前页面url   
`driver.window_handles` -> 返回当前浏览器的所有窗口     
`driver.current_window_handle` -> 返回当前浏览器的窗口句柄   
#### 8. 窗口操作：
`driver.switch_to_window(“window_name”)` -> 选择窗口  
`driver.maximize_window()` -> 浏览器最大化  
`driver.set_window_size(480, 800)` -> 设置浏览器的高度为800像素，宽度为480像素  
#### 9. 对话框操作：  
`driver.switch_to_alert()` -> 选择窗口对象    
`accept()` -> 点击确认  
`dismiss()` -> 点击取消  
`text` -> 获取文本值  
`send_keys(“key”)` -> 输入值   
driver.find_element_by_id("mn_Nfb8a").send_keys(Keys.DOWN)   #driver.find_element_by_id("mn_Nfb8a").send_keys(Keys.ENTER)   Keys.TAB
ActionChains(driver).context_click(qqq).perform()  鼠标右键
#### 10. 定位  
`driver.find_element(by=”id”,value=None)` -> 定位元素(单个)  
`driver.find_element_by_id(“id_name”)` -> Id定位  
`driver.find_element_by_name(“name”)` -> Name定位    
`driver.find_element_by_class_name(class_name)` -> class定位  
`driver.find_element_by_tag_name(“foo”)` -> Tag定位   
`driver.find_element_by_css_selector(“#foo”)` -> css定位  
`driver.find_element_by_xpath(“//select[@id=’nr’]/option[2]”)` -> xpath定位  
`driver.find_element_by_link_text(u”链接”)` -> link定位  
`browser.find_element_by_partial_link_text("贴")` -> 部分link定位

#### 11. 截图
`driver.get_screenshot_as_file('/Screenshots/foo.png')` -> 截取当前页面
#### 12. 上传文件  
```python
select = driver.find_element_by_id("upload")
filePath = "C:test/uploadfile/media_ads/test.jpg"
FileUpload.send_keys(filePath)```

#### 13. 提交
`driver.find_element_by_id("upload").submit()`
#### 14. 点击
`browser.find_element_by_id("su").click()`  
`browser.find_element_by_id("su").clickAndWait()`
#### 15. cookie处理
`driver.get_cookies()` -> 获取cookie  
`driver.add_cookie({dicts})` -> 添加cookie   
`delete_cookie(name)` -> 删除特定(部分)的cookie  
`delete_all_cookies()` -> 删除所有cookie  

#### 16. 鼠标方法：
```
context_click() ##右击
douch_click() ##双击
drag_and_drop() ##拖拽
move_to_element() ##鼠标停在一个元素上
click_and_hold() # 按下鼠标左键在一个元素上```

#### 17. 键盘事件：
```python
send_keys(Keys.BACK_SPACE)	删除键(BackSpace)
send_keys(Keys.SPACE)	空格键(Space)
send_keys(Keys.TAB)	制表键(Tab)
send_keys(Keys.ESCAPE)	回退键(Esc)
send_keys(Keys.ENTER)	回车键(Enter)
send_keys(Keys.CONTROL,'a')	全选（Ctrl+A）
send_keys(Keys.CONTROL,'c')	复制（Ctrl+C）
```
