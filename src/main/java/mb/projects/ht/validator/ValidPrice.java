package mb.projects.ht.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mb.projects.ht.validator.impl.PriceValidator;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
@Documented
public @interface ValidPrice {
    String message() default "OneTimePrice must be set or at least one recurring price must be defined. Not both";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
