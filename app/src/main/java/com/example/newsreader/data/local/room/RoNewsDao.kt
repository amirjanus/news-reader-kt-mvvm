package com.example.newsreader.data.local.room

import androidx.room.*
import com.example.newsreader.data.local.room.models.RoArticle
import com.example.newsreader.data.local.room.models.RoNewsSource
import com.example.newsreader.utils.models.Article
import java.util.*

@Dao
abstract class RoNewsDao {

    /**
     * Inserts news data together with list of articles in database.
     *
     * @param roNewsSource    News data to insert.
     * @param roArticleList List of articles to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNewsDataWithArticles(roNewsSource: RoNewsSource, roArticleList: List<RoArticle>)

    /**
     * Inserts news data together with list of articles in database while delete old articles.
     *
     * @param roNewsSource    News data to insert.
     * @param roArticleList List of articles to insert.
     */
    @Transaction
    open fun insertOrReplaceNewsDataWithArticles(roNewsSource: RoNewsSource, roArticleList: List<RoArticle>) {
        // Delete old articles.
        deleteArticles(roNewsSource.id)

        // Insert ( or replace ) news data and insert new articles.
        insertNewsDataWithArticles(roNewsSource, roArticleList)
    }

    /**
     * Returns list of articles from database.
     *
     * @param newsSourceId Articles's parent object ID.
     * @return List of articles or empty list if none exist that satisfy search query.
     */
    @Query(
        """SELECT * FROM roarticle 
        WHERE newsSourceId = :newsSourceId""" )
    abstract fun getArticles(newsSourceId: String): List<Article>

    /**
     * Returns list of articles from database.
     *
     * @param newsSourceId Articles's parent object ID.
     * @param date       Searches for articles newer than this Date.
     * @return List of articles or empty list if none exist that satisfy search query.
     */
    @Query( """SELECT * FROM roarticle 
            INNER JOIN RoNewsSource ON RoNewsSource.id = roarticle.newsSourceId 
            WHERE RoNewsSource.id = :newsSourceId AND RoNewsSource.date >= :date 
            ORDER BY roarticle.sortOrder ASC""" )
    abstract fun getArticles(newsSourceId: String, date: Date): List<Article>

    /**
     * Delete articles from database.
     *
     * @param newsSourceId Articles's parent object ID.
     * @return Number of rows removed from the database
     */
    @Query( """DELETE FROM roarticle 
            WHERE newsSourceId = :newsSourceId""" )
    abstract fun deleteArticles(newsSourceId: String)

}
