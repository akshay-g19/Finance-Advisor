package com.example.financeadvisor.Controller;

import com.example.financeadvisor.Service.ChatService;
import com.example.financeadvisor.Service.IngestionService;
import com.example.financeadvisor.dto.FinanceRequest;
import com.example.financeadvisor.dto.FinanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    private static final Logger log = LoggerFactory.getLogger(FinanceController.class);

    private final ChatService chatService;
    private final IngestionService ingestionService;

    public FinanceController(ChatService chatService, IngestionService ingestionService) {
        this.chatService = chatService;
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ask")
    public ResponseEntity<FinanceResponse> ask(@RequestBody FinanceRequest request) {
        log.info("Received question: {}", request.question());

        String answer = chatService.ask(request.question());

        return ResponseEntity.ok(new FinanceResponse(
                request.question(),
                answer,
                "success"
        ));
    }

    @PostMapping(value = "/ask-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String>  askStream(@RequestBody FinanceRequest request) {
        log.info("Received question: {}", request.question());
        return chatService.askStream(request.question());
    }
}
