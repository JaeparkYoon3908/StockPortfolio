package com.yjpapp.stockportfolio.ui.view

interface MemoReadWriteView {
    fun showCompleteButton()

    fun hideCompleteButton()

    fun showDeleteButton()

    fun hideDeleteButton()

    fun onDeleteButtonClick()

    fun onCompleteButtonClick()
}