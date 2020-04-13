package com.example.newsreader.data.local.room

import com.example.newsreader.data.local.interfaces.NewsLocalDb
import com.example.newsreader.data.local.room.models.RoArticle
import com.example.newsreader.data.local.room.models.RoNewsSource
import com.example.newsreader.utils.models.Article
import com.example.newsreader.utils.models.NewsSource
import java.util.*

class RoomNewsLocalDb(roNewsDao: RoNewsDao) : NewsLocalDb {

    private val mDoa: RoNewsDao = roNewsDao

    /**
     * Get articles from database.
     *
     * @param newsSourceId NewsSourceNames ID.
     * @param date         Articles are returned if the are newer than passed Date.
     */
    override fun getArticles(newsSourceId: String, date: Date): List<Article> {
        return mDoa.getArticles(newsSourceId, date)
    }

    /**
     * Get articles from database.
     *
     * @param newsSourceId Articles's parent object ID.
     */
    override fun getArticles(newsSourceId: String): List<Article> {
        return mDoa.getArticles(newsSourceId)
    }

    /**
     * Inserts articles in database ( or updates if it already exist ).
     *
     * @param newsSource NewsSourceNames data to save in database.
     */
    override fun insertNews(newsSource: NewsSource) {
        val roNewsSource: RoNewsSource = RoNewsSource(newsSource)
        val roArticleList: List<RoArticle> = roNewsSource.toRoArticleList(newsSource)

        mDoa.insertOrReplaceNewsDataWithArticles(roNewsSource, roArticleList)
    }

}
