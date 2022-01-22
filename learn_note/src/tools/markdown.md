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
>>> 1个引用符号
>> 2个引用符号
> 3个引用符号

### 插入图像

### 




参考文章
[Markdown语法手册](https://blog.csdn.net/witnessai1/article/details/52551362?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4.pc_relevant_default&utm_relevant_index=9)  
[Markdown语法图文全面详解(10分钟学会)](https://blog.csdn.net/u014061630/article/details/81359144?spm=1001.2101.3001.6650.19&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-19.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-19.nonecase)  