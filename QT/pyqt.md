```python
import sys
from PyQt4 import QtGui

def main():

    app = QtGui.QApplication(sys.argv)

    w = QtGui.QWidget()
    w.resize(250, 150)
    w.move(300, 300)
    w.setWindowTitle('Simple')
    w.show()
    sys.exit(app.exec_())

if __name__ == '__main__':
    main()
```
self.gridLayout = QtWidgets.QGridLayout() #创建栅格布局对象
self.verticalLayout = QtWidgets.QVBoxLayout() #创建横向布局对象
self.setLayout(self.gridLayout) #做顶层布局，栅格布局一般作为顶层布局使用


def initUI(self):

    someAct = QAction(QIcon('.png'), 'Exit', self)  #为动作设置图标
    someAct.setShortcut('Ctrl+J')                   #为动作设置快捷键
    someAct.setStatusTip('Set')                     #为动作设置状态提示
    someAct.triggered.connect(qApp.quit)

    menuBar = self.menuBar()            #菜单栏
    someMenu = menuBar.addMenu('&File') #添加一个菜单
    someMenu.addAction(someAct)         #添加动作

    self.statusBar()

    tbar = self.addToolBar('Exit')      #工具栏
    tbar.addAction(someAct)             #添加动作

    self.setGeometry(400, 200, 600, 400)
    self.setWindowTitle('MainWindow')
    setWindowIcon(QIcon())
    self.show()
