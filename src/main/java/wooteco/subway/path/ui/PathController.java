package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPaths(@RequestParam Long source,
        @RequestParam Long target) {
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.findPath(pathRequest);
        return ResponseEntity.ok(pathResponse);
    }
}
