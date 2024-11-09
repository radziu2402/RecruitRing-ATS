package pl.pwr.recruitringcore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pwr.recruitringcore.model.entities.Application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByJobPostingOfferCode(UUID offerCode);

    Optional<Application> findByApplicationCode(UUID applicationCode);

    @Query("SELECT COUNT(a) FROM Application a JOIN a.jobPosting j JOIN j.recruiters r WHERE a.status = 'NEW' AND r.id = :recruiterId")
    int countNewCandidatesByRecruiterId(Long recruiterId);

    @Query("SELECT COALESCE(AVG(DATEDIFF(day, a.appliedAt, a.hiredAt)), 0) " +
            "FROM Application a JOIN a.jobPosting j JOIN j.recruiters r WHERE a.status = 'HIRED' AND r.id = :recruiterId")
    int calculateAvgTimeToHireByRecruiterId(Long recruiterId);

    @Query("SELECT COALESCE(SUM(CASE WHEN a.rating = 5 THEN 1 ELSE 0 END) * 100.0 / COUNT(a), 0) " +
            "FROM Application a JOIN a.jobPosting j JOIN j.recruiters r WHERE r.id = :recruiterId")
    int calculateCandidateRatingsPercentageByRecruiterId(Long recruiterId);

    @Query("SELECT COALESCE(SUM(CASE WHEN a.status = 'CV_REJECTED' THEN 1 ELSE 0 END) * 100.0 / COUNT(a), 0) " +
            "FROM Application a JOIN a.jobPosting j JOIN j.recruiters r WHERE r.id = :recruiterId")
    int calculateRejectedCandidatesPercentageByRecruiterId(Long recruiterId);

    @Query("SELECT COALESCE(SUM(CASE WHEN a.status = 'HIRED' THEN 1 ELSE 0 END) * 100.0 / COUNT(a), 0) " +
            "FROM Application a JOIN a.jobPosting j JOIN j.recruiters r WHERE r.id = :recruiterId")
    int calculateHiredCandidatesPercentageByRecruiterId(Long recruiterId);

}
