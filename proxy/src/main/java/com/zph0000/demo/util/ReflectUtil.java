package com.zph0000.demo.util;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * Created by zph  Date：2017/6/12.
 */
public class ReflectUtil {

    public static String getCodeBase(Class<?> cls) {
        if (cls == null)
            return null;
        ProtectionDomain domain = cls.getProtectionDomain();
        if (domain == null)
            return null;
        CodeSource source = domain.getCodeSource();
        if (source == null)
            return null;
        URL location = source.getLocation();
        if (location == null)
            return null;
        return location.getFile();
    }
}
