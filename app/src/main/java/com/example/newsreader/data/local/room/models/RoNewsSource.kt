package com.example.newsreader.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsreader.utils.models.NewsSource
import java.util.*
import kotlin.collections.ArrayList

@Entity
class RoNewsSource() {

    @PrimaryKey
    var id: String = ""

    /* Timestamp showing when was http response received. */
    var date: Date? = null

    constructor(newsSource: NewsSource) : this() {
        id = newsSource.id
        date = newsSource.date
    }

    /**
     * Returns RoArticle list from NewsSourceNames.
     *
     * @param newsSource NewsSourceNames from which to create RoArticle list.
     * @return RoArticle list.
     */
    fun toRoArticleList(newsSource: NewsSource): List<RoArticle> {
        val roArticleList: MutableList<RoArticle> = mutableListOf()

        for ((index, value) in newsSource.articles.withIndex())
            roArticleList.add(RoArticle(newsSource.id, value, index))

        return roArticleList
    }

}