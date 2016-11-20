package com.someth.www.jobrunner

import akka.actor.Actor
import com.someth.www.share.CONF
import com.someth.www.share.loadbalance.{DCStates, RoleStates}
import org.apache.commons.configuration.{Configuration, ConfigurationUtils}

/**
  * Created by asus-pc on 2016/10/28.
  */

class JobExcuter(conf: Configuration, dCStates: DCStates) extends Actor{

//  val sessionMaxNum = CONF.MAX_SESSION_NUM
  override def receive: Receive = {

    case wordline: String =>
      println("---------ddd-----------" + wordline)
      var wordnum = wordline.split(" ").length
      sender ! wordnum
  }

}
