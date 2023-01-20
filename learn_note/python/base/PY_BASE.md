



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

