package com.systex.hwajax.dao;

import com.systex.hwajax.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersDao extends JpaRepository<Users,Integer> {

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailAndPassword(String email, String password);
}
