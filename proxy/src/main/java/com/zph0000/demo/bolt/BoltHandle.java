package com.zph0000.demo.bolt;


import com.zph0000.demo.rpc.drpc.DrpcRequest;
import com.zph0000.demo.rpc.drpc.DrpcResponse;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zrk-PC
 * Date: 2017/4/28
 * Time: 11:07
 */
public interface BoltHandle extends Serializable{

    void prepare();

    DrpcResponse execute(DrpcRequest drpcRequest);

}
