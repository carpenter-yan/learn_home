



## 一、简介

### Python是一种什么语言？
Python是一种高级解释性程序设计语言。 著名的“龟叔”Guido van Rossum在1989年圣诞节期间，为了打发无聊的圣诞节而编写的一个编程语言。

### Python的应用范围？

- 适合做：

首选是网络应用，包括网站、后台服务等等；  
其次是许多日常需要的小工具，包括系统管理员需要的脚本任务等等；  
另外就是把其他语言开发的程序再包装起来，方便使用。  

- 不适合做：

底层应用。比如写操作系统，这个只能用C语言写；  
手机应用，只能用Swift/Objective-C（针对iPhone）和Java（针对Android）；  
3D游戏，最好用C或C++。

### Python的优缺点？

- 优点

简单易懂  
龟叔给Python的定位是“优雅”、“明确”、“简单”，所以Python程序看上去总是简单易懂，初学者学Python，不但入门容易，而且将来深入下去，可以编写那些非常非常复杂的程序。

生态较好  
Python就为我们提供了非常完善的基础代码库，覆盖了网络、文件、GUI、数据库、文本等大量内容，被形象地称作“内置电池（batteries included）”。  
除了内置的库外，Python还有大量的第三方库，也就是别人开发的，供你直接使用的东西。

- 缺点

第一个缺点就是运行速度慢，和C程序相比非常慢，因为Python是解释型语言。  
但是大量的应用程序不需要这么快的运行速度，因为用户根本感觉不出来。
例如开发一个下载MP3的网络应用程序，C程序的运行时间需要0.001秒，而Python程序的运行时间需要0.1秒，慢了100倍，
但由于网络更慢，需要等待1秒，你想，用户能感觉到1.001秒和1.1秒的区别吗？

第二个缺点就是代码不能加密。如果要发布你的Python程序，实际上就是发布源代码。

## 二、开发环境搭建

### python安装

Python有两个版本，一个是2.x版，一个是3.x版，这两个版本是不兼容的。由于3.x版越来越普及，推荐使用。  
[Python下载地址](https://www.python.org/downloads/)
在Windows上运行Python时，请先启动命令行，然后运行python。  
在Mac和Linux上运行Python时，请打开终端，然后运行python3。 

### python解释器

当我们编写Python代码时，我们得到的是一个包含Python代码的以.py为扩展名的文本文件。要运行代码，就需要Python解释器去执行.py文件。

CPython：当我们从Python官方网站下载并安装好Python 3.x后，我们就直接获得了一个官方版本的解释器：CPython。
这个解释器是用C语言开发的，所以叫CPython。在命令行下运行python就是启动CPython解释器。

其他还有IPython、PyPy、Jython、IronPython等。Python的解释器很多，但使用最广泛的还是CPython。

### 命令模式和交互模式

命令行模式  
在Windows开始菜单选择“命令提示符”，就进入到命令行模式，它的提示符类似C:\>：

Python交互模式  
在命令行模式下敲命令python，就看到类似如下的一堆文本输出，然后就进入到Python交互模式，它的提示符是>>>。  
在Python交互模式下输入 **exit()** 并回车，就退出了Python交互模式，并回到命令行模式：

### 文本编辑器

推荐微软出品的Visual Studio Code，它不是那个大块头的Visual Studio，它是一个精简版的迷你Visual Studio，
并且Visual Studio Code可以跨平台！！！Windows、Mac和Linux通用。

不要用Word和Windows自带的记事本。Word保存的不是纯文本文件，
而记事本会自作聪明地在文件开始的地方加上几个特殊字符（UTF-8 BOM），结果会导致程序运行出现莫名其妙的错误。

[2022 Visual Studio Code配置Python环境(保姆级)](https://blog.csdn.net/qq_42228145/article/details/114673705)  
