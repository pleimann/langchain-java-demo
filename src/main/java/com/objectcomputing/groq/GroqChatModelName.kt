package com.objectcomputing.groq

enum class GroqChatModelName private constructor(private val stringValue: String) {
    MIXTRAL_8X7B_32768("mixtral-8x7b-32768"),
    LLAMA2_70B("LLaMA2-70b"),
    ;

    public override fun toString(): String {
        return this.stringValue
    }
}
