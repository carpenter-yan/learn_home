



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

### 高级特性
