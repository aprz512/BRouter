package com.aprz.brouter.processor;

import com.aprz.brouter.annotation.Route;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.aprz.brouter.processor.Constant.ACTIVITY;
import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.aprz.brouter.annotation.Route"})
public class RouterProcessor extends BaseProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!CollectionUtils.isEmpty(set)) {
            Set<? extends Element> routeElements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            try {
                this.parseRoutes(routeElements);
            } catch (Exception ignored) {
            }
            return true;
        }

        return false;
    }

    private void parseRoutes(Set<? extends Element> routeElements) throws IOException {
        if (CollectionUtils.isEmpty(routeElements)) {
            return;
        }

        /*
         * 这个时候，需要想一下，我们生成的类是个什么样子，才能满足我们的需求
         *
         * class A {
         *     public Map routeMap() {
         *          Map m = new Map();
         *          m.add("path", ActivityA.class);
         *         return Map();
         *     }
         * }
         *
         * 这样的类符合我们的需求吗？
         * 看起来是符合的，但是仔细想一下，是不是有点不对劲
         * 假设我们使用遍历Dex的方式来加载映射表，首先我们拿到了 A 的 class，如下：
         * Class A;
         * 就算我们将这个类的对象实例化出来了，那么也只能用 Object 来接收：
         * Object o = A.newInstance();
         * 那么，我们就没法调用 routerMap 方法了啊！！！这不就很沙雕
         * 所以，我们需要一个接口，来抽象出这个方法行为，然后生成的类为这样：
         *
         * class A implements IRouterMap {
         *
         *     public void loadInto(Map<String, Class<? extends Activity>> routeMap) {
         *          routeMap.add("path", ActivityA.class);
         *     }
         *
         * }
         *
         * 上面的代码中，Map 不过是换了一种来源，我们按照 ARouter 的写法吧，按照我们原本的方式也是可以的。
         * 然后，我们就可以愉快的调用 loadInto 方法了：
         * IRouterMap map = A.newInstance();
         * map.loadInto(xxx);
         */

        String mapClass = buildMapClass(routeElements);

        /*
        懒加载
        生成两个类，
        一个类用来记录映射表，我们叫做 module map
        一个类用来记录 module map 这个类，我们叫做 module group
        其实就是为了节省内存，用户不用的 module，就暂时不用加载它的映射表

        class M implements IRouteModule {

            void loadModule(List<IRouteMap> moduleList) {
                moduleList.add(xxx);
            }

        }

        class N implements IRouteMap {

            void loadMap(Map<String, Class<? extends Activity>> routeMap) {
                routeMap.put("xxx", yyy.class);
            }

        }

        然后，初始化的时候只加载 M 类，执行跳转逻辑的时候，再去加载 N 类，这样就省了内存。
         */

        buildModuleClass(mapClass);
    }

    private String buildMapClass(Set<? extends Element> routeElements) throws IOException {
        TypeMirror type_Activity = elementUtils.getTypeElement(ACTIVITY).asType();


        // 生成一个 Map<String, Class<? extends Activity>> 类型，后面会用到
        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(type_Activity))
                )
        );

        // 生成 routeMap 参数
        ParameterSpec groupParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, "routeMap").build();

        // 生成 loadMap 方法
        MethodSpec.Builder loadIntoMethodBuilder = MethodSpec.methodBuilder("loadMap")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(groupParamSpec);

        // 填充 loadMap 方法，由于映射表较多，所以使用一个循环
        for (Element element : routeElements) {
            TypeMirror tm = element.asType();
            Route route = element.getAnnotation(Route.class);

            if (types.isSubtype(tm, type_Activity)) {
                loadIntoMethodBuilder.addStatement("routeMap.put($S, $T.class)", route.path(), TypeName.get(tm));
            }

        }

        String routeMapFileName = "BRouter$$RouteMap$$" + moduleName;
        JavaFile.builder("com.aprz.brouter.routes",
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement("com.aprz.brouter.api.IRouteMap")))
                        .addModifiers(PUBLIC)
                        .addMethod(loadIntoMethodBuilder.build())
                        .build()
        ).build().writeTo(mFiler);
        return routeMapFileName;
    }

    private void buildModuleClass(String routeMapFileName) throws IOException {


        // 再生成一个类
        // 生成一个 List<IRouteMap> moduleList
        ParameterizedTypeName moduleList = ParameterizedTypeName.get(
                ClassName.get(List.class),
                ClassName.get(elementUtils.getTypeElement("com.aprz.brouter.api.IRouteMap")));

        // 生成 moduleList 参数
        ParameterSpec moduleSpec = ParameterSpec.builder(moduleList, "moduleList").build();

        // 生成 loadMap 方法
        MethodSpec.Builder loadModuleSpec = MethodSpec.methodBuilder("loadModule")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(moduleSpec);

        loadModuleSpec.addStatement("moduleList.add(new $L())", routeMapFileName);

        String loadModuleClass = "BRouter$$RouteModule$$" + moduleName;


        JavaFile.builder("com.aprz.brouter.routes",
                TypeSpec.classBuilder(loadModuleClass)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement("com.aprz.brouter.api.IRouteModule")))
                        .addModifiers(PUBLIC)
                        .addMethod(loadModuleSpec.build())
                        .build()
        ).build().writeTo(mFiler);

    }

}
