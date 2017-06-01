## 一.导入宏包
```
\documentclass[a4paper]{ctexart}
\usepackage[top=1in, bottom=1in, left=1.25in, right=1.25in]{geometry}   %设置页边距
\usepackage{hyperref}   %让生成的文章目录有链接
\usepackage{appendix}   %设置附录格式
\usepackage{lipsum}     %随机生成文本的宏包
\usepackage{graphicx}   %插图宏集  
\usepackage{titletoc}   %要调整章节标题在目录页中的格式，可以用titletoc宏包 title of contents
%\titlecontents{标题层次}[左间距]{整体格式}{标题序号}{标题内容}{指引线和页码}[下间距]  
\usepackage{titlesec}   %其中 center 可使标题居中,还可设为 raggedleft (居左，默认),设置页眉页脚  
%\usepackage{abstract}摘要分栏的宏包  
\usepackage{fontspec, xunicode, xltxtra}  
\usepackage{amsmath}
\usepackage{xeCJK}%中文字体
```
## 二.设置字体
    \setmainfont{ }     %衬线字体  
    \setsansfont{ }     %无衬线字体  
    \setmonofont{ }     %等宽字体，一般是打印机字体(中文都是等宽的)

```
\setmainfont{Times New Roman}   %衬线字体缺省英文字体.serif是有衬线字体sans serif无衬线字体
\setsansfont{Helvetica/Arial}   %西文默认无衬线字体
\setmonofont{Courier New}是西文默认的等宽字体。
-----------------------------------------
\setCJKmainfont[ItalicFont={楷体}, BoldFont={黑体}]{宋体}
\setCJKsansfont{黑体}
\setCJKmonofont{仿宋_GB2312}%中文等宽字体
-----------------------------------------
\setCJKmainfont{simsun.ttc} %宋体
\setCJKsansfont{msyh.ttf} %微软雅黑
\setCJKmonofont{FZYTK.ttf} %方正姚体
-----------------------------------------
\setCJKmainfont[
BoldFont = Source Han Sans CN Medium,
ItalicFont = Adobe Kaiti Std R]
{Source Han Sans CN Light}
% 无衬线字体同上\setCJKsansfont[]{}
% 等宽字体/打印机字体
\setCJKmonofont[
BoldFont = Source Han Sans CN Medium,
ItalicFont = Adobe Kaiti Std R]
{Source Han Sans CN Light}
```
```
xeCJK 宏包说：
\newCJKfontfamily[song]\songti{SimSun}
等价于
\setCJKfamilyfont{song}{SimSun}
\newcommand{\song}{\CJKfamily{song}}```
## 代码字体
```
\documentclass[a4paper]{ctexart}
\usepackage{fontspec}
\usepackage{color}
\usepackage{listings}

\setmonofont[Mapping={}]{Monaco}    %英文引号之类的正常显示，相当于设置英文字体
\setsansfont{Monaco} %设置英文字体 Monaco, Consolas,  Fantasque Sans Mono
\setmainfont{Monaco} %设置英文字体
% \setCJKmainfont{方正兰亭黑简体}  %中文字体设置
% \setCJKsansfont{华康少女字体} %设置中文字体
% \setCJKmonofont{华康少女字体} %设置中文字体
%-------------------------------------------------
\definecolor{mygreen}{rgb}{0,0.6,0}
\definecolor{mygray}{rgb}{0.5,0.5,0.5}
\definecolor{mymauve}{rgb}{0.58,0,0.82}
\lstset{ %
backgroundcolor=\color{white},   % choose the background color
basicstyle=\footnotesize\ttfamily,        % size of fonts used for the code
columns=fullflexible,
breaklines=true,                 % automatic line breaking only at whitespace
captionpos=b,                    % sets the caption-position to bottom
tabsize=4,
commentstyle=\color{mygreen},    % comment style
escapeinside={\%*}{*)},          % if you want to add LaTeX within your code
keywordstyle=\color{blue},       % keyword style
stringstyle=\color{mymauve}\ttfamily,     % string literal style
frame=single,
% rulesepcolor=\color{red!20!green!20!blue!20},
% identifierstyle=\color{red},
language=c++,
}
%-------------------------------------------------
\begin{document}
\begin{lstlisting}
inline int gcd(int a, int b) { // 如果a<b，则递归得gcd(b,a%b)即gcd(b, a)，即交换了位置，时间复杂度O(log max(a, b))
	 printf("%D",a)
    return b==0?a:gcd(b,a%b)
}
inline int lcm(int a, int b) {
    return a/gcd(a,b)*b;
}
\end{lstlisting}
\end{document}
```
