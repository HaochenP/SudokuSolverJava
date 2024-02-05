package com.example.demo;

import java.util.Arrays;

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
        // Step 1: Fetch the SudokuResponse as a Mono
        Mono<SudokuResponse> responseMono = fetchResponse(url);

        // Step 2: Process the response to extract the board
        Mono<int[][]> boardMono = processResponseToBoard(responseMono);

        return boardMono;
    }

    private Mono<SudokuResponse> fetchResponse(String url) {
        return this.webClient.get().uri(url)
            .retrieve()
            .bodyToMono(SudokuResponse.class)
            .doOnNext(response -> System.out.println("Fetched Response: " + response));
    }

    private Mono<int[][]> processResponseToBoard(Mono<SudokuResponse> responseMono) {
        return responseMono.map(SudokuResponse::toBoard)
            .doOnSuccess(board -> System.out.println("Processed Board: " + Arrays.deepToString(board)));
    }
}