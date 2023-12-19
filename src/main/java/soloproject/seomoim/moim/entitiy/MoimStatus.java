package soloproject.seomoim.moim.entitiy;

import lombok.Getter;

public enum MoimStatus {
    MOIM_ClOSE("모집마감"), MOIM_OPEN("모집중"), MOIM_FINISH("모임종료");

    @Getter
    private final String status;

    MoimStatus(String status) {

        this.status = status;
    }
}
