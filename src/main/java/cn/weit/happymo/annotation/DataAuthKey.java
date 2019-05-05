package cn.weit.happymo.annotation;


import cn.weit.happymo.constants.DataAuth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface DataAuthKey {
    DataAuth data();
    String paramKey();
}
