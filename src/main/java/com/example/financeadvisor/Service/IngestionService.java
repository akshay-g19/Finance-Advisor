package com.example.financeadvisor.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class IngestionService {
    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);

    private final VectorStore vectorStore;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestAll() throws IOException{
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:knowledge-base/*.md");

        List<Document> allDocuments = new ArrayList<>();
        for (Resource resource : resources) {
            log.info("Ingesting file: {}", resource.getFilename());

            TextReader reader = new TextReader(resource);

            allDocuments.addAll(reader.get());
        }

        TokenTextSplitter splitter = new TokenTextSplitter();

        List<Document> chunks = splitter.apply(allDocuments);
        log.info("Total Chunk to embed and store: {}",chunks.size());

        vectorStore.add(chunks);
        log.info("✅ Ingestion complete! {} chunks stored in VectorDB.", chunks.size());
        return chunks.size();
    }
}
