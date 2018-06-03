package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

public class HelloWorld extends UntypedActor {
    ActorRef greeter;
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Gretter.Msg.DONE) {
            greeter.tell(Gretter.Msg.GREET, getSelf());
            getContext().stop(getSelf());
        }else {
            unhandled(message);
        }
    }

    @Override
    public void preStart() throws Exception {
        greeter = getContext().actorOf(Props.create(Gretter.class), "greeter");
        System.out.println("Greeter Actor Path:" + greeter.path());
        greeter.tell(Gretter.Msg.GREET, getSelf());
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("samplehello.conf"));
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println("HelloWorld Actor Path:" + a.path());
    }
}
