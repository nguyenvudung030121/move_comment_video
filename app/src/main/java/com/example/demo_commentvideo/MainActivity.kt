package com.example.demo_commentvideo

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.demo_commentvideo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), CommentListener {
    lateinit var binding: ActivityMainBinding
    lateinit var adapterComment: listCommentAdapter
    private var listComment: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user4 = User(R.drawable.avatar, "Nguyen Vu Dung", true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        userComment(
            binding.cancelButton,
            binding.sendButton,
            binding.edtUserComment,
            listComment,
            user4
        )

    }


    override fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
        listComment: MutableList<Comment>,
        user: User
    ) {
        cancelButton.visibility = View.GONE
        sendButton.visibility = View.GONE
        onWriteCommentListener(editText, cancelButton, sendButton)
        onCancelUserComment(cancelButton, editText)
        onSendUserComment(sendButton, listComment, editText, cancelButton, user)
        onLoadComment(listComment)

    }

    override fun onWriteCommentListener(
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton
    ) {

        editText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        cancelButton.visibility = View.VISIBLE
                        sendButton.visibility = View.VISIBLE
                    } else {
                        cancelButton.visibility = View.GONE
                        sendButton.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
/*                if (!s.isNullOrEmpty()) {
                    binding.edtUserComment.clearFocus()
                }*/
                }

            })


            onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        v?.let { hideKeyboard(it) }
                    }
                }
        }

    }

    override fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText) {
        cancelButton.setOnClickListener {
            clearEdittext(editText, cancelButton)
        }
    }

    override fun onSendUserComment(
        sendButton: AppCompatButton,
        listComment: MutableList<Comment>,
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: User
    ) {
        sendButton.setOnClickListener {
            listComment.add(
                0,
                Comment(4, editText.text.toString().trim(), "Just now", mutableListOf(), user)
            )
            binding.listComment.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = adapterComment
            }
            clearEdittext(editText, cancelButton)
        }
    }

    override fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton) {
        editText.text = null
        editText.clearFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            cancelButton.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    override fun onLoadComment(listComment: MutableList<Comment>) {
        getData()
        adapterComment = listCommentAdapter(listComment, object : listCommentAdapter.ReplyListener {
            override fun userComment(
                cancelButton: AppCompatButton,
                sendButton: AppCompatButton,
                editText: AppCompatEditText,
                listCommentReply: MutableList<Comment>,
                list: RecyclerView,
                user: User
            ) {

                cancelButton.visibility = View.GONE
                sendButton.visibility = View.GONE
                onWriteCommentListener(editText, cancelButton, sendButton)
                onCancelUserComment(cancelButton, editText)
                onSendUserReply(
                    sendButton,
                    list,
                    listCommentReply,
                    editText,
                    cancelButton,
                    user
                )
            }

            override fun onWriteCommentListener(
                editText: AppCompatEditText,
                cancelButton: AppCompatButton,
                sendButton: AppCompatButton
            ) {
                editText.apply {
                    addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            if (!s.isNullOrEmpty()) {
                                cancelButton.visibility = View.VISIBLE
                                sendButton.visibility = View.VISIBLE
                            } else {
                                cancelButton.visibility = View.GONE
                                sendButton.visibility = View.GONE
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {
/*                if (!s.isNullOrEmpty()) {
                    binding.edtUserComment.clearFocus()
                }*/
                        }

                    })


                    onFocusChangeListener =
                        View.OnFocusChangeListener { v, hasFocus ->
                            if (!hasFocus) {
                                v?.let { hideKeyboard(it) }
                            }
                        }
                }

            }

            override fun onCancelUserComment(
                cancelButton: AppCompatButton,
                editText: AppCompatEditText
            ) {
                cancelButton.setOnClickListener {
                    clearEdittext(editText, cancelButton)
                }
            }

            override fun onSendUserReply(
                sendButton: AppCompatButton,
                list: RecyclerView,
                listCommentReply: MutableList<Comment>,
                editText: AppCompatEditText,
                cancelButton: AppCompatButton,
                user: User
            ) {
                sendButton.setOnClickListener {
                    listCommentReply.add(
                        0,
                        Comment(
                            5,
                            editText.text.toString().trim(),
                            "Just now",
                            mutableListOf(),
                            user,
                            true
                        )
                    )

                    for (i in listCommentReply){
                        Log.d("DUNG",i.content)
                    }

                    var adapterReply = listReplyAdapter(listCommentReply)
                    list.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = adapterReply
                    }
                    clearEdittext(editText, cancelButton)
                }
            }

            override fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton) {
                editText.text = null
                editText.clearFocus()
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    cancelButton.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
            }

            override fun hideKeyboard(view: View) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }


        })

        binding.listComment.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterComment
        }
    }


    private fun getData() {

        val user1 = User(R.drawable.avatar, "Vu Dung", false)
        val user2 = User(R.drawable.avatar, "Taylor Swift", true)
        val user3 = User(R.drawable.avatar, "Justin Bieber", false)
        val user4 = User(R.drawable.avatar, "Nguyen Vu Dung", true)


        listComment.add(
            Comment(
                1, "DSMLMFLSKEMFKLM", "Just now",
                mutableListOf(
                    Comment(1, "HIHIHAHAHAH", "Just now", mutableListOf(), user1),
                    Comment(2, "ASDASDASDASSD", "Just now", mutableListOf(), user2),
                    Comment(3, "BDSDBADASB", "Just now", mutableListOf(), user1),
                    Comment(4, "WEREWREWREWREW", "Just now", mutableListOf(), user2),
                ), user1
            )
        )

        listComment.add(Comment(2, "ALO SONDASDK", "Just now", mutableListOf(), user2))
        listComment.add(Comment(3, "KAMAVINGAR HALANDES", "Just now", mutableListOf(), user3))
        listComment.add(Comment(4, "SDASDESADASD", "Just now", mutableListOf(), user4))


    }


}