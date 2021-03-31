package com.njuse.jvmfinal.classloader.classfilereader.pathType;

import java.io.File;
import java.io.IOException;

public class CompositeEntry extends PathEntry {

    public CompositeEntry(String classPath) {
        super(classPath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String[] classpathList = classPath.split(File.pathSeparator);
        byte[] ret = null;
        for (String atomClasspath : classpathList) {
            PathEntry entry = null;
            if (atomClasspath.endsWith(FILE_SEPARATOR + "*")) {
                entry = new WildEntry(atomClasspath);
            } else if (atomClasspath.endsWith(".zip") || atomClasspath.endsWith(".ZIP" )|| atomClasspath.endsWith(".jar") || atomClasspath.endsWith(".JAR")) {
                entry = new ArchivedEntry(atomClasspath);
            } else {
                entry = new DirEntry(atomClasspath);
            }
            ret = entry.readClassFile(className);
            if (ret == null) {
                continue;
            } else {
                return ret;
            }
        }

        return ret;
    }
}
