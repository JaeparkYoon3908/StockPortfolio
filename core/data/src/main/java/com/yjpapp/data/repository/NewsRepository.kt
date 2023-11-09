package com.yjpapp.data.repository

import com.yjpapp.data.datasource.NewsDataSource
import com.yjpapp.data.model.NewsData
import com.yjpapp.data.model.ResponseResult
import javax.inject.Inject

interface NewsRepository {
    suspend fun getNewsList(url: String): ResponseResult<List<NewsData>>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsDataSource: NewsDataSource
): NewsRepository {
    override suspend fun getNewsList(url: String): ResponseResult<List<NewsData>> =
        newsDataSource.getNewsList(url)
}