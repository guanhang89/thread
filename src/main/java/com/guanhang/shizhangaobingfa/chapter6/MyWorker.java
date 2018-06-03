package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("inboxdemo", ConfigFactory.load("samplehello.conof"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class), "worker");
        final Inbox inbox = Inbox.create(system);
        inbox.watch(worker);
        inbox.send(worker, Msg.WORKING);
        inbox.send(worker, Msg.DONE);
        inbox.send(worker, Msg.CLOSE);

        while (true) {
            Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if (msg == Msg.CLOSE) {
                System.out.println("myworker is closing");
            } else if (msg instanceof Terminated) {
                System.out.println("myworkder is dead");
                system.terminate();
                break;
            } else {
                System.out.println(msg);
            }
        }
    }
}
