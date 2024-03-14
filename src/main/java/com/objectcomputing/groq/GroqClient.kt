package com.objectcomputing.groq

import java.time.Duration

class GroqClient(
    baseUrl: String? = "https://api.groq.com/openai/v1/",
    apiKey: ByteArray,
    timeout: Duration
) {

    fun chat(chatRequest: ChatRequest): ChatResponse {
        TODO("Not yet implemented")
    }
}