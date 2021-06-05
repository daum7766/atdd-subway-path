package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Graph;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource());
        Station target = stationDao.findById(pathRequest.getTarget());

        ShortestPath shortestPath = new ShortestPath(graph());

        Path path = shortestPath.findPath(source, target);

        return PathResponse.of(path);
    }

    public Graph graph() {
        List<Section> sections = sectionDao.findAll();
        return new Graph(sections);
    }
}
