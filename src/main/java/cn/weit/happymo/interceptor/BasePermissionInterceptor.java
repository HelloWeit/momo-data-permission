package cn.weit.happymo.interceptor;

import cn.weit.happymo.annotation.CountSpliceSql;
import cn.weit.happymo.utils.MapUtils;
import com.google.common.collect.Lists;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author weitong
 */
public abstract class BasePermissionInterceptor implements Interceptor {
    private static final Logger LOG = LoggerFactory.getLogger(BasePermissionInterceptor.class);

    private static final String INSERT_NAME = "insert";

    public static final String WHERE_NAME = "where";


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        filterParam(null, null, null, args);
        Object result = invocation.proceed();
        if (!(result instanceof List)) {
            if (isJavaClass(result.getClass())) {
                return result;
            }
            return filterResult(result, null, null);
        }
        ArrayList resultList = (ArrayList) result;
        List<Object> objects = Lists.newArrayList();
        for (Object obj : resultList) {
            if (isJavaClass(obj.getClass())) {
                objects.add(obj);
                continue;
            }
            Object finalResult = filterResult(obj, null, null);
            if (finalResult != null) {
                objects.add(finalResult);
            }
        }
        return objects;
    }

    private Object filterResult(Object obj, Set<Integer> serviceInstanceIds, Set<Integer> nodeIds) {
        Map<String, Object> map;
        try {
            map = MapUtils.obj2Map(obj);
        } catch (IllegalAccessException e) {
            LOG.error("Result:{} convert Map error", obj, e);
            //todo Custom exception
            throw new RuntimeException("");
        }
        Object firstResult = filterProductResult(obj, map, serviceInstanceIds);
        return filterPriceResult(firstResult, map, nodeIds);
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



    CountSpliceSql paraseAop(MappedStatement ms) {
        String id = ms.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        try {
            final Class cls = Class.forName(className);
            final Method[] method = cls.getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(CountSpliceSql.class)) {
                    return me.getAnnotation(CountSpliceSql.class);
                }
            }
        } catch (ClassNotFoundException e) {
            LOG.error("Annotation CountSpliceSql invalid", e);
        }
        return null;
    }


    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    String getSqlString(String key, Set<Integer> values) {
        StringBuffer sb = new StringBuffer(key +" in (");
        values.forEach(id -> sb.append(id).append(","));
        sb.deleteCharAt(sb.lastIndexOf(",")).append(")");
        return sb.toString();
    }

    abstract Object filterProductResult(Object obj, Map<String, Object> map, Set<Integer> serviceInstanceIds);

    abstract Object filterPriceResult(Object obj, Map<String, Object> map, Set<Integer> nodeIds);

    abstract void filterParam(Set<Integer> userServiceInstanceIds, Set<Integer> userNodeIds, String userName, Object[] args);
}
