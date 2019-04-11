package cn.weit.happymo.interceptor;

import lombok.extern.java.Log;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author weitong
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args =
                {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args =
                {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
@Log
@Component
public class PermissionInterceptor implements Interceptor {
    // 相关规则限制可以放到redis里
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        if (!RequestHolder.isDataHolderExist()) {
//            return invocation.proceed();
//        }
        //查询前拦截
        Object result = invocation.proceed();
        // 查询后结果过滤
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
