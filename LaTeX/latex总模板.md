## [1](http://www.kancloud.cn/digest/latexeverywhere/181887)
```latex
\documentclass[10pt,a4paper]{article}
\usepackage{xcolor}
\usepackage{titlesec}
\usepackage{fontspec}
\defaultfontfeatures{Mapping=tex-text}
\usepackage{xunicode}
\usepackage{xltxtra}
\usepackage{polyglossia}
\usepackage{indentfirst}             % 段首缩进

\setdefaultlanguage{english}
% 设置字体
\setsansfont{Calibri}
\setmainfont[BoldFont=SimHei]{STKaiti}
\usepackage{amsmath}
\usepackage{amsfonts}
\usepackage{amssymb}
\usepackage{graphicx}
% 设置页边距
%\usepackage[left=2cm,right=2cm,top=2cm,bottom=2cm]{geometry}
% MATLAB代码插入包
\usepackage{listings}
%\usepackage[framed,numbered,autolinebreaks,useliterate]{mcode}
% 新定义字体
\newfontfamily\song{SimSun}          % 宋体
\newfontfamily\hei{SimHei}           % 黑体
\XeTeXlinebreaklocale "zh"           % 中文断行

% Define light and dark Microsoft blue colours
\definecolor{MSBlue}{rgb}{.204,.353,.541}
\definecolor{MSLightBlue}{rgb}{.31,.506,.741}
% Define a new fontfamily for the subsubsection font
% Don't use \fontspec directly to change the font
\newfontfamily\subsubsectionfont[Color=MSLightBlue]{Times New Roman}
% Set formats for each heading level
\titleformat*{\section}{\Large\bfseries\sffamily\color{MSBlue}}
\titleformat*{\subsection}{\large\bfseries\sffamily\color{MSLightBlue}\song}
\titleformat*{\subsubsection}{\itshape\subsubsectionfont}

\author{郭大为\footnote{email: guodw3@mail2.sysu.edu.cn}\\[2ex]
    国立交通大学应数系\\[2ex]}
\title{Homework \uppercase\expandafter{\romannumeral4}}
\date{November 16, 2015}
\begin{document}

%%%% 段落首行缩进两个字 %%%%
\makeatletter
\let\@afterindentfalse\@afterindenttrue
\@afterindenttrue
\makeatother
\setlength{\parindent}{2em}  %中文缩进两个汉字位

\maketitle

\section{A section}
以下为正文部分，可以任意替换或删除

This is some text.

\subsection{A subsection 演示插入MATLAB代码}
各级标题效果演示
\subsubsection{A subsubsection}
演示代码插入效果：
\begin{lstlisting}[numbers=left]
point = [9; 1];
H = [1, 0; 0, 9];
figure
ezcontour('x^2/2+9*y^2/2',[-9, 9, -6, 6])

% steptest decent method
sdm_points = [9; 1];
count = 0;
while(norm(point) > 1e-5)
    count = count + 1;
    g = [point(1); 9*point(2)];
    point = point - g'*g/(g'*H*g).*g;
    sdm_points = [sdm_points, point];
end
hold on
plot(sdm_points(1, :), sdm_points(2, :),'-','LineWidth',3);
count
\end{lstlisting}
图片插入效果图：
    \begin{center}
      %  \includegraphics[width=1\textwidth]{1.eps}
    \end{center}
\end{document}
```

## 修改字体
查看字体：`fc-list :lang-zh`
字体映射：
```
STCaiyun,华文彩云:style=Regular

YouYuan,幼圆:style=Regular

PMingLiU,新細明體:style=Regular

STHupo,华文琥珀:style=Regular

FZYaoTi,方正姚体:style=Regular

NSimSun,新宋体:style=Regular

FangSong,仿宋:style=Regular,Normal,obyčejné,Standard,Κανονικά,Normaali,Normál,Normale,Standaard,Normalny,Обычный,Normálne,Navadno,Arrunta

DFKai\-SB,標楷體:style=Regular

KaiTi,楷体:style=Regular,Normal,obyčejné,Standard,Κανονικά,Normaali,Normál,Normale,Standaard,Normalny,Обычный,Normálne,Navadno,Arrunta

STSong,华文宋体:style=Regular

Microsoft YaHei,微软雅黑:style=Regular,Normal,obyčejné,Standard,Κανονικά,Normaali,Normál,Normale,Standaard,Normalny,Обычный,Normálne,Navadno,Arrunta

SimSun,宋体:style=Regular

STFangsong,华文仿宋:style=Regular

STXinwei,华文新魏:style=Regular

Arial Unicode MS:style=Regular,Normal,obyčejné,Standard,Κανονικά,Normaali,Normál,Normale,Standaard,Normalny,Обычный,Normálne,Navadno,Arrunta

STXingkai,华文行楷:style=Regular

Microsoft JhengHei,微軟正黑體:style=Negreta,Bold,tučné,fed,Fett,Έντονα,Negrita,Lihavoitu,Gras,Félkövér,Grassetto,Vet,Halvfet,Pogrubiony,Negrito,Полужирный,Fet,Kalın,Krepko,Lodia

STLiti,华文隶书:style=Regular

SimHei,黑体:style=Regular,Normal,obyčejné,Standard,Κανονικά,Normaali,Normál,Normale,Standaard,Normalny,Обычный,Normálne,Navadno,Arrunta

MingLiU,細明體:style=Regular

STZhongsong,华文中宋:style=Regular

Microsoft YaHei,微软雅黑:style=Bold,Negreta,tučné,fed,Fett,Έντονα,Negrita,Lihavoitu,Gras,Félkövér,Grassetto,Vet,Halvfet,Pogrubiony,Negrito,Полужирный,Fet,Kalın,Krepko,Lodia

FZShuTi,方正舒体:style=Regular

MingLiU_HKSCS,細明體_HKSCS:style=Regular

STXihei,华文细黑:style=Regular

Microsoft JhengHei,微軟正黑體:style=Normal,Regular,obyčejné,Standard,Κανονικά,Normaali,Normál,Normale,Standaard,Normalny,Обычный,Normálne,Navadno,Arrunta

LiSu,隶书:style=Regular

STKaiti,华文楷体:style=Regular
```
```
\documentclass[11pt]{article}
%\usepackage{xeCJK}
\usepackage{fontspec,xunicode,xltxtra}
\usepackage{titlesec}
\usepackage[top=1in,bottom=1in,left=1.25in,right=1.25in]{geometry}
\usepackage{indentfirst}             % 段首缩进

\setmainfont[BoldFont=SimHei]{SimSun}% 默认字体为宋体

\XeTeXlinebreaklocale "zh"           % 中文断行

\newfontfamily\kai{STKaiti}          % 楷体
\newfontfamily\hei{SimHei}           % 黑体

\begin{document}
\section{举例}
% 无需排版的多行内容
\begin{verbatim}           
标点。                              
\end{verbatim}

汉字Chinese数学$x=y$空格

\section{楷体}  

\kai 楷体

\section{黑体}  

\hei 黑体

\end{document}
```
