package mb.projects.ht.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mb.projects.ht.validator.impl.UserIdOrTransactionIdValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdOrTransactionIdValidator.class)
public @interface ValidUserIdOrTransactionId {
    String message() default "userId or transactionId must be provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
