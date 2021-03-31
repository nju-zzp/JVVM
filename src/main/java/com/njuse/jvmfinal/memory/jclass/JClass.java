package com.njuse.jvmfinal.memory.jclass;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.classloader.classfileparser.ClassFile;
import com.njuse.jvmfinal.classloader.classfileparser.FieldInfo;
import com.njuse.jvmfinal.classloader.classfileparser.MethodInfo;
import com.njuse.jvmfinal.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.jvmfinal.classloader.classfilereader.loadtype.Loader;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.NonArrayObject;
import com.njuse.jvmfinal.memory.struct.Vars;
import com.njuse.jvmfinal.memory.struct.array.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Getter
@Setter
public class JClass {
    private short accessFlags;  //创建时从classFile获取
    private String name;  //创建时从classFile获取
    private String superClassName;  //创建时从classFile获取
    private String[] interfaceNames;  //创建时从classFile获取
    private RuntimeConstantPool runtimeConstantPool;  //创建时从classFile解析获取
    private Field[] fields;  //创建时从classFile解析获取
    private Method[] methods;  //创建时从classFile解析获取
    private Loader defineLoader;  //加载该类的define loader，在classLoader中defineClass()赋值
    private JClass superClass;  //该类的父类JClass，在classLoader中defineClass()加载解析
    private JClass[] interfaces;  //该类的接口JClass数组，在classLoader中的defineClass()加载解析
    private int instanceSlotCount;  //实例成员变量槽数，在classLoader中的prepare阶段获取
    private int staticSlotCount;  //静态成员变量槽数，在classLoader中的prepare阶段获取
    private Vars staticVars; //静态成员变量表，在classLoader中的prepare阶段解析
    private InitState initState; //该类的初始化状态

    /**
     * 创建非数组类时的构造方法
     * @param classFile
     */
    public JClass(ClassFile classFile) {
        this.accessFlags = classFile.getAccessFlags();
        this.name = classFile.getClassName();
        if (!this.name.equals("java/lang/Object")) {
            // index of super class of java/lang/Object is 0
            this.superClassName = classFile.getSuperClassName();
        } else {
            this.superClassName = "";
        }
        this.interfaceNames = classFile.getInterfaceNames();
        this.fields = parseFields(classFile.getFields());
        this.methods = parseMethods(classFile.getMethods());
        this.runtimeConstantPool = parseRuntimeConstantPool(classFile.getConstantPool());
    }

    /**
     * 创建数组类时的构造方法
     */
    public JClass() {}

    /**
     * 创建JClass时对该类的filed的解析
     * @param info
     * @return Field[]
     */
    private Field[] parseFields(FieldInfo[] info) {
        int len = info.length;
        fields = new Field[len];
        for (int i = 0; i < len; i++) {
            fields[i] = new Field(info[i], this);
        }
        return fields;
    }

    /**
     * 创建JClass时对该类的method的解析
     * @param info
     * @return Method[]
     */
    private Method[] parseMethods(MethodInfo[] info) {
        int len = info.length;
        methods = new Method[len];
        for (int i = 0; i < len; i++) {
            methods[i] = new Method(info[i], this);
        }
        return methods;
    }

    /**
     * 创建JClass时对该类的运行时常量池RuntimeConstantPool的解析
     * @param cp
     * @return RuntimeConstantPool
     */
    private RuntimeConstantPool parseRuntimeConstantPool(ConstantPool cp) {
        return new RuntimeConstantPool(cp, this);
    }


    public String getPackageName() {
        int index = name.lastIndexOf('/');
        if (index >= 0) return name.substring(0, index);
        else return "";
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & AccessFlags.ACC_PUBLIC);
    }

    /**
     *此类作为被调用类，对于调用者是否是可访问的的
     * @param caller 调用者
     */
    public boolean isAccessibleTo (JClass caller) {
        if (this.isPublic()) {
            return true;
        } else {
            if (this.getPackageName().equals(caller.getPackageName())) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 类的初始化（初始化静态部分，执行clinit方法）
     * @param thread
     */
    public void initialClass (JThread thread) {
        JClass clazz = this;
        if (clazz.initState == InitState.SUCCESS) {
            return;
        }

        //开始初始化
        clazz.setInitState(InitState.BUSY);

        //查找并push<clinit>方法的栈帧
        Method clinitMethod = getMethodInClass("<clinit>", "()V", true);
        if (clinitMethod != null) {
            StackFrame stackFrame = new StackFrame(thread, clinitMethod, thread.getPC(),
                    clinitMethod.getMaxStack(),
                    clinitMethod.getMaxLocal());
            thread.pushFrame(stackFrame);
            thread.setPC(0);
        }

        //检查父类是否初始化
        JClass superClazz = this.superClass;
        if (superClazz != null && superClazz.initState == InitState.PREPARED) {
            superClazz.initialClass(thread);
        }

        //完成类的初始化
        clazz.setInitState(InitState.SUCCESS);
    }


    /**
     * 在该类的方法表中找到对应name和descriptor的method
     */
    private Method getMethodInClass(String name, String descriptor, boolean isStatic) {
        JClass clazz = this;
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getDescriptor().equals(descriptor)
                    && m.getName().equals(name)
                    && m.isStatic() == isStatic) {
                return m;
            }
        }
        return null;
    }

    /**
     * 创建该非数组类的一个对象
     */
    public NonArrayObject newObject () {
        return new NonArrayObject(this);
    }

    /**
     * 创建该数组类的一个对象
     */
    public ArrayObject newArrayObject (int length) {
        switch (this.getName()) {
            case "[Z" : return new BooleanArrayObject(length);
            case "[C" : return new CharArrayObject(length);
            case "[F" : return new FloatArrayObject(length);
            case "[D" : return new DoubleArrayObject(length);
            case "[B" : return new ByteArrayObject(length);
            case "[S" : return new ShortArrayObject(length);
            case "[I" : return new IntArrayObject(length);
            case "[J" : return new LongArrayObject(length);
            default : return new RefArrayObject(length, this);
        }
    }

    /**
     * 获取该类作为组件的数组类
     */
    public JClass getArrayClass () {
        String arrayClassName;

        if (this.name.charAt(0) == '[') {
            arrayClassName = this.name;
        } else {
            arrayClassName = "L" + this.name + ";";
        }

        arrayClassName = "[" + arrayClassName;

        try {
            return ClassLoader.getInstance().loadClass(arrayClassName, this.defineLoader);
        } catch (IOException var3) {
            var3.printStackTrace();
            throw new RuntimeException("Cannot load arrayClass:" + arrayClassName);
        }
    }

    public JClass getComponentClass() {
        if (this.name.charAt(0) != '[') {
            throw new RuntimeException("Invalid Array:" + this.name);
        } else {
            ClassLoader classLoader = ClassLoader.getInstance();
            String componentTypeDescriptor = this.name.substring(1);
            String classToLoad = null;
            if (componentTypeDescriptor.charAt(0) == '[') {
                classToLoad = componentTypeDescriptor;
            } else if (componentTypeDescriptor.charAt(0) == 'L') {
                classToLoad = componentTypeDescriptor.substring(1, componentTypeDescriptor.length() - 1);
            }
//            else if (this.getPrimitiveType() != null) {
//                classToLoad = this.getPrimitiveType();
//            }

            try {
                return classLoader.loadClass(classToLoad, this.defineLoader);
            } catch (IOException var5) {
                var5.printStackTrace();
                throw new RuntimeException("Cannot load arrayClass:" + classToLoad);
            }
        }
    }

//    private String getPrimitiveType() {
//        HashMap<String, String> primitiveType = new HashMap();
//        primitiveType.put("void", "V");
//        primitiveType.put("boolean", "Z");
//        primitiveType.put("byte", "B");
//        primitiveType.put("short", "S");
//        primitiveType.put("char", "C");
//        primitiveType.put("int", "I");
//        primitiveType.put("long", "J");
//        primitiveType.put("float", "F");
//        primitiveType.put("double", "D");
//        return (String)primitiveType.get(this.name);
//    }

    /**
     * 获取该类的main方法
     */
    public Method getMainMethod() {
        return getMethodInClass("main", "([Ljava/lang/String;)V", true);
    }

    /**
     * 判断此类是否ACC_SUPER（可访问父类）
     * 用于invokeSpecial指令
     */
    public boolean isAccSuper() {
        return 0 != (this.accessFlags & AccessFlags.ACC_SUPER);
    }


    /**
     * 判断该类的对象是否可以转换为指定的已解析的类型
     * 根据下列规则来判断转换是否成立
     * refer to jvm8 6.5 instanceof inst
     * @param other
     * @return
     */
    public boolean isAssignableFrom(JClass other) {
        JClass t = other;
        JClass s = this;
        if (s == t) return true;
        if (!s.isArray()) {
            if (!s.isInterface()) {
                if (!t.isInterface()) {
                    return s.isSubClassOf(t);
                } else {
                    return s.isImplementOf(t);
                }
            } else {
                if (!t.isInterface()) {
                    return t.isJObjectClass();
                } else {
                    return t.isSuperInterfaceOf(s);
                }
            }
        } else {
            if (!t.isArray()) {
                if (!t.isInterface()) {
                    return t.isJObjectClass();
                } else {
                    return t.isJIOSerializable() || t.isJlCloneable();
                }
            } else {
                JClass sc = s.getComponentClass();
                JClass tc = t.getComponentClass();
                return sc == tc || t.isJIOSerializable();
            }
        }
    }

    public boolean isArray() {
        return this.name.charAt(0) == '[';
    }

    public boolean isInterface() {
        return 0 != (this.accessFlags & AccessFlags.ACC_INTERFACE);
    }

    public boolean isJObjectClass() {
        return this.name.equals("java/lang/Object");
    }

    public boolean isJIOSerializable() {
        return this.name.equals("java/io/Serializable");
    }

    public boolean isJlCloneable() {
        return this.name.equals("java/lang/Cloneable");
    }

    private boolean isSubClassOf(JClass otherClass) {
        JClass superClass = this.getSuperClass();
        while (superClass != null) {
            if (superClass == otherClass) return true;
            superClass = superClass.getSuperClass();
        }
        return false;
    }

    private boolean isImplementOf(JClass otherInterface) {
        JClass superClass = this;
        while (superClass != null) {
            for (JClass i : this.getInterfaces()) {
                if (i == otherInterface || i.isSubInterfaceOf(otherInterface)) return true;
            }
            superClass = this.getSuperClass();
        }
        return false;
    }

    private boolean isSubInterfaceOf(JClass otherInterface) {
        JClass[] superInterfaces = this.getInterfaces();
        for (JClass i : superInterfaces) {
            if (i == otherInterface || i.isSubInterfaceOf(otherInterface)) return true;
        }
        return false;
    }

    private boolean isSuperInterfaceOf(JClass otherInterface) {
        return otherInterface.isSubInterfaceOf(this);
    }

}
