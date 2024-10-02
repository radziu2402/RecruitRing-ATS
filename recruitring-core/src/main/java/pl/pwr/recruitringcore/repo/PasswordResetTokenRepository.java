package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByToken(String token);
}
