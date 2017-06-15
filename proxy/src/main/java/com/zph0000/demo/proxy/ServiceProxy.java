package com.zph0000.demo.proxy;

import com.zph0000.demo.rpc.RpcHandle;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zph  Date：2017/6/12.
 */
public class ServiceProxy<T> implements InvocationHandler,Serializable {

    /*每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到了一个handler，
    当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的 invoke
    方法来进行调用。我们来看看InvocationHandler这个接口的唯一一个方法 invoke方法*/
    private final RpcHandle rpcHandle;

    private final Class<T> serviceInterface;

    private final Map<Method, ServiceMethod> methodCache = new ConcurrentHashMap<Method, ServiceMethod>();

    public ServiceProxy(RpcHandle rpcHandle, Class<T> serviceInterface) {
        this.rpcHandle = rpcHandle;
        this.serviceInterface = serviceInterface;
    }


    /**
     *proxy:　　指代我们所代理的那个真实对象
     method:　　指代的是我们所要调用真实对象的某个方法的Method对象
     args:　　指代的是调用真实对象某个方法时接受的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //Object 中的方法
        if(Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw new Exception("Object method invoke exception!");
            }
            //业务方法
        } else {
            ServiceMethod serviceMethod = cachedServiceMethod(method);
            return rpcHandle.exec(serviceMethod,args);
        }
    }

    private ServiceMethod cachedServiceMethod(Method method) {
        ServiceMethod serviceMethod = this.methodCache.get(method);
        if(serviceMethod == null) {
            serviceMethod = new ServiceMethod(this.serviceInterface, method);
            this.methodCache.put(method, serviceMethod);
        }
        return serviceMethod;
    }



}
