package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class BabyActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static enum Msg{
        SLEEP,PLAY,CLOSE
    }

    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            System.out.println("angryApply:" + param);
            if (param == Msg.SLEEP) {
                getSender().tell("i am already angry", getSelf());
                System.out.println("I am already angry");
            } else if (param == Msg.PLAY) {
                System.out.println("i like playing");
                getContext().become(happy);
            }
        }
    };

    Procedure<Object> happy = (Procedure<Object>) param -> {
        System.out.println("happy apply");
        if (param == Msg.PLAY) {
            getSender().tell("I am already happy", getSelf());
            System.out.println("i am already happy");
        } else if (param == Msg.SLEEP) {
            System.out.println("I dont want to sleep");
            getContext().become(angry);
        }
    };
    @Override
    public void onReceive(Object message) throws Throwable {
        //根据消息转换状态
        if (message == Msg.SLEEP) {
            getContext().become(angry);
        } else if (message == Msg.PLAY) {
            getContext().become(happy);
        } else {
            unhandled(message);
        }

    }
}
