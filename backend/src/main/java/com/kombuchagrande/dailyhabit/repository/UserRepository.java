package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
