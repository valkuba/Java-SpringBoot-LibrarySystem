package pa165.project.notificationstatsservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pa165.project.notificationstatsservice.dto.PopularBookDto;
import pa165.project.notificationstatsservice.dto.StatsDto;
import pa165.project.notificationstatsservice.facade.StatsFacade;
import java.util.List;

/* *
 * REST controller for API endpoints related to statistics
 * */

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics API", description = "API for managing statistics")
public class StatsRestController {
    private final StatsFacade statsFacade;

    @GetMapping
    @Operation(summary = "Get all statistics")
    public ResponseEntity<List<StatsDto>> getAllStats() {
        return ResponseEntity.ok(statsFacade.getAllStats());
    }

    @GetMapping("/popular-books")
    @Operation(summary = "Get top N popular books")
    public ResponseEntity<List<PopularBookDto>> getTopNBooks(@RequestParam(defaultValue = "5") @Min(1) int limit) {
        return ResponseEntity.ok(statsFacade.getTopNBooks(limit));
    }
}
