package com.njuse.jvmfinal.classloader.classfilereader;

import com.njuse.jvmfinal.classloader.classfilereader.loadtype.ApplicationLoader;
import com.njuse.jvmfinal.classloader.classfilereader.loadtype.Loader;
import com.njuse.jvmfinal.classloader.classfilereader.pathType.*;
import com.njuse.jvmfinal.util.IOUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;

public class ClassFileReader {
    private static ClassFileReader classFileReader = new ClassFileReader();  //单例设计模式，仅存在一个ClassFileReader
    private static final String FILE_SEPARATOR = File.separator; //文件层次分隔符‘\\’
    private static final String PATH_SEPARATOR = File.pathSeparator; //路径分隔符';'

    private ClassFileReader () {}

    public static ClassFileReader getInstance() {
        return classFileReader;
    }

    /**
     * 实现双亲委派模型
     * 灵感来源于官方文档的实现形式，但是改进为不使用extends，而采用composition的方式
     */
    public Pair<byte[], Loader> readClassFile (String className, Loader initialLoader) throws IOException {
        //转化为与文件名相同的className
        className = IOUtil.classNameTransform(className);

        //检查initialLoader，如果为null就默认为ApplicationLoader
        Loader loader = (initialLoader == null ? ApplicationLoader.getInstance() : initialLoader);
        Pair<byte[], Loader> result = null;

        //检查initialLoader是否有super，若有，则委托给super递归调用
        if (loader.getSuperLoader() != null) {
            result = ClassFileReader.getInstance().readClassFile(className, loader.getSuperLoader());
        } else {
            return loader.readClassFile(className);
        }

        //检查父类加载器是否加载成功
        if (result.getKey() == null) {
            // 若父类加载器无法加载，再调用自身的加载器加载
            result = loader.readClassFile(className);
        }

        return result;
    }


    /**
     * 通过classPath格式来寻找正确的PathEntry来加载类
     * @param classPath
     * @return PathEntry
     */
    public static PathEntry choosePathEntry (String classPath) {
        if (classPath.contains(PATH_SEPARATOR)) {
            return new CompositeEntry(classPath);
        }
        if (classPath.endsWith("*")) {
            return new WildEntry(classPath);
        }
        if (classPath.endsWith(".jar") || classPath.endsWith(".JAR")
                || classPath.endsWith(".zip") || classPath.endsWith(".ZIP")) {
            return new ArchivedEntry(classPath);
        }
        return new DirEntry(classPath);
    }


}
