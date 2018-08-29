package app.com.skylinservice.data

import app.com.skylinservice.data.remote.Api
import app.com.skylinservice.data.remote.model.Post
import io.reactivex.Observable

interface PostDataSource {
    fun getPosts(): Observable<List<Post>>
    fun getReadyData(): Observable<List<String>>
}

class PostRepository(private val api: Api) : PostDataSource {
    override fun getReadyData(): Observable<List<String>> = api.getReadyData()
    override fun getPosts(): Observable<List<Post>> = api.getPosts()
}