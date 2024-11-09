package pl.pwr.recruitringcore.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pwr.recruitringcore.dto.DashboardStatsDTO;

@RequestMapping("/api/v1/dashboard")
public interface DashboardApi {

    @GetMapping("/stats")
    ResponseEntity<DashboardStatsDTO> getDashboardStats();
}
