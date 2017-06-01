#include <cstring>
#include <iostream>
using namespace std;

void perm(string sofar,string rest) {
    if (rest == "") cout << sofar << endl;
    else {
        for (int i = 0; i < rest.length();i++) {    // 把rest中的字符慢慢地往sofar中移
            string next = sofar + rest[i];
            string remaining = rest.substr(0, i) + rest.substr(i + 1); // 选了第一个，剩下的生成
            perm(next, remaining);
        }
    }
}






#include <stdio.h>
#include <stdlib.h>
#include <cstdio>
#include <cstring>
#include <iostream>
#include <iomanip>
#include <algorithm>
using namespace std;

void test(int x[]) {
    int a = x[0] * 1000 + x[1] * 100 + x[2] * 10 + x[3];
    int b = x[4] * 10000 + x[5] * 1000 + x[6] * 100 + x[7] * 10 + x[8];
    if (a * 3 == b)
        cout << a << " " << b << endl;
}

//k表示当前选取到第几个数
void f(int x[], int k) {
	int i,t;
	if(k>=9) {
		test(x);
		return;
    }

	for(i=k; i<9; i++){ //第i个数分别与它后面的数字交换就能得到新的排列
        swap(x[k],x[i]);
        f(x,k+1);
        swap(x[k],x[i]);
	}
}

int main()
{
	int x[] = {1,2,3,4,5,6,7,8,9};
	f(x,0);
}
