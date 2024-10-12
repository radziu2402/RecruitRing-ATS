package pl.pwr.recruitringcore.dto;

import lombok.Getter;
import lombok.Setter;
import pl.pwr.recruitringcore.model.entities.Address;

import java.util.UUID;

@Getter
@Setter
public class ApplicationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UUID offerCode;
    private Address address;
}
