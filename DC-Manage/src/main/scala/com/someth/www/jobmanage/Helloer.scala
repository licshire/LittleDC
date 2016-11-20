package com.someth.www.jobmanage

import akka.actor.Actor

/**
  * Created by asus-pc on 2016/10/28.
  */
object Helloer {
  case object Start
  case object Hello
  case object Done
}

class Helloer extends Actor {
  def receive = {
    case Helloer.Hello =>
      println("Hello World!")
      sender() ! Helloer.Done
  }
}
