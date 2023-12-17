package soloproject.seomoim.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastLocalDateValidator implements ConstraintValidator<PastDate, LocalDate> {

    @Override
    public void initialize(PastDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate startedAt, ConstraintValidatorContext context) {
        LocalDate now = LocalDate.now();

       return startedAt.isAfter(now);
    }
}

