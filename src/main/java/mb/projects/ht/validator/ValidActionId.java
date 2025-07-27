package mb.projects.ht.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mb.projects.ht.validator.impl.ValidActionIdValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidActionIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidActionId {
    String message() default "Invalid action ID. Valid values are: 1 (ADD), 2 (DELETE), 3 (MODIFY)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
