package com.zph0000.demo.proxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by zph  Date：2017/6/12.
 */
public class ServiceMethod {

    Class<?> serviceInterface;

    private String clazz;

    private String methodName;

    private Class<?> returnType;

    Class<?>[] exceptionClassses;



    private final SortedMap<Integer, String> params;

    public ServiceMethod(Class<?> serviceInterface, Method method) {
        this.serviceInterface = serviceInterface;
        this.clazz = serviceInterface.getCanonicalName();
        this.methodName = method.getName();
        this.returnType = method.getReturnType();
        this.exceptionClassses = method.getExceptionTypes();
        this.params = Collections.unmodifiableSortedMap(this.getParams(method));
    }

    public SortedMap<Integer, String> getParams(Method method) {
        TreeMap params = new TreeMap();
        Class[] argTypes = method.getParameterTypes();
        for(int i = 0; i < argTypes.length; ++i) {
            String paramName = argTypes[i].getCanonicalName();
            params.put(Integer.valueOf(i), paramName);
        }
        return params;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Class<?>[] getExceptionClassses() {
        return exceptionClassses;
    }

    public void setExceptionClassses(Class<?>[] exceptionClassses) {
        this.exceptionClassses = exceptionClassses;
    }

    @Override
    public int hashCode() {
        return clazz.hashCode() + methodName.hashCode() + params.hashCode() + returnType.getName().hashCode();
    }
}
