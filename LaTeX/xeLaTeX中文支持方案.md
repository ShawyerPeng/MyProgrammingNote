LaTeX的中文支持一直是个问题，11年左右刚开始学习LaTeX的时候，那时的中文解决办法是CJK宏包。到了12年或13年的时候，再接触LaTeX的时候，LaTeX中文支持的最优解决办法变成了xeLaTeX+xeCJK，这大概是目前为止最优的中文解决方案。  

后来，CTEX学会将xeCJK的功能进行打包，发布了ctex宏包，进一步简化了用LaTeX写中文文档的问题。  

下面是利用xeLaTeX和 xeCJK实现中文支持的最小模板:  
```
\documentclass{article}
\usepackage{xeCJK}
\begin{document}
中文English混合示例。
\end{document}
```
相对于英文的最小LaTeX模板来说多了两句:`\usepackage{xeCJK}`

xeCJK会自动使用TeXLive自带的Fandole字体。想要该模板能够编译通过，需要将TeXLive自带的中文字体安装到系统中，最简单的办法是在~/.fonts目录下建一个软链接:
`ln -s /opt/texlive/2014/texmf-dist/fonts/opentype/public/fandol ~/.fonts/`  

用如下命令编译:`xelatex test.tex`
