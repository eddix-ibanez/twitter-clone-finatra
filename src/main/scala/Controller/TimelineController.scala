package Controller

import com.twitter.finagle.http.Request
import com.twitter.finatra.http._
import Dao.{FollowDao, TweetDao}
import Model.User

class TimelineController extends Controller {
  val tweetDao: TweetDao = new TweetDao
  val followDao: FollowDao = new FollowDao


  get("/user/:id/timeline") { request: Request =>
    val userId = request.getIntParam("id")
    val following: List[Long] = followDao.getFollowingIdList(userId)

    following.flatMap(i=>tweetDao.findByUser(i))
  }
}