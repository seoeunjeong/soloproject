package soloproject.seomoim.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginDto {

    @NotEmpty
    public String loginId;
    @NotEmpty
    public String password;

}
