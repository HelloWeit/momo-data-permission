package cn.weit.happymo.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface PermissionMeta {
    Class<?> type();
    int paramPos() default 0;
    DataAuthKey dataAuthKey();
}
