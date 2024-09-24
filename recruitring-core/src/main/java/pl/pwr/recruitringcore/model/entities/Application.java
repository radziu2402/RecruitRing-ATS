package pl.pwr.recruitringcore.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Interview interview;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<Note> notes;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<Document> documents;
}
