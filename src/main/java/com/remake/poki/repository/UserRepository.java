package com.remake.poki.repository;

import com.remake.poki.dto.UserRoomDTO;
import com.remake.poki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUser(String user);

    Optional<User> findById(Long id);

    @Query(value = "SELECT new com.remake.poki.dto.UserRoomDTO(u, cp, p, ep) " +
            "FROM Pet p " +
            "JOIN User u ON u.petId = p.id AND u.id = :userId "+
            "JOIN EnemyPet ep ON ep.idPet = :enemyPetId "+
            "LEFT JOIN CountPass cp ON cp.idUser = u.id AND ep.idPet = cp.idPet" )
    UserRoomDTO findInfoRoom(Long userId, Long enemyPetId);

    @Query(value = "SELECT new com.remake.poki.dto.UserRoomDTO(u, null, p, ep) " +
            "FROM Pet p " +
            "JOIN User u ON u.petId = p.id AND u.id = :userId "+
            "JOIN EnemyPet ep ON ep.idPet = :enemyPetId WHERE p.flagLegend = true")
    UserRoomDTO findInfoRoomHT(Long userId, Long enemyPetId);
}