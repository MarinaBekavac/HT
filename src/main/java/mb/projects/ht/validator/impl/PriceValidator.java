package mb.projects.ht.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import mb.projects.ht.validator.ValidPrice;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<ValidPrice, Price> {

    @Override
    public boolean isValid(Price price, ConstraintValidatorContext context) {
        if (price == null) {
            return false; // Or false depending on your null policy
        }

        boolean hasOneTimePrice = price.getOneTimePrice() != null;
        //boolean hasRecurringPrices = price.getRecurringPrice() != null && !price.getRecurringPrices().isEmpty();
        boolean hasRecurringPrices = price.getRecurringPrice() != null;

        // Passes only if exactly one is defined
        return hasOneTimePrice ^ hasRecurringPrices;  // XOR operator
    }
}

