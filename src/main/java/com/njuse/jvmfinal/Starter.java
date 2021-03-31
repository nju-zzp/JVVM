package com.njuse.jvmfinal;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.classloader.classfilereader.ClassFileReader;
import com.njuse.jvmfinal.classloader.classfilereader.loadtype.*;
import com.njuse.jvmfinal.classloader.classfilereader.pathType.PathEntry;
import com.njuse.jvmfinal.execution.Interpreter;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.MethodArea;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.util.IOUtil;

import java.io.File;
import java.io.IOException;


public class Starter {
    private static ClassLoader classLoader = ClassLoader.getInstance();
    private static final String FILE_SEPARATOR = File.separator; //文件层次分隔符‘\\’
    private static final String PATH_SEPARATOR = File.pathSeparator; //路径分隔符';'
    private static final String javaHome = System.getenv("JAVA_HOME"); //JAVA_HOME环境变量对应的路径

    public static void main(String[] args) throws IOException {
        String cp = String.join(File.separator, "src", "test", "java");
        runTest("cases.mytest.invokeSpecialTest", cp);
    }

    /**
     * ⚠️警告：不要改动这个方法签名，这是和测试用例的唯一接口
     */
    public static void runTest(String mainClassName, String cp) {
        setEnv(cp);
        try {
            JClass clazz = classLoader.loadClass(mainClassName, null);
            JThread thread = new JThread();
            Method mainMethod = clazz.getMainMethod();
            StackFrame stackFrame = new StackFrame(thread, mainMethod, thread.getPC(), mainMethod.getMaxStack(), mainMethod.getMaxLocal());
            thread.pushFrame(stackFrame);
            thread.setPC(0);
            clazz.initialClass(thread);
            Interpreter.interpret(thread);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 配置环境变量，确定不同的加载器读取文件时对应的path搜索范围
     * @param classpath
     */
    private static void setEnv (String classpath) {
        // 确定路径
        String bootstrapClassPath = String.join(FILE_SEPARATOR, javaHome, "jre", "lib","*");
        String extensionClassPath = String.join(FILE_SEPARATOR, javaHome, "jre", "lib", "ext","*");
        String userClassPath = IOUtil.pathTransform(classpath);
        PathEntry bootstrapPath = ClassFileReader.choosePathEntry(bootstrapClassPath);
        PathEntry extensionPath = ClassFileReader.choosePathEntry(extensionClassPath);
        PathEntry applicationPath = ClassFileReader.choosePathEntry(userClassPath);

        //配置环境变量
        BootstrapLoader.getInstance().setPathEntry(bootstrapPath);
        ExtensionLoader.getInstance().setPathEntry(extensionPath);
        ApplicationLoader.getInstance().setPathEntry(applicationPath);
    }

    private static void printMethodArea () {
        for (String clazzName : MethodArea.getInstance().getClassMap().keySet()) {
            System.out.println(clazzName);
        }
    }

}
