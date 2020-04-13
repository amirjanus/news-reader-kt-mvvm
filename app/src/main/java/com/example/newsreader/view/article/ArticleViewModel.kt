package com.example.newsreader.view.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsreader.data.MainMvvmModel
import com.example.newsreader.data.constants.NewsSourceNames
import com.example.newsreader.utils.models.Article
import com.example.newsreader.utils.containers.Pair
import io.reactivex.disposables.Disposable

class ArticleViewModel(mainMvvmModel: MainMvvmModel) : ViewModel() {

    private val mModel: MainMvvmModel = mainMvvmModel

    private var mDisposable: Disposable? = null

    private val mArticleListLiveData: MutableLiveData<Pair<List<Article>, Int>> = MutableLiveData()
    private val mArticleTitleLiveData: MutableLiveData<String> = MutableLiveData()
    private val mViewStateLiveData: MutableLiveData<ViewState> = MutableLiveData()

    private var mInitialSlide: Int = 0

    init {
        fetchArticles()
    }

    /**
     * Called when new page is set in ViewPager.
     *
     * @param index Position index of the new ViewPager page.
     */
    fun articlePageChanged(index: Int) {
        mArticleListLiveData.value?.second = index
        mArticleTitleLiveData.value = mArticleListLiveData.value?.first?.get(index)?.title
    }

    /**
     * Set which ViewPager slide show to when Articles are shown for the first time in ViewPager.
     *
     * @param initialSlide Position index of ViewPager slide.
     */
    fun setInitialSlide(initialSlide: Int) {
        mInitialSlide = initialSlide
    }

    /**
     * Get article list live data.
     *
     * @return Article list live data.
     */
    fun getArticleListLiveData(): LiveData<Pair<List<Article>, Int>> {
        return mArticleListLiveData
    }

    /**
     * Get article title live data.
     *
     * @return Article title live data.
     */
    fun getArticleTitleLiveData(): LiveData<String> {
        return mArticleTitleLiveData
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
     * Get articles from database.
     */
    private fun fetchArticles() {
        mDisposable = mModel.getLocalArticles(NewsSourceNames.BBC.getString())
            .subscribe(
                { articleList: List<Article> -> mArticleListLiveData.value = Pair(articleList, mInitialSlide) },
                { mViewStateLiveData.value = ViewState.SHOW_ERROR }
            )
    }
}
