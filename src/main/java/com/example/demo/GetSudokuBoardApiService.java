package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GetSudokuBoardApiService {
    
    private final WebClient webClient;
    
    public GetSudokuBoardApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://sudoku-api.vercel.app/api/dosuku").build();
    }

    public Mono<int[][]> fetchSudokuBoard(String url) {

        Mono<SudokuResponse> responseMono = fetchResponse(url);
        Mono<int[][]> boardMono = processResponseToBoard(responseMono);

        return boardMono;
    }

    private Mono<SudokuResponse> fetchResponse(String url) {
        return this.webClient.get().uri(url)
            .retrieve()
            .bodyToMono(SudokuResponse.class);
    }

    private Mono<int[][]> processResponseToBoard(Mono<SudokuResponse> responseMono) {
        return responseMono.map(SudokuResponse::toBoard);
    }
}