## multiprocessing
multiprocessing.dummy 模块与 multiprocessing 模块的区别： dummy 模块是多线程，而 multiprocessing 是多进程， api 都是通用的。 所有可以很方便将代码在多线程和多进程之间切换。

	# from multiprocessing import Pool
	from multiprocessing.dummy import Pool as ThreadPool
	import time
	import urllib2
	
	urls = [
	    'http://www.baidu.com',
	    'http://home.baidu.com/',
	    'http://tieba.baidu.com/',
	    'http://zhidao.baidu.com/',
	    'http://music.baidu.com/',
	    'http://image.baidu.com/',
	    'http://python-china.org/',
	    'http://python-china.org/node/about',
	    'http://python-china.org/node/',
	    'http://python-china.org/account/signin',
	    'http://python-china.org/account/signup',
	    'http://www.qq.com',
	    'http://www.youku.com',
	    'http://www.tudou.com'
	]
	
	start = time.time()
	results = map(urllib2.urlopen, urls)
	print('Normal:', time.time() - start)
	
	start2 = time.time()
	# 开8个 worker，没有参数时默认是 cpu 的核心数
	pool = ThreadPool(processes=8)
	# 在线程中执行 urllib2.urlopen(url) 并返回执行结果
	results2 = pool.map(urllib2.urlopen, urls)
	pool.close()
	pool.join()
	print('Thread Pool:', time.time() - start2)
