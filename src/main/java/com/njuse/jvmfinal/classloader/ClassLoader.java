package com.njuse.jvmfinal.classloader;

import com.njuse.jvmfinal.classloader.classfileparser.ClassFile;
import com.njuse.jvmfinal.classloader.classfilereader.ClassFileReader;
import com.njuse.jvmfinal.classloader.classfilereader.loadtype.Loader;
import com.njuse.jvmfinal.memory.MethodArea;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.*;
import com.njuse.jvmfinal.memory.struct.NullObject;
import com.njuse.jvmfinal.memory.struct.Vars;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class ClassLoader {
    private static ClassLoader classLoader = new ClassLoader();  //单例设计模式，仅存在一个classLoader实例
    private ClassFileReader classFileReader;
    private MethodArea methodArea;

    private ClassLoader () {
        classFileReader = ClassFileReader.getInstance();
        methodArea = MethodArea.getInstance();
    }

    public static ClassLoader getInstance() {
        return classLoader;
    }

    /**
     * 类加载的主方法
     * @param className
     * @param initialLoader
     */
    public JClass loadClass (String className, Loader initialLoader) throws IOException {
        //先判断该类是否已经在方法区中（已经被加载）
        JClass result = methodArea.findClass(className);

        //若未加载则进行加载
        if (result == null) {
            if (!className.startsWith("[")) {
                result = loadNonArrayClass(className, initialLoader);
            } else {
                result = loadArrayClass(className, initialLoader);
            }
        }

        return result;
    }

    /**
     * 创建加载数组类
     * 过程：
     *      1.检验方法区中是否已经加载过该数组类（此步骤已经在类加载主方法loadClass()中完成）
     *      2.递归加载创建组件类型，如果为引用类型则加载引用类型，否则停止
     *      3.如果组件类型为引用类型，可见性由组件类型可见性决定；否则设为public (此处简化为默认public)
     */
    private JClass loadArrayClass (String className, Loader initialLoader) throws IOException {
        // 递归加载创建组件类型
        String componentName = className.substring(1);
        if (componentName.startsWith("L")) { //组件为非数组引用类型 format: Ljava/lang/String; 故类名需要去除首位
            String referenceName = componentName.substring(1, componentName.length() - 1);
            this.loadClass(referenceName, initialLoader);
        } else if (componentName.startsWith("[")) { //组件为数组类型，递归加载创建
            this.loadClass(componentName, initialLoader);
        } //若为基础类型则停止 创建该数组类即可

        //设置数组类的类名，访问标志public，设置父类和接口，最后完成数组类的初始化，添加到方法区

        JClass arrayClazz = new JClass();
        arrayClazz.setName(className);
        arrayClazz.setAccessFlags((short)1);

        //父类：java/lang/Object
        arrayClazz.setSuperClassName("java/lang/Object");
        arrayClazz.setSuperClass(this.loadClass("java/lang/Object", null));

        //接口：java/lang/Cloneable 和 java/io/Serializable
        String[] interfaceNames = new String[] {"java/lang/Cloneable", "java/io/Serializable"};
        arrayClazz.setInterfaceNames(interfaceNames);
        JClass[] interfaces = new JClass[] {
                this.loadClass("java/lang/Cloneable", null),
                this.loadClass("java/io/Serializable", null)};
        arrayClazz.setInterfaces(interfaces);

        arrayClazz.setInitState(InitState.SUCCESS);
        MethodArea.getInstance().addClass(className, arrayClazz);

        return arrayClazz;
    }

    /**
     * 加载非数组类
     */
    private JClass loadNonArrayClass (String className, Loader initialLoader) throws IOException {
        //获取该类的二进制数据 和 defineLoader
        Pair<byte[], Loader> temp = classFileReader.readClassFile(className, initialLoader);

        //创建JClass对象，同时加载其父类和父接口，加入到方法区
        JClass result = defineClass(temp.getKey(), temp.getValue());

        //对该类链接 (验证 -> 准备 -> 后续解析)
        linkClass(result);

        return result;
    }

    /**
     * 创建该类的JClass，并赋值defineLoader，同时在该类完成加载前，加载该类的父类和接口类，并加入到方法区
     * @param data 该类的二进制数据
     * @param defineLoader 该类的最终加载器defineLoader
     * @return JClass 该类的JClass
     */
    private JClass defineClass (byte[] data, Loader defineLoader) throws IOException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        clazz.setDefineLoader(defineLoader);  //设置该类的defineLoader

        //递归加载父类
        if (!clazz.getSuperClassName().equals("")) {
            String superClassName = clazz.getSuperClassName();
            JClass superClazz = loadClass(superClassName, defineLoader);
            clazz.setSuperClass(superClazz);
        }

        //递归加载父接口
        String[] interfacesNames = clazz.getInterfaceNames();
        int length = interfacesNames.length;
        JClass[] interfaces = new JClass[length];
        for (int i = 0; i < length; i++) {
            interfaces[i] = loadClass(interfacesNames[i], defineLoader);
        }
        clazz.setInterfaces(interfaces);

        //将该加载完成的类添加到方法区
        methodArea.addClass(clazz.getName(), clazz);

        return clazz;
    }

    /**
     * 链接阶段：此处包括验证和准备过程
     * @param clazz
     */
    private void linkClass (JClass clazz) {
        verify(clazz);
        prepare(clazz);
    }

    /**
     * 验证阶段 但是过于复杂 暂不实现
     * @param clazz
     */
    private void verify (JClass clazz) {
        // too complicated to finish
    }

    /**
     * 准备阶段：
     * 正式为类中定义的静态变量(static field)，分配内存并设置类变量初始值的阶段
     * @param clazz
     */
    private void prepare (JClass clazz) {
        //计算出该JClass的静态成员变量的Slot数目，同时设定static field的slotID
        calculateStaticSlots(clazz);

        //计算出该JClass的实例成员变量的Slot的数目，同时设定instance field的slotID
        calculateInstanceSlots(clazz);

        //分配内存并设置类变量初始值的阶段
        allocateStaticVars(clazz);

        //设置类的初始化状态为prepared
        clazz.setInitState(InitState.PREPARED);
    }

    /**
     * 计算出该JClass的静态成员变量的Slot数目，同时设定static field的slotID
     * Long 和 Double 占据2个Slot
     */
    private void calculateStaticSlots (JClass clazz) {
        int staticSlotID = 0;
        for (Field field : clazz.getFields()) {
            if (field.isStatic()) {
                field.setSlotID(staticSlotID);
                staticSlotID += (field.isLongOrDouble() ? 2 : 1);
            }
        }
        clazz.setStaticSlotCount(staticSlotID);
    }

    /**
     * 计算出该JClass的实例成员变量的Slot的数目，同时设定instance field的slotID
     * Long 和 Double 占据2个Slot
     * 注意instance与static的不同：
     *      子类完全单独持有父类static变量（否则子类改变父类static变量值但父类本身static不改变）
     *      但instance变量需要完全持有父类的instance变量，便于创建子类对象时持有这些instance属性字段
     */
    private void calculateInstanceSlots (JClass clazz) {
        int instanceSlotID = 0;

        if (clazz.getSuperClass() != null) {
            instanceSlotID = clazz.getSuperClass().getInstanceSlotCount();
        }

        for (Field field : clazz.getFields()) {
            if (! field.isStatic()) {
                field.setSlotID(instanceSlotID);
                instanceSlotID += field.isLongOrDouble() ? 2 : 1;
            }
        }

        clazz.setInstanceSlotCount(instanceSlotID);
    }

    /**
     * 为该JClass的static field正式分配内存，并设置类变量初始值的阶段
     * 注意：
     *      在实际的程序中，只有同时被final和static修饰的字段才有ConstantValue属性
     *      且限于基本类型和String
     * @param clazz
     */
    private void allocateStaticVars (JClass clazz) {
        // 首先创建并设置Vars数据结构
        Vars staticFieldVars = new Vars(clazz.getStaticSlotCount());
        clazz.setStaticVars(staticFieldVars);

        //根据是否有final static修饰符即是否有constantValue属性来设置初始值，不含有该属性则为零值
        //且限于基本类型和String
        for (Field field : clazz.getFields()) {
            if (field.isStatic() && field.isFinal()) {
                if (!field.getDescriptor().startsWith("L") || field.getDescriptor().equals("Ljava/lang/String;")) {
                    allocateConstantValue(clazz, field);
                } else {
                    allocateZero(clazz, field);
                }
            } else if (field.isStatic()){
                allocateZero(clazz, field);
            }
        }
    }

    /**
     * 无constantValue属性，设为零值
     * @param clazz
     * @param field
     */
    private void allocateZero (JClass clazz, Field field) {
        Vars staticVars = clazz.getStaticVars();
        String descriptor = field.getDescriptor();
        int slotID = field.getSlotID();
        switch (descriptor.charAt(0)) {
            case 'B':
            case 'C':
            case 'S':
            case 'Z':
            case 'I':
                staticVars.setInt(slotID, 0);
                break;
            case 'J':
                staticVars.setLong(slotID, 0L);
                break;
            case 'D':
                staticVars.setDouble(slotID, 0.0);
                break;
            case 'F':
                staticVars.setFloat(slotID, 0.0F);
                break;
            default:
                staticVars.setObjectRef(slotID, new NullObject());
        }
    }

    /**
     * static final field存在constantValue属性，设置初始值
     * constantValue通过field的constantIndex从RuntimeConstantPool获取
     * @param clazz
     * @param field
     */
    private void allocateConstantValue (JClass clazz, Field field) {
        Vars staticVars = clazz.getStaticVars();
        String descriptor = field.getDescriptor();
        int slotID = field.getSlotID();

        RuntimeConstantPool runtimeConstantPool = clazz.getRuntimeConstantPool();
        int constantValueIndex = field.getConstValueIndex();

        switch (descriptor.charAt(0)) {
            case 'B':
            case 'C':
            case 'S':
            case 'Z':
            case 'I':
                int intValue = ((IntWrapper)runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setInt(slotID, intValue);
                break;
            case 'J':
                long longValue = ((LongWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setLong(slotID, longValue);
                break;
            case 'D':
                double doubleValue = ((DoubleWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setDouble(slotID, doubleValue);
                break;
            case 'F':
                float floatValue = ((FloatWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setFloat(slotID, floatValue);
                break;
            case 'L':
                if (descriptor.equals("Ljava/lang/String;")) {
                    String string = ((StringWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                    staticVars.setStringObject(slotID, string);
                }
                break;
        }
    }


}
