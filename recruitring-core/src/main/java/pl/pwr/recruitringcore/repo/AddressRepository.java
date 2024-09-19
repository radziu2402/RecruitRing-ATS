package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pwr.recruitringcore.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}