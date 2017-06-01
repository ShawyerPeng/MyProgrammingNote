 titlesec、caption、geometry。mdframed、fancybox 等宏包你可能需要。  
 插图的技巧，图片的宽度，标题，图片中文字的字体大小、图片前后间距，子图的排列、间距等等都需要注意到。合理地使用 cutwin、wrapfig 等宏包来合理的排版图片。


## verbatim环境
    \begin{verbatim}
    public static void main(String[] args) {
    System.out.println("Hello World");
    }
    \end{verbatim}

## listing宏包
```latex
\begin{lstlisting}[language=C或{[ANSI]C}] 
int main(intargc, char ** argv) 
{ 
    printf("Hello world!\n");  
    return 0; 
} 
\end{lstlisting}
```
#### 要使用 listings 宏包提供的语法高亮，需要 xcolor 宏包支持
`keywordstyle=color{blue!70},commentstyle=color{red!50!green!50!blue!50},rulesepcolor=color{red!20!green!20!blue!20}
`
#### 添加边框
`frame=shadowbox`
#### 添加行号
`numbers=left或right`表示行号显示在代码的左侧还是右侧
#### 全局设置`\lstset`
```
\lstset{numbers=left, numberstyle=\tiny, keywordstyle=\color{blue!70},  commentstyle=\color{red!50!green!50!blue!50}, frame=shadowbox, rulesepcolor=\color{red!20!green!20!blue!20} }
```
#### 显示中文
在`\lstset`命令中设置逃逸字串的开始符号与终止符号，推荐使用的符号是左引号  
``escapeinside='`'``  
使用中文：``printf("`我爱中文`!\n");``
#### 调整一下边距
代码框的宽度默认是与页芯等宽的，其上边距也过于小，可根据自己的审美观念适度调整一下。我通常是将代码框的左右边距设置为 2em，上边距为 1em，下边距采用默认值即可:  
```
lstset{numbers=left, numberstyle=tiny, keywordstyle=color{blue!70}, commentstyle=color{red!50!green!50!blue!50}, frame=shadowbox, rulesepcolor=color{red!20!green!20!blue!20},escapeinside=``, xleftmargin=2em,xrightmargin=2em, aboveskip=1em}
```


```
\documentclass[11pt,a4paper]{article} 
\usepackage{xltxtra} 

\usepackage{fontspec} 
\usepackage{float} 
\usepackage{listings} 
\usepackage{xcolor} 
\usepackage{color} 
\usepackage[top=0.8in,bottom=0.8in,left=1.2in,right=0.6in]{geometry} 
\lstset{ 
    language=[ANSI]c,   
    basicstyle=\small,   
    numbers=left, 
    keywordstyle=\color{blue}, 
    numberstyle={\tiny\color{lightgray}},   
    stepnumber=1, %行号会逐行往上递增   
    numbersep=5pt, 
    commentstyle=\small\color{red}, 
    backgroundcolor=\color[rgb]{0.95,1.0,1.0},   
    showspaces=false,   
    showtabs=false, 
    frame=shadowbox, 
    framexleftmargin=5mm, 
    rulesepcolor=\color{red!20!green!20!blue!20!}, 
    % frame=single, 
    %  TABframe=single, 
    tabsize=4, 
    breaklines=tr, 
    extendedchars=false %这一条命令可以解决代码跨页时，章节标题，页眉等汉字不显示的问题 }   
    %字体 
    \setmainfont[BoldFont={SimHei},ItalicFont={KaiTi}]{SimSun} 
    \setsansfont[BoldFont=SimHei]{KaiTi} 
    \setmonofont{NSimSun}  
    %设置中文 
    %中文断行 
    \XeTeXlinebreaklocale "zh" 
    \XeTeXlinebreakskip = 0pt plus 1pt minus 0.1pt  

    \begin{document} 
    %此页故意留作空白 
    \newpage 
    \subsection{LaTex汉字测试} 
    %测试文件 
    \lstinputlisting{src/function.h} 
    \lstinputlisting{src/function.c} 
    \begin{lstlisting} 
        int main(intargc, char ** argv) { 
        /* 测试汉字注释 */ 
        printf("Hello world!\n");  
        return 0; } 
    \end{lstlisting}  
    \end{document}
```


## 参考资料
1. [listings 宏包](http://latexfly.com/docs/packages/listings.html)
