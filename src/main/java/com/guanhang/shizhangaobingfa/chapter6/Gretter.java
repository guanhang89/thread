package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.UntypedAbstractActor;

public class Gretter extends UntypedAbstractActor {
    public static enum Msg{
        GREET,DONE
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.GREET) {
            System.out.println("Hello world");
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(message);
        }
    }
}
