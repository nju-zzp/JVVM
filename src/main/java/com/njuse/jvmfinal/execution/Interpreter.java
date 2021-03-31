package com.njuse.jvmfinal.execution;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.Method;

import java.nio.ByteBuffer;

public class Interpreter {
    private static ByteBuffer codeReader;  //当前栈帧对应当前方法的code

    /**
     * 解释执行该线程的当前栈帧对应的当前方法的字节码 —— 主方法
     */
    public static void interpret (JThread thread) {
        initialCodeReader(thread);
        loop(thread);
    }

    /**
     * 初始化此次执行的codeReader，确定解释执行的下一条指令的位置
     */
    private static void initialCodeReader (JThread thread) {
        byte[] codes = thread.getTopFrame().getMethod().getCode();
        int nextPC = thread.getPC();
        if (codes == null) {
            Method tempMethod = thread.getTopFrame().getMethod();
            throw new AbstractMethodError(tempMethod.getClazz().getName() + "-" + tempMethod.getName() + ": it is abstract");
        }
        codeReader = ByteBuffer.wrap(codes);
        codeReader.position(nextPC);
    }

    /**
     * 循环解释执行：
     * */
    private static void loop (JThread thread) {
        while (true) {
            //获取当前栈帧与当前方法
            StackFrame originalTop = thread.getTopFrame();
            Method method = originalTop.getMethod();

            //检验该方法是否parseCode
            if (!method.isParsed()) {
                method.parseCode();
            }

            //根据PC设置当前指令位置
            codeReader.position(thread.getPC());

            //取指令，同时译码获取指令对象，再获取操作数
            int opcode = codeReader.get() & 0xff;
            Instruction instruction = Decoder.decode(opcode);
            instruction.fetchOperands(codeReader);

            //更新PC
            int nextPC = codeReader.position();
            thread.setPC(nextPC);

//            System.out.println(instruction);

            //执行指令（注意：指令执行时必须改变该thread 的 PC的值）
            instruction.execute(originalTop);

            //检查是否切换栈帧。如果栈帧为空，则停止执行。如果不相同，则更新codeReader。
            StackFrame newTop = thread.getTopFrame();
            if (newTop == null) {
                break;
            } else if (newTop != originalTop) {
                try {
                    initialCodeReader(thread);
                } catch (AbstractMethodError e) {
                    throw new AbstractMethodError("the instruction is " + instruction.toString());
                }
            }

        }
    }
}
