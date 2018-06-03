package com.guanhang.shizhangaobingfa.chapter6.router;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.*;
import com.guanhang.shizhangaobingfa.chapter6.MyWorker;

import java.util.ArrayList;
import java.util.List;

public class WatchActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public Router router;
    {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef worker = getContext().actorOf(Props.create(MyWorker.class), "worker_" + i);
            getContext().watch(worker);
            routees.add(new ActorRefRoutee(worker));
        }
        //轮询的路由策略
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof MyWorker.Msg) {
            router.route(message, getSender());
        } else if (message instanceof Terminated) {
            router = router.removeRoutee(((Terminated) message).actor());
            System.out.println("worker is closed");
            if (router.routees().size() == 0) {
                System.out.println("close system");
                //RouteMain.flag.send(false)
                getContext().system().terminate();
            }
        }else {
            unhandled(message);
        }
    }
}
