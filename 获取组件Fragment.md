### 获取组件 Fragment

在 ARouter 中，获取 activity 与 fragment 是同一个注解。为了简化逻辑，BRouter 中使用了两个注解，把 Fragment 当作与 Service ，Interceptor 一样的类型来处理，所以，处理逻辑与之前的是类似的。

### 实现过程

#### 使用注解来获取 Module 中的所有 Fragment

> com.aprz.brouter.fragments.BRouter$$Fragment$$card

```java
public class BRouter$$Fragment$$card implements IModuleFragment {
    @Override
    public Map<String, Class<? extends Fragment>> fragments() {
        Map<String, Class<? extends Fragment>> result = new HashMap<String, Class<? extends Fragment>>();
        result.put("card/preview", PreviewFragment.class);
        return result;
    }
}
```

#### 使用反射将这些集合合并

> com.aprz.brouter.module.BRouter$$Module$$card#onCreate

```java
    @Override
    public void onCreate(Application application) {
        this.module.onCreate(application);
        ...
        FragmentHelper.addModuleFragment("card");
    }
```

#### 获取 framgnet

> com.aprz.home.fragment.FragmentRouteFragment#onCreateView

```java
Fragment fragment = FragmentHelper.getFragment(CardRouteUrl.Fragment.PREVIEW);
```



### 注意

fragment 中的字段暂时不支持自动绑定，因为 Bind 注解处理器还没有处理相应的逻辑。