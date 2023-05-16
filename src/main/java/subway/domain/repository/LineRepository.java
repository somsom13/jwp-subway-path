package subway.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.dto.SectionDto;
import subway.dao.entity.LineEntity;
import subway.domain.Line;
import subway.domain.Sections;
import subway.exception.LineNotFoundException;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line findById(Long id) {
        Optional<LineEntity> optionalLineEntity = lineDao.findById(id);
        if (optionalLineEntity.isEmpty()) {
            throw new LineNotFoundException();
        }
        LineEntity lineEntity = optionalLineEntity.get();
        return new Line(id, lineEntity.getName(), lineEntity.getColor(), findSectionsInLine(id));
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(entity -> findById(entity.getId()))
                .collect(Collectors.toList());
    }

    private Sections findSectionsInLine(Long lineId) {
        List<SectionDto> foundSections = sectionDao.findAllSectionsByLineId(lineId);
        return new Sections(foundSections.stream()
                .map(SectionDto::toDomain)
                .collect(Collectors.toList()));
    }

    public long save(Line line) {
        return lineDao.insert(new LineEntity(line.getName(), line.getColor()));
    }

    public boolean isDuplicateLine(Line line) {
        return lineDao.existsByNameAndColor(line.getColor(), line.getName());
    }

    public List<Line> findAllWithNoSections() {
        return lineDao.findAll()
                .stream()
                .map(entity -> new Line(entity.getId(), entity.getName(), entity.getColor()))
                .collect(Collectors.toList());
    }

    public Line findByIdWithNoSections(Long id) {
        Optional<LineEntity> optionalLineEntity = lineDao.findById(id);
        if (optionalLineEntity.isEmpty()) {
            throw new LineNotFoundException();
        }
        LineEntity lineEntity = optionalLineEntity.get();
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Line update(Line line) {
        Optional<LineEntity> optionalLineEntity = lineDao.findById(line.getId());
        if (optionalLineEntity.isEmpty()) {
            throw new LineNotFoundException();
        }
        LineEntity lineEntity = optionalLineEntity.get();
        lineEntity.updateNameAndColor(line.getName(), line.getColor());
        lineDao.update(lineEntity);
        return findByIdWithNoSections(line.getId());
    }

    public void delete(Line line) {
        lineDao.deleteById(line.getId());
    }
}
