注解处理器类库(java.lang.reflect.AnnotatedElement)
=========================
Java使用Annotation接口来代表程序元素前面的注解，该接口是所有Annotation类型的父接口。除
此之外，Java在java.lang.reflect 包下新增了AnnotatedElement接口，该接口代表程序中可以
接受注解的程序元素，该接口主要有如下几个实现类：

- Class：类定义
- Constructor：构造器定义
- Field：累的成员变量定义
- Method：类的方法定义
- Package：类的包定义

java.lang.reflect 包下主要包含一些实现反射功能的工具类，实际上，java.lang.reflect 包
所有提供的反射API扩充了读取运行时Annotation信息的能力。当一个Annotation类型被定义为运
行时的Annotation后，该注解才能是运行时可见，当class文件被装载时被保存在class文件中的
Annotation才会被虚拟机读取。

AnnotatedElement 接口是所有程序元素（Class、Method和Constructor）的父接口，所以程序
通过反射获取了某个类的AnnotatedElement对象之后，程序就可以调用该对象的如下四个个方法来
访问Annotation信息：

1. \<T extends Annotation> T getAnnotation(Class\<T> annotationClass): 返回该
程序元素上存在的、指定类型的注解，如果该类型注解不存在，则返回null。
2. Annotation[] getAnnotations():返回该程序元素上存在的所有注解。
3. boolean is AnnotationPresent(Class\<?extends Annotation> annotationClass):
判断该程序元素上是否包含指定类型的注解，存在则返回true，否则返回false.
4. Annotation[] getDeclaredAnnotations()：返回直接存在于此元素上的所有注释。与此
接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度
为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任
何影响。