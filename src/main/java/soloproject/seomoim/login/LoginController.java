package soloproject.seomoim.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.utils.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class LoginController {

    private final LoginService loginService;

/*    *//*뷰 랜더링 오류 loginForm 스트링 Long 타입 Convert 실패*//*
    @GetMapping("/loginForm")
    public String loginForm() {
        return "/members/loginFrom";
    }*/
////
//    @GetMapping("/access-denied")
//    public String accessDenied() {
//        return "access-denied";
//    }
//    @PostMapping("/login")
//    public String login() {
//        System.out.println("Login successfully!");
//        return "/";
//    }

    @GetMapping("/loginFrom")
    public String loginForm(){
        return "/members/loginFrom";
    }

    @PostMapping("/login")
    public String LoginMember(@Valid @ModelAttribute LoginDto loginDto, HttpServletRequest request) {

        Member loginMember = loginService.login(loginDto.loginId, loginDto.password);
        if (loginMember == null) {
            return "members/loginMember";
        }
        //로그인성공시에
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }
//    @PostMapping("/login")
//    public ResponseEntity LoginMember(@ResquestBody LoginDto loginDto, HttpServletRequest request) {
//
//        Member loginMember = loginService.login(loginDto.loginId, loginDto.password);
//        if (loginMember == null) {
//            return new ResponseEntity(ErrorResponse.of(ExceptionCode.NOT_ALLOW), HttpStatus.BAD_REQUEST);
//        }
//
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//
//
//        return new ResponseEntity<>(loginMember.getId(), HttpStatus.OK);
//
//    }
    @GetMapping("/logout")
    public String logoutMember(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session !=null){
            session.invalidate();
        }
            return "redirect:/";
    }


}
