package com.someth.www.jobmanage.job

import akka.actor.{Actor, ActorRef, ActorSelection}
import akka.pattern.ask
import akka.util.Timeout
import com.someth.www.jobmanage.Helloer

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by asus-pc on 2016/11/13.
  */
class JobActor(remoteActor: ActorSelection, wordLine: String) extends  Actor{

  implicit val timeout = Timeout(5 seconds)
  private var backmessage: Option[ActorRef] = None

  override def receive = {
    case Helloer.Start =>
      val future = (remoteActor ? wordLine).mapTo[Int]
      val result = Await.result(future, timeout.duration)
      sender ! result
  }
}
