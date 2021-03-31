package com.njuse.jvmfinal.classloader.classfilereader.pathType;

import java.io.File;
import java.io.IOException;

/**
 * 根据路径格式不同，选择不同的加载方式
 */
public abstract class PathEntry {
    protected String classPath;
    public final String PATH_SEPARATOR = File.pathSeparator;
    public final String FILE_SEPARATOR = File.separator;

    public PathEntry(String classPath) {
        this.classPath = classPath;
    }

    public String getClassPath () {
        return classPath;
    }

    public abstract byte[] readClassFile(String className) throws IOException;

}
