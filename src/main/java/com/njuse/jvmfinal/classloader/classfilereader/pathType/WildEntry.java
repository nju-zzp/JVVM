package com.njuse.jvmfinal.classloader.classfilereader.pathType;

import java.io.File;
import java.io.IOException;

/**
 * format : dir/.../*
 */
public class WildEntry extends PathEntry {
    public WildEntry(String classPath) {
        super(classPath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String parentClassPath = classPath.substring(0, classPath.length() - 2);
        File parent = new File(parentClassPath);
//        File parent = new File(classpath).getParentFile();
        String[] files = parent.list();
        byte[] ret = null;
        for (String file : files) {
            if (file.endsWith(".jar") || file.endsWith(".JAR") || file.endsWith(".zip") || file.endsWith(".ZIP")) {
                PathEntry entry = new ArchivedEntry( parentClassPath + FILE_SEPARATOR + file);
                ret = entry.readClassFile(className);
                if (ret == null) {
                    continue;
                } else {
                    return ret;
                }
            }
        }
        return ret;
    }
}
