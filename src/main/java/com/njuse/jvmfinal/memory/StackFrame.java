package com.njuse.jvmfinal.memory;

import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.struct.Vars;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackFrame {
    private Vars localVars;  //局部变量表
    private OperandStack operandStack;  //操作数栈
    private RuntimeConstantPool runtimeConstantPool;  //动态链接：指向当前方法所属的类的运行时常量池的引用
    private int returnAddress;  //返回地址
    private Method method;  //当前栈帧对应的当前方法
    private JThread thread;  //与当前JThread构成composition关系，便于随方法执行来改变JThread中PC的值

    public StackFrame (JThread thread, Method method, int returnAddress, int maxStackSize, int maxVarSize) {
        this.thread = thread;
        this.method = method;
        this.runtimeConstantPool = method.getClazz().getRuntimeConstantPool();
        this.returnAddress = returnAddress;
        this.operandStack = new OperandStack(maxStackSize);
        this.localVars = new Vars(maxVarSize);
    }


}
