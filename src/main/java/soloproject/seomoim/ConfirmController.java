package soloproject.seomoim;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmController {

    @GetMapping("/confirm")
    public String confirm(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);
        System.out.println(" ============================ " );
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User = " + oAuth2User);
        System.out.println(" ============================ " );
        Object attribute = oAuth2User.getAttribute("name");
        System.out.println("attribute = " + attribute);
        System.out.println(" ============================ " );

        return "oh!";
    }
}
