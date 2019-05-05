package cn.weit.happymo.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CountSpliceSql {
    DataAuthKey[] dataAuthKey();
}
