package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderTypeAndProviderId(ProviderType provider, String providerId);

}
