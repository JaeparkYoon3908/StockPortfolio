package com.yjpapp.stockportfolio.data.repository

import com.yjpapp.stockportfolio.data.datasource.NewsDataSource
import com.yjpapp.stockportfolio.data.model.NewsData
import javax.inject.Inject

interface NewsRepository {
    suspend fun getMkFinanceNewsList(): MutableList<NewsData>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsDataSource: NewsDataSource
): NewsRepository {
    override suspend fun getMkFinanceNewsList(): MutableList<NewsData> = newsDataSource.getNkFinanceNewsList()
}