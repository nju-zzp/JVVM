package com.njuse.jvmfinal.memory;

import lombok.Getter;
import lombok.Setter;

import java.util.EmptyStackException;
import java.util.Stack;

@Setter
@Getter
public class VMStack {
    private static int maxSize;  //虚拟机栈的最大深度
    private int currentSize;  //虚拟机栈此刻的深度
    private Stack<StackFrame> stack;  //栈帧

    static {
        maxSize = 64 * 1024;  //linux x86_64 default value 256KB
    }

    public VMStack() {
        stack = new Stack<>();
    }

    public void pushFrame (StackFrame frame) {
        if (currentSize >= maxSize) {
            throw new StackOverflowError();
        }
        stack.push(frame);
        currentSize ++;
    }

    public StackFrame popFrame () {
        if (currentSize == 0) {
            throw new EmptyStackException();
        }
        currentSize --;
        return stack.pop();
    }

    public StackFrame getTopFrame () {
        if (currentSize == 0) {
            return null;
        } else {
            return stack.lastElement();
        }
    }

}
