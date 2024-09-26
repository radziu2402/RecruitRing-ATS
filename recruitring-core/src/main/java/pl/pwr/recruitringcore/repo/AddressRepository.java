package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}