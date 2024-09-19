package pl.pwr.recruitringcore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
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

    @Column(name = "requirements", nullable = false, columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "job_posting_recruiters",
            joinColumns = @JoinColumn(name = "job_posting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> recruiters;

    @OneToMany(mappedBy = "jobPosting")
    private Set<Application> applications;
}
