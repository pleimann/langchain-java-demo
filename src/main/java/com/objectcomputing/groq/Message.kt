package com.objectcomputing.groq

data class Message(
    var role: Role? = null,
    var content: String? = null,
    var name: String? = null,
    var seed: Int,
)
