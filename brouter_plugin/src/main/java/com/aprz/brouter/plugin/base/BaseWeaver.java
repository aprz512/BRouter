package com.aprz.brouter.plugin.base;

import com.aprz.brouter.plugin.util.FileUtil;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.FileTime;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/9
 * <p>
 * Class desc: 处理 class 与 jar 文件
 */
public abstract class BaseWeaver implements IWeaver {

    private static final FileTime ZERO = FileTime.fromMillis(0);

    private static final String FILE_SEP = File.separator;

    public final void weaveJar(File inputJar, File outputJar) throws IOException {
        ZipFile inputZip = new ZipFile(inputJar);
        ZipOutputStream outputZip = new ZipOutputStream(new BufferedOutputStream(
                java.nio.file.Files.newOutputStream(outputJar.toPath())));
        Enumeration<? extends ZipEntry> inEntries = inputZip.entries();
        while (inEntries.hasMoreElements()) {
            ZipEntry entry = inEntries.nextElement();
            InputStream originalFile =
                    new BufferedInputStream(inputZip.getInputStream(entry));
            ZipEntry outEntry = new ZipEntry(entry.getName());
            byte[] newEntryContent;
            // separator of entry name is always '/', even in windows
            if (!isWeavableClass(outEntry.getName().replace("/", "."))) {
                newEntryContent = org.apache.commons.io.IOUtils.toByteArray(originalFile);
            } else {
                newEntryContent = weaveSingleClassToByteArray(originalFile);
            }

            FileUtil.addZipEntry(outputZip, outEntry, new ByteArrayInputStream(newEntryContent));
        }

        outputZip.flush();
        FileUtil.closeQuietly(outputZip);
    }

    public final void weaveClass(File inputFile, File outputFile, String inputBaseDir) throws IOException {
        if (!inputBaseDir.endsWith(FILE_SEP)) {
            inputBaseDir = inputBaseDir + FILE_SEP;
        }
        if (isWeavableClass(inputFile.getAbsolutePath()
                .replace(inputBaseDir, "")
                .replace(FILE_SEP, "."))) {
            FileUtils.touch(outputFile);
            InputStream inputStream = new FileInputStream(inputFile);
            byte[] bytes = weaveSingleClassToByteArray(inputStream);
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(bytes);
            fos.close();
            inputStream.close();
        } else {
            if (inputFile.isFile()) {
                FileUtils.touch(outputFile);
                FileUtils.copyFile(inputFile, outputFile);
            }
        }
    }

    @Override
    public byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException {
        ClassReader classReader = new ClassReader(inputStream);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = newClassAdapter(Opcodes.ASM5, classWriter);
        // TODO 第二个参数需要搞清楚其意义
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    protected abstract ClassVisitor newClassAdapter(int opcode, ClassWriter classWriter);

    @Override
    public boolean isWeavableClass(String fullQualifiedClassName) {
        return fullQualifiedClassName.endsWith(".class")
                && !fullQualifiedClassName.contains("R$")
                && !fullQualifiedClassName.contains("R.class")
                && !fullQualifiedClassName.contains("BuildConfig.class");
    }


}
