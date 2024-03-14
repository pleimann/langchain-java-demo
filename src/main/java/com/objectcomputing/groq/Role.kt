package com.objectcomputing.groq

enum class Role {
    SYSTEM,  // sets the behavior of the assistant and can be used to provide specific instructions for how it should behave throughout the conversation.
    USER, // Messages written by a user of the LLM.
    ASSISTANT, // Messages written by the LLM in a previous completion.
}
