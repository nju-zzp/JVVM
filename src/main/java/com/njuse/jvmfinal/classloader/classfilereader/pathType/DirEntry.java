package com.njuse.jvmfinal.classloader.classfilereader.pathType;

import com.njuse.jvmfinal.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * format : dir/subdir/.../
 */
public class DirEntry extends PathEntry {

    public DirEntry(String classPath) {
        super(classPath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        File file = new File(classPath, className);
        if (file.exists()) {
            return IOUtil.readFileByBytes(new FileInputStream(file));
        } else {
            return null;
        }
    }
}
