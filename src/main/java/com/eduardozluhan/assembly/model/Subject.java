package com.eduardozluhan.assembly.model;

import com.eduardozluhan.assembly.controller.request.SubjectRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    private String title;
    private String details;

    public Subject(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public static Subject from(SubjectRequest request) {
        return new Subject(request.getTitle(), request.getDetails());
    }
}