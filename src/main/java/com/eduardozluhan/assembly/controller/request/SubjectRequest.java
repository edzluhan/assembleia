package com.eduardozluhan.assembly.controller.request;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class SubjectRequest {
    @NotNull
    String title;
    String details;
}