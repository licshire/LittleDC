package com.someth.www.jobmanage

import java.util
import javax.management.relation.RoleStatus

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.someth.www.jobmanage.job.JobActor
import com.someth.www.share.CONF
import com.someth.www.share.loadbalance.DCStates
import com.typesafe.config.ConfigFactory
import org.apache.commons.configuration.{Configuration, ConfigurationUtils}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.collection.JavaConversions._
import scala.util.control._

/**
  * Created by asus-pc on 2016/10/28.
  */
class  ManegeSystem(roleStatus: DCStates, conf: Configuration) {
  val system = ActorSystem("RemoteNodeApp", ConfigFactory.load().getConfig("ServerSys"))

  def uuid() = java.util.UUID.randomUUID.toString

  //should async todo
  def getFreeSlave(roleStatus: DCStates, conf: Configuration): String ={
    var slavePath = conf.getString(CONF.SLAVE_ROOTPATH, CONF.SLAVE_ROOTPATH_DEFAULT)
    var slaveList = roleStatus.listGroup(slavePath)
    if(slaveList == null || slaveList.isEmpty){
      println("no slave in zk")
      return null
    }
    val loop = new Breaks;
    for(slave <- slaveList){
      var slaveDataPath = slavePath + "/" + slave;
      var slaveData = roleStatus.getDataFromZk(slaveDataPath).split("#")
      loop.breakable{
        if(slaveData == null || slaveData.length < 2){
          println("slave Data error")
          loop.break;
        }
        else if(slaveData(1) == "free"){
            return slaveData(0)
        }
      }
    }
    println("no free slave......")
    return null
  }

  def wcOneLine(wordline: String): Any = {
    //to do
    var slaveHost = getFreeSlave(roleStatus, conf)
//    println(slaveHost + "******************")
    if(slaveHost == null){
      println("no slave or slave are all busy")
      return null
    }
    var targetActorSystem = "akka.tcp://SlaveNodeApp@" + slaveHost + "/user/jobExcuter"
    val remoteActor = system.actorSelection(targetActorSystem)
    implicit val timeout = Timeout(5 seconds)
    val actorName = uuid()
    val actor = system.actorOf(Props(new JobActor(remoteActor, wordline)), name = actorName)
    val future =  actor ? Helloer.Start
    val result = Await.result(future, timeout.duration)
    return result

  }
}
