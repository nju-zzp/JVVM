package com.njuse.jvmfinal.util;

import java.io.*;

public class IOUtil {
    public static byte[] readFileByBytes(InputStream is) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedInputStream in;
            in = new BufferedInputStream(is);
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String pathTransform (String classPath) {
        String result = classPath;
        if (classPath.contains("/")) {
            result = classPath.replace("/", File.separator);
        }
        return result;
    }

    public static String classNameTransform (String className) {
        String result = className;

        if (!className.endsWith(".class")) {
            result = className.replace('.', '/') + ".class";
        }

        return result;
    }
}
