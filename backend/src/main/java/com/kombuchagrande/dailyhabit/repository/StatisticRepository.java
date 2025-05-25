package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
