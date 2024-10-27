package pl.pwr.recruitringcore.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pwr.recruitringcore.model.entities.JobPosting;
import pl.pwr.recruitringcore.model.enums.WorkType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobPosting, Long> {
    Optional<JobPosting> findByOfferCode(UUID offerCode);

    @Query("SELECT j FROM JobPosting j WHERE " +
            "(:titleId IS NULL OR j.title.id = :titleId) AND " +
            "(:locationId IS NULL OR j.location.id = :locationId) AND " +
            "(:jobCategoryId IS NULL OR j.jobCategory.id = :jobCategoryId) AND " +
            "(:workType IS NULL OR j.workType = :workType)")
    Page<JobPosting> findByFilters(@Param("titleId") Long titleId,
                                   @Param("locationId") Long locationId,
                                   @Param("jobCategoryId") Long jobCategoryId,
                                   @Param("workType") WorkType workType,
                                   Pageable pageable);

    @Query("SELECT j FROM JobPosting j JOIN j.recruiters r WHERE r.id = :recruiterId")
    List<JobPosting> findByRecruiterId(@Param("recruiterId") Long recruiterId);

}
