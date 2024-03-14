package com.objectcomputing.groq

import com.objectcomputing.groq.GroqChatModelName.MIXTRAL_8X7B_32768


data class ChatRequest(
    var model: GroqChatModelName = MIXTRAL_8X7B_32768,
    val messages: List<Message>? = null,
)