package com.example.newsreader.view.article

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.newsreader.databinding.ActivityArticleBinding
import com.example.newsreader.utils.containers.Pair
import com.example.newsreader.utils.dialogs.ErrorDialog
import com.example.newsreader.utils.models.Article
import com.example.newsreader.view.article.adapter.PagerArticleAdapter
import com.example.newsreader.view.main.MainActivity
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.parameter.parametersOf

class ArticleActivity : AppCompatActivity() {

    private val mViewModel: ArticleViewModel by currentScope.viewModel(this)
    private val mPagerArticleAdapter: PagerArticleAdapter by currentScope.inject{ parametersOf(this) }

    private var mBinding: ActivityArticleBinding? = null

    private lateinit var mOnPageChangeCallback: ViewPager2.OnPageChangeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set view binding.
        mBinding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)

        // Make view's content to appear behind the navigation bar.
        mBinding?.root?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        initPagerArticles()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding?.pagerArticles?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mBinding = null
    }

    /**
     * Display Articles list in view.
     *
     * @param listIntegerPair Pair of Article List and position index of Article to show as current item.
     */
    private fun showArticles(listIntegerPair: Pair<List<Article>, Int>) {
        mPagerArticleAdapter.setDataset(listIntegerPair.first)

        mBinding?.pagerArticles?.setCurrentItem(listIntegerPair.second, false)
    }

    /**
     * Called when views gets notified that the view state has changed.
     *
     * @param viewState New view state.
     */
    private fun onViewStateChange(viewState: ViewState) {
        when (viewState) {
            ViewState.SHOW_ERROR -> showErrorMessage()
        }
    }

    /**
     * Show error message to user.
     */
    private fun showErrorMessage() {
        val errorDialog: DialogFragment = ErrorDialog.newInstance()
        errorDialog.show(supportFragmentManager, "ErrorDialog")
    }

    /**
     * Display title in app's toolbar.
     *
     * @param title String to show in toolbar.
     */
    private fun showToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    /**
     * Initialize ViewModel.
     */
    private fun initViewModel() {
        // Get Article index from Intent that started this Activity.
        if (intent.hasExtra(MainActivity.EXTRA_ARTICLE_INDEX)) {
            mViewModel.setInitialSlide(intent.getIntExtra(MainActivity.EXTRA_ARTICLE_INDEX, 0))

            intent.removeExtra(MainActivity.EXTRA_ARTICLE_INDEX)
        }

        // Set view model observers.
        mViewModel.getArticleListLiveData().observe(this, Observer { listIntPair: Pair<List<Article>, Int> -> showArticles(listIntPair) })
        mViewModel.getArticleTitleLiveData().observe(this, Observer { title: String -> showToolbarTitle(title) })
        mViewModel.getViewStateLiveData().observe(this, Observer { viewState: ViewState -> onViewStateChange(viewState) })
    }

    /**
     * Initialize articles ViewPager.
     */
    private fun initPagerArticles() {
        mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            /**
             * This method will be invoked when a new page becomes selected.
             *
             * @param position Position index of the new selected page.
             */
            override fun onPageSelected(position: Int) {
                mViewModel.articlePageChanged(position)
            }
        }

        mBinding?.pagerArticles?.registerOnPageChangeCallback(mOnPageChangeCallback)

        mBinding?.pagerArticles?.adapter = mPagerArticleAdapter
    }

}
