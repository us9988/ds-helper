package com.dshelper.app.data.api

import com.dshelper.app.data.api.dto.PostDetailResponse
import com.dshelper.app.data.api.dto.PostListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {
    @GET("posts")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "desc",
        @Query("sortBy") sortBy: String = "createdAt"
    ): PostListResponse

    @GET("posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: String
    ): PostDetailResponse

}
