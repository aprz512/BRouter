package com.aprz.brouter


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.DefaultFileCollectionFactory;


class CodeIsolationPlugin implements Plugin<Project> {

    private static final String mainModuleName = "mainModuleName"

    @Override
    void apply(Project project) {

        if (!project.rootProject.hasProperty(mainModuleName)) {
            throw new RuntimeException("请在根工程的 gradle.properties 里面配置 mainModuleName 属性，比如（mainModuleName=app）")
        }

        String moduleName = project.path.replace(":", "")
        String mainModuleName = project.rootProject.property(mainModuleName)

        // 2. 是否是打包任务
        boolean assembleTask = isAssembleTask(project.gradle.startParameter.taskNames)

        project.dependencies.metaClass.component { Object value ->
            // 打包主 module
            // 添加依赖
            if (moduleName == mainModuleName && assembleTask) {
                return value
            }
            // 否则，随便 return 一个空的
            return project.fileTree(["dir": "_brouter_not_exist", "exclude": "**"])
        }

    }

    private static boolean isAssembleTask(List<String> taskNames) {
        for (String task : taskNames) {
            if (task.toUpperCase().contains("ASSEMBLE")
                    || task.contains("aR")
                    || task.contains("asR")
                    || task.contains("asD")
                    || task.toUpperCase().contains("TINKER")
                    || task.toUpperCase().contains("INSTALL")
                    || task.toUpperCase().contains("RESGUARD")) {
                return true
            }
        }
        return false
    }

}