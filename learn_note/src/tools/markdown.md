[TOC]

# 目录
+ [目录](#目录)
    + [斜体和粗体](#斜体和粗体)
    + [分级标题](#分级标题)
    + [超链接](#超链接)

### 斜体和粗体
markdown使用星号（*）和底线（_）作为标记强调字词的符号  
*一个星号是斜体*  
**两个星号是粗体**  
***三个星号是粗斜体***  
_一个下划线_  
__两个个下划线__  
___三个下划线___  
~~两个波浪线是删除线~~

### 分级标题
使用井号（#）标记标题，1~6个井号代表1~6级标题，井号后面有个空格
# 一级标题
## 二级标题
### 三级标题
#### 四级标题
##### 五级标题
###### 六级标题

### 超链接
行内式: 支持两种形式的链接语法： 行内式和参考式两种形式
```[链接文字](链接地址 "链接标题")这样的形式。链接地址与链接标题前有一个空格```
[今日头条](https://www.toutiao.com/ "今日头条")
[vscode](vscode.md)
[jvm](../java/base/jvm.md)

参考式: 如果某一个链接在文章中多处使用，那么使用引用 的方式创建链接将非常好，它可以让你对链接进行统一的管理
自动链接: 使用<>包起来 <https://www.jd.com/>

### 锚点
只支持在标题后插入锚点{#锚点名}，在跳转的地方(#锚点名)。如果是锚点名和标题名相同可以省略锚点定义

### 列表
无序列表:使用 *，+，- 表示无序列表，有序列表:使用数字接着一个英文句点，无序和有序列表可以嵌套使用，列表可以使用多个空格和tab缩进

### 引用
使用>符号以及一个空格，可以多层嵌套
> 1个引用符号
>> 2个引用符号  
>>> 3个引用符号

### 插入图像
图片的创建方式与超链接相似，而且和超链接一样也有两种写法，行内式和参考式写法。
语法中图片Alt的意思是如果图片因为某些原因不能显示，就用定义的图片Alt文字来代替图片。 图片Title则和链接中的Title一样，表示鼠标悬停与图片上时出现的文字。 Alt 和 Title 都不是必须的，可以省略，但建议写上。
![百度](https://dss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1-66368c33f8.png "百度")

### 其它
换行: 行尾2个空格加回车或者html换行标记
缩进: 多个空格，建议4个缩进
颜色: html的font标签
背景色: html标签实现
分隔线: 用三个以上的星号、减号、底线来建立一个分隔线，行内不能有其他东西。你也可以在星号或是减号中间插入空格。下面每种写法都可以建立分隔线

### 表格
不管是哪种方式，第一行为表头，第二行分隔表头和主体部分，第三行开始每一行为一个表格行。  
列于列之间用管道符|隔开。原生方式的表格每一行的两边也要有管道符。  
第二行还可以为不同的列指定对齐方向。默认为左对齐，在-右边加上:就右对齐。  
学号|姓名|分数
-|-|-
小明|男|75
小红|女|79
小陆|男|92

### 代码
一种是利用缩进(Tab), 另一种是用六个（`）包裹多行代码包裹代码。
注意： 缩进式插入前方必须有空行

参考文章
[Markdown语法手册](https://blog.csdn.net/witnessai1/article/details/52551362?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4.pc_relevant_default&utm_relevant_index=9)  
[Markdown语法图文全面详解(10分钟学会)](https://blog.csdn.net/u014061630/article/details/81359144?spm=1001.2101.3001.6650.19&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-19.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-19.nonecase)  