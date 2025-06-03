package com.example.filesave_back.entity.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="token_id")
    private Long id;

    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    private String IP;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
