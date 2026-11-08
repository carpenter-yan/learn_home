# 排序
## 什么是选择排序？请用 java 代码给出简单示例
### 一、选择排序原理
1. 找到数组中最小的元素，将它和数组第1个元素交换
2. 找到剩余元素中最小的，将它和数组第2个元素交换
3. 如此反复，直到将整个数组排序
### 二、Java 完整代码示例
```
public class SelectionSort {
    public static void selectionSort(int[] arr) {
    if (arr == null || arr.length <= 1) {
        return;
    }
    int len = arr.length;
    //i 代表已排序区间末尾，每轮确定arr[i]是剩余最小值
    for (int i = 0; i < len - 1; i++) {
        // 假设未排序区间第一个是最小值下标
        int minIndex = i;
        // 遍历未排序区间，找真正最小值下标
        for (int j = i + 1; j < len; j++) {
            if (arr[j] < arr[minIndex]) {
                minIndex = j;
            }
        }
        // 最小值和当前 i 位置交换
        if (minIndex != i) {
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }
}
```
### 三、面试口述精简版
选择排序每一轮在剩余未排序部分找出最小值，放到当前头部；全程必须完整遍历找最小值，不能像冒泡一样提前退出；
只交换一次元素，但属于不稳定排序，业务中极少使用，仅作基础算法理解。

## 什么是插入排序？
### 一、插入排序原理类比整理扑克牌：把数组分为已排序区和未排序区。
通常和人们整理桥牌时的方法是一张一张的来，将每一张牌插入到其他已经有序的牌中的适当位置
- 默认第一个元素是有序区；
- 依次取出未排序区第一个元素，向前遍历有序区，找到合适位置插入；
- 比当前元素大的元素统一后移，腾出空位放入当前值。
  时间复杂度：
  最好（数组已有序）O(n)，平均/最坏 O(n^2)
  空间：O(1)原地排序
  稳定性：稳定排序（相等元素不交换，相对顺序不变）
  优势：数据接近有序时效率极高，工程上小数组常用插入排序优化快排
### 二、Java 代码示例
```
public class InsertSort {
    public static void insertSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int len = arr.length;
        // i是未排序区间第一个元素，从第二个开始
        for (int i = 1; i < len; i++) {
            int cur = arr[i]; // 当前待插入元素
            int j = i - 1;    // 有序区末尾下标
            // 向前遍历有序区，大于cur就后移
            while (j >= 0 && arr[j] > cur) {
                arr[j + 1] = arr[j];
                j--;
            }
            // 找到插入位置 j+1
            arr[j + 1] = cur;
        }
    }
}
```
### 三、面试口述精简版
插入排序像插扑克牌，逐个把未排序元素插入前面有序区间；数组越接近有序速度越快，稳定排序。
业务很少单独手写，但快排底层递归到小数组时会改用插入排序做优化。

## 什么是冒泡排序？
### 一、冒泡排序原理
- 遍历数组，相邻两个元素两两对比，如果前一个>后一个，交换两者；
- 每一轮遍历结束后，当前未排序区间最大的元素会 “冒泡” 到尾部；
- 优化：如果某一轮全程没有发生交换，说明数组已经有序，直接退出循环。

时间复杂度：平均/最坏O(n^2)，最优（已有序）O(n)
稳定性：稳定排序（相等元素不交换，相对顺序不变）
### 二、Java 完整示例（带优化）
```
public static void bubbleSort(int[] arr) {
    if (arr == null || arr.length <= 1) {
        return;
    }
    int len = arr.length;
    // 外层控制轮次，每轮确定一个最大值放到末尾
    for (int i = 0; i < len - 1; i++) {
        boolean swapFlag = false; // 标记本轮是否发生交换
        // 内层只遍历未排序区间，len-1-i 后面已经排好序
        for (int j = 0; j < len - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换相邻元素
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapFlag = true;
            }
        }
        // 本轮无交换，数组已有序，提前结束
        if (!swapFlag) {
            break;
        }
    }
}
```
### 三、面试口述精简版
冒泡排序就是相邻元素依次比较逆序交换，每一轮把最大元素移到数组末尾；通过交换标记优化有序数组场景，属于稳定的平方级排序，
只适合少量数据排序，业务代码基本不用，主要用于理解基础排序思想。

#### 补充：为什么不稳定？举例
数组 [2, 2, 1] 第一轮找到最小值 1，和第一个 2 交换 → [1, 2, 2] 原本两个2的相对顺序颠倒，因此不稳定。

## 快速排序
### 一、快速排序原理分治思想：
选一个基准值pivot，把数组划分成两部分：小于pivot放左边，大于等于pivot 放右边；
基准元素划分完成后，它的位置就是最终有序位置；
递归处理左区间、右区间，直到区间只有一个元素（天然有序）。

核心特性
平均时间复杂度：O(n log n)
最坏：O(n^2)（数组完全有序，基准选两端）
空间：O(log n)递归栈开销
稳定性：不稳定排序
工程最常用，底层 Arrays.sort 对基础类型就是快排变体
二、Java 简易实现（好理解，面试手写够用）
```
public class QuickSort {
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        sort(arr, 0, arr.length - 1);
    }

    // 递归排序 [left, right]
    private static void sort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        // 分区，返回基准下标
        int pivotIdx = partition(arr, left, right);
        sort(arr, left, pivotIdx - 1);
        sort(arr, pivotIdx + 1, right);
    }

    // 分区函数，以最右侧为基准
    private static int partition(int[] arr, int l, int r) {
        int pivot = arr[r];
        int smallArea = l; // 小于pivot区域的右边界

        for (int i = l; i < r; i++) {
            if (arr[i] < pivot) {
                swap(arr, smallArea, i);
                smallArea++;
            }
        }
        // 把基准放到分割点
        swap(arr, smallArea, r);
        return smallArea;
    }

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {4, 7, 2, 9, 1, 5, 3};
        quickSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
        // 输出：1 2 3 4 5 7 9
    }
}
```
### 三、面试口述精简回答
快排采用分治，选定基准将数组分割为大小两部分，基准归位后递归左右子数组；平均效率最优，但不稳定，有序数组会退化到平方复杂度，一般通过随机基准、三数取中来优化最坏情况。

### 四、高频面试简答
为什么不稳定？
等值元素会被分到基准两侧，原有相对顺序改变。
怎么避免最坏O(n^2) ?
随机选基准、三数取中法；数组长度很小时改用插入排序。
和归并排序对比？
快排原地排序、空间小；归并稳定，但需要额外数组空间。

## 归并排序 Merge Sort
### 一、归并排序核心原理
分治思想，分两步：分 + 合
分：把数组从中间拆成左右两半，递归拆分，直到子数组只有1个元素（天然有序）；
合：将两个有序子数组，合并成一个完整有序数组。

特性
时间复杂度：稳定O(nlogn)，无论数据有序/乱序都不变
空间复杂度：O(n)，需要额外辅助数组存合并结果
稳定性：稳定排序（等值元素相对顺序不变）
适用场景：超大文件、海量数据外排序；内存数组优先快排，大文件优先归并

### 二、Java 完整示例
```
public class MergeSort {
    // 对外入口
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int[] temp = new int[arr.length]; // 辅助数组，避免频繁创建
        sort(arr, temp, 0, arr.length - 1);
    }

    // 递归拆分区间 [l, r]
    private static void sort(int[] arr, int[] temp, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = l + (r - l) / 2;
        sort(arr, temp, l, mid);      // 拆分左半
        sort(arr, temp, mid + 1, r);  // 拆分右半
        merge(arr, temp, l, mid, r);   // 合并左右有序区间
    }

    // 合并两个有序区间：[l,mid] 和 [mid+1,r]
    private static void merge(int[] arr, int[] temp, int l, int mid, int r) {
        int i = l;    // 左区间指针
        int j = mid + 1; // 右区间指针
        int t = 0;    // 辅助数组指针

        // 依次取左右区间较小值放入temp
        while (i <= mid && j <= r) {
            if (arr[i] <= arr[j]) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }
        // 左区间剩余元素拷贝
        while (i <= mid) {
            temp[t++] = arr[i++];
        }
        // 右区间剩余元素拷贝
        while (j <= r) {
            temp[t++] = arr[j++];
        }
        // 把temp有序数据写回原数组
        t = 0;
        while (l <= r) {
            arr[l++] = temp[t++];
        }
    }

    public static void main(String[] args) {
        int[] nums = {5, 2, 9, 1, 5, 6};
        mergeSort(nums);
        for (int num : nums) {
            System.out.print(num + " ");
        }
        // 输出：1 2 5 5 6 9
    }
}
```
### 三、面试口述精简版
归并排序采用分治，先拆分数组为左右两部分递归排序，再合并两个有序数组；时间复杂度稳定 O(nlogn)，是稳定排序，但需要额外数组空间，适合超大文件外排序。
### 四、高频对比考点
和快排区别：
快排原地排序、空间小、不稳定，有序数据会退化；
归并空间开销大、稳定、性能不受原始数据顺序影响。
为什么稳定？
合并时 arr[i] <= arr[j]，等值优先取左边数组元素，原有顺序保留。


## 堆排序 Heap Sort
构建大顶堆，堆顶是最大值，循环取出堆顶再调整堆。
不稳定；O (nlogn)，原地排序空间 O (1)；海量数据 TopK专用。

# 一天速成算法高频题（后端面试必考，优先刷，总量 20 道，分模块）
## 一、数组/双指针（4题，最简单必拿分）
### 1.两数之和 LeetCode 1
给定一个整数数组nums和一个整数目标值target，请你在该数组中找出和为目标值target的那两个整数，并返回它们的数组下标。
你可以假设每种输入只会对应一个答案，并且你不能使用两次相同的元素。你可以按任意顺序返回答案。

示例 1：
输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。

示例 2：
输入：nums = [3,2,4], target = 6
输出：[1,2]

示例 3：
输入：nums = [3,3], target = 6
输出：[0,1]

方法一：暴力枚举
思路及算法
最容易想到的方法是枚举数组中的每一个数 x，寻找数组中是否存在target - x。
当我们使用遍历整个数组的方式寻找target - x 时，需要注意到每一个位于x之前的元素都已经和x匹配过，因此不需要再进行匹配。
而每一个元素不能被使用两次，所以我们只需要在x后面的元素中寻找target - x。
```
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }
}
```

### 2.三数之和 LeetCode 15
给你一个整数数组 nums ，判断是否存在三元组[nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，
同时还满足 nums[i] + nums[j] + nums[k] == 0 。请你返回所有和为 0 且不重复的三元组。
注意：答案中不可以包含重复的三元组。

示例 1：
输入：nums = [-1,0,1,2,-1,-4]
输出：[[-1,-1,2],[-1,0,1]]
解释：
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
注意，输出的顺序和三元组的顺序并不重要。

示例 2：
输入：nums = [0,1,1]
输出：[]
解释：唯一可能的三元组和不为 0 。

示例 3：
输入：nums = [0,0,0]
输出：[[0,0,0]]
解释：唯一可能的三元组和为 0 。

### 3.滑动窗口 - 无重复最长子串 LeetCode 3
### 4.移除元素 LeetCode 27

## 二、链表（3 题，手写高频）
### 5.反转链表 LeetCode 206
### 6.链表两数相加 LeetCode 2
### 7.环形链表 LeetCode 141
三、哈希表（2 题，秒杀题）
### 有效的字母异位词 LeetCode 242
### 最长连续序列 LeetCode 128
四、二分查找（2 题，边界必考）
### 二分查找基础 LeetCode 704
### 搜索旋转排序数组 LeetCode 33
五、栈 / 队列（2 题）
### 有效的括号LeetCode 20
### 滑动窗口最大值LeetCode 239
六、二叉树（3 题，递归基础）
二叉树层序遍历 LeetCode 102
二叉树反转 LeetCode 226
### 二叉树最大深度 LeetCode 104
七、动态规划简单款（2 道，只背基础模板）
### 爬楼梯 LeetCode 70
### 打家劫舍 LeetCode 198
八、贪心（2 道）
### 分发饼干 LeetCode 455
### 跳跃游戏 LeetCode 55
九、SQL必刷2道（后端必考）
### 查找重复的电子邮箱 LeetCode 182
### 第N高的薪水 LeetCode 177