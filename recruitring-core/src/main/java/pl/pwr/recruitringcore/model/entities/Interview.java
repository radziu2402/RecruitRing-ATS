package pl.pwr.recruitringcore.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.recruitringcore.model.enums.InterviewStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "location", nullable = true, length = 255)
    private String location;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
