package com.followorkback.followorkback.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    protected LocalDateTime timestamps;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developperMessage;
    protected Collection<?> data;
}
