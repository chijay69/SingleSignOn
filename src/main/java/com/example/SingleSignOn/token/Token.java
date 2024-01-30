package com.example.SingleSignOn.token;

import com.example.SingleSignOn.models.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public int Id;

    @Column(name = "token", unique = true, length = 512)
    public String token;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
