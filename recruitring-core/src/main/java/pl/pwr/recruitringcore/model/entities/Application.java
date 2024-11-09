package pl.pwr.recruitringcore.model.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "application_code", updatable = false, insertable = false)
    private UUID applicationCode;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "hired_at")
    private LocalDateTime hiredAt;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "rating")
    @Builder.Default
    private Integer rating = 0;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<Note> notes;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<Document> documents;
}
