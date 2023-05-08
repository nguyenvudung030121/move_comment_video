package com.example.demo_commentvideo

class Comment(
    val id: Int,
    val content: String,
    val timeOfComment: String,
    var listChild: MutableList<Comment>,
    val user: User,
    var isChild: Boolean = false
) {
}