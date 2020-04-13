package com.example.newsreader.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsreader.data.MainMvvmModel
import com.example.newsreader.data.constants.NewsSourceNames
import com.example.newsreader.utils.models.Article
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

class MainViewModel(mainMvvmModel: MainMvvmModel) : ViewModel() {

    private val mModel: MainMvvmModel = mainMvvmModel

    private var mDisposable: CompositeDisposable? = null

    private val mArticleListLiveData: MutableLiveData<List<Article>> = MutableLiveData()
    private val mViewStateLiveData: MutableLiveData<ViewState> = MutableLiveData()
    private val mSelectedArticleLiveData: MutableLiveData<Int> = MutableLiveData()

    /**
     * Called when user clicks on item in Article RecyclerView.
     *
     * @param articleIndex Position index of selected article item.
     */
    fun articleSelected(articleIndex: Int) {
        mSelectedArticleLiveData.value = articleIndex
    }

    /**
     * Get article list live data.
     *
     * @return Article list live data.
     */
    fun getArticleListLiveData(): LiveData<List<Article>> {
        return mArticleListLiveData
    }

    /**
     * Get view state live data.
     *
     * @return View state live data.
     */
    fun getViewStateLiveData(): LiveData<ViewState> {
        return mViewStateLiveData
    }

    /**
     * Get selected article live data.
     *
     * @return Selected article live data.
     */
    fun getSelectedArticleLiveData(): LiveData<Int> {
        return mSelectedArticleLiveData
    }

    /**
     * Start process of fetching articles.
     */
    fun fetchArticles() {
        dispose()

        mDisposable = CompositeDisposable()

        mViewStateLiveData.value = ViewState.SHOW_LOADER

        // Try to get news data from ( local ) database.
        val disposable: Disposable = mModel.getLocalArticles(NewsSourceNames.BBC.getString(), getDate())
            .subscribe({ articleList: List<Article> -> onGetLocalNewsSuccess(articleList) }, { getNewsFromApi() })

        mDisposable?.add(disposable)
    }

    /**
     * Dispose of disposables.
     */
    private fun dispose() {
        if (mDisposable?.isDisposed == false)
            mDisposable?.dispose()
    }

    /**
     * Subtract 5 minutes from current Date.
     *
     * @return Date.
     */
    private fun getDate(): Date {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.add(
            Calendar.MINUTE, -1
        )

        return calendar.time
    }

    /**
     * Called when Articles are successfully returned from local database.
     */
    private fun onGetLocalNewsSuccess(articleList: List<Article>) {
        if (articleList.isNotEmpty()) {
            // Articles were found in local database. Show them in view.
            mViewStateLiveData.value = ViewState.HIDE_LOADER
            mArticleListLiveData.value = articleList
        } else
            // No articles in local database. Try to get them from api.
            getNewsFromApi()
    }

    /**
     * Try to get news from NewsApi.
     */
    private fun getNewsFromApi() {
        val disposable: Disposable = mModel.getRemoteArticles(NewsSourceNames.BBC.getString(), "top")
            .subscribe(
                { articleList: List<Article>? ->
                    mViewStateLiveData.value = ViewState.HIDE_LOADER
                    mArticleListLiveData.value = articleList
                },
                {
                    mViewStateLiveData.value = ViewState.HIDE_LOADER
                    mViewStateLiveData.value = ViewState.SHOW_ERROR
                })

        mDisposable?.add(disposable)
    }

}
