package soloproject.seomoim.valid;

import org.junit.jupiter.api.Test;
import soloproject.seomoim.member.dto.MemberDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidTest {

//    /*Validator를 통해 유효성검증 test*/
//    @Test
//    void validateMember() {
//        MemberDto.Signup member = new MemberDto.Signup();
//        member.setEmail("dmswjd4015@naver.com");
//        member.setPassword("dkssud1!");
//        member.setConfirmPassword("dkssud1!");
//
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        Validator validator = validatorFactory.getValidator();
//        Set<ConstraintViolation<MemberDto.Signup>> violations = validator.validate(member);
//        for (ConstraintViolation<MemberDto.Signup> cv : new ArrayList<>(violations)) {
//            System.out.println(cv);
//        }
//
//        assertEquals(0, violations.size());
//    }

}
