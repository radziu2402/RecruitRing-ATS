package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String username);
    Optional<User> findByEmail(String email);
}