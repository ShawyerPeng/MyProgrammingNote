## 一维数组作参数
1. `void fun(int *a);`
2. `void fun(int a[]);`
3. `void fun(int a[10]);`  
4. `void fun(int a[],int n)`

实参可以使数组名/整型指针，而虚参只能是整型指针。

## 二维数组作参数
1. `void fun(int a[3][10]);`
2. `void fun(int a[][10]);`
3. `void fun(int *(a)[10]);`
4. `void Fun(int **a);`
5. `void fun(int (*a)[n]);`

# 动态申请列大小固定的二维数组
申请一维数数组,再其指针强制转化成为二维数组指针
```c++
//列数固定  
const int MAXCOL = 3;

//输入行数
int nRow;
cin >> nRow;

//申请一维数据并将其转成二维数组指针  
int *pp_arr = new int[nRow * MAXCOL];
int(*p)[MAXCOL] = (int(*)[MAXCOL])pp_arr;

//为二维数组赋值  
int i, j;
for (i = 0; i < nRow; i++)
    for (j = 0; j < MAXCOL; j++)
        p[i][j] = i + j;

//输出二维数组  
for (i = 0; i < nRow; i++)
{
    for (j = 0; j < MAXCOL; j++)
        printf("%5d", p[i][j]);
    putchar('\n');
}

//释放资源  
delete[] pp_arr;
return 0;
}
```






















## 1. 一维数组的动态分配和释放
```c++
int *array1D;
//假定数组长度为m
//动态分配空间
array1D = new int [m];
//释放
delete [] array1D;
```

## 2. 二维数组的动态分配和释放
```c++
int **array2D;
//假定数组第一维长度为m， 第二维长度为n
//动态分配空间
array2D = new int *[m];
for( int i=0; i<m; i++ )
{
    array2D[i] = new int [n]  ;
}
//释放
for( int i=0; i<m; i++ )
{
    delete [] arrar2D[i];   //或者就delete [] array2D;
}
delete array2D;
```

## 3. 三维数组的动态分配和释放
```c++
int ***array3D;
//假定数组第一维为m， 第二维为n， 第三维为h
//动态分配空间
array3D = new int **[m];
for( int i=0; i<m; i++ )
{
    array3D[i] = new int *[n];
    for( int j=0; j<n; j++ )
    {
         array3D[i][j] = new int [h];
    }
}
//释放
for( int i=0; i<m; i++ )
{
    for( int j=0; j<n; j++ )
    {
         delete array3D[i][j];
    }
    delete array3D[i];
}
delete array3D;
```



```c++
void swap1(int *a,int *b)   //指针的方式传递
{
	int c;
	c = *a;
	*a = *b;
	*b = c;
}
void swap2(int &a, int &b)  //引用的方式传递
{
	int c;
	c = a;
	a = b;
	b = c;
}
int main()
{
	int x = 3, y = 4;
	swap1(&x, &y);
	cout << x << " " << y << endl;
    swap2(x, y);
    cout << x << " " << y << endl;
}
```
