package com.example.newsreader.view.main

enum class ViewState {

    /**
     * Started to fetch data. View should show loader.
     */
    SHOW_LOADER,

    /**
     * Data was fetched successfully. View should hide loader.
     */
    HIDE_LOADER,

    /**
     * View should show error message.
     */
    SHOW_ERROR

}
