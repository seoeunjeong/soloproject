package soloproject.seomoim.KakaoApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentDto {

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;

    @JsonProperty("address_type")
    private String addressType;

    @JsonProperty("address")
    private AddressDto address;

    @JsonProperty("road_address")
    private AddressDto roadAddress;

    public String getRegion3DepthName() {
        if ("REGION_ADDR".equals(addressType) && address != null) {
            return address.getRegion3DepthName();
        } else if ("ROAD_ADDR".equals(addressType) && roadAddress != null) {
            return roadAddress.getRegion3DepthName();
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AddressDto {
        @JsonProperty("region_3depth_name")
        private String region3DepthName;
    }

}
