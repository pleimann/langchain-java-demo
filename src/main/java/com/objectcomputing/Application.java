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
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Command(description = "...", mixinStandardHelpOptions = true)
public class Application implements Runnable {
	@Inject
	CustomerSupportAgent agent;

	public static void main(String[] args) {
		PicocliRunner.run(Application.class, args);
	}

	/**
	 * Run CustomerSupportApplicationTest to see simulated conversation with customer support agent
	 */
	public void run() {
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("User: ");
				String userMessage = scanner.nextLine();

				if ("exit".equalsIgnoreCase(userMessage)) {
					System.exit(0);
				}

				String agentMessage = agent.chat(userMessage);
				System.out.println("Agent: " + agentMessage);
			}
		}
	}
}
