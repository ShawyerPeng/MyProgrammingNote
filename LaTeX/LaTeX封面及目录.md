## 1. 设置封面及目录不标页码
#### 1. 把标题页的内容放在titlepage里
```
\begin{titlepage}
这是标题页
\end{titlepage}
```
#### 2.
这种情况的解决方法是, 将\maketitle放在 \setcounter{page}{0} \thispagestyle{empty}之前,即
```
\begin{document}
\maketitle
\setcounter{page}{0}    %其实只要这两行
\thispagestyle{empty}   %其实只要这两行

\newpage
something esle
\end{document}
```

## 2.设置页眉页脚(必须在 document 环境中设置，不能在导言区设置)
`\usepackage{titlesec}   %设置页眉页脚的宏包`
```
\newpagestyle{main}{            
    \sethead{左页眉}{中页眉}{右页眉}     %设置页眉
    \setfoot{左页脚}{中页脚}{右页脚}      %设置页脚，可以在页脚添加 \thepage  显示页数
    \headrule                                      % 添加页眉的下划线
    \footrule                                       %添加页脚的下划线
}
\pagestyle{main}    %使用该style
```
## 3.设置页边距(导言区设置)
`\geometry{left=3cm,right=2.5cm,top=2.5cm,bottom=2.5cm}  %设置 上、左、下、右 页边距`

## 4. 设置页芯(导言区设置)
```
\setlength{\textwidth}{页面宽度}
\setlength{\textheight}{页面长度}
```
