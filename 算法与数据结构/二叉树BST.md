# 一、概述
二分搜索树（Binary Search Tree）是一种能够将链表插入的灵活性和有序数组查找的高效性结合起来的符号表实现。具体来说，就是使用每个结点含有两个链接（链表中每个结点只含有一个链接）的二叉查找树来高效地实现符号表，这也是计算机科学中最重要的算法之一。  
顾名思义，其主要目的用于搜索，它是二叉树结构中最基本的一种数据结构，是后续理解B树、B+树、红黑树的基础，后三者在具体的工程实践中更常用，比如C++中STL就是利用红黑树做Map，B树用于磁盘上的数据库维护等，后三者均是在二叉搜索树的基础上演变而来的，理解二分搜索树是学习后者的基础。

![](http://p1.bqimg.com/567571/453dd406b9233161.png)
![](http://p1.bqimg.com/567571/5a773f6b7db00ff3.png)

与基础的数据结构如链表、堆、栈等基本结构一样，学习二叉搜索树的关键是深入理解访问与操作二叉树的算法及性能分析，本文如下部分首先介绍二叉搜索树的特征；然后重点介绍二叉搜索树的遍历、查找（包括最值查找、前驱后继查找）、以及插入和删除等操作，最后简单进行分析。

# 二、基本原理及性质
如果你不想看下面这些话，只想一句话明白搜索二叉树怎么实现：（key比自身小放左面，key比自身大放右边）。

每个结点的键值大于左孩子，小于右孩子。以左右孩子为根的子树认为二分搜索树（天然包括递归结构）
由于不一定是完全二叉树，用数组表示不方便，Node结点（指针）表示，

```cpp
template <typename Key, typename Value>
class BST{
private:
    struct Node {
        Key key;    
        Value value;
        Node *left;     // 左孩子(指向Node的指针)
        Node *right;    // 右孩子(指向Node的指针)

        Node(Key key, Value value) {    // Node结构的构造函数
            this->key = key;
            this->value = value;
            this->left = this->right = NULL;
        }

        Node(Node *node){
            this->key = node->key;
            this->value = node->value;
            this->left = node->left;
            this->right = node->right;
        }
    };
    Node *root; // 根结点
    int count;  // 二分搜索树存的总结点数

public:
    BST(){  // 二分搜索树的构造函数
        root = NULL;
        count = 0;
    }
    ~BST(){ // 二分搜索树的析构函数
        destroy( root );
    }
    int size(){
        return count;
    }
    bool isEmpty(){
        return count == 0;
    }
}
```

## 1. 添加元素
三种不同情况：  
![](http://p1.bpimg.com/567571/608105b70fc03d52.gif)  
1. 60与根结点41比较，60大所以放到41右边
2. 然后要插入到以58为根的二分搜索树中，60与58比较，60大所以放到58右边

![](http://i1.piimg.com/567571/b036829a4f45fc0f.gif)  
1. 28与根结点41比较，28小所以放到41左边
2. 然后要插入到以22为根的二分搜索树中，28与22比较，28大所以放到22右边
3. 然后要插入到以33为根的二分搜索树中，28与33比较，28小所以放到33左边

![](http://i1.piimg.com/567571/5f485d589b955b20.gif)
1. 42与根结点41比较，42大所以放到41右边
2. 然后要插入到以58为根的二分搜索树中，42与58比较，42小所以放到58左边
3. 然后要插入到以50为根的二分搜索树中，42与50比较，42小所以放到50左边
4. 由于原来50的左孩子也是42，两个值一样大，所以用新的42代替原来的42

```c
public:
void insert(Key key, Value value) {
     root = insert(root, key, value);
}

private:
// 向以node为根的二叉搜索树中,插入结点(key, value)
// 返回插入新结点后的二叉搜索树的根
Node* insert(Node *node, Key key, Value value) {
    if( node == NULL ){      // 如果结点为空,就在此结点处加入x信息（边界条件）
        count ++;
        return new Node(key, value);
    }

    if( key == node->key )  
        node->value = value;  //如果相等,就把频率加1
    else if( key < node->key )
        node->left = insert( node->left , key, value);  // 如果x小于结点的值,就继续在结点的左子树中插入x
    else
        node->right = insert( node->right, key, value);  // 如果x大于结点的值,就继续在结点的右子树中插入x

    return node;    // 返回结点本身
}
```

## 2. contain包含
```c
bool contain(Key key) {
    return contain(root, key);
}

// 查看以node为根的二叉搜索树中是否包含键值为key的结点
bool contain(Node* node, Key key) {
    if( node == NULL )
        return false;

    if( key == node->key )
        return true;
    else if( key < node->key )
        return contain( node->left , key );
    else // key > node->key
        return contain( node->right , key );
}
```

## 3. search查找
与contain一样，只是返回值不同
```c
Value* search(Key key){
    return search( root , key );
}

// 在以node为根的二叉搜索树中查找key所对应的value
Value* search(Node* node, Key key){
    if( node == NULL )
        return NULL;

    if( key == node->key )
        return &(node->value);  // 返回value值对应的地址
    else if( key < node->key )
        return search( node->left , key );
    else
        return search( node->right, key );
}
```

## 4. 遍历（深度优先遍历）
### 前序遍历 中序遍历 后序遍历（相对于根结点）

前序遍历：先遍历根结点，然后遍历左子树，最后遍历右子树。（先访问当前结点，再依次递归访问左右子树）（遍历到左就打印）（遍历整个树，对元素做某些事情，最常用）   
![](http://p1.bqimg.com/567571/14c2db93a15cdefa.gif) 
![](http://i1.piimg.com/567571/e2bd33c0c932536c.png)

---

中序遍历：先遍历左子树，然后遍历根结点，最后遍历右子树。（先递归访问左子树，再访问自身，再递归访问右子树）（遍历到中间就打印）（元素从小到大排序，应用：排序）  
![](http://i1.piimg.com/567571/788b7a8c84012733.gif)
![](http://p1.bpimg.com/567571/2fba299b3127a324.png)

---

后序遍历：先遍历左子树，然后遍历右子树，最后遍历根结点。（先递归访问左右子树，再访问自身结点）（遍历到右才打印）（先删除左右子结点，再删除自身，应用：释放整个二叉树）  
![](http://p1.bpimg.com/567571/8cdd8a3e85e5a703.gif)
![](http://i1.piimg.com/567571/40830f830baa0e9c.png)

```c
void preOrder(){
    preOrder(root);
}

// 对以node为根的二叉搜索树进行前序遍历
void preOrder(Node* node){
    if( node != NULL ){
        cout<<node->key<<endl;
        preOrder(node->left);
        preOrder(node->right);
    }
}
```

```c
void inOrder(){
    inOrder(root);
}

// 对以node为根的二叉搜索树进行中序遍历
void inOrder(Node* node){
    if( node != NULL ){
        inOrder(node->left);
        cout<<node->key<<endl;
        inOrder(node->right);
    }
}
```

```c
void postOrder(){
    postOrder(root);
}

// 对以node为根的二叉搜索树进行后序遍历
void postOrder(Node* node){
    if( node != NULL ){
        postOrder(node->left);
        postOrder(node->right);
        cout<<node->key<<endl;
    }
}
```

销毁整个二叉树destroy（后序遍历）
```c
void destroy(Node* node){
    if( node != NULL ){
        destroy(node->left);
        destroy(node->right);
        delete node;
        count--;
    }
}
```

## 5. 层序遍历（广度优先遍历）
![](http://p1.bpimg.com/567571/574ed9273be30475.png)

引入队列（先进先出，后进后出）  
只要队列不为空，就将元素拿出来遍历或操作  
然后拿到它的左右孩子入队16 30  
再将16拿出来遍历或操作，并将16的左右孩子13 22入队  
然后30拿出来遍历或操作，并将30的左右孩子29 42入队  

```c
void levelOrder(){
    queue<Node*> q;
    q.push(root);
    while( !q.empty() ){    // 只要队列不为空，就拿出队首元素出队进行操作
        Node *node = q.front();
        q.pop();    // 队首元素出队

        cout<<node->key<<endl;  // 对队首元素某些操作

        if( node->left )            // 将它的左右孩子入队
            q.push( node->left );
        if( node->right )
            q.push( node->right );
    }
}
```

## 6. 最大值/最小值
通过二分搜索树的性质知道，结点的左孩子比它小，右孩子比它大。  
寻找最小值就是一直沿着它的左孩子方向找，直到一个结点没有左孩子，说明没有元素比它还小了，那么该结点就是整个二分搜索树的最小值  
寻找最大值就是一直沿着它的右孩子方向找，直到一个结点没有右孩子，说明没有元素比它还大了，那么该结点就是整个二分搜索树的最大值

```c
Key minimum() {
    assert( count != 0 );
    Node* minNode = minimum( root );
    return minNode->key;
}

// 在以node为根的二叉搜索树中,返回最小键值的结点
Node* minimum(Node* node) {
    if( node->left == NULL )    // 边界条件。如果左孩子为空，说明它自己就是最小值了
        return node;

    return minimum(node->left);
}
```

```c
Key maximum() {
    assert( count != 0 );
    Node* maxNode = maximum(root);
    return maxNode->key;
}

// 在以node为根的二叉搜索树中,返回最大键值的结点
Node* maximum(Node* node) {
    if( node->right == NULL )   // 边界条件。如果右孩子为空，说明它自己就是最大值了
        return node;

    return maximum(node->right);
}
```

## 7. 删除最小/大值所在结点
直接把该最小值结点的右孩子代替自己的位置

![](http://p1.bpimg.com/567571/f91093392cb1bed5.gif)
![](http://p1.bqimg.com/567571/cd2d7bfb034c4745.gif)

```c
void removeMin() {
    if( root )
        root = removeMin( root );
}

// 删除掉以node为根的二分搜索树中的最小结点
// 返回删除结点后新的二分搜索树的根
Node* removeMin(Node* node) {
    if( node->left == NULL ) {  // 看有没有左孩子，为空则它自己就是最小值
        Node* rightNode = node->right;  // 右孩子代替父结点的位置
        delete node;
        count --;
        return rightNode;
    }

    node->left = removeMin(node->left); // 继续查找最小值
    return node;
}
```

```c
void removeMax() {
    if( root )
        root = removeMax( root );
}

// 删除掉以node为根的二分搜索树中的最大结点
// 返回删除结点后新的二分搜索树的根
Node* removeMax(Node* node) {   
    if( node->right == NULL ) { // 看有没有右孩子，为空则它自己就是最大值
        Node* leftNode = node->left;    // 右孩子代替父结点的位置
        delete node;
        count --;
        return leftNode;
    }

    node->right = removeMax(node->right);   // 继续查找最大值
    return node;
}
```

## 8. 删除任意结点
如果要删除的结点只有一个孩子，很简单，只要用它的孩子代替它的位置即可  

如果要删除的结点左右孩子都有，有一个算法：Hibbard Deletion算法解决  
从孩子中找一个结点代替要删除的结点d，然而既不应该是左孩子，也不应该是右孩子，应该是右子树中的最小值结点s：  
`s->right = delMin(d->right)`将右子树中的最小值结点s删除掉，然后该右子树成为s结点的右子树(将s指向原来结点58的右子树)，s结点的左孩子就是原来d结点的左孩子50
最后彻底将结点d删除掉：`s->left = d->left`

![](http://p1.bqimg.com/567571/01feee00a962757c.gif)

```c
void remove(Key key) {
    root = remove(root, key);
}

// 删除掉以node为根的二分搜索树中键值为key的结点
// 返回删除结点后新的二分搜索树的根
Node* remove(Node* node, Key key) {
    if( node == NULL )  // 没有改结点
        return NULL;

    if( key < node->key ) { // 
        node->left = remove( node->left , key );
        return node;
    }
    else if( key > node->key ) {    // 在node右结点中寻找并删除
        node->right = remove( node->right, key );
        return node;
    }
    else{   // key == node->key就删除该结点

        if( node->left == NULL ) {  // 左孩子为空，只有右孩子
            Node *rightNode = node->right;  // 将右孩子node->right保存下来变成rightNode
            delete node;
            count --;
            return rightNode;   // 返回node的右结点rightNode
        }

        if( node->right == NULL ) { // 右孩子为空，只有左孩子
            Node *leftNode = node->left;    // 将左孩子node->left保存下来变成leftNode
            delete node;
            count--;
            return leftNode;    // 返回node的左结点leftNode
        }

        // node->left != NULL && node->right != NULL 左右孩子都不为空
        Node *successor = new Node(minimum(node->right));   // 找到右子树中的最小值作为要删除结点的后继.new Node()调用了构造函数，使之重新构建了一个node结点
        count ++;

        successor->right = removeMin(node->right);  // 后继的右孩子就是删除掉最小值之后返回的指针
        successor->left = node->left;   // 后继的左孩子就是原来要删除结点的左孩子

        delete node;
        count --;

        return successor;
    }
}
```
