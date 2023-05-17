package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.IllegalStationException;
import subway.exception.StationNotFoundException;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder from(List<Sections> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initializeWithSections(sections, graph);
        return new PathFinder(graph);
    }

    private static void initializeWithSections(List<Sections> totalSections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Sections sections : totalSections) {
            addSectionsToGraph(sections.getSections(), graph);
        }
    }

    private static void addSectionsToGraph(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            Station source = section.getStartStation();
            Station dest = section.getEndStation();
            graph.addVertex(source);
            graph.addVertex(dest);
            graph.setEdgeWeight(graph.addEdge(source, dest), section.getDistance());
        }
    }

    public Path calculateShortestPath(Station source, Station dest) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        validateTargetInPath(source, dest);
        return Path.of(getShortestPathStations(shortestPath, source, dest), shortestPath.getPathWeight(source, dest));
    }

    private void validateTargetInPath(Station source, Station dest) {
        if (graph.containsVertex(source) && graph.containsVertex(dest)) {
            return;
        }
        throw new StationNotFoundException();
    }

    private List<Station> getShortestPathStations(DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath,
                                                  Station source,
                                                  Station dest) {
        GraphPath<Station, DefaultWeightedEdge> foundPath = shortestPath.getPath(source, dest);
        if (foundPath == null) {
            throw new IllegalStationException("요청한 출발역과 도착역 사이의 경로가 없습니다.");
        }
        return foundPath.getVertexList();
    }

}