package soloproject.seomoim.member.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Region {

    /*주소는 지번주소로 받아 읍면동으로 필터링 한다.*/
    private String zipCode;
    private String address;
    private String DetailAddress;

    protected Region(){
    }

    public Region(String zipCode, String address, String detailAddress) {
        this.zipCode = zipCode;
        this.address = address;
        DetailAddress = detailAddress;
    }
}
