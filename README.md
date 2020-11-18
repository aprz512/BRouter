

## 注意
- 本项目只用于学习，不用于生产。
- 项目中代码以简单易懂为主。
- 在看懂本项目的情况下，再去看其他的开源库会轻松很多。

#### 功能实现原理
[组件跳转](组件跳转.md)

[组件路由懒加载](组件路由懒加载.md)





一些参考项目  

https://github.com/xiaojinzi123/Component

Gradle资料

https://mp.weixin.qq.com/s/UlnHuM2-Dqad3MXE77xJyg

#### 组件的生命周期
组件的加载-卸载是一个c-s过程，其生命周期也是体现在容器是否维持着组件的对象。注解在这里的作用是生成中间类，控制被注解类实例的生成和销毁。
组件要想和服务端的控制器通信那么必须实现预定的接口，用于接收onCreate()，onDestory()等回调事件，类似于所有的客户端Activity都要继承Activity。控制器只知道是个Activity，不关心是个具体的什么Activity。


#### 跨组件获取Fragment

1. 首先有一个FragmentManager的东西，用于保存注册的Fragment。
2. 注册-获取，用注解将需要注册的Fragment进行标识，注册处理器会生成一个中间类，由容器持有。
3. 客户端传入fragment的标识，向容器索要对应的Fragement(中间类延迟创建Fragment实例)。

注解处理器生成的Fragment中间类会实际处理客户端Fragment的创建销毁逻辑。这样下来，客户端的Fragment只需要向往常一样处理业务就行，只需加上对应的注解就可以让注解处理器去完成剩下的工作。
