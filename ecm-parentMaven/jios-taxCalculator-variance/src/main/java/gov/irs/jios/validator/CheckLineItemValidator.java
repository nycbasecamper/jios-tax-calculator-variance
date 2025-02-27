package gov.irs.jios.validator;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.irs.jios.model.Form;
import gov.irs.jios.model.JiosBody;
import gov.irs.jios.model.LineItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckLineItemValidator implements ConstraintValidator<CheckLineItem, JiosBody> {

    @Override
    public boolean isValid(JiosBody body, ConstraintValidatorContext context) {
        List<Form> forms = body.getForms();
        boolean isValid = true;
        String calcType = null != body.getCalcType() ? body.getCalcType() : StringUtils.EMPTY;
        Boolean validateEmpty = calcType.equalsIgnoreCase("penalty") || calcType.equalsIgnoreCase("interest") ? Boolean.FALSE : Boolean.TRUE;

        context.disableDefaultConstraintViolation();
        for (Form form : forms) {
            List<LineItem> lines = form.getLineItems();
            if (null != lines) {

                for (LineItem line : lines) {
                    if (validateEmpty && null != line.getPerReturnValueTxt() && StringUtils.isEmpty(line.getPerReturnValueTxt())) {
                        context.buildConstraintViolationWithTemplate(
                                        "Line value cannot be null or empty #" + line.getLineNameTxt() + "#" + line.getLineItemReferenceKeyId())
                                .addConstraintViolation();
                        isValid = false;
                    } else if (validateEmpty && null == line.getPerReturnValueTxt()) {
                        context.buildConstraintViolationWithTemplate(
                                        "Line value cannot be null or empty #" + line.getLineNameTxt() + "#" + line.getLineItemReferenceKeyId())
                                .addConstraintViolation();
                        isValid = false;
                    } else if (StringUtils.isNotEmpty(line.getPerReturnValueTxt())) {
                        try {
                            BigDecimal lineValue = new BigDecimal(line.getPerReturnValueTxt());
                            if (lineValue.compareTo(new BigDecimal("999999999999999.99999")) > 0) {
                                context.buildConstraintViolationWithTemplate(
                                                "Line value exceeded maximum limit 999999999999999.99999 #" +
                                                        line.getLineNameTxt() + "#" + line.getPerReturnValueTxt() + "#" + line.getLineItemReferenceKeyId())
                                        .addConstraintViolation();
                                isValid = false;
                            } else if (line.getPerReturnValueTxt().contains(".")
                                    && line.getPerReturnValueTxt().split("\\.")[1].length() > 5) {
                                context.buildConstraintViolationWithTemplate(
                                                "The total number of decimal digits in the line value exceeds maximum of five decimal places. #"
                                                        + line.getLineNameTxt() + "#" + line.getPerReturnValueTxt() + "#" + line.getLineItemReferenceKeyId())
                                        .addConstraintViolation();
                                isValid = false;
                            }
                        } catch (NumberFormatException e) {
                            // Do Nothing
                        }
                    }

                }
            }
        }
        return isValid;
    }
}
