package com.wgy.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author WGY
 * @Date 2021/3/7
 * @Time 21:11
 * To change this template use File | Settings | File Templates.
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
// 说明了Annotation所修饰的对象范围
@Retention(RUNTIME) // 注解按生命周期来划分可分为3类：class（class文件） source（源文件） runtime（jvm加载完仍然存在）
@Documented // 做标识
@Constraint(validatedBy = {ListValueConstrainValidator.class})
public @interface ListValue {

    // @Constraint(validatedBy = {ListValueConstrainValidator.class})
    // 代表注解的处理逻辑是 使用后面这个class来进行校验
    String message() default "{com.wgy.common.validate.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};
}
