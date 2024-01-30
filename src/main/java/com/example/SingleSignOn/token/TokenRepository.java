package com.example.SingleSignOn.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "SELECT t from Token")
    List<Token> findAllToken();

    @Query(value = "select t from Token t inner join User on t.user = u.id where u.id =:id and (t.expired = false or t.revoked = false)")
    List<Token> findValidTokensByUser(Integer id);

    @Query(value = "SELECT t FROM Token t INNER JOIN User u ON t.user = u.id WHERE u.id = :id AND (t.expired = false OR t.revoked = false) ORDER BY t.createdAt DESC")
    Optional<Token> findValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

}