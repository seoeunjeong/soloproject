package soloproject.seomoim.moim.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PastLocalDateValidator implements ConstraintValidator<PastDate, LocalDateTime> {

    @Override
    public void initialize(PastDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();

        return  dateTime.truncatedTo(ChronoUnit.MINUTES).isAfter(now.truncatedTo(ChronoUnit.MINUTES));
    }

}

