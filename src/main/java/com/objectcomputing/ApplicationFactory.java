package com.objectcomputing;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Factory
public class ApplicationFactory {

	@Bean
	ChatLanguageModel chatLanguageModel(@Property(name = "langchain4j.open-ai.chat-model.api-key")
										String openAiApiKey
	) {
		return OpenAiChatModel.withApiKey(openAiApiKey);
	}

	@Bean
	CustomerSupportAgent customerSupportAgent(
			ChatLanguageModel chatLanguageModel,
			BookingTools bookingTools,
			ContentRetriever contentRetriever
	) {
		return AiServices.builder(CustomerSupportAgent.class)
						 .chatLanguageModel(chatLanguageModel)
						 .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
						 .tools(bookingTools)
						 .contentRetriever(contentRetriever)
				.build();
	}

	@Bean
	ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
		// You will need to adjust these parameters to find the optimal setting, which will depend on two main factors:
		// - The nature of your data
		// - The embedding model you are using
		int maxResults = 1;
		double minScore = 0.6;

		return EmbeddingStoreContentRetriever.builder()
											 .embeddingStore(embeddingStore)
											 .embeddingModel(embeddingModel)
											 .maxResults(maxResults)
											 .minScore(minScore)
				.build();
	}

	@Bean
	EmbeddingModel embeddingModel() {
		return new AllMiniLmL6V2EmbeddingModel();
	}

	@Bean
	EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel) throws IOException {
		// Normally, you would already have your embedding store filled with your data.
		// However, for the purpose of this demonstration, we will:

		// 1. Create an in-memory embedding store
		EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

		// 2. Load an example document ("Miles of Smiles" terms of use)
		ClassPathResourceLoader resourceLoader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get();
		Optional<URL> resource = resourceLoader.getResource("classpath:miles-of-smiles-terms-of-use.txt");
		Document document;
		if (resource.isPresent()) {
			document = loadDocument(resource.get().getPath(), new TextDocumentParser());
		} else {
			throw new FileNotFoundException("Terms of Use file not found!");
		}

		// 3. Split the document into segments 100 tokens each
		// 4. Convert segments into embeddings
		// 5. Store embeddings into embedding store
		// All this can be done manually, but we will use EmbeddingStoreIngestor to automate this:
		DocumentSplitter documentSplitter = DocumentSplitters.recursive(100, 0, new OpenAiTokenizer("gpt-3.5-turbo"));
		EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
																.documentSplitter(documentSplitter)
																.embeddingModel(embeddingModel)
																.embeddingStore(embeddingStore)
				.build();
		ingestor.ingest(document);

		return embeddingStore;
	}
}
