# Httperf
[Httperf](http://www.hpl.hp.com/research/linux/httperf/)  

测试：
`httperf --server 192.168.1.10 --port 80 --uri /index.html --rate 300 --num-conn 30000 --num-call 1 --timeout 5`
重复下载`http://192.168.1.10/index.html`，每秒300次，总共30000次请求

* `--server`：指定主机地址
* `--port`：指定主机端口
* `--uri`：指定测试的页面（相对路径）
* `--rate`：指定每秒发送的请求数
* `--num-conn`：指定总共发多少个请求
* `--num-call`指定每次连接发送的请求数（通常为1）
* `--timeout`：指定请求超时时间

参考：[网站压力测试有什么方法论和工具推荐？](https://segmentfault.com/q/1010000000129817)  
[Httperf测试web服务器](http://m.blog.itpub.net/230160/viewspace-604600/)  
[linux web站点常用压力测试工具httperf](https://zhangchaoquan.com/index.php/linux/183.html)  

# Autobench
[Autobench](http://www.xenoclast.org/autobench/)

`autobench --single_host --host1 192.168.1.10 --uri1 /index.html --quiet --low_rate 20 --high_rate 200 --rate_step 20 --num_call 10 --num_conn 5000 --timeout 5 --file results.tsv`

`--host1`：The website host name that you wish to test
`--uri1`：The path of the file that will be downloaded
`--quiet`：Does not display the httperf information on the screen
`--low_rate`：Connections per second at the beginning of the test
`--high_rate`：Connections per second at the end of the test
`--rate_step`：The number of connections by which to increase the rate after each test
`--num_call`：The number of requests that should be sent per connection
`--num_conn`：Total number of connections
`--timeout`：The number of seconds elapsed before a request is considered as lost
`--file`：Export results as specified (.tsv file)

# OpenWebLoad
[OpenWebLoad](http://openwebload.sourceforge.net)  

`openload example.com/index.html 10`

`Tps` (transactions per second): A transaction corresponds to a completed request (back and forth)

`MaTps`: Average TPS over the last 20 seconds

`Resp Time`: Average response time for the elapsed second

`Err` (error rate): Errors occur when the server returns a response that is not the expected HTTP 200 OK

`Count`: Total transaction count