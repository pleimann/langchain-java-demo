package com.objectcomputing.groq

data class Options(
    val temperature: Double?, // Controls randomness of responses. A lower temperature leads to more predictable outputs while a higher temperature results in more varies and sometimes more creative outputs.
    val maxTokens: Int?, // The maximum number of tokens that the model can process in a single response. This limits ensures computational efficiency and resource management.
    val topP: Double?, // A method of text generation where a model will only consider the most probable next tokens that make up the probability p. 0.5 means half of all likelihood-weighted options are considered.
    val stream: Boolean = false, // User server-side events to send the completion in small deltas rather than in a single batch after all processing has finished. This reduces the time to first token received.
    val stop: List<String>? = null // A stop sequence is a predefined or user-specified text string that signals an AI to stop generating content, ensuring its responses remain focused and concise.
)