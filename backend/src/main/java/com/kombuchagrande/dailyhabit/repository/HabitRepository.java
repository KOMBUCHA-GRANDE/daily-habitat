package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
