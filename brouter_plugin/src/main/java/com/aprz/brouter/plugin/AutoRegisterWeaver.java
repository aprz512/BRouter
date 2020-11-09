package com.aprz.brouter.plugin;

import com.aprz.brouter.plugin.base.BaseWeaver;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Arrays;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/9
 * <p>
 * Class desc: 流程有点问题
 * 需要先收集所有的 class 然后再处理
 */
public class AutoRegisterWeaver extends BaseWeaver {


    @Override
    protected ClassVisitor newClassAdapter(int opcode, ClassWriter classWriter) {
        return new AutoRegisterClassAdapter(opcode, classWriter);
    }

    class AutoRegisterClassAdapter extends ClassVisitor {

        boolean needInject;

        public AutoRegisterClassAdapter(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            boolean implementation = Arrays.toString(interfaces).contains("com/aprz/brouter/api/IRouteGroup");
            String className = name.replace("/", ".");
            int index = className.lastIndexOf(".");
            if (index != -1) {
                String packageName = className.substring(0, index + 1);
                needInject = implementation && "com.aprz.brouter.routes".equals(packageName);
            }
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (!needInject) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
            MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new AutoRegisterMethodAdapter(api, methodVisitor, access, name, descriptor);
        }
    }


    class AutoRegisterMethodAdapter extends AdviceAdapter {

        protected AutoRegisterMethodAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
        }

    }
}

