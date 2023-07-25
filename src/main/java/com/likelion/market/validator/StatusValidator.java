package com.likelion.market.validator;

import com.likelion.market.annotations.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusValidator implements ConstraintValidator<Status, String> {
    private List<String> statusList;
    @Override
    public void initialize(Status constraintAnnotation) {
        statusList = new ArrayList<>();
        statusList.addAll(Arrays.asList(constraintAnnotation.statusList()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.statusList.contains(value);
    }
}
