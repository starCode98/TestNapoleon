package pe.com.starcode.testnapoleonsystem.main.repository

import android.content.Context
import pe.com.starcode.testnapoleonsystem.base.retrofit.ApiHelper
import pe.com.starcode.testnapoleonsystem.base.retrofit.response.PostResponse
import pe.com.starcode.testnapoleonsystem.base.room.DataBaseApp
import pe.com.starcode.testnapoleonsystem.base.room.Post

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getPosts() = apiHelper.getPosts()

    fun savePost(body: PostResponse?, c: Context) :List<Post>?{
        if (body != null) {
            val db = DataBaseApp(c)
            var count = 0
            body.forEach { item ->
                var i = Post()
                i.id = item.id
                i.body = item.body
                i.title = item.title
                i.userId = item.userId
                count++
                if(count<=20){
                    i.state = true
                }
                db.postDao().insert(i)
            }
            return db.postDao().listAll()
        }
        return null
    }

    fun updatePost(post: Post, c: Context) {
        val db = DataBaseApp(c)
        db.postDao().update(post)
    }
}