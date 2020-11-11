package com.aprz.brouter.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.aprz.brouter.plugin.base.BaseTransform;
import com.aprz.brouter.plugin.base.BaseWeaver;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor;
import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

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

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        // 在这里注入代码
        if(AutoRegisterSettings.injectFile.getName().endsWith("jar")) {
            injectJar();
        } else {
            injectFile();
        }
    }

    private void injectJar() throws IOException {
//        File jarFile = AutoRegisterSettings.injectFile;
//        File optJar = new File(jarFile.getParent(), jarFile.getName() + ".opt");
//        if (optJar.exists())
//            optJar.delete()
//        JarFile file = new JarFile(AutoRegisterSettings.injectFile);
//        Enumeration enumeration = file.entries();
//        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar));
//
//        while (enumeration.hasMoreElements()) {
//            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
//            String entryName = jarEntry.getName()
//            ZipEntry zipEntry = new ZipEntry(entryName)
//            InputStream inputStream = file.getInputStream(jarEntry)
//            jarOutputStream.putNextEntry(zipEntry)
//            if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {
//
//                Logger.i('Insert init code to class >> ' + entryName)
//
//                def bytes = referHackWhenInit(inputStream)
//                jarOutputStream.write(bytes)
//            } else {
//                jarOutputStream.write(IOUtils.toByteArray(inputStream))
//            }
//            inputStream.close()
//            jarOutputStream.closeEntry()
//        }
//        jarOutputStream.close()
//        file.close()
//
//        if (jarFile.exists()) {
//            jarFile.delete()
//        }
//        optJar.renameTo(jarFile)
    }

    private void injectFile() {

    }

    class InjectAdapter extends ClassAdapter{
        public InjectAdapter(ClassVisitor cv) {
            super(cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}
