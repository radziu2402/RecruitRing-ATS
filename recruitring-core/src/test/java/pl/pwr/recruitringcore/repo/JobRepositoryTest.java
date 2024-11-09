package pl.pwr.recruitringcore.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.pwr.recruitringcore.model.entities.JobCategory;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.entities.Location;
import pl.pwr.recruitringcore.model.entities.Recruiter;
import pl.pwr.recruitringcore.model.entities.Title;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Test
    void shouldFindByFilters() {
        // GIVEN
        Title title = titleRepository.save(Title.builder().name("Software Engineer").build());
        Location location = locationRepository.save(Location.builder().name("New York").build());
        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("IT").build());

        JobPosting jobPosting = JobPosting.builder()
                .title(title)
                .location(location)
                .jobCategory(jobCategory)
                .workType(WorkType.REMOTE)
                .description("A job description")
                .createdAt(LocalDate.now())
                .build();
        jobRepository.save(jobPosting);

        // WHEN
        Page<JobPosting> result = jobRepository.findByFilters(
                title.getId(),
                location.getId(),
                jobCategory.getId(),
                WorkType.REMOTE,
                PageRequest.of(0, 10)
        );

        // THEN
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(Recruiter.builder()
                .firstName("Jane")
                .lastName("Doe")
                .position("Recruiter")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .build());

        Title title = titleRepository.save(Title.builder().name("Backend Developer").build());
        Location location = locationRepository.save(Location.builder().name("Los Angeles").build());
        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("Engineering").build());

        JobPosting jobPosting = JobPosting.builder()
                .title(title)
                .location(location)
                .jobCategory(jobCategory)
                .workType(WorkType.STATIONARY)
                .recruiters(Set.of(recruiter))
                .description("Job for Backend Developer")
                .createdAt(LocalDate.now())
                .build();
        jobRepository.save(jobPosting);

        // WHEN
        List<JobPosting> result = jobRepository.findByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(1, result.size());
    }

    @Test
    void shouldCountOpenRecruitmentsByRecruiterId() {
        // GIVEN
        Recruiter recruiter = recruiterRepository.save(Recruiter.builder()
                .firstName("John")
                .lastName("Smith")
                .position("HR Specialist")
                .dateOfBirth(LocalDate.of(1990, 10, 20))
                .build());

        Title title = titleRepository.save(Title.builder().name("Frontend Developer").build());
        Location location = locationRepository.save(Location.builder().name("San Francisco").build());
        JobCategory jobCategory = jobCategoryRepository.save(JobCategory.builder().name("Tech").build());

        JobPosting jobPosting1 = JobPosting.builder()
                .title(title)
                .location(location)
                .jobCategory(jobCategory)
                .workType(WorkType.REMOTE)
                .recruiters(Set.of(recruiter))
                .description("Frontend Developer position")
                .createdAt(LocalDate.now())
                .build();
        jobRepository.save(jobPosting1);

        JobPosting jobPosting2 = JobPosting.builder()
                .title(title)
                .location(location)
                .jobCategory(jobCategory)
                .workType(WorkType.REMOTE)
                .recruiters(Set.of(recruiter))
                .description("React Developer position")
                .createdAt(LocalDate.now())
                .build();
        jobRepository.save(jobPosting2);

        // WHEN
        int openRecruitments = jobRepository.countOpenRecruitmentsByRecruiterId(recruiter.getId());

        // THEN
        assertEquals(2, openRecruitments);
    }
}
