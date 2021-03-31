package com.njuse.jvmfinal.memory.jclass;

import com.njuse.jvmfinal.classloader.classfileparser.MethodInfo;
import com.njuse.jvmfinal.classloader.classfileparser.attribute.CodeAttribute;
import com.njuse.jvmfinal.execution.Decoder;
import com.njuse.jvmfinal.instructions.base.Instruction;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.ArrayList;


@Getter
@Setter
public class Method extends ClassMember {
    private int maxStack;  //对应操作数栈的最大深度maxStackDepth
    private int maxLocal;  //局部变量表的maxSize
    private int argc;  //该方法传入参数的Slot数目
    private byte[] code;  //该方法对应字节码指令的操作码操作数的字节流
    private ArrayList<String> instList;  //该方法的指令序列
    boolean parsed = false;  //该方法是否解析完成

    public Method(MethodInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();

        CodeAttribute codeAttribute = info.getCodeAttribute();
        if (codeAttribute != null) {
            maxLocal = codeAttribute.getMaxLocal();
            maxStack = codeAttribute.getMaxStack();
            code = codeAttribute.getCode();
            if (code == null) {
                throw new NullPointerException(this.getName() + ": this method has no code");
            }
        }
        argc = calculateArgcFromDescriptor(descriptor);
    }

    /**
     * 计算该方法的参数列表的参数所占Slot数目
     * @param descriptor
     * @return
     */
    private int calculateArgcFromDescriptor(String descriptor) {
        char[] chars = descriptor.toCharArray();
        int maxIndex = descriptor.lastIndexOf(')');
        assert maxIndex != -1;
        int idx = descriptor.indexOf('(');
        assert idx != -1;
        //skip the index of '('
        idx++;
        int cnt = 0;
        while (idx + 1 <= maxIndex) {
            switch (chars[idx++]) {
                case 'J':
                case 'D':
                    cnt++;
                    //fall through
                case 'F':
                case 'I':
                case 'B':
                case 'C':
                case 'S':
                case 'Z':
                    cnt++;
                    break;
                case 'L':
                    cnt++;
                    while (idx < maxIndex && chars[idx++] != ';') ;
                    break;
                case '[':
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown descriptor!");
            }
        }
        return cnt;
    }

    /**
     * 解析该方法的指令序列
     * ( 虽然不知道将code[] parse成ArrayList(String) 有什么具体作用，暂且理解为javap时输出的指令表 )
     * ( formation: index opcode [operands …… ] )
     */
    public void parseCode() {
        ByteBuffer codeReader = ByteBuffer.wrap(this.code);
        int position = 0;
        codeReader.position(position);
        int size = this.code.length;

        for(this.instList = new ArrayList<>(); position <= size - 1; position = codeReader.position()) {
            int opcode = codeReader.get() & 255;
            Instruction instruction = Decoder.decode(opcode);
            instruction.fetchOperands(codeReader);
            this.instList.add(position + " " + instruction.toString());
        }

        this.parsed = true;
    }

    public boolean isParsed () {
        return this.parsed;
    }

    public byte[] getCode() {
        return this.code;
    }

    public boolean isAbstract () {
        return ((this.accessFlags & AccessFlags.ACC_ABSTRACT) != 0);
    }

}
