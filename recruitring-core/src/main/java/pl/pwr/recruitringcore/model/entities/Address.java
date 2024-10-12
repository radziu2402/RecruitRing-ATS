package pl.pwr.recruitringcore.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "\"post code\"", nullable = false, length = 10)
    private String postCode;

    @Column(name = "street", length = 50)
    private String street;

    @Column(name = "\"street number\"")
    private Integer streetNumber;

    @Column(name = "\"flat number\"")
    private Integer flatNumber;

}