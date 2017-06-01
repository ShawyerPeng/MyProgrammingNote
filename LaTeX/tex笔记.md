\documentclass[a4paper]{ctexart} %CTEX报告文章格式

## 导入宏包
```LaTeX
\usepackage[top=3cm,bottom=2cm,left=2cm,right=2cm]{geometry} % 页边距
\usepackage{amsmath} %数学公式
\usepackage{amsthm}
\usepackage{longtable} %长表格
\usepackage{graphicx} %图片
\usepackage{tikz} %画图
\usepackage{cite}
\usepackage{listings}
\usepackage{amsfonts}
\usepackage{subfigure}
\usepackage{float}
\usepackage[colorlinks,linkcolor=black,hyperindex,CJKbookmarks,dvipdfm]{hyperref}
\lstset{language=Mathematica}%这条命令可以让LaTeX排版时将Mathematica键字突出显示
\lstset{extendedchars=false}%这一条命令可以解决代码跨页时，章节标题，页眉等汉字不显示的问题
\usetikzlibrary{shapes,arrows} %tikz图形库
\usepackage{overpic} %图上标记
\usepackage{ccaption} %中英文题注
%\usepackage[numbers,sort&compress]{natbib} %参数代表：数字和排序与压缩
%\bibliographystyle{GBT7714-2005NLang} %参考文献格式设为GBT7714-2005N.bst
%\usepackage[draft=false,colorlinks=true,CJKbookmarks=true,linkcolor=black,citecolor=black,urlcolor=blue]{hyperref} %参考文献跳转，此宏包会自动载入graphicx
\usepackage{textcomp} %摄氏度符号
%\usepackage{ccmap} %pdf中文复制
%\usepackage{myfont} %字体
\usepackage{color} %gnuplot彩色文字
%\usepackage{texshade} %texshade，此宏包与graphicx冲突，故放最后
% \usepackage{indentfirst}
```

#### 说明
1. `\usepackage[paperwidth=9.2cm,paperheight=12.4cm,width=9cm,height=12cm,top=0.2cm,bottom=0.4cm,left=0.2cm,
right=0.2cm,foot=0cm, nohead,nofoot]{geometry}`  
这个包主要用来控制整个页面，paperwidth,paperheight设置页面大小，
top指出第一行文字离上边的距离，bottom距离下边的距离，left文字距离左边的距离，right距离右边的距离。  
2. ``
