package com.njuse.jvmfinal.classloader.classfilereader.loadtype;

import com.njuse.jvmfinal.classloader.classfilereader.pathType.PathEntry;
import org.apache.commons.lang3.tuple.Pair;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public abstract class Loader {
    protected PathEntry pathEntry;  //每个具体实现的loader都拥有自己的path搜索区域
    private Loader superLoader;  //每个实现具体的loader都拥有自己的super来完成双亲委派机制，bootstrap的super为null


    /**
     * 读取class文件的二进制，同时返回读取文件的加载器即define loader
     * @param className
     * @return Pair<byte[]/ Loader>
     * @throws IOException
     */
    public Pair<byte[], Loader> readClassFile (String className) throws IOException {
        return Pair.of(pathEntry.readClassFile(className), this);
    }

}
