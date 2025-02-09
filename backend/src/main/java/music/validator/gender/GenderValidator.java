package music.validator.gender;

import music.constant.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<GenderValid, Gender> {
    @Override
    public void initialize(GenderValid constraintAnnotation) {}

    @Override
    public boolean isValid(Gender gender, ConstraintValidatorContext constraintValidatorContext) {
        return gender.name().matches("^(?)(MALE|FEMALE|OTHER)$");
    }
}

