package com.example.newsreader.data.remote.volley

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.newsreader.R
import com.example.newsreader.data.remote.newsapi.NewsApi
import com.example.newsreader.utils.models.NewsSource
import com.google.gson.Gson
import io.reactivex.Single
import java.util.*

class VolleyNewsApi(context: Context) : NewsApi {

    private val TAG_GET_NEWS: String = "GET_NEWS"

    private val mRequestQueue: RequestQueue = Volley.newRequestQueue(context)

    private val mBaseUrl: String = "https://newsapi.org/";
    private val mPath: String = "v1/articles";
    private val mApiKey: String = context.resources.getString(R.string.news_api_key)

    /**
     * Get news articles from NewsApi.
     *
     * @param source Article source.
     * @param sortBy How to sort articles.
     * @return NewsSourceNames object with timestamp and articles list.
     */
    override fun getNews(source: String, sortBy: String): Single<NewsSource> {
        return Single.create { emitter ->
            val request: StringRequest = StringRequest(
                Request.Method.GET,
                "$mBaseUrl$mPath?source=$source&sortBy=$sortBy&apiKey=$mApiKey",
                Response.Listener { response: String -> emitter.onSuccess(responseToNewsSource(response, source)) },
                Response.ErrorListener { error: VolleyError -> emitter.onError(error) }
            )

            request.tag = TAG_GET_NEWS

            emitter.setCancellable { mRequestQueue.cancelAll(TAG_GET_NEWS) }

            mRequestQueue.add(request)
        }
    }

    /**
     * Create NewsSourceNames object from news api response.
     *
     * @param response       NewsApi response.
     * @param newsSourceName Article source.
     * @return Created NewsSource object.
     */
    private fun responseToNewsSource(response: String, newsSourceName: String): NewsSource {
        val gson: Gson = Gson()

        val newsSource: NewsSource = gson.fromJson(response, NewsSource::class.java)
        newsSource.date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
        newsSource.id = newsSourceName

        return newsSource
    }

}
