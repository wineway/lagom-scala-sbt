package org.example.lagomdemo.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import org.example.lagomdemo.api.LagomdemoService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class LagomdemoLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomdemoApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomdemoApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomdemoService])
}

abstract class LagomdemoApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[LagomdemoService](wire[LagomdemoServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = LagomdemoSerializerRegistry

  // Register the lagomdemo persistent entity
  persistentEntityRegistry.register(wire[LagomdemoEntity])
}
