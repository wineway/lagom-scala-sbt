package org.example.lagomdemo.impl

import org.example.lagomdemo.api
import org.example.lagomdemo.api.{LagomdemoService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the LagomdemoService.
  */
class LagomdemoServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagomdemoService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagomdemo entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomdemoEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagomdemo entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomdemoEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagomdemoEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagomdemoEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
