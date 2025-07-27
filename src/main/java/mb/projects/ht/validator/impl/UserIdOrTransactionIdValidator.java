package mb.projects.ht.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mb.projects.ht.request.DeleteItemFromCartRequest;
import mb.projects.ht.validator.ValidUserIdOrTransactionId;

public class UserIdOrTransactionIdValidator
        implements ConstraintValidator<ValidUserIdOrTransactionId, DeleteItemFromCartRequest> {

    @Override
    public boolean isValid(DeleteItemFromCartRequest request, ConstraintValidatorContext context) {
        return (request.getUserId() != null && !request.getUserId().isBlank()) ||
                (request.getTransactionId() != null && !request.getTransactionId().isBlank());
    }
//    @Override
//    public boolean isValid(DeleteItemFromCartRequest request, ConstraintValidatorContext context) {
//        boolean isUserId = request.getUserId()!=null && !request.getUserId().isEmpty() && !request.getUserId().isBlank();
//        boolean isTransactionId = request.getTransactionId()!=null && !request.getTransactionId().isEmpty()
//                && !request.getTransactionId().isBlank();
//        return isUserId ^ isTransactionId;
//    }
}
