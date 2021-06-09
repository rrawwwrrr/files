package ru.lanit.rnd.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lanit.rnd.files.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}