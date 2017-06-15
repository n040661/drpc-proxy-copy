package com.zph0000.demo.util;

import java.io.*;

/**
 * Created by zph  Date：2017/6/12.
 */
public class SerializableUtil {

    public static String ObjToStr(Object obj){
        String serStr = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(objectOutputStream!=null)
                try {objectOutputStream.close();} catch (IOException e1) {}
            if(byteArrayOutputStream!=null)
                try {byteArrayOutputStream.close();} catch (IOException e1) {}
        }
        return serStr;
    }

    public static <T> T StrToObj(String str,Class<T> clazz){
        T t = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            t= (T)objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(objectInputStream!=null)
                try {objectInputStream.close();} catch (IOException e1) {}
            if(byteArrayInputStream!=null)
                try {byteArrayInputStream.close();} catch (IOException e1) {}
        }
        return t;
    }
}
