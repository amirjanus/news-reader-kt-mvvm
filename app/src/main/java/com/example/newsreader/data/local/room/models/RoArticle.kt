package com.example.newsreader.data.local.room.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.newsreader.utils.models.Article
import com.example.newsreader.utils.models.NewsSource

@Entity
class RoArticle() {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    /* Parent's primary key. */
    var newsSourceId: String? = null

    /* Article's title. */
    var title: String? = null

    /* Article's text. */
    var description: String? = null

    /* Url to article's image. */
    var urlToImage: String? = null

    /* Order in which articles were received from NewsApi. */
    var sortOrder = 0

    @Ignore
    constructor(newsSourceId: String, article: Article, index: Int) : this() {
        this.newsSourceId = newsSourceId

        title = article.title
        description = article.description
        urlToImage = article.urlToImage
        sortOrder = index
    }

}