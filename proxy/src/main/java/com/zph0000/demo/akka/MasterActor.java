package com.zph0000.demo.akka;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

/**
 * Created by zph  Date：2017/6/12.
 */
public class MasterActor extends UntypedActor {

    public static Props props(){
        return Props.create(new Creator<Actor>() {
            public Actor create() throws Exception {
                return new MasterActor();
            }
        });
    }


    @Override
    public void onReceive(Object message) throws Throwable {
        ActorRef worker = context().actorOf(WorkerActor.props());
        worker.tell(message,self());
    }
}
