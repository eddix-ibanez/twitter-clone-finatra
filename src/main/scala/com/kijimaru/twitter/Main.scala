package com.kijimaru.twitter

import com.twitter.finagle.Http
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.finagle.tracing.NullTracer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.HttpServer
import Controller._
import com.kijimaru.twitter.module.DBModule
import com.kijimaru.twitter.infrastructure.Config._
import com.kijimaru.twitter.controller._

object Main extends HttpServer {

  // DIの設定
  override def modules = Seq(
    DBModule
  )

  // 色々な設定項目はBaseHttpServerを参照
  override def defaultHttpPort: String = conf.getString("port")

  override def defaultHttpServerName: String = conf.getString("host")

  override def configureHttpServer(server: Http.Server): Http.Server = {
    server
      .withCompressionLevel(0)
      .withStatsReceiver(NullStatsReceiver)
      .withTracer(NullTracer)
  }

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[TweetController]
      .add[UserManageController]
      .add[TweetSearchController]
      .add[TimelineController]
      .add[ProfileController]
  }
}
