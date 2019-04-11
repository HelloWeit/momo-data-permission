package cn.weit.happymo.annotation.aspect;


import cn.weit.happymo.annotation.AuthData;
import cn.weit.happymo.annotation.PermissionMeta;
import cn.weit.happymo.constants.DataAuth;
import cn.weit.happymo.utils.MapUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author weitong
 */
@Component
@Aspect
@Order(1)
public class AuthDataAspect {
    private static final Logger LOG = LoggerFactory.getLogger(AuthDataAspect.class);

    @Value("${auth.data.permission}")
    private Boolean isOpen;

    @Pointcut(value = "@annotation(authData)", argNames = "authData")
    protected void checkAuthPoint(AuthData authData) {
    }

    @Before(value = "cn.weit.happymo.annotation.aspect.AuthDataAspect.checkAuthPoint(authData)", argNames = "pjp,authData")
    public void beforeCheckAuth(JoinPoint pjp, AuthData authData) throws Throwable {
//        if (!isOpen) {
//            LOG.debug("Data-auth not turn on");
//            return;
//        }
//        RequestHolder.addDataHolder();  添加数据开关
        PermissionMeta[] permissionMetas = authData.value();
        if (permissionMetas.length == 0) {
            LOG.error("Auth-aop not set meta");
            return;
        }

        for (PermissionMeta permissionMeta : permissionMetas) {
            if (permissionMeta.data().equals(DataAuth.PRODUCT)) {
                Object obj = pjp.getArgs()[permissionMeta.paramPos()];
                int productId = getServiceInstanceId(obj, permissionMeta.paramKey());
                if (permissionMeta.type().equals(Integer.class)) {
                    productId = (int) obj;
                } else {
                    Map<String, Object> map = MapUtils.obj2Map(obj);
                    Object object =  map.get(permissionMeta.paramKey());
                }
                //todo 处理
            }
        }


    }

    private int getServiceInstanceId(Object obj, String key) throws IllegalAccessException {
        if (obj.getClass().equals(Integer.class)) {
            return (int) obj;
        }
        Map<String, Object> map = MapUtils.obj2Map(obj);
        Object object =  map.get(key);
        return getServiceInstanceId(object, key);
    }

    @AfterReturning(value = "cn.weit.happymo.annotation.aspect.AuthDataAspect.checkAuthPoint(authData)", argNames = "authData")
    public void afterCheckAuth(AuthData authData) {
        //关闭开关
//        RequestHolder.removeDataHolder();
    }

}
