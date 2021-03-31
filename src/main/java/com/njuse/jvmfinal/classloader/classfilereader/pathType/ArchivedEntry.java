package com.njuse.jvmfinal.classloader.classfilereader.pathType;

import com.njuse.jvmfinal.util.IOUtil;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * format : dir/subdir/target.jar
 */
public class ArchivedEntry extends PathEntry{
    public ArchivedEntry(String classPath) {
        super(classPath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        ZipFile zipFile = new ZipFile(classPath);
        ZipEntry zipEntry = zipFile.getEntry(className);
        if (zipEntry != null) {
            return IOUtil.readFileByBytes(zipFile.getInputStream(zipEntry));
        } else {
            return null;
        }
    }
}
