package pl.pwr.recruitringcore.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.pwr.recruitringcore.model.entities.*;
import pl.pwr.recruitringcore.model.enums.ApplicationStatus;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobPostingRepository;
    @Autowired
    private JobCategoryRepository jobCategoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TitleRepository titleRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Test
    void shouldCountNewCandidatesByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(Recruiter.builder().firstName("Jane").lastName("Doe").position("Recruiter").dateOfBirth(LocalDate.now()).build());

        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("IT").build());

        Location location = locationRepository.save(Location.builder().name("Wroclaw").build());

        Title title = titleRepository.save(Title.builder().name("Java Developer").build());

        JobPosting jobPosting = JobPosting.builder()
                .recruiters(Set.of(recruiter))
                .workType(WorkType.STATIONARY)
                .jobCategory(jobCategory)
                .createdAt(LocalDate.now())
                .description("Job description")
                .location(location)
                .title(title)
                .build();
        jobPosting = jobPostingRepository.save(jobPosting);

        Candidate candidate = Candidate.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.com)")
                .phone("123456789")
                .build();
        candidate = candidateRepository.save(candidate);

        Application application = Application.builder()
                .jobPosting(jobPosting)
                .status(ApplicationStatus.NEW)
                .candidate(candidate)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // WHEN
        int count = applicationRepository.countNewCandidatesByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(1, count);
    }

    @Test
    void shouldCalculateAvgTimeToHireByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(
                Recruiter.builder().firstName("Jane").lastName("Doe").position("Recruiter").dateOfBirth(LocalDate.now()).build()
        );

        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("IT").build());
        Location location = locationRepository.save(Location.builder().name("Wroclaw").build());
        Title title = titleRepository.save(Title.builder().name("Java Developer").build());

        JobPosting jobPosting = JobPosting.builder()
                .recruiters(Set.of(recruiter))
                .workType(WorkType.STATIONARY)
                .jobCategory(jobCategory)
                .createdAt(LocalDate.now())
                .description("Job description")
                .location(location)
                .title(title)
                .build();
        jobPosting = jobPostingRepository.save(jobPosting);

        Candidate candidate = candidateRepository.save(
                Candidate.builder().firstName("John").lastName("Doe").email("email@email.com").phone("123456789").build()
        );

        Application application = Application.builder()
                .jobPosting(jobPosting)
                .status(ApplicationStatus.HIRED)
                .candidate(candidate)
                .appliedAt(LocalDateTime.now().minusDays(5))
                .hiredAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // WHEN
        int avgTimeToHire = applicationRepository.calculateAvgTimeToHireByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(5, avgTimeToHire);
    }

    @Test
    void shouldCalculateCandidateRatingsPercentageByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(
                Recruiter.builder().firstName("Jane").lastName("Doe").position("Recruiter").dateOfBirth(LocalDate.now()).build()
        );

        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("IT").build());
        Location location = locationRepository.save(Location.builder().name("Wroclaw").build());
        Title title = titleRepository.save(Title.builder().name("Java Developer").build());

        JobPosting jobPosting = JobPosting.builder()
                .recruiters(Set.of(recruiter))
                .workType(WorkType.STATIONARY)
                .jobCategory(jobCategory)
                .createdAt(LocalDate.now())
                .description("Job description")
                .location(location)
                .title(title)
                .build();
        jobPosting = jobPostingRepository.save(jobPosting);

        Candidate candidate = candidateRepository.save(
                Candidate.builder().firstName("John").lastName("Doe").email("email@email.com").phone("123456789").build()
        );

        Application application = Application.builder()
                .jobPosting(jobPosting)
                .status(ApplicationStatus.NEW)
                .rating(5)
                .candidate(candidate)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // WHEN
        int percentage = applicationRepository.calculateCandidateRatingsPercentageByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(100, percentage);
    }

    @Test
    void shouldCalculateRejectedCandidatesPercentageByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(
                Recruiter.builder().firstName("Jane").lastName("Doe").position("Recruiter").dateOfBirth(LocalDate.now()).build()
        );

        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("IT").build());
        Location location = locationRepository.save(Location.builder().name("Wroclaw").build());
        Title title = titleRepository.save(Title.builder().name("Java Developer").build());

        JobPosting jobPosting = JobPosting.builder()
                .recruiters(Set.of(recruiter))
                .workType(WorkType.REMOTE)
                .jobCategory(jobCategory)
                .createdAt(LocalDate.now())
                .description("Job description")
                .location(location)
                .title(title)
                .build();
        jobPosting = jobPostingRepository.save(jobPosting);

        Candidate candidate = candidateRepository.save(
                Candidate.builder().firstName("John").lastName("Doe").email("email@email.com").phone("123456789").build()
        );

        Application application = Application.builder()
                .jobPosting(jobPosting)
                .status(ApplicationStatus.CV_REJECTED)
                .candidate(candidate)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // WHEN
        int percentage = applicationRepository.calculateRejectedCandidatesPercentageByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(100, percentage);
    }

    @Test
    void shouldCalculateHiredCandidatesPercentageByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(
                Recruiter.builder().firstName("Jane").lastName("Doe").position("Recruiter").dateOfBirth(LocalDate.now()).build()
        );

        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("IT").build());
        Location location = locationRepository.save(Location.builder().name("Wroclaw").build());
        Title title = titleRepository.save(Title.builder().name("Java Developer").build());

        JobPosting jobPosting = JobPosting.builder()
                .recruiters(Set.of(recruiter))
                .workType(WorkType.REMOTE)
                .jobCategory(jobCategory)
                .createdAt(LocalDate.now())
                .description("Job description")
                .location(location)
                .title(title)
                .build();
        jobPosting = jobPostingRepository.save(jobPosting);

        Candidate candidate = candidateRepository.save(
                Candidate.builder().firstName("John").lastName("Doe").email("email@email.com").phone("123456789").build()
        );

        Application application = Application.builder()
                .jobPosting(jobPosting)
                .status(ApplicationStatus.HIRED)
                .candidate(candidate)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        // WHEN
        int percentage = applicationRepository.calculateHiredCandidatesPercentageByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(100, percentage);
    }

}
