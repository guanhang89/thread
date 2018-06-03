package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.UntypedActor;

public class RestartActor extends UntypedActor {
    public enum Msg{
        DONE,RESTART
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.DONE) {
            getContext().stop(getSelf());
        } else if (message == Msg.RESTART) {
            System.out.println(((Object) null).toString());
            double a = 0 / 0;
        }
        unhandled(message);
    }
}
