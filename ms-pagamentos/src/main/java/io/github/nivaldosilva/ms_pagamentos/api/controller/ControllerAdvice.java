package io.github.nivaldosilva.ms_pagamentos.api.controller;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.github.nivaldosilva.ms_pagamentos.exceptions.PagamentoDuplicadoException;
import io.github.nivaldosilva.ms_pagamentos.exceptions.PagamentoNotFoundException;
import io.github.nivaldosilva.ms_pagamentos.exceptions.StatusInvalidoException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    private ProblemDetail construirProblemDetailPadrao(HttpStatus status, String title, String detail, String typePath,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(typePath));
        problemDetail.setProperty("timestamp", Instant.now());

        if (request instanceof ServletWebRequest) {
            String requestUri = ((ServletWebRequest) request).getRequest().getRequestURI();
            problemDetail.setInstance(URI.create(requestUri));
        }
        return problemDetail;
    }

    @ExceptionHandler(PagamentoNotFoundException.class)
    public ResponseEntity<ProblemDetail> tratarErroPagamentoNaoEncontrado(PagamentoNotFoundException ex,
            WebRequest request) {
        log.error("Erro 404: Pagamento não encontrado", ex);
        ProblemDetail problemDetail = construirProblemDetailPadrao(
                HttpStatus.NOT_FOUND,
                "Recurso Não Encontrado",
                ex.getMessage(),
                "/errors/not-found",
                request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(StatusInvalidoException.class)
    public ResponseEntity<ProblemDetail> tratarErroStatusInvalido(StatusInvalidoException ex, WebRequest request) {
        log.error("Erro de Negócio: Status inválido na operação", ex);
        ProblemDetail problemDetail = construirProblemDetailPadrao(
                HttpStatus.FORBIDDEN,
                "Violação de Regra do Sistema",
                ex.getMessage(),
                "/errors/business-rule",
                request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
    }

    @ExceptionHandler(PagamentoDuplicadoException.class)
    public ResponseEntity<ProblemDetail> tratarErroPagamentoDuplicado(PagamentoDuplicadoException ex,
            WebRequest request) {
        log.error("Erro 409: Tentativa de pagamento duplicado", ex);
        ProblemDetail problemDetail = construirProblemDetailPadrao(
                HttpStatus.CONFLICT,
                "Pagamento Já Processado",
                ex.getMessage(),
                "/errors/duplicate",
                request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Erro de validação: {}", fieldErrors);

        ProblemDetail problemDetail = construirProblemDetailPadrao(
                HttpStatus.valueOf(status.value()),
                "Dados de Entrada Inválidos",
                "Um ou mais campos da requisição estão incorretos. Verifique os campos listados abaixo.",
                "/errors/validation",
                request);

        problemDetail.setProperty("erros_campos", fieldErrors);

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> tratarErroGenericoSistema(Exception ex, WebRequest request) {
        log.error("Erro não tratado (500):", ex);

        ProblemDetail problemDetail = construirProblemDetailPadrao(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Falha Interna do Sistema",
                "O sistema encontrou um erro inesperado. Por favor, tente novamente mais tarde.",
                "/errors/internal-server",
                request);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}