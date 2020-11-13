package com.aprz.brouter.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project;

/**
 * 从 Matrix 学来的，将 java 代码写到 java 目录，方便些
 */
class AutoRegisterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 添加一个 transform
        def ext = project.extensions.getByType(BaseExtension)
        def transform = new RouteRegisterTransform(project)
        AutoRegisterSettings.RouteGroup = new AutoRegisterSettings.RouteGroup()
        ext.registerTransform(transform)
    }

}