package com.remake.poki.repository;

import com.remake.poki.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = """
                select c from Card c
                join UserCard uc on c.id = uc.cardId
                where uc.userId = :userId
            """)
    List<Card> findAllByUserId(Long userId);
}
