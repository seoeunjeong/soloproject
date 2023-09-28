package soloproject.seomoim;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Region {
    private String city;
    private String gu;
    private String dong;

    protected Region(){
    }
    public Region(String city, String gu, String dong) {
        this.city = city;
        this.gu = gu;
        this.dong = dong;
    }
}
