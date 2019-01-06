package org.example.lagomdemostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.example.lagomdemostream.api.LagomdemoStreamService
import org.example.lagomdemo.api.LagomdemoService

import scala.concurrent.Future

/**
  * Implementation of the LagomdemoStreamService.
  */
class LagomdemoStreamServiceImpl(lagomdemoService: LagomdemoService) extends LagomdemoStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomdemoService.hello(_).invoke()))
  }
}
