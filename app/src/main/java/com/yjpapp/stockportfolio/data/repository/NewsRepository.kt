package com.yjpapp.stockportfolio.data.repository

import com.yjpapp.stockportfolio.data.datasource.NewsDataSource
import com.yjpapp.stockportfolio.data.model.NewsData
import javax.inject.Inject

interface NewsRepository {
    suspend fun getNewsList(url: String): MutableList<NewsData>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsDataSource: NewsDataSource
): NewsRepository {
    override suspend fun getNewsList(url: String): MutableList<NewsData> =
        newsDataSource.getNewsList(url)
}