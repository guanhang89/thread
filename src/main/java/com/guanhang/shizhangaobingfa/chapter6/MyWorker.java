package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyWorker extends UntypedAbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static enum Msg{
        WORKING,DONE,CLOSE
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.WORKING) {
            System.out.println("i am working");
        }
        if (message == Msg.DONE) {
            System.out.println("Stop working");
        }
        if (message == Msg.CLOSE) {
            System.out.println("i will shutdown");
            getSender().tell(Msg.CLOSE, getSelf());
            getContext().stop(getSelf());
        }else {
            unhandled(message);
        }
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("MyWorker is starting");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("MyWorker is stopping");
    }
}
