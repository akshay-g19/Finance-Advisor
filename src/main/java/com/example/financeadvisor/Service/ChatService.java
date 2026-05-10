package com.example.financeadvisor.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final QuestionAnswerAdvisor questionAnswerAdvisor;

    private static final String SYSTEM_PROMPT = """
            You are a helpful personal finance advisor for Indian investors.
                        Answer questions ONLY based on the context provided to you.
                        If the answer is not in the context, say "I don't have information on that topic yet."
                        Keep answers concise, practical, and easy to understand.
                        Always mention amounts in Indian Rupees (₹) where applicable.
            """;

    public ChatService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.questionAnswerAdvisor = QuestionAnswerAdvisor.builder(this.vectorStore)
                .searchRequest(SearchRequest.builder()
                        .topK(4)
                        .similarityThreshold(0.5)
                        .build())
                .build();
    }

    public String ask(String question) {
        log.info("Processing question: {}", question);

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                .advisors(questionAnswerAdvisor)
                .call()
                .content();
    }

    public Flux<String> askStream(String question) {
        log.info("Processing streaming question: {}", question);

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                .advisors(questionAnswerAdvisor)
                .stream()
                .content()
                .map(chunk -> "data: " + chunk + "\n\n");
    }
}
