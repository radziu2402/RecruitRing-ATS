package pl.pwr.recruitringcore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "street", nullable = false, length = 50)
    private String street;

    @Column(name = "\"post code\"", length = 10)
    private String postCode;

    @Column(name = "\"street number\"")
    private Integer streetNumber;

    @Column(name = "\"flat number\"")
    private Integer flatNumber;

}