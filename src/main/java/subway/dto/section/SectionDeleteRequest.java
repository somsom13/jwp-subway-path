package subway.dto.section;

import javax.validation.constraints.NotBlank;

public class SectionDeleteRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    private String stationName;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
