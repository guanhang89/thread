package com.guanhang.shizhangaobingfa.chapter6;

import akka.actor.*;
import akka.japi.Function;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Supervisor extends UntypedActor {
    private static SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES), new Function<Throwable, SupervisorStrategy.Directive>() {
        @Override
        public SupervisorStrategy.Directive apply(Throwable t) throws Exception {
            if(t instanceof ArithmeticException){
                System.out.println("meet ArithmeticException, just resume");
                return SupervisorStrategy.resume();
            } else if (t instanceof NullPointerException) {
                System.out.println("meet NullPointException,restart");
                return SupervisorStrategy.restart();
            } else if (t instanceof IllegalArgumentException) {
                return SupervisorStrategy.stop();
            } else {
                return SupervisorStrategy.escalate();
            }

        }
    });
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Props) {
            //用来新建一个名为restartActor的子Actor，这个字Actor就由当前的Supervisor进行监督
            getContext().actorOf((Props) message, "restartActor");
        } else {
            unhandled(message);
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("lifecycle", ConfigFactory.load("lifecycle.conf"));
        customStrategy(system);
    }

    private static void customStrategy(ActorSystem system) {
        //创建了Supervisor
        ActorRef a = system.actorOf(Props.create(Supervisor.class), "Supervisor");
        //发送了一个RestartActor的Props，使得Supervisor创建RestartActor
        a.tell(Props.create(RestartActor.class), Actor.noSender());
        ActorSelection sel = system.actorSelection("akka://lifecycle/user/Supervisor/restartActor");
        for (int i = 0; i < 100; i++) {
            //向RestartActor发送了100多条RESTART消息
            sel.tell(RestartActor.Msg.RESTART, ActorRef.noSender());
        }
    }
}
