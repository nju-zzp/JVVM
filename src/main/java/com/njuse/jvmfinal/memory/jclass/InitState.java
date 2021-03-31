package com.njuse.jvmfinal.memory.jclass;

public enum InitState {
    PREPARED,  //类已经加载验证准备，还未初始化
    BUSY,  //类正在初始化
    SUCCESS,  //类已经初始化成功
    FAIL  //初始化失败
}
