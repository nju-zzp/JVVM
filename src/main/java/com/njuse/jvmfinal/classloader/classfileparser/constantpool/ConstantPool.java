package com.njuse.jvmfinal.classloader.classfileparser.constantpool;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.ConstantPoolInfo;
import org.apache.commons.lang3.tuple.Pair;

public class ConstantPool {
    private ConstantPoolInfo[] infos;
    //infos[0] ~ infos[constantPoolCount-2] 有数据，infos[constantPoolCount-1]无数据

    /**
     * 此处使用getInstance来实例化对象
     * 主要为了返回一个Pair，不仅包含对象本身，还包含额外的数据和信息
     * 同时在创建时也方便对新创建的对象进行个性化的操作，不需要另外override构造函数影响全局
     *
     * Integer: 该ConstantPool在class二进制文件中占据的字节数
     */
    public static Pair<ConstantPool, Integer> getInstance(int constantPoolCnt, byte[] in, int offset) {
        ConstantPool constantPool = new ConstantPool();
        constantPool.infos = new ConstantPoolInfo[constantPoolCnt];
        int currentOfs = offset;
        int entryCnt = 0;
        while (entryCnt < constantPoolCnt - 1) {
            Pair<ConstantPoolInfo, Integer> infoInt = ConstantPoolInfo.getConstantPoolInfo(constantPool, in, currentOfs);
            ConstantPoolInfo info = infoInt.getKey();
//            System.out.println("infoIndex in ClassFile: " + entryCnt);
            constantPool.infos[entryCnt] = info;
            entryCnt += info.getEntryLength();
            currentOfs += infoInt.getValue();
        }
        return Pair.of(constantPool, currentOfs - offset);
    }

    public ConstantPoolInfo get(int i) {
        if (i <= 0 || i > infos.length - 1) {
            throw new UnsupportedOperationException("Invalid CP index " + i);
        }

        ConstantPoolInfo info = infos[i - 1];
        if (info == null) {
            throw new UnsupportedOperationException("Invalid CP index " + i);
        }
        return info;
    }

    public ConstantPoolInfo[] getInfos() {
        return infos;
    }

}
