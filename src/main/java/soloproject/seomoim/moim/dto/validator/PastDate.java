package soloproject.seomoim.moim.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastLocalDateValidator.class)
public @interface PastDate {
    String message() default "날짜는 현재 날짜 이후만 등록 가능 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

