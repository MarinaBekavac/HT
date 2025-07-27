package mb.projects.ht.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.validator.ValidActionId;

public class ValidActionIdValidator implements ConstraintValidator<ValidActionId, Integer> {
    @Override
    public boolean isValid(Integer actionId, ConstraintValidatorContext context) {
        return ActionEnum.isValidActionId(actionId);
    }
}
