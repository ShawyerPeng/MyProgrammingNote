## 1. 不编号的{itemize}环境
对文本进行简单的排列，不是采用序号，而是实心圆点符号。要和`\item`配合使用。
```
\begin{itemize}
\item LaTeX 1
\item LaTeX 1
\item LaTeX 1
\end{itemize}
```
不想使用实心圆点符号进行排列的话可以在\item[]的中括号里面指定需要的编号符号。例如我们使用-进行编号，改变代码如下：
```
\begin{itemize}
\item[-] LaTeX 1
\item[-] LaTeX 1
\item[-] LaTeX 1
\end{itemize}
```
## 2. 编号的{enumerate}环境
产生带需要的编号，默认是采用数字1,2,3……进行排列。如果你想用其他排列方式例如(1),(2)…的话需要先加载\usepackage{enumerate}  
```
\begin{enumerate}[(1)]
\item LaTeX 1
\item LaTeX 1
\item LaTeX 1
\end{enumerate}
```
## 3. 使用关键字的description环境
```
\begin{description}[(1)]
\item[key1] LaTeX 1
\item[key2] LaTeX 2
\item[key3] LaTeX 3
\end{description}
```


## 重定义`\theenumi`和`labelenumi` (P100)
`\theenumi`(默认`\arabic{enumi}`)  
`labelenumi`(默认`\theenumi.`)  
```
\renewcommand\theenumi{\roman{enumi}}
\renewcommand\labelenumi{(\theenumi)}
```

##
