# Python基础
<!-- GFM-TOC -->
* [Python 基础](#python-基础)
    * [一、简介](#一简介)
        * [Python语言](#Python语言)
        * [Python应用范围](#Python应用范围)
        * [Python的优缺点](#Python的优缺点)
    * [二、开发环境搭建](#二开发环境搭建)
        * [Python安装](#Python安装)
        * [Python解释器](#Python解释器)
        * [命令模式和交互模式](#命令模式和交互模式)
        * [文本编辑器](#文本编辑器)
    * [三、Python基础](#Python基础)
        * [基础语法](#基础语法)
        * [数据类型和变量](#数据类型和变量)
        * [条件判断](#条件判断)
        * [循环](#循环)
        * [使用list和tuple](#使用list和tuple)
        * [使用dict和set](#使用dict和set)
    * [四、Python函数](#四Python函数)
        * [函数调用](#函数调用)
        * [函数定义](#函数定义)
        * [函数参数](#函数参数)
        * [递归函数](#递归函数)
    * [五、高级特性](#五高级特性)
        * [切片](#切片)
        * [迭代](#迭代)
        * [列表生成式](#列表生成式)
        * [生成器](#生成器)
        * [迭代器](#迭代器)
    * [六、函数式编程](#六函数式编程)
    * [七、模块](#七模块)

<!-- GFM-TOC -->



## 一、简介

### Python语言
Python是一种高级解释性程序设计语言。 著名的“龟叔”Guido van Rossum在1989年圣诞节期间，为了打发无聊的圣诞节而编写的一个编程语言。

### Python应用范围

- 适合做：

首选是网络应用，包括网站、后台服务等等；  

其次是许多日常需要的小工具，包括系统管理员需要的脚本任务等等；  

另外就是把其他语言开发的程序再包装起来，方便使用。  

- 不适合做：

底层应用。比如写操作系统，这个只能用C语言写；  

手机应用，只能用Swift/Objective-C（针对iPhone）和Java（针对Android）；  

3D游戏，最好用C或C++。

### Python的优缺点

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

### Python安装

[Python下载地址](https://www.python.org/downloads/)  
Python有两个版本，一个是2.x版，一个是3.x版，这两个版本是不兼容的。由于3.x版越来越普及，推荐使用。  

在Windows上运行Python时，请先启动命令行，然后运行python。  

在Mac和Linux上运行Python时，请打开终端，然后运行python3。 

### Python解释器

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

其中，格式化整数和浮点数还可以指定是否补0和整数与小数的位数：
```python
print('%2d-%02d' % (3, 1))
print('%.2f' % 3.1415926)
```

如果你不太确定应该用什么，%s永远起作用，它会把任何数据类型转换为字符串：
```python
>>> 'Age: %s. Gender: %s' % (25, True)
'Age: 25. Gender: True'
```

有些时候，字符串里面的%是一个普通字符怎么办？这个时候就需要转义，用%%来表示一个%：
```python
>>> 'growth rate: %d %%' % 7
'growth rate: 7 %'
```

另一种格式化字符串的方法是使用字符串的format()方法，它会用传入的参数依次替换字符串内的占位符{0}、{1}……：
```python
>>> 'Hello, {0}, 成绩提升了 {1:.1f}%'.format('小明', 17.125)
'Hello, 小明, 成绩提升了 17.1%'
```

最后一种格式化字符串的方法是使用以f开头的字符串，称之为f-string，它和普通字符串不同之处在于，字符串如果包含{xxx}，就会以对应的变量替换：
```python
>>> r = 2.5
>>> s = 3.14 * r ** 2
>>> print(f'The area of a circle with radius {r} is {s:.2f}')
The area of a circle with radius 2.5 is 19.62
```

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

Python的循环有两种，一种是for...in循环，依次把list或tuple中的每个元素迭代出来
```python
sum = 0
for x in [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]:
    sum = sum + x
print(sum)
```

如果要计算1-100的整数之和，从1写到100有点困难，
幸好Python提供一个range()函数，可以生成一个整数序列，再通过list()函数可以转换为list。
比如range(5)生成的序列是从0开始小于5的整数：
```python
sum = 0
for x in list(range(101)):
    sum = sum + x
print(sum)
```

第二种循环是while循环，只要条件满足，就不断循环，条件不满足时退出循环
```python
sum = 0
n = 99
while n > 0:
    sum = sum + n
    n = n - 2
print(sum)
```

在循环中，break语句可以提前退出循环
```python
n = 1
while n <= 100:
    if n > 10: # 当n = 11时，条件满足，执行break语句
        break # break语句会结束当前循环
    print(n)
    n = n + 1
print('END')
```

在循环过程中，也可以通过continue语句，跳过当前的这次循环，直接开始下一次循环。
```python
n = 0
while n < 10:
    n = n + 1
    if n % 2 == 0: # 如果n是偶数，执行continue语句
        continue # continue语句会直接继续下一轮循环，后续的print()语句不会执行
    print(n)
```

要特别注意，不要滥用break和continue语句。
break和continue会造成代码执行逻辑分叉过多，容易出错。
大多数循环并不需要用到break和continue语句。

有些时候，如果代码写得有问题，会让程序陷入“死循环”，也就是永远循环下去。
这时可以用Ctrl+C退出程序，或者强制结束Python进程。

### 使用list和tuple

- list

Python内置的一种数据类型是列表：list。list是一种有序的集合，可以随时添加和删除其中的元素。
```python
>>> classmates = ['Michael', 'Bob', 'Tracy']
```

用len()函数可以获得list元素的个数

用索引来访问list中每一个位置的元素，记得索引是从0开始的。
当索引超出了范围时，Python会报一个IndexError错误。
最后一个元素的索引是len(classmates) - 1。
用-1做索引，直接获取最后一个元素，以此类推，可以获取倒数第2个、倒数第3个。
```python
>>> len(classmates)
3
>>> classmates[0]
'Michael'
>>> classmates[-1]
'Tracy'
```

list相关操作
```python
#追加元素到末尾
>>> classmates.append('Adam')
>>> classmates
['Michael', 'Bob', 'Tracy', 'Adam']

#插入到指定的位置
>>> classmates.insert(1, 'Jack')
>>> classmates
['Michael', 'Jack', 'Bob', 'Tracy', 'Adam']

#删除list末尾的元素
>>> classmates.pop()
'Adam'
>>> classmates
['Michael', 'Jack', 'Bob', 'Tracy']

#删除指定位置的元素
>>> classmates.pop(1)
'Jack'
>>> classmates
['Michael', 'Bob', 'Tracy']

#替换某个元素
>>> classmates[1] = 'Sarah'
>>> classmates
['Michael', 'Sarah', 'Tracy']
```

list里面的元素的数据类型也可以不同，list元素也可以是另一个list
```python
>>> L = ['Apple', 123, True]

>>> s = ['python', 'java', ['asp', 'php'], 'scheme']
```
要拿到'php'可以写p[1]或者s[2][1]，因此s可以看成是一个二维数组，类似的还有三维、四维……数组，不过很少用到。

- tuple

另一种有序列表叫元组：tuple。tuple和list非常类似，但是tuple一旦初始化就不能修改
```python
>>> classmates = ('Michael', 'Bob', 'Tracy')
```
现在，classmates这个tuple不能变了，它也没有append()，insert()这样的方法。
其他获取元素的方法和list是一样的，你可以正常地使用classmates[0]，classmates[-1]，但不能赋值成另外的元素。

不可变的tuple有什么意义？因为tuple不可变，所以代码更安全。如果可能，能用tuple代替list就尽量用tuple。

tuple的陷阱：当你定义一个tuple时，在定义的时候，tuple的元素就必须被确定下来，比如：
```python
>>> t = (1, 2)
>>> t
(1, 2)
```

要定义一个只有1个元素的tuple，如果你这么定义：
```python
>>> t = (1)
>>> t
1
```

定义的不是tuple，是1这个数！这是因为括号()既可以表示tuple，又可以表示数学公式中的小括号，这就产生了歧义，
因此，Python规定，这种情况下，按小括号进行计算，计算结果自然是1。

只有1个元素的tuple定义时必须加一个逗号,，来消除歧义：
```python
>>> t = (1,)
>>> t
(1,)
```

### 使用dict和set

- dict

Python内置了字典：dict的支持，dict全称dictionary，在其他语言中也称为map，使用键-值（key-value）存储，具有极快的查找速度。
```python
>>> d = {'Michael': 95, 'Bob': 75, 'Tracy': 85}
>>> d['Michael']
95
```

其他操作
```python
#1、添加+修改：
#由于一个key只能对应一个value，所以，多次对一个key放入value，后面的值会把前面的值冲掉
>>> d['Adam'] = 67
>>> d['Adam']
67

#如果key不存在，dict就会报错
#要避免key不存在的错误，有两种办法，一是通过in判断key是否存在
>>> 'Thomas' in d
False

#二是通过dict提供的get()方法，如果key不存在，可以返回None，或者自己指定的value：
>>> d.get('Thomas')
>>> d.get('Thomas', -1)
-1
#注意：返回None的时候Python的交互环境不显示结果。

#2、删除
>>> d.pop('Bob')
75
>>> d
{'Michael': 95, 'Tracy': 85}
```

正确使用dict非常重要，需要牢记的第一条就是dict的key必须是不可变对象。
这是因为dict根据key来计算value的存储位置，如果每次计算相同的key得出的结果不同，那dict内部就完全混乱了。
这个通过key计算位置的算法称为哈希算法（Hash）。
在Python中，字符串、整数等都是不可变的，因此，可以放心地作为key。而list是可变的，就不能作为key

- set

set和dict类似，也是一组key的集合，但不存储value。由于key不能重复，所以，在set中，没有重复的key。

要创建一个set，需要提供一个list作为输入集合：
```python
>>> s = set([1, 2, 3])
>>> s
{1, 2, 3}
```

重复元素在set中自动被过滤。其他操作
```python
#1、添加
>>> s.add(4)
>>> s
{1, 2, 3, 4}

#2、删除
>>> s.remove(4)
>>> s
{1, 2, 3}

#3、集合操作
>>> s1 = set([1, 2, 3])
>>> s2 = set([2, 3, 4])
>>> s1 & s2
{2, 3}
>>> s1 | s2
{1, 2, 3, 4}
```

## 四、Python函数

### 函数调用

Python内置了很多有用的函数，我们可以直接调用。
[Built-in Functions](http://docs.python.org/3/library/functions.html#abs)
也可以在交互式命令行通过help(abs)查看abs函数的帮助信息。

要调用一个函数，需要知道函数的名称和参数，比如求绝对值的函数abs，只有一个参数。
```python
>>> abs(100)
100
>>> abs(-20)
20
>>> abs(12.34)
12.34
```

调用函数的时候，如果传入的参数数量不对，会报TypeError的错误，
如果传入的参数数量是对的，但参数类型不能被函数所接受，也会报TypeError的错误。

函数名其实就是指向一个函数对象的引用，完全可以把函数名赋给一个变量，相当于给这个函数起了一个“别名”：
```python
>>> a = abs # 变量a指向abs函数
>>> a(-1) # 所以也可以通过a调用abs函数
1
```

### 函数定义

在Python中，定义一个函数要使用def语句，依次写出函数名、括号、括号中的参数和冒号:，然后，在缩进块中编写函数体，函数的返回值用return语句返回。
```python
def my_abs(x):
    if x >= 0:
        return x
    else:
        return -x
```
请注意，函数体内部的语句在执行时，一旦执行到return时，函数就执行完毕，并将结果返回。

如果没有return语句，函数执行完毕后也会返回结果，只是结果为None。return None可以简写为return。

- 空函数

如果想定义一个什么事也不做的空函数，可以用pass语句：
```python
def nop():
    pass
```
pass语句什么都不做，那有什么用？实际上pass可以用来作为占位符，比如现在还没想好怎么写函数的代码，就可以先放一个pass，让代码能运行起来。

pass还可以用在其他语句里，比如：
```python
if age >= 18:
    pass
```

- 参数检查

数据类型检查可以用内置函数isinstance()实现
```python
def my_abs(x):
    if not isinstance(x, (int, float)):
        raise TypeError('bad operand type')
    if x >= 0:
        return x
    else:
        return -x
```

- 返回多个值

函数可以同时返回多个值，但其实就是一个tuple。
```python
import math

def move(x, y, step, angle=0):
    nx = x + step * math.cos(angle)
    ny = y - step * math.sin(angle)
    return nx, ny
    
>>> x, y = move(100, 100, 60, math.pi / 6)
>>> print(x, y)
151.96152422706632 70.0

>>> r = move(100, 100, 60, math.pi / 6)
>>> print(r)
(151.96152422706632, 70.0)
```
在语法上，返回一个tuple可以省略括号，而多个变量可以同时接收一个tuple，按位置赋给对应的值

### 函数参数

- 位置参数
```python
def power(x, n):
    s = 1
    while n > 0:
        n = n - 1
        s = s * x
    return s
    
>>> power(5, 2)
25
>>> power(5, 3)
125
```
power(x, n)函数有两个参数：x和n，这两个参数都是位置参数，调用函数时，传入的两个值按照**位置顺序**依次赋给参数x和n。

- 默认参数
```python
def power(x, n=2):
    s = 1
    while n > 0:
        n = n - 1
        s = s * x
    return s
    
>>> power(5)
25
>>> power(5, 2)
25
```
当我们调用power(5)时，相当于调用power(5, 2)

默认参数可以简化函数的调用。设置默认参数时，有几点要注意：
- 一是必选参数在前，默认参数在后，否则Python的解释器会报错
- 二是如何设置默认参数。当函数有多个参数时，把变化大的参数放前面，变化小的参数放后面。变化小的参数就可以作为默认参数。

```python
def enroll(name, gender, age=6, city='Beijing'):
    print('name:', name)
    print('gender:', gender)
    print('age:', age)
    print('city:', city)
    
enroll('Bob', 'M', 7)
enroll('Adam', 'M', city='Tianjin')
```
有多个默认参数时，调用的时候，既可以按顺序提供默认参数，
比如调用enroll('Bob', 'M', 7)，意思是，除了name，gender这两个参数外，
最后1个参数应用在参数age上，city参数由于没有提供，仍然使用默认值。

也可以不按顺序提供部分默认参数。当不按顺序提供部分默认参数时，需要把参数名写上。
比如调用enroll('Adam', 'M', city='Tianjin')，意思是，city参数用传进去的值，其他默认参数继续使用默认值。

注意：定义默认参数要牢记一点：默认参数必须指向不变对象！

- 可变参数

可变参数就是传入的参数个数是可变的，可以是1个、2个到任意个，还可以是0个。
```python
def calc(*numbers):
    sum = 0
    for n in numbers:
        sum = sum + n * n
    return sum
```
参数前面加了一个*号。在函数内部，参数numbers接收到的是一个tuple

入参为list或者tuple，Python允许在前面加一个*号，把list或tuple的元素变成可变参数传进去：
```python
>>> nums = [1, 2, 3]
>>> calc(*nums)
14
```

-  关键字参数

关键字参数允许你传入0个或任意个含参数名的参数，这些关键字参数在函数内部自动组装为一个dict
```python
def person(name, age, **kw):
    print('name:', name, 'age:', age, 'other:', kw)
    
>>> extra = {'city': 'Beijing', 'job': 'Engineer'}
>>> person('Jack', 24, **extra)
name: Jack age: 24 other: {'city': 'Beijing', 'job': 'Engineer'}
#**extra表示把extra这个dict的所有key-value用关键字参数传入到函数的**kw参数
```
kw将获得一个dict，注意kw获得的dict是extra的一份拷贝，对kw的改动不会影响到函数外的extra。

- 命名关键字参数

关键字参数可以传入任意不受限制的关键字参数，如果要限制关键字参数的名字，就可以用命名关键字参数

例如，只接收city和job作为关键字参数。这种方式定义的函数如下：
命名关键字参数必须传入参数名，这和位置参数不同。如果没有传入参数名，调用将报错
```python
#和关键字参数**kw不同，命名关键字参数需要一个特殊分隔符*，*后面的参数被视为命名关键字参数。
def person(name, age, *, city, job):
    print(name, age, city, job)
    
>>> person('Jack', 24, city='Beijing', job='Engineer')
Jack 24 Beijing Engineer
```

命名关键字参数可以有缺省值，从而简化调用：
```python
def person(name, age, *, city='Beijing', job):
    print(name, age, city, job)
    
>>> person('Jack', 24, job='Engineer')
Jack 24 Beijing Engineer
```
使用命名关键字参数时，要特别注意，如果没有可变参数，就必须加一个\*作为特殊分隔符。
如果缺少\*，Python解释器将无法识别位置参数和命名关键字参数

- 参数组合

在Python中定义函数，可以用必选参数、默认参数、可变参数、关键字参数和命名关键字参数，这5种参数都可以组合使用。
但是请注意，参数定义的顺序必须是：必选参数、默认参数、可变参数、命名关键字参数和关键字参数。
```python
def f1(a, b, c=0, *args, **kw):
    print('a =', a, 'b =', b, 'c =', c, 'args =', args, 'kw =', kw)

def f2(a, b, c=0, *, d, **kw):
    print('a =', a, 'b =', b, 'c =', c, 'd =', d, 'kw =', kw)
    
>>> f1(1, 2)
a = 1 b = 2 c = 0 args = () kw = {}
>>> f1(1, 2, c=3)
a = 1 b = 2 c = 3 args = () kw = {}
>>> f1(1, 2, 3, 'a', 'b')
a = 1 b = 2 c = 3 args = ('a', 'b') kw = {}
>>> f1(1, 2, 3, 'a', 'b', x=99)
a = 1 b = 2 c = 3 args = ('a', 'b') kw = {'x': 99}
>>> f2(1, 2, d=99, ext=None)
a = 1 b = 2 c = 0 d = 99 kw = {'ext': None}
```

### 递归函数

在函数内部，可以调用其他函数。如果一个函数在内部调用自身本身，这个函数就是递归函数。
```python
def fact(n):
    if n==1:
        return 1
    return n * fact(n - 1)
    
>>> fact(1)
1
>>> fact(5)
120
```

使用递归函数需要注意防止栈溢出。
在计算机中，函数调用是通过栈（stack）这种数据结构实现的，进入函数调用，会加一层栈帧， 函数返回，减一层栈帧。
由于栈的大小不是无限的，所以，递归调用的次数过多，会导致栈溢出。

## 五、高级特性

### 切片

取一个list或tuple的部分元素，Python提供了切片（Slice）操作符。
```python
>>> L = ['Michael', 'Sarah', 'Tracy', 'Bob', 'Jack']
>>> L[:3]
['Michael', 'Sarah', 'Tracy']
#从索引1开始，取出2个元素出来
>>> L[1:3]
['Sarah', 'Tracy']
#Python支持L[-1]取倒数第一个元素，那么它同样支持倒数切片。住倒数第一个元素的索引是-1。
>>> L[-2:]
['Bob', 'Jack']
>>> L[-2:-1]
['Bob']
#前10个数，每两个取一个
>>> L = list(range(100))
>>> L[:10:2]
[0, 2, 4, 6, 8]
#所有数，每5个取一个
>>> L[::5]
[0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95]
#只写[:]就可以原样复制一个list
>>> L[:]
[0, 1, 2, 3, ..., 99]
```

tuple也是一种list，唯一区别是tuple不可变。因此，tuple也可以用切片操作，只是操作的结果仍是tuple
```python
>>> (0, 1, 2, 3, 4, 5)[:3]
(0, 1, 2)
```

字符串也可以看成是一种list，每个元素就是一个字符。因此，字符串也可以用切片操作，只是操作结果仍是字符串
```python
>>> 'ABCDEFG'[:3]
'ABC'
>>> 'ABCDEFG'[::2]
'ACEG'
```
很多编程语言中，针对字符串提供了很多各种截取函数（例如，substring），其实目的就是对字符串切片。
Python没有针对字符串的截取函数，只需要切片一个操作就可以完成。

### 迭代

如果给定一个list或tuple，我们可以通过for循环来遍历这个list或tuple，这种遍历我们称为迭代（Iteration）。

Python的for循环不仅可以用在list或tuple上，还可以作用在其他可迭代对象上。
```python
>>> d = {'a': 1, 'b': 2, 'c': 3}
>>> for key in d:
...     print(key)
...
a
c
b
```
因为dict的存储不是按照list的方式顺序排列，所以，迭代出的结果顺序很可能不一样。

默认情况下，dict迭代的是key。如果要迭代value，可以用for value in d.values()，
如果要同时迭代key和value，可以用for k, v in d.items()。

由于字符串也是可迭代对象，因此，也可以作用于for循环：
```python
>>> for ch in 'ABC':
...     print(ch)
...
A
B
C
```

所以，当我们使用for循环时，只要作用于一个可迭代对象，for循环就可以正常运行，
那么，如何判断一个对象是可迭代对象呢？方法是通过collections.abc模块的Iterable类型判断：
```python
>>> from collections.abc import Iterable
>>> isinstance('abc', Iterable) # str是否可迭代
True
>>> isinstance([1,2,3], Iterable) # list是否可迭代
True
>>> isinstance(123, Iterable) # 整数是否可迭代
False
```

如果要对list实现类似Java那样的下标循环怎么办？Python内置的enumerate函数可以把一个list变成索引-元素对，这样就可以在for循环中同时迭代索引和元素本身：
```python
>>> for i, value in enumerate(['A', 'B', 'C']):
...     print(i, value)
...
0 A
1 B
2 C
```

### 列表生成式

列表生成式即List Comprehensions，是Python内置的非常简单却强大的可以用来创建list的生成式。

写列表生成式时，把要生成的元素x * x放到前面，后面跟for循环，就可以把list创建出来
```python
>>> [x * x for x in range(1, 11)]
[1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
#for循环后面还可以加上if判断，这样我们就可以筛选出仅偶数的平方
>>> [x * x for x in range(1, 11) if x % 2 == 0]
[4, 16, 36, 64, 100]
#还可以使用两层循环，可以生成全排列。三层和三层以上的循环就很少用到了。
>>> [m + n for m in 'ABC' for n in 'XYZ']
['AX', 'AY', 'AZ', 'BX', 'BY', 'BZ', 'CX', 'CY', 'CZ']
#for循环其实可以同时使用两个甚至多个变量
>>> d = {'x': 'A', 'y': 'B', 'z': 'C' }
>>> [k + '=' + v for k, v in d.items()]
['y=B', 'x=A', 'z=C']
```

### 生成器
通过列表生成式，我们可以直接创建一个列表。但是，受到内存限制，列表容量肯定是有限的。
而且，创建一个包含100万个元素的列表，不仅占用很大的存储空间，如果我们仅仅需要访问前面几个元素，那后面绝大多数元素占用的空间都白白浪费了。

如果列表元素可以按照某种算法推算出来，是否可以在循环的过程中不断推算出后续的元素呢？这样就不必创建完整的list，从而节省大量的空间。

在Python中，这种一边循环一边计算的机制，称为生成器：generator。

1. 要创建一个generator，有很多种方法。第一种方法很简单，只要把一个列表生成式的[]改成()，就创建了一个generator：
```python
>>> L = [x * x for x in range(10)]
>>> L
[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
>>> g = (x * x for x in range(10))
>>> g
<generator object <genexpr> at 0x1022ef630>
```

创建L和g的区别仅在于最外层的[]和()，L是一个list，而g是一个generator。

怎么打印出generator的每一个元素呢？可以通过next()函数获得generator的下一个返回值：
```python
>>> next(g)
0
#中间省略
>>> next(g)
81
>>> next(g)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
StopIteration
```

generator保存的是算法，每次调用next(g)，就计算出g的下一个元素的值，直到计算到最后一个元素，没有更多的元素时，抛出StopIteration的错误。

上面这种不断调用next(g)实在是太变态了，正确的方法是使用for循环，因为generator也是可迭代对象：
```python
>>> g = (x * x for x in range(10))
>>> for n in g:
        print(n)
0
#中间省略
81
```
我们创建了一个generator后，基本上永远不会调用next()，而是通过for循环来迭代它，并且不需要关心StopIteration的错误

2. 如果推算的算法比较复杂，用类似列表生成式的for循环无法实现的时候，还可以用函数来实现。

斐波拉契数列用列表生成式写不出来，但是，用函数把它打印出来却很容易：
```python
def fib(max):
    n, a, b = 0, 0, 1
    while n < max:
        print(b)
        a, b = b, a + b
        n = n + 1
    return 'done'
    
#注意，赋值语句：a, b = b, a + b 相当于：
#t = (b, a + b) # t是一个tuple
#a = t[0]
#b = t[1]

>>> fib(6)
1
1
2
3
5
8
'done'
```

fib函数实际上是定义了斐波拉契数列的推算规则，可以从第一个元素开始，推算出后续任意的元素，这种逻辑其实非常类似generator。

要把fib函数变成generator函数，只需要把print(b)改为yield b就可以了：
```python
def fib(max):
    n, a, b = 0, 0, 1
    while n < max:
        yield b
        a, b = b, a + b
        n = n + 1
    return 'done'
```

如果一个函数定义中包含yield关键字，那么这个函数就不再是一个普通函数，而是一个generator函数，
调用一个generator函数将返回一个generator：
```python
>>> f = fib(6)
>>> f
<generator object fib at 0x104feaaa0>

>>> for n in fib(6):
...     print(n)
...
1
1
2
3
5
8
```

generator函数和普通函数的执行流程不一样。普通函数是顺序执行，遇到return语句或者最后一行函数语句就返回。
而变成generator的函数，在每次调用next()的时候执行，遇到yield语句返回，再次执行时从上次返回的yield语句处继续执行。

### 迭代器

我们已经知道，可以直接作用于for循环的数据类型有以下几种：
- 集合数据类型，如list、tuple、dict、set、str等；
- generator，包括生成器和带yield的generator function。

这些可以直接作用于for循环的对象统称为可迭代对象：Iterable。 可以使用isinstance()判断一个对象是否是Iterable对象：
```python
>>> from collections.abc import Iterable
>>> isinstance([], Iterable)
True
>>> isinstance({}, Iterable)
True
>>> isinstance('abc', Iterable)
True
>>> isinstance((x for x in range(10)), Iterable)
True
>>> isinstance(100, Iterable)
False
```

而生成器不但可以作用于for循环，还可以被next()函数不断调用并返回下一个值，直到最后抛出StopIteration错误表示无法继续返回下一个值了。

可以被next()函数调用并不断返回下一个值的对象称为迭代器：Iterator。 可以使用isinstance()判断一个对象是否是Iterator对象：
```python
>>> from collections.abc import Iterator
>>> isinstance((x for x in range(10)), Iterator)
True
>>> isinstance([], Iterator)
False
>>> isinstance({}, Iterator)
False
>>> isinstance('abc', Iterator)
False
```

生成器都是Iterator对象，但list、dict、str虽然是Iterable，却不是Iterator。

把list、dict、str等Iterable变成Iterator可以使用iter()函数：
```python
>>> isinstance(iter([]), Iterator)
True
>>> isinstance(iter('abc'), Iterator)
True
```
为什么list、dict、str等数据类型不是Iterator？

这是因为Python的Iterator对象表示的是一个数据流，Iterator对象可以被next()函数调用并不断返回下一个数据，直到没有数据时抛出StopIteration错误。
可以把这个数据流看做是一个有序序列，但我们却不能提前知道序列的长度，只能不断通过next()函数实现按需计算下一个数据，
所以Iterator的计算是惰性的，只有在需要返回下一个数据时它才会计算。

Iterator甚至可以表示一个无限大的数据流，例如全体自然数。而使用list是永远不可能存储全体自然数的。

## 六、函数式编程

函数是Python内建支持的一种封装，我们通过把大段代码拆成函数，通过一层一层的函数调用，可以把复杂任务分解成简单的任务，这种分解可以称之为面向过程的程序设计。函数就是面向过程的程序设计的基本单元。
而函数式编程（请注意多了一个“式”字）——Functional Programming，虽然也可以归结到面向过程的程序设计，但其思想更接近数学计算。

我们首先要搞明白计算机（Computer）和计算（Compute）的概念。

在计算机的层次上，CPU执行的是加减乘除的指令代码，以及各种条件判断和跳转指令，所以，汇编语言是最贴近计算机的语言。
而计算则指数学意义上的计算，越是抽象的计算，离计算机硬件越远。 
对应到编程语言，就是越低级的语言，越贴近计算机，抽象程度低，执行效率高，比如C语言；越高级的语言，越贴近计算，抽象程度高，执行效率低，比如Lisp语言。

函数式编程就是一种抽象程度很高的编程范式，纯粹的函数式编程语言编写的函数没有变量，因此，任意一个函数，只要输入是确定的，输出就是确定的，这种纯函数我们称之为没有副作用。
而允许使用变量的程序设计语言，由于函数内部的变量状态不确定，同样的输入，可能得到不同的输出，因此，这种函数是有副作用的。

函数式编程的一个特点就是，允许把函数本身作为参数传入另一个函数，还允许返回一个函数！

Python对函数式编程提供部分支持。由于Python允许使用变量，因此，Python不是纯函数式编程语言。

### 高阶函数

高阶函数英文叫Higher-order function。

- 变量可以指向函数
```python
>>> abs(-10)
10
#把结果赋值给变量
>>> x = abs(-10)
>>> x
10
#变量可以指向函数。直接调用abs()函数和调用变量f()完全相同
>>> f = abs
>>> f(-10)
10
```

- 函数名也是变量
```python
#把abs指向10后，就无法通过abs(-10)调用该函数了！因为abs这个变量已经不指向求绝对值函数而是指向一个整数10！
>>> abs = 10
>>> abs(-10)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
TypeError: 'int' object is not callable
```
函数名其实就是指向函数的变量！对于abs()这个函数，完全可以把函数名abs看成变量，它指向一个可以计算绝对值的函数！

- 传入函数
```python
def add(x, y, f):
    return f(x) + f(y)

>>>add(-5, 6, abs)
>>>11
```

把函数作为参数传入，这样的函数称为高阶函数，函数式编程就是指这种高度抽象的编程范式。

1. map/reduce

如果你读过Google的那篇大名鼎鼎的论文[MapReduce: Simplified Data Processing on Large Clusters](https://research.google/pubs/pub62/)，你就能大概明白map/reduce的概念。

map()函数接收两个参数，一个是函数，一个是Iterable，map将传入的函数依次作用到序列的每个元素，并把结果作为新的Iterator返回。
```python
>>> def f(x):
...     return x * x
...
>>> r = map(f, [1, 2, 3, 4, 5, 6, 7, 8, 9])
>>> list(r)
[1, 4, 9, 16, 25, 36, 49, 64, 81]
```

reduce把一个函数作用在一个序列x1, x2, x3, ...上，这个函数必须接收两个参数，reduce把结果继续和序列的下一个元素做累积计算，其效果就是：
```python
>>> from functools import reduce
>>> def add(x, y):
...     return x + y
...
>>> reduce(add, [1, 3, 5, 7, 9])
25
```

2. filter

filter()也接收一个函数和一个序列。和map()不同的是，filter()把传入的函数依次作用于每个元素，然后根据返回值是True还是False决定保留还是丢弃该元素。
```python
def is_odd(n):
    return n % 2 == 1

list(filter(is_odd, [1, 2, 4, 5, 6, 9, 10, 15]))
# 结果: [1, 5, 9, 15]
```
注意到filter()函数返回的是一个Iterator，也就是一个惰性序列，所以要强迫filter()完成计算结果，需要用list()函数获得所有结果并返回list。

3. sorted

Python内置的sorted()函数就可以对list进行排序：
```python
>>> sorted([36, 5, -12, 9, -21])
[-21, -12, 5, 9, 36]
```

sorted()函数也是一个高阶函数，它还可以接收一个key函数来实现自定义的排序，例如按绝对值大小排序：
```python
>>> sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower)
['about', 'bob', 'Credit', 'Zoo']
```

要进行反向排序，不必改动key函数，可以传入第三个参数reverse=True：
```python
>>> sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower, reverse=True)
['Zoo', 'Credit', 'bob', 'about']
```

### 返回函数

高阶函数除了可以接受函数作为参数外，还可以把函数作为结果值返回。

```python
def lazy_sum(*args):
    def sum():
        ax = 0
        for n in args:
            ax = ax + n
        return ax
    return sum
#当我们调用lazy_sum()时，返回的并不是求和结果，而是求和函数：
>>> f = lazy_sum(1, 3, 5, 7, 9)
>>> f
<function lazy_sum.<locals>.sum at 0x101c6ed90>
#调用函数f时，才真正计算求和的结果：
>>> f()
25
```

函数lazy_sum中又定义了函数sum，并且，内部函数sum可以引用外部函数lazy_sum的参数和局部变量，
当lazy_sum返回函数sum时，相关参数和变量都保存在返回的函数中，这种称为“闭包（Closure）”的程序结构拥有极大的威力。

请再注意一点，当我们调用lazy_sum()时，每次调用都会返回一个新的函数，即使传入相同的参数：

```python
>>> f1 = lazy_sum(1, 3, 5, 7, 9)
>>> f2 = lazy_sum(1, 3, 5, 7, 9)
>>> f1==f2
False
```
f1()和f2()的调用结果互不影响。

返回一个函数时，牢记该函数并未执行，返回函数中不要引用任何可能会变化的变量。


### 匿名函数

在Python中，对匿名函数提供了有限支持。
```python
>>> list(map(lambda x: x * x, [1, 2, 3, 4, 5, 6, 7, 8, 9]))
[1, 4, 9, 16, 25, 36, 49, 64, 81]
#通过对比可以看出，匿名函数lambda x: x * x实际上就是：
def f(x):
    return x * x
    
#匿名函数也是一个函数对象，也可以把匿名函数赋值给一个变量，再利用变量来调用该函数：
>>> f = lambda x: x * x
>>> f
<function <lambda> at 0x101c6ef28>
>>> f(5)
25
#同样，也可以把匿名函数作为返回值返回
def build(x, y):
    return lambda: x * x + y * y
```
关键字lambda表示匿名函数，冒号前面的x表示函数参数。
匿名函数有个限制，就是只能有一个表达式，不用写return，返回值就是该表达式的结果。


### 装饰器

由于函数也是一个对象，而且函数对象可以被赋值给变量，所以，通过变量也能调用该函数。

```python
>>> def now():
...     print('2015-3-25')
...
>>> f = now
>>> f()
2015-3-25

#函数对象有一个__name__属性（注意：是前后各两个下划线），可以拿到函数的名字：
>>> now.__name__
'now'
>>> f.__name__
'now'
```

现在，假设我们要增强now()函数的功能，比如，在函数调用前后自动打印日志，但又不希望修改now()函数的定义，
这种在代码运行期间动态增加功能的方式，称之为“装饰器”（Decorator）。 
本质上，decorator就是一个返回函数的高阶函数。所以，我们要定义一个能打印日志的decorator，可以定义如下：
```python
def log(func):
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
```
观察上面的log，因为它是一个decorator，所以接受一个函数作为参数，并返回一个函数。
我们要借助Python的@语法，把decorator置于函数的定义处：

```python
@log
def now():
print('2015-3-25')

#调用now()函数，不仅会运行now()函数本身，还会在运行now()函数前打印一行日志：
>>> now()
call now():
2015-3-25

#把@log放到now()函数的定义处，相当于执行了语句：
now = log(now)
```

由于log()是一个decorator，返回一个函数，所以，原来的now()函数仍然存在，只是现在同名的now变量指向了新的函数，于是调用now()将执行新函数，即在log()函数中返回的wrapper()函数。

wrapper()函数的参数定义是(*args, **kw)，因此，wrapper()函数可以接受任意参数的调用。在wrapper()函数内，首先打印日志，再紧接着调用原始函数。

### 偏函数

Python的functools模块提供了很多有用的功能，其中一个就是偏函数（Partial function）。要注意，这里的偏函数和数学意义上的偏函数不一样。

在介绍函数参数的时候，我们讲到，通过设定参数的默认值，可以降低函数调用的难度。而偏函数也可以做到这一点。举例如下：


```python
#int()函数可以把字符串转换为整数，当仅传入字符串时，int()函数默认按十进制转换：
>>> int('12345')
12345

#但int()函数还提供额外的base参数，默认值为10。如果传入base参数，就可以做N进制的转换：
>>> int('12345', base=8)
5349
>>> int('12345', 16)
74565
```

假设要转换大量的二进制字符串，每次都传入int(x, base=2)非常麻烦，于是，我们想到，可以定义一个int2()的函数，默认把base=2传进去：
```python
def int2(x, base=2):
    return int(x, base)

#这样，我们转换二进制就非常方便了：
>>> int2('1000000')
64
>>> int2('1010101')
85
```

functools.partial就是帮助我们创建一个偏函数的，不需要我们自己定义int2()，可以直接使用下面的代码创建一个新的函数int2：

```python
>>> import functools
>>> int2 = functools.partial(int, base=2)
>>> int2('1000000')
64
>>> int2('1010101')
85

#注意到上面的新的int2函数，仅仅是把base参数重新设定默认值为2，但也可以在函数调用时传入其他值：
>>> int2('1000000', base=10)
1000000
```

所以，简单总结functools.partial的作用就是，把一个函数的某些参数给固定住（也就是设置默认值），返回一个新的函数，调用这个新函数会更简单。


## 七、模块

在Python中，一个.py文件就称之为一个模块（Module）。

使用模块有什么好处？
- 提高了代码的可维护性
- 编写代码不必从零开始  
  当一个模块编写完毕，就可以被其他地方引用。我们在编写程序的时候，也经常引用其他模块，包括Python内置的模块和来自第三方的模块。
- 避免函数名和变量名冲突  
  相同名字的函数和变量可以存在不同的模块中，因此，我们自己在编写模块时，不必考虑名字会与其他模块冲突。但要注意，尽量不要与内置函数名字冲突。

为了避免模块名冲突，Python又引入了按目录来组织模块的方法，称为包（Package）。

举例mycompany目录下有__init__.py, abc.py, xyz.py上个文件
请注意，每一个包目录下面都会有一个__init__.py的文件，这个文件是必须存在的，否则，Python就把这个目录当成普通目录，而不是一个包。
__init__.py可以是空文件，也可以有Python代码，因为__init__.py本身就是一个模块，而它的模块名就是mycompany。

### 使用模块

我们以内建的sys模块为例，编写一个hello的模块：
```python
##第1行注释可以让这个hello.py文件直接在Unix/Linux/Mac上运行，第2行注释表示.py文件本身使用标准UTF-8编码；
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

##第4行是一个字符串，表示模块的文档注释，任何模块代码的第一个字符串都被视为模块的文档注释；
' a test module '

##第6行使用__author__变量把作者写进去，这样当你公开源代码后别人就可以瞻仰你的大名；
__author__ = 'Michael Liao'

##使用sys模块的第一步，就是导入该模块：
import sys

def test():
##利用sys这个变量，就可以访问sys模块的所有功能
##argv变量，用list存储了命令行的所有参数。argv至少有一个元素，因为第一个参数永远是该.py文件的名称
    args = sys.argv
    if len(args)==1:
        print('Hello, world!')
    elif len(args)==2:
        print('Hello, %s!' % args[1])
    else:
        print('Too many arguments!')

##当我们在命令行运行hello模块文件时，Python解释器把一个特殊变量__name__置为__main__，而如果在其他地方导入该hello模块时，if判断将失败
if __name__=='__main__':
    test()
```

#### 作用域

有的函数和变量我们希望给别人使用，有的函数和变量我们希望仅仅在模块内部使用。在Python中，是通过_前缀来实现的。

- 正常的函数和变量名是公开的（public），可以被直接引用
- 类似__xxx__这样的变量是特殊变量，可以被直接引用，但是有特殊用途，我们自己的变量一般不要用这种变量名；
- 类似_xxx和__xxx这样的函数或变量就是非公开的（private），不应该被直接引用

之所以我们说，private函数和变量“不应该”被直接引用，而不是“不能”被直接引用，
是因为Python并没有一种方法可以完全限制访问private函数或变量，但是，从编程习惯上不应该引用private函数或变量。

### 安装第三方模块

[安装第三方模块](https://www.liaoxuefeng.com/wiki/1016959663602400/1017493741106496)