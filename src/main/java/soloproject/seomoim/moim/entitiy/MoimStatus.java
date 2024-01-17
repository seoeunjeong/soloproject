package soloproject.seomoim.moim.entitiy;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MoimStatus {
    MOIM_ClOSE("모집마감"), MOIM_OPEN("모집중"), MOIM_FINISH("모임종료");

    @Getter
    @JsonValue
    private final String status;

    MoimStatus(String status) {

        this.status = status;
    }
}
