package com.zph0000.demo.akka;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import com.alibaba.fastjson.JSON;
import com.zph0000.demo.bolt.WrapTuple;
import com.zph0000.demo.rpc.drpc.DrpcRequest;
import com.zph0000.demo.rpc.drpc.DrpcResponse;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zph  Date：2017/6/12.
 */
public class WorkerActor extends UntypedActor {

    private static Logger logger = LoggerFactory.getLogger(WorkerActor.class);

    public static Props props(){
        return Props.create(new Creator<Actor>() {
            public Actor create() throws Exception {
                return new WorkerActor();
            }
        });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(logger.isDebugEnabled())
            logger.debug("do AkkaActor onReceive,message:{}",message);
        WrapTuple wrapTuple = (WrapTuple) message;
        Tuple tuple = wrapTuple.getTuple();
        String param = tuple.getString(0);
        DrpcRequest request = JSON.parseObject(param,DrpcRequest.class);
        DrpcResponse response = wrapTuple.getBoltHandle().execute(request);
        wrapTuple.getCollector().emit(new Values(JSON.toJSONString(response), tuple.getValue(1)));
        context().stop(self());
    }
}
