package com.objectcomputing.groq

data class ChatResponse(
    var model: GroqChatModelName? = null,
    var createdAt: String? = null,
    var message: Message? = null,
    var done: Boolean? = null,
    var promptEvalCount: Int? = null,
    var evalCount: Int? = null,
)
