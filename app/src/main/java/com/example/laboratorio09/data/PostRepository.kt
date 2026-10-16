package com.example.laboratorio09.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostRepository(
    private val service: PostApiService = Retrofit.Builder()
        .baseUrl("https://json-placeholder.mock.beeceptor.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PostApiService::class.java)
) {
    suspend fun getPosts(): List<PostModel> = service.getUserPosts()

    suspend fun getPost(id: Int): PostModel = service.getUserPostById(id)
}
