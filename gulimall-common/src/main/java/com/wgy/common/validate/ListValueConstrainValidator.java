package com.wgy.common.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author WGY
 * @Date 2021/3/7
 * @Time 21:17
 * To change this template use File | Settings | File Templates.
 **/
public class ListValueConstrainValidator implements ConstraintValidator<ListValue, Integer> {

    private final Set<Integer> set = new HashSet<>();

    /**
     * 初始化方法
     * @param constraintAnnotation 详细信息
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        final int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }

    /**
     * 判断是否校验成功
     * @param value 需要校验的值
     * @param context 校验上下文的环境信息
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
