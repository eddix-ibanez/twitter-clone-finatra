package Dao

import Model.Retweet
import io.getquill._
import java.util.Date

class RetweetDao {

  lazy val ctx: MysqlJdbcContext[SnakeCase.type] =
    new MysqlJdbcContext(SnakeCase, "ctx")

  import ctx._

  def retweet(tweetId: Long, userId: Long) = {
    val now = "%tY-%<tm-%<td %<tH:%<tM:%<tS"
    val q = quote {
      query[Retweet].insert(
        _.tweetId -> lift(tweetId),
        _.userId -> lift(userId),
        _.timestamp->lift(new Date))
    }
    ctx.run(q)
  }

  def findByUserId(userId: Long): List[Retweet] = {
    val q = quote {
      query[Retweet].withFilter(_.userId == lift(userId)).sortBy(_.timestamp)
    }
    ctx.run(q)
  }
}