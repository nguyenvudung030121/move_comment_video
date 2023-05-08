package com.example.demo_commentvideo

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

interface CommentListener {
    fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
        listComment: MutableList<Comment>,
        user: User
    )

    fun onWriteCommentListener(
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton
    )

    fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText)
    fun onSendUserComment(
        sendButton: AppCompatButton,
        listComment: MutableList<Comment>,
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: User
    )

    fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton)
    fun hideKeyboard(view: View)
    fun onLoadComment(listComment: MutableList<Comment>)
}