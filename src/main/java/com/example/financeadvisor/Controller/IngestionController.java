package com.example.financeadvisor.Controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeadvisor.Service.IngestionService;

@RestController
@RequestMapping("/api/finance")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<Map<String, Object>> ingest() throws IOException{
        int count = ingestionService.ingestAll();
        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "chunkStored", count,
                "message", "Knowledge based integrated into PGVector successfully!"
        ));
    }
}
