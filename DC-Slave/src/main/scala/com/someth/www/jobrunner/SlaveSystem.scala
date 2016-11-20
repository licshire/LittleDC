package com.someth.www.jobrunner

import akka.actor.{ActorSystem, Props}
import com.someth.www.share.loadbalance.{DCStates, RoleStates}
import com.typesafe.config.ConfigFactory
import org.apache.commons.configuration.{Configuration, ConfigurationUtils}

/**
  * Created by asus-pc on 2016/11/10.
  */
class SlaveSystem(conf: Configuration, dCStates: DCStates) {

  val system = ActorSystem("SlaveNodeApp", ConfigFactory.load().getConfig("SlaveSys"))
  val localActor = system.actorOf(Props(new JobExcuter(conf, dCStates)), name = "jobExcuter")
  dCStates.setDataToZk("free")
  Thread.sleep(4000)

}
