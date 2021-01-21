package com.yjpapp.stockportfolio.ui.view

/**
 * MemoReadWriteActivity의 View
 *
 * @author Yun Jae-park
 * @since 2020.12
 */
interface MemoReadWriteView {
    fun showCompleteButton()

    fun hideCompleteButton()

    fun showDeleteButton()

    fun hideDeleteButton()

    fun onDeleteButtonClick()

    fun onCompleteButtonClick()
}