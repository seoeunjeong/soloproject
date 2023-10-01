package soloproject.seomoim.login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ErrorResponse;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.utils.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity LoginMember(@RequestBody LoginDto loginDto, HttpServletRequest request) {

        Member loginMember = loginService.login(loginDto.loginId, loginDto.password);
        if (loginMember == null) {
            return new ResponseEntity(ErrorResponse.of(ExceptionCode.NOT_ALLOW), HttpStatus.BAD_REQUEST);
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);


        return new ResponseEntity<>(loginMember.getId(), HttpStatus.OK);

    }
    @PostMapping("/logout")
    public void logoutMember(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session !=null){
            session.invalidate();
        }
    }

}
