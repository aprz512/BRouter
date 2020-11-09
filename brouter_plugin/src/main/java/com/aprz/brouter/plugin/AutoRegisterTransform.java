package com.aprz.brouter.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.aprz.brouter.plugin.base.BaseTransform;
import com.aprz.brouter.plugin.base.BaseWeaver;

import java.util.Set;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/4
 * <p>
 * Class desc: 实现一个 transform， 支持增量，支持多线程
 */
public class AutoRegisterTransform extends BaseTransform {

    private static final String TAG = "BRouterAutoRegisterTransform";

    public AutoRegisterTransform(BaseWeaver weaver) {
        super(weaver);
    }


    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

}
