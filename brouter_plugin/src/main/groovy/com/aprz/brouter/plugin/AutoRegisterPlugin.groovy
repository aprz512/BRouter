package com.aprz.brouter.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project;


class AutoRegisterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 添加一个 transform
        def ext = project.extensions.getByType(BaseExtension)
        ext.registerTransform(new AutoRegisterTransform(project))
    }

}