package com.zph0000.demo.rpc;

import com.zph0000.demo.proxy.ServiceMethod;

/**
 * Created by zph  Date：2017/6/12.
 */
public interface RpcHandle {

    Object exec(ServiceMethod serviceMethod, Object[] args) throws Throwable;

}
