package com.njuse.jvmfinal.classloader.classfileparser;

import com.njuse.jvmfinal.classloader.classfileparser.attribute.AttributeBuilder;
import com.njuse.jvmfinal.classloader.classfileparser.attribute.AttributeInfo;
import com.njuse.jvmfinal.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.ClassInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

@Getter
@Setter
public class ClassFile {
    private int magic; //u4
    private short minorVersion; //u2
    private short majorVersion; //u2
    private short constantPoolCount; //u2
    private ConstantPool constantPool; //cp_info
    private short accessFlags; //u2
    private short thisClass; //u2
    private short superClass; //u2
    private short interfacesCount; //u2
    private short[] interfaces; //u2 (a valid index into the constant_pool table which is CONSTANT_Class_info)
    private short fieldsCount; //u2
    private FieldInfo[] fields; //field_info
    private short methodsCount; //u2
    private MethodInfo[] methods; //method_info
    private short attributeCount; //u2
    private AttributeInfo[] attributes; //attribute_info

    private ByteBuffer in;
    Supplier<AttributeInfo> attrBuilder = this::getAttribute;  //用于调用getAttribute方法,返回AttributeInfo

    public ClassFile(byte[] classfile) {
        in = ByteBuffer.wrap(classfile);
        this.magic = in.getInt();
        if (this.magic != 0xCAFEBABE) {
            throw new UnsupportedOperationException(
                    "Wrong magic number! Expect 0xCAFEBABE but actual is " + Integer.toHexString(this.magic));
        }
        this.minorVersion = in.getShort();
        this.majorVersion = in.getShort();
        parseConstantPool(classfile);  //将byte数组中的constantPool转换进classFile
        this.accessFlags = in.getShort();
        this.thisClass = in.getShort();
        this.superClass = in.getShort();
        parseInterfaces();  //转换接口信息进classFile
        parseFields();  //转换成员变量进classFile
        parseMethods();  //转换成员方法进classFile
        parseAttributes();  //转换类的属性信息进classFile
    }

    private void parseAttributes() {
        this.attributeCount = in.getShort();
        this.attributes = new AttributeInfo[0xFFFF & this.attributeCount];
        for (int i = 0; i < attributes.length; i++) {
            this.attributes[i] = attrBuilder.get();
        }
    }


    private void parseMethods() {
        this.methodsCount = in.getShort();
        this.methods = new MethodInfo[0xFFFF & this.methodsCount];
        for (int i = 0; i < this.methods.length; i++) {
            this.methods[i] = new MethodInfo(this.constantPool, this.attrBuilder, in);
        }
    }

    private void parseFields() {
        this.fieldsCount = in.getShort();
        this.fields = new FieldInfo[0xFFFF & this.fieldsCount];
        for (int i = 0; i < this.fields.length; i++) {
            this.fields[i] = new FieldInfo(this.constantPool, this.attrBuilder, in);
        }
    }

    private void parseInterfaces() {
        this.interfacesCount = in.getShort();
        interfaces = new short[0xFFFF & this.interfacesCount];
        for (int i = 0; i < this.interfaces.length; i++) {
            this.interfaces[i] = in.getShort();
        }
    }

    private void parseConstantPool(byte[] classfile) {
        this.constantPoolCount = in.getShort();
        //记录转换constantPool信息前buffer的position，同byte[]传给ConstantPool构造器
        int currentPos = in.position();
        //返回constantPool实例，以及constantPool在二进制文件中占据的字节数
        Pair<ConstantPool, Integer> cpInt = ConstantPool.getInstance(constantPoolCount, classfile, currentPos);
        constantPool = cpInt.getKey();
        currentPos += cpInt.getValue();
        in.position(currentPos);
    }

    public AttributeInfo getAttribute() {
        return AttributeBuilder.createAttribute(new BuildUtil(this.constantPool, in));
    }

    public String getClassName() {
        return ((ClassInfo) constantPool.get(thisClass)).getClassName();
    }

    public String getSuperClassName() {
        return ((ClassInfo) constantPool.get(superClass)).getClassName();
    }

    public String[] getInterfaceNames() {
        String[] ret = new String[interfacesCount];
        for (int i = 0; i < interfacesCount; i++) {
            ret[i] = ((ClassInfo) constantPool.get(interfaces[i])).getClassName();
        }
        return ret;
    }

}
