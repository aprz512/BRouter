package com.aprz.brouter.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.aprz.brouter.plugin.util.FileUtil;
import com.aprz.brouter.plugin.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.NEW;

public class RouteRegisterTransform extends Transform {

    private static final String TAG = "RouteRegisterTransform";

    /**
     * 尽量的敲诈电脑的资源
     */
    private ExecutorService executor = Executors.newFixedThreadPool(16);
    private List<Future<?>> result = new ArrayList<>();


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
    public boolean isIncremental() {
        /*
         * 仔细想了一下，这个玩意好像没法支持增量
         * 假设，删除了一个目标文件，那么增量的情况下没法去删除前面增加的代码
         */
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        // 是否增量编译
        final boolean isIncremental = transformInvocation.isIncremental() && this.isIncremental();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        //如果非增量，则清空旧的输出内容
        if (!isIncremental) {
            outputProvider.deleteAll();
        }

        Collection<TransformInput> transformInputs = transformInvocation.getInputs();
        collectClass(isIncremental, outputProvider, transformInputs);


        // 多线程一定要等待完成，这里调了一下午...
        for (Future<?> future : result) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        result.clear();

        if (AutoRegisterSettings.injectFile == null
                || AutoRegisterSettings.routeGroup.classList.isEmpty()) {
            return;
        }

        // 在这里注入代码
//        Log.e(TAG, "transform " + AutoRegisterSettings.injectFile.getName());
        if (AutoRegisterSettings.injectFile.getName().endsWith("jar")) {
            injectJar();
        } else {
            injectFile();
        }
    }


    private void collectClass(boolean isIncremental, TransformOutputProvider outputProvider, Collection<TransformInput> transformInputs) throws IOException {
        for (TransformInput transformInput : transformInputs) {
            for (DirectoryInput directoryInput : transformInput.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);

                if (isIncremental) {
                    String srcDirPath = directoryInput.getFile().getAbsolutePath();
                    String destDirPath = dest.getAbsolutePath();
                    Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
                    for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
                        Status status = changedFile.getValue();
                        File inputFile = changedFile.getKey();
                        String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath);
                        File destFile = new File(destFilePath);
                        switch (status) {
                            case NOTCHANGED:
                                // do nothing
                                break;
                            case REMOVED:
                                if (destFile.exists()) {
                                    //noinspection ResultOfMethodCallIgnored
                                    destFile.delete();
                                }
                                break;
                            case ADDED:
                            case CHANGED:
                                transformFile(inputFile, destFile);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    transformDir(directoryInput.getFile(), dest);
                }

            }
            for (JarInput jarInput : transformInput.getJarInputs()) {
                Status status = jarInput.getStatus();
                File src = jarInput.getFile();
                File dest = outputProvider.getContentLocation(
                        src.getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);

                if (isIncremental) {
                    switch (status) {
                        case NOTCHANGED:
                            // do nothing
                            break;
                        case ADDED:
                        case CHANGED:
                            transformJar(src, dest);
                            break;
                        case REMOVED:
                            if (dest.exists()) {
                                FileUtils.forceDelete(dest);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    transformJar(src, dest);
                }
            }
        }
    }

    private void transformJar(final File source, final File dest) {
        Future<?> submit = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // 复制文件
                    FileUtils.copyFile(source, dest);
                    // 扫描文件
                    scanJar(source, dest);
//                    Log.e(TAG, "transformJar SOURCE " + source);
//                    Log.e(TAG, "transformJar DEST " + dest);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        });
        result.add(submit);

    }

    private void scanJar(File source, File dest) throws IOException {

        ZipFile inputZip = new ZipFile(source);
        Enumeration<? extends ZipEntry> inEntries = inputZip.entries();
        while (inEntries.hasMoreElements()) {
            ZipEntry entry = inEntries.nextElement();
            String name = entry.getName();
            if (name.startsWith("com/aprz/brouter/routes/")) {
//                Log.e(TAG, "scanJar - startsWith " + name);
                InputStream inputStream = inputZip.getInputStream(entry);
                scanClass(inputStream);
                inputStream.close();
            } else if (AutoRegisterSettings.ROUTE_HELPER_CLASS.equals(name)) {
                AutoRegisterSettings.injectFile = dest;
//                Log.e(TAG, "scanJar - ROUTE_HELPER_CLASS " + dest + "/" + dest.getName());
            }
        }
        inputZip.close();
    }

    private void scanClass(InputStream inputStream) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        FileUtil.closeQuietly(inputStream);
    }

    private void transformFile(final File inputFile, final File outputFile) {
        Future<?> submit = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.copyFile(inputFile, outputFile);
                    if (isRouteGroupClass(inputFile)) {
                        scanClass(new FileInputStream(inputFile));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        });
        result.add(submit);
    }

    private void transformDir(final File inputDir, final File outputDir) {
        Future<?> submit = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.copyDirectory(inputDir, outputDir);
                    for (File file : com.android.utils.FileUtils.getAllFiles(outputDir)) {
//                        Log.e(TAG, "file " + file.getAbsolutePath());
                        if (isRouteGroupClass(file)) {
                            scanClass(new FileInputStream(file));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        });
        result.add(submit);
    }

    private boolean isRouteGroupClass(File file) {
        return file.getAbsolutePath().startsWith(AutoRegisterSettings.ROUTE_GROUP_PACKAGE);
    }

    static class ScanClassVisitor extends ClassVisitor {

        ScanClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature,
                          String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            boolean implementation = Arrays.toString(interfaces).contains("com/aprz/brouter/api/IRouteGroup");
//            Log.e(TAG, "ScanClassVisitor - implementation - " + implementation);
            String className = name.replace("/", ".");
//            Log.e(TAG, "ScanClassVisitor - className - " + className);
            int index = className.lastIndexOf(".");
//            Log.e(TAG, "ScanClassVisitor - index - " + index);
            if (index != -1) {
                String packageName = className.substring(0, index);
//                Log.e(TAG, "ScanClassVisitor - packageName - " + packageName);
                if (implementation && "com.aprz.brouter.routes".equals(packageName)) {
                    // 将所有 RouterGroup 类都收集起来
                    AutoRegisterSettings.routeGroup.addClass(name);
//                    Log.e(TAG, "ScanClassVisitor - ADD " + name);
                }
            }
        }
    }

    private void injectJar() throws IOException {

//        Log.e(TAG, "injectJar " + injectFile.getAbsolutePath());

        File jarFile = AutoRegisterSettings.injectFile;
        File optJar = new File(jarFile.getParent(), jarFile.getName() + ".opt");
        if (optJar.exists()) {
            optJar.delete();
        }
        JarFile file = new JarFile(AutoRegisterSettings.injectFile);
        Enumeration enumeration = file.entries();
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar));

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement();
            String entryName = jarEntry.getName();
            ZipEntry zipEntry = new ZipEntry(entryName);
            InputStream inputStream = file.getInputStream(jarEntry);
            jarOutputStream.putNextEntry(zipEntry);
            // 找到指定的类文件
            if (AutoRegisterSettings.ROUTE_HELPER_CLASS.equals(entryName)) {
                byte[] bytes = referHackWhenInit(inputStream);
                jarOutputStream.write(bytes);
            } else {
                jarOutputStream.write(IOUtils.toByteArray(inputStream));
            }
            inputStream.close();
            jarOutputStream.closeEntry();
        }
        jarOutputStream.close();
        file.close();

        if (jarFile.exists()) {
            jarFile.delete();
        }
        optJar.renameTo(jarFile);
    }

    private byte[] referHackWhenInit(InputStream inputStream) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new InjectVisitor(Opcodes.ASM5, cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        inputStream.close();
        return cw.toByteArray();
    }

    private void injectFile() throws IOException {
        File classFile = AutoRegisterSettings.injectFile;
        FileInputStream fileInputStream = new FileInputStream(classFile);
        ClassReader cr = new ClassReader(fileInputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new InjectVisitor(Opcodes.ASM5, cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        FileUtil.closeQuietly(fileInputStream);
    }

    static class InjectVisitor extends org.objectweb.asm.ClassVisitor {

        InjectVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            // 给指定的方法插入代码
            if ("injectRouteByPlugin".equals(name)) {
                mv = new RouteMethodVisitor(Opcodes.ASM5, mv);
            }
            return mv;
        }

    }

    static class RouteMethodVisitor extends MethodVisitor {

        RouteMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            //generate code before return
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {

                for (String className : AutoRegisterSettings.routeGroup.classList) {
                    // 使用 ASM Bytecode Outline 插件
                    mv.visitTypeInsn(NEW, className);
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
                    mv.visitMethodInsn(INVOKESTATIC, "com/aprz/brouter/api/core/RouteHelper", "register", "(Lcom/aprz/brouter/api/IRouteGroup;)V", false);
                }

            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 2, maxLocals);
        }
    }
}
