package com.example.newsreader.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.ActivityMainBinding
import com.example.newsreader.utils.dialogs.ErrorDialog
import com.example.newsreader.utils.models.Article
import com.example.newsreader.view.article.ArticleActivity
import com.example.newsreader.view.main.adapter.RecyclerArticleAdapter
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel

class MainActivity : AppCompatActivity(), RecyclerArticleAdapter.OnArticleClickListener {

    companion object {
        const val EXTRA_ARTICLE_INDEX = "EXTRA_ARTICLE_INDEX"
    }

    private val mViewModel: MainViewModel by currentScope.viewModel(this)
    private val mAdapter: RecyclerArticleAdapter by currentScope.inject()

    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init view binding.
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)

        // Make view's content to appear behind the navigation bar.
        mBinding?.root?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        initRecyclerArticle()

        initViewModel()
    }

    override fun onResume() {
        super.onResume()

        mViewModel.fetchArticles()
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }

    /**
     * Callback that will be called.
     *
     * @param articlePosition Position of the item in the adapter's data set.
     */
    override fun onArticleClick(articlePosition: Int) {
        mViewModel.articleSelected(articlePosition)
    }

    /**
     * Starts ArticleActivity.
     *
     * @param articleIndex Position index of selected article item.
     */
    private fun startArticleActivity(articleIndex: Int) {
        val intent: Intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(EXTRA_ARTICLE_INDEX, articleIndex)

        startActivity(intent)
    }

    /**
     * Display Articles list in view.
     *
     * @param articleList List of Articles to show.
     */
    private fun showArticles(articleList: List<Article>) = mAdapter.setDataset(articleList)

    /**
     * Called when views gets notified that the view state has changed.
     *
     * @param viewState New view state.
     */
    private fun onViewStateChange(viewState: ViewState) {
        when (viewState) {
            ViewState.SHOW_LOADER -> showProgressBar(true)
            ViewState.HIDE_LOADER -> showProgressBar(false)
            ViewState.SHOW_ERROR -> showErrorMessage()
        }
    }

    /**
     * Shows ProgressBar in view.
     *
     * @param show True to show ProgressBar, false to hide it.
     */
    private fun showProgressBar(show: Boolean) {
        if (show) mBinding?.progressLoader?.visibility = View.VISIBLE
        else mBinding?.progressLoader?.visibility = View.GONE
    }

    /**
     * Show error message to user.
     */
    private fun showErrorMessage() {
        val errorDialog: DialogFragment = ErrorDialog.newInstance()
        errorDialog.show(supportFragmentManager, "ErrorDialog")
    }

    /**
     * Initialize ViewModel.
     */
    private fun initViewModel() {
        mViewModel.getArticleListLiveData().observe(this, Observer { articleList -> showArticles(articleList) })
        mViewModel.getSelectedArticleLiveData().observe(this, Observer { articleIndex -> startArticleActivity(articleIndex) })
        mViewModel.getViewStateLiveData().observe(this, Observer { viewState -> onViewStateChange(viewState) })
    }

    /**
     * Initialize RecyclerView to show Articles list.
     */
    private fun initRecyclerArticle() {
        mBinding?.recyclerArticle?.layoutManager = LinearLayoutManager(this)
        mBinding?.recyclerArticle?.adapter = mAdapter

        mAdapter.setOnArticleClickListener(this)
    }

}
