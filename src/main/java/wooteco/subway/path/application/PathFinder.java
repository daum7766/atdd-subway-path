package wooteco.subway.path.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathDto;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.station.domain.Station;

@Component
public class PathFinder {

    private final PathAlgorithms pathAlgorithms;

    public PathFinder() {
        pathAlgorithms = new DijkstraAlgorithm();
    }

    public Path findPath(List<Station> stations, List<Section> sections, PathRequest pathRequest) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(graph, stations);
        addEdge(graph, sections);

        PathDto pathDto = pathAlgorithms.findPath(graph, pathRequest);
        Map<Long, Station> idToStation = idToStation(stations);
        List<Station> pathStations = getPathStations(pathDto, idToStation);

        return new Path(pathStations, pathDto.getDistance());
    }

    private void addVertex(WeightedMultigraph<Long, DefaultWeightedEdge> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    private void addEdge(WeightedMultigraph<Long, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            Long upStationId = section.getUpStation().getId();
            Long downStationId = section.getDownStation().getId();
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), section.getDistance());
        }
    }

    private Map<Long, Station> idToStation(List<Station> stations) {
        Map<Long, Station> idToStation = new HashMap<>();
        for (Station station : stations) {
            idToStation.put(station.getId(), station);
        }
        return idToStation;
    }

    private List<Station> getPathStations(PathDto pathDto, Map<Long, Station> idToStation) {
        List<Station> stations = new ArrayList<>();
        for (Long id : pathDto.getPaths()) {
            stations.add(idToStation.get(id));
        }
        return stations;
    }
}
