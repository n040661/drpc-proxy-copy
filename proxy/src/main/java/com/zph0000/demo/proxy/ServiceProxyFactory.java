package com.zph0000.demo.proxy;

import com.zph0000.demo.rpc.RpcHandle;

import java.lang.reflect.Proxy;

/**
 * Created by zph  Date：2017/6/12.
 */
public class ServiceProxyFactory {

    public static <T> T newInstance(Class<T> clazz, RpcHandle rpcHandle) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new ServiceProxy(rpcHandle,clazz));
    }


}
