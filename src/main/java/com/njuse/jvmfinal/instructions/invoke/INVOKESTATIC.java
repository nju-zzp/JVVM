package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.jvmfinal.memory.struct.Slot;
import com.njuse.jvmfinal.memory.struct.Vars;

public class INVOKESTATIC extends Index16Instruction {

    /**
     *逻辑顺序：
     * 1. 从常量池中获取即将执行的静态方法
     * 2. 解析方法为直接引用
     * 3. 判断是否本地方法，是本地方法则跳过
     * 4. 初始化该静态方法所在的类
     * 5. 创建新的栈帧，并push operandStack，传递参数，设置PC
     *
     * ！！！注意注意注意：
     * 虽说逻辑按照这个顺序执行，但是栈帧所在的虚拟机栈vmStack是栈结构，先进后出，解释器从栈顶开始逐步向下执行，
     * 故实现invokestatic时需要将pushStackFrame的顺序与逻辑顺序相反，
     * 先push的后执行，后push的先执行。
     *
     *  具体来看，就是先push该方法的frame，再push子类的初始化方法的frame，再push父类初始化方法的frame。
     *  @param frame
     */

    @Override
    public void execute(StackFrame frame) {
        MethodRef methodRef = (MethodRef) frame.getRuntimeConstantPool().getConstant(index);
        Method method = methodRef.resolveMethodRef();  //解析方法
        OperandStack operandStack = frame.getOperandStack();
        JThread thread = frame.getThread();

        if (method.isNative()) {
            return;
        }

        //hack TestUtil
        if (method.getClazz().getName().equals("cases/TestUtil")) {
            switch (method.getName()) {
                case "reach" :
                    int value = operandStack.popInt();
                    System.out.println(value);
                    break;

                case "equalInt" :
                    int b = operandStack.popInt();
                    int a = operandStack.popInt();
                    if (a == b) {
                        operandStack.pushInt(1);
                    } else {
                        throw new RuntimeException(a+"!="+b);
                    }
                    break;

                case "equalFloat" :
                    float bb = operandStack.popFloat();
                    float aa = operandStack.popFloat();
                    if (Math.abs(aa - bb) < 1e-5) {
                        operandStack.pushInt(1);
                    } else {
                        throw new RuntimeException(aa+"!="+bb);
                    }
                    break;

            }
        }

        // common
        else {
            //检验是否需要将参数传入操作数栈，传递该方法的参数列表，pushFrameStack
            int argc = method.getArgc();
            Slot[] argValues = new Slot[argc];
            for (int i = 0; i < argc; i++) {
                argValues[i] = operandStack.popSlot();
            }

            StackFrame newFrame = new StackFrame(thread, method, thread.getPC(), method.getMaxStack(), method.getMaxLocal());
            Vars locals = newFrame.getLocalVars();
            for (int i = 0; i < argc; i++) {
                locals.setSlot(i, argValues[argc - i - 1]);
            }
            thread.pushFrame(newFrame);
            thread.setPC(0);


            //初始化调用该静态方法的类
            JClass invokeClazz = method.getClazz();  //调用该静态方法的类
            invokeClazz.initialClass(frame.getThread());
        }

    }
}
