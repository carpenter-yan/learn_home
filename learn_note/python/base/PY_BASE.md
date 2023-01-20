



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

[Python下载地址](https://www.python.org/downloads/)  
Python有两个版本，一个是2.x版，一个是3.x版，这两个版本是不兼容的。由于3.x版越来越普及，推荐使用。  

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

## 三、Python基础

### 基础语法

Python的语法比较简单，采用**缩进**方式，写出来的代码就像下面的样子：
```python
# print absolute value of an integer:
a = 100
if a >= 0:
    print(a)
else:
    print(-a)
```

以#开头的语句是**注释**，解释器会忽略掉注释。其他每一行都是一个语句，当语句以冒号:结尾时，缩进的语句视为代码块。  

缩进有利有弊。好处是强迫你写出格式化的代码，但没有规定缩进是几个空格还是Tab。按照约定俗成的惯例，应该始终坚持使用**4个空格**的缩进。  
缩进的另一个好处是强迫你写出缩进较少的代码，你会倾向于把一段很长的代码拆分成若干函数，从而得到缩进较少的代码。  
缩进的坏处就是“复制－粘贴”功能失效了。当你重构代码时，粘贴过去的代码必须重新检查缩进是否正确。
此外，IDE很难像格式化Java代码那样格式化Python代码。  

最后，请务必注意，Python程序是**大小写敏感**的，如果写错了大小写，程序会报错。

### 输入和输出

- 输出

用print()在括号中加上字符串，就可以向屏幕上输出指定的文字。比如输出'hello, world'，用代码实现如下：
```python
>>> print('hello, world')
```

print()函数也可以接受多个字符串，用逗号“,”隔开，就可以连成一串输出：
```python
>>> print('The quick brown fox', 'jumps over', 'the lazy dog')
The quick brown fox jumps over the lazy dog
```
print()会依次打印每个字符串，遇到逗号“,”会输出一个空格。

- 输入

Python提供了一个input()，可以让用户输入**字符串**，并存放到一个变量里。比如输入用户的名字：
```python
>>> name = input()
Michael
```
当你输入name = input()并按下回车后，Python交互式命令行就在等待你的输入了。
这时，你可以输入任意字符，然后按回车后完成输入。

input()可以让你显示一个字符串来提示用户，于是我们把代码改成：
```python
name = input('please enter your name: ')
print('hello,', name)
```

### 数据类型和变量

- 整数

Python可以处理任意大小的整数，当然包括负整数，表示方法和数学上的写法一模一样，例如：1，100，-8080，0，等等。  

注：Python的整数没有大小限制，而某些语言的整数根据其存储长度是有大小限制的，例如Java对32位整数的范围限制在-2147483648-2147483647。

十六进制用0x前缀和0-9，a-f表示，例如：0xff00，0xa5b4c3d2，等等。

对于很大的数，例如10000000000，很难数清楚0的个数。
Python允许在数字中间以_分隔，因此，写成10_000_000_000和10000000000是完全一样的。
十六进制数也可以写成0xa1b2_c3d4。

- 浮点数

浮点数也就是小数，Python的浮点数也没有大小限制，但是超出一定范围就直接表示为inf（无限大）。

浮点数可以用数学写法，如1.23，3.14，-9.01，等等。

但是对于很大或很小的浮点数，就必须用科学计数法表示，把10用e替代，
1.23x109就是1.23e9，或者12.3e8，0.000012可以写成1.2e-5，等等。

整数和浮点数在计算机内部存储的方式是不同的，整数运算永远是精确的（除法难道也是精确的？是的！），
而浮点数运算则可能会有四舍五入的误差。

在Python中，有两种除法，一种除法是/
```python
>>> 10 / 3
3.3333333333333335
```
/除法计算结果是浮点数，即使是两个整数恰好整除，结果也是浮点数：
```python
>>> 9 / 3
3.0
```
还有一种除法是//，称为地板除，两个整数的除法仍然是整数：
```python
>>> 10 // 3
3
```
你没有看错，整数的地板除//永远是整数，即使除不尽。要做精确的除法，使用/就可以。

因为//除法只取结果的整数部分，所以Python还提供一个余数运算，可以得到两个整数相除的余数：
```python
>>> 10 % 3
1
```

- 布尔值

布尔值和布尔代数的表示完全一致，一个布尔值只有True、False两种值(请注意大小写)

- 字符串
字符串是以单引号'或双引号"括起来的任意文本，比如'abc'，"xyz"等等。

如果'本身也是一个字符，那就可以用""括起来，比如"I'm OK"

如果字符串内部既包含'又包含"怎么办？可以用转义字符\来标识，比如：'I\'m \"OK\"!'。
转义字符\可以转义很多字符，比如\n表示换行，\t表示制表符，字符\本身也要转义，所以\\表示的字符就是\

如果字符串内部有很多换行，用\n写在一行里不好阅读，为了简化，Python允许用'''...'''的格式表示多行内容
```python
>>> print('''line1
... line2
... line3''')
line1
line2
line3
```

ASCII、Unicode、UTF-8三种编码相关信息请参考

[字符串和编码](https://www.liaoxuefeng.com/wiki/1016959663602400/1017075323632896)  
[Unicdoe【真正的完整码表】对照表（一）](https://guoxiaodong.blog.csdn.net/article/details/9045765?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-5-9045765-blog-108942063.pc_relevant_aa2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-5-9045765-blog-108942063.pc_relevant_aa2&utm_relevant_index=10)

在最新的Python 3版本中，字符串是以Unicode编码的，也就是说，Python的字符串支持多语言。

对于单个字符的编码，Python提供了ord()函数获取字符的整数表示，chr()函数把编码转换为对应的字符：
```python
>>> ord('A')
65
>>> ord('中')
20013
>>> chr(66)
'B'
>>> chr(25991)
'文'
```

如果知道字符的整数编码，还可以用十六进制这么写
```python
>>> '\u4e2d\u6587'
'中文'
```

由于Python的字符串类型是str，在内存中以Unicode表示，一个字符对应若干个字节。
如果要在网络上传输，或者保存到磁盘上，就需要把str变为以字节为单位的bytes。
Python对bytes类型的数据用带b前缀的单引号或双引号表示：
```python
x = b'ABC'
```

以Unicode表示的str通过encode()方法可以编码为指定的bytes，例如：
```python
>>> 'ABC'.encode('ascii')
b'ABC'
>>> '中文'.encode('utf-8')
b'\xe4\xb8\xad\xe6\x96\x87'
```

反过来，如果我们从网络或磁盘上读取了字节流，那么读到的数据就是bytes。要把bytes变为str，就需要用decode()方法：
```python
>>> b'ABC'.decode('ascii')
'ABC'
>>> b'\xe4\xb8\xad\xe6\x96\x87'.decode('utf-8')
'中文'
```

要计算str包含多少个字符，可以用len()函数。如果换成bytes，len()函数就计算字节数。
```python
>>> len("中文")
2
>>> len('中文'.encode('utf-8'))
6
```

在操作字符串时，我们经常遇到str和bytes的互相转换。为了避免乱码问题，应当始终坚持使用UTF-8编码对str和bytes进行转换。

由于Python源代码也是一个文本文件，所以，当你的源代码中包含中文的时候，在保存源代码时，就需要务必指定保存为UTF-8编码。
当Python解释器读取源代码时，为了让它按UTF-8编码读取，我们通常在文件开头写上这两行：
```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
```
第一行注释是为了告诉Linux/OS X系统，这是一个Python可执行程序，Windows系统会忽略这个注释；
第二行注释是为了告诉Python解释器，按照UTF-8编码读取源代码，否则，你在源代码中写的中文输出可能会有乱码。

在Python中，采用的格式化方式和C语言是一致的，用%实现，举例如下：
```python
>>> 'Hello, %s' % 'world'
'Hello, world'
>>> 'Hi, %s, you have $%d.' % ('Michael', 1000000)
'Hi, Michael, you have $1000000.'
```
常见的占位符有：

|占位符|替换内容|
|-|-|
|%d|整数|
|%f|浮点数|
|%s|字符串|
|%x|十六进制整数|


- 空值

空值是Python里一个特殊的值，用None表示

- 变量

变量在程序中用变量名表示，变量名必须是大小写英文、数字和_的组合，且不能用数字开头

在Python中，等号=是赋值语句，可以把任意数据类型赋值给变量，同一个变量可以反复赋值，而且可以是不同类型的变量

变量本身类型不固定的语言称之为动态语言，与之对应的是静态语言。
静态语言在定义变量时必须指定变量类型，如果赋值的时候类型不匹配，就会报错。例如Java是静态语言

- 常量

常量就是不能变的变量，比如常用的数学常数π就是一个常量。通常用全部大写的变量名表示常量：PI = 3.14159265359

但事实上PI仍然是一个变量，Python根本没有任何机制保证PI不会被改变，
所以，用全部大写的变量名表示常量只是一个习惯上的用法，
如果你一定要改变变量PI的值，也没人能拦住你。

### 条件判断
```python
age = 3
if age >= 18:
    print('adult')
elif age >= 6:
    print('teenager')
else:
    print('kid')
```
if语句执行有个特点，它是从上往下判断，如果在某个判断上是True，把该判断对应的语句执行后，就忽略掉剩下的elif和else

```python
birth = input('birth: ')
if birth < 2000:
    print('00前')
else:
    print('00后')
```
输入1982，结果报错：
这是因为input()返回的数据类型是str，str不能直接和整数比较，必须先把str转换成整数。
Python提供了int()函数来完成这件事情：
```python
s = input('birth: ')
birth = int(s)
if birth < 2000:
    print('00前')
else:
    print('00后')
```

### 循环

### 使用list和tuple

### 使用dict和set