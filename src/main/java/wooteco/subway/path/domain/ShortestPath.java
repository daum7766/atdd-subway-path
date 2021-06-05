package wooteco.subway.path.domain;

import java.util.Optional;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.exceptions.SubWayException;
import wooteco.subway.exceptions.SubWayExceptionSet;
import wooteco.subway.station.domain.Station;

public class ShortestPath {

    private final Graph graph;

    public ShortestPath(Graph graph) {
        this.graph = graph;
    }

    public Path findPath(Station source, Station target) {
        validateStation(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(graph.getGraph());

        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
            .map(item -> new Path(item.getVertexList(), (int) item.getWeight()))
            .orElseThrow(
                () -> new SubWayException(SubWayExceptionSet.NOT_CONNECT_STATION_EXCEPTION));
    }

    private void validateStation(Station source, Station target) {
        graph.validateStation(source);
        graph.validateStation(target);
    }
}
