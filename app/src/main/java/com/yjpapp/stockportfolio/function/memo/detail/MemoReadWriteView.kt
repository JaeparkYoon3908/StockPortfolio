package com.yjpapp.stockportfolio.function.memo.detail

/**
 * MemoReadWriteActivity의 View
 *
 * @author Yoon Jae-park
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