package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
