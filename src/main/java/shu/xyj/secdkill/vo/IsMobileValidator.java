package shu.xyj.secdkill.vo;

import org.thymeleaf.util.StringUtils;
import shu.xyj.secdkill.utils.ValidatorUtil;
import shu.xyj.secdkill.validator.IsMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        // 初始化的时候获取设置的值
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(s);
        } else {
            if (StringUtils.isEmpty(s)) {
                return true;
            }
            return ValidatorUtil.isMobile(s);
        }

    }
}
