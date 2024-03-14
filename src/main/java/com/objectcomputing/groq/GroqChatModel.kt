package com.objectcomputing.groq

import com.objectcomputing.groq.GroqChatModelName.MIXTRAL_8X7B_32768
import com.objectcomputing.groq.GroqMessageUtils.toGroqMessages
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.internal.RetryUtils
import dev.langchain4j.internal.Utils
import dev.langchain4j.internal.ValidationUtils
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.output.Response
import dev.langchain4j.model.output.TokenUsage
import java.time.Duration

class GroqChatModel(apiKey: ByteArray, baseUrl: String?, modelName: GroqChatModelName?, temperature: Double? = null, maxTokens: Int? = null,
                    topP: Double? = null, stop: List<String>? = null, timeout: Duration = Duration.ofSeconds(60),
                    maxRetries: Int = 3,
) : ChatLanguageModel {
    constructor(apiKey: ByteArray, baseUrl: String?, modelName: GroqChatModelName?) : this(apiKey, baseUrl, modelName, maxRetries = 3)

    private val client: GroqClient
    private val modelName: GroqChatModelName
    private var options: Options
    private val maxRetries: Int

    init {
        this.client = GroqClient(baseUrl, apiKey, timeout)
        this.modelName = modelName ?: MIXTRAL_8X7B_32768
        this.options = Options(temperature, maxTokens, topP, stream = false, stop)
        this.maxRetries = maxRetries ?: 3
    }

    public override fun generate(messages: List<ChatMessage>): Response<AiMessage> {
        ValidationUtils.ensureNotEmpty(messages, "messages")

        val request = ChatRequest(modelName, toGroqMessages(messages))

        val retryPolicy = RetryUtils.retryPolicyBuilder()
            .maxAttempts(maxRetries)
            .build()

        val response: ChatResponse = retryPolicy
            .withRetry { client.chat(request) }

        return Response.from(
            AiMessage.from(response.message?.content),
            TokenUsage(response.promptEvalCount, response.evalCount)
        )
    }

    override fun generate(messages: List<ChatMessage>?, toolSpecification: ToolSpecification?): Response<AiMessage> {


        return super.generate(messages, toolSpecification)
    }
}
