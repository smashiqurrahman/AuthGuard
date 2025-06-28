package com.ashiq.AuthGuard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<R> {
    private boolean success = true;
    private String message;
    private String errorMessage;
    private Map<String, R> model;
    private List<R> items;
    private R obj;
    private Page page;
    private Set<String> ItemSet;
    private Map<?,?> map;
}
