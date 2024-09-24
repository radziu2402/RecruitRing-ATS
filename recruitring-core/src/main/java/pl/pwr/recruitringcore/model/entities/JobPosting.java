package pl.pwr.recruitringcore.model.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_postings")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "job_posting_requirements",
            joinColumns = @JoinColumn(name = "job_posting_id"),
            inverseJoinColumns = @JoinColumn(name = "requirement_id")
    )
    private Set<Requirement> requirements;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "work_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "job_posting_recruiters",
            joinColumns = @JoinColumn(name = "job_posting_id"),
            inverseJoinColumns = @JoinColumn(name = "recruiter_id")
    )
    private Set<Recruiter> recruiters;

    @OneToMany(mappedBy = "jobPosting")
    private Set<Application> applications;

    @Column(name = "job_category", nullable = false, length = 100)
    private String jobCategory;
}
