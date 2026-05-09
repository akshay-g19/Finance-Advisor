package com.example.financeadvisor.dto;

public record FinanceResponse(
        String question,
        String answer,
        String status
) {}
