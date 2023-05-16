package subway.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.exception.StationNotFoundException;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findByName(String name) {
        Optional<StationEntity> foundStationEntity = stationDao.findByName(name);
        if (foundStationEntity.isEmpty()) {
            throw new StationNotFoundException();
        }
        return foundStationEntity.get().toDomain();
    }

    public boolean isDuplicateStation(Station station) {
        return stationDao.existsByName(station.getName());
    }

    public Long save(Station station) {
        return stationDao.insert(new StationEntity(station.getName()));
    }

    public List<Station> findAll() {
        return stationDao.findAll()
                .stream()
                .map(StationEntity::toDomain)
                .collect(Collectors.toList());
    }

    public Station update(Station station) {
        Optional<StationEntity> optionalStationEntity = stationDao.findById(station.getId());
        if (optionalStationEntity.isEmpty()) {
            throw new StationNotFoundException();
        }
        StationEntity stationEntity = optionalStationEntity.get();
        stationEntity.updateName(station.getName());
        stationDao.update(stationEntity);
        return findById(station.getId());
    }

    public Station findById(Long id) {
        Optional<StationEntity> optionalStationEntity = stationDao.findById(id);
        if (optionalStationEntity.isEmpty()) {
            throw new StationNotFoundException();
        }
        return optionalStationEntity.get().toDomain();
    }

    public void delete(Station station) {
        stationDao.deleteById(station.getId());
    }
}
