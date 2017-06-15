/**
 *    Copyright 2010-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.zph0000.demo.spring;

import com.zph0000.demo.proxy.ServiceProxyFactory;
import com.zph0000.demo.rpc.RpcHandle;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by rongkang on 2017/3/13.
 * 扫描Service接口
 *
 */
public class ServiceFactoryBean<T>  implements FactoryBean<T> {

  private Class<T> serviceInterface;

  private RpcHandle rpcHandle;

  public ServiceFactoryBean() {
  }

  /*
  * FactoryBean<T>
    T getObject():返回由FactoryBean创建的bean实例，如果isSingleton()返回true,则该实例会放到Spring容器中单例缓存池中。
    boolean isSingleton();返回由FactoryBean创建的bean实例的作用域是singleton还是prototype.
    Class<T> getObjectType():返回FactoryBean创建的bean类型
  * */
  
  public ServiceFactoryBean(Class<T> mapperInterface) {
    this.serviceInterface = mapperInterface;
  }

  public T getObject() throws Exception {
    return ServiceProxyFactory.newInstance(this.serviceInterface,this.rpcHandle);

  }

  public Class<T> getObjectType() {
    return this.serviceInterface;
  }

  public boolean isSingleton() {
    return true;
  }


  public void setServiceInterface(Class<T> serviceInterface) {
    this.serviceInterface = serviceInterface;
  }

  public Class<T> getServiceInterface() {
    return serviceInterface;
  }

  public RpcHandle getRpcHandle() {
    return rpcHandle;
  }

  public void setRpcHandle(RpcHandle rpcHandle) {
    this.rpcHandle = rpcHandle;
  }
}
