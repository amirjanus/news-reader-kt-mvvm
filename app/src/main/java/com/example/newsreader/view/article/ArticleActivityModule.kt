package com.example.newsreader.view.article

import androidx.fragment.app.FragmentActivity
import com.example.newsreader.view.article.adapter.PagerArticleAdapter
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val articleActivityModule = module {

    scope(named<ArticleActivity>()) {
        factory { (activity: FragmentActivity) -> PagerArticleAdapter(activity) }

        viewModel { ArticleViewModel(get()) }
    }

}
