package cn.weit.happymo.interceptor;

import cn.weit.happymo.annotation.CountSpliceSql;
import cn.weit.happymo.annotation.DataAuthKey;
import cn.weit.happymo.constants.DataAuth;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author weitong
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args =
                {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args =
                {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
@Component
public class QueryPermissionInterceptor extends BasePermissionInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(QueryPermissionInterceptor.class);




    @Override
    Object filterProductResult(Object obj, Map<String, Object> map, Set<Integer> serviceInstanceIds) {
        //todo
        return null;
    }

    @Override
    Object filterPriceResult(Object obj, Map<String, Object> map, Set<Integer> nodeIds) {
        //todo

        return null;
    }

    private void filterCountParam(CountSpliceSql countSpliceSql, Set<Integer> nodeIds, Set<Integer> serviceInstanceIds, Object[] args) {
        MappedStatement ms = (MappedStatement) args[0];
        Object  param = args[1];
        BoundSql boundSql = ms.getBoundSql(param);
        String sql = boundSql.getSql();
        LOG.info("Before Interceptor execute, sql :{}", sql);
        String newSql = getNewSql(sql, countSpliceSql, nodeIds, serviceInstanceIds);
        LOG.info("After splice, newSql :{}", newSql);
        MappedStatement newMs = rewriteMs(ms, new BoundSqlSqlSource(boundSql));
        MetaObject msObject =  MetaObject.forObject(newMs, new DefaultObjectFactory(),
                new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", newSql);
        args[0] = newMs;
    }

    private MappedStatement rewriteMs(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private String getNewSql(String sql, CountSpliceSql countSpliceSql, Set<Integer> nodeIds, Set<Integer> serviceInstanceIds) {
        String nodeIdSql = null;
        String serviceInstanceIdSql = null;
        for (DataAuthKey dataAuthKey : countSpliceSql.dataAuthKey()) {
            if (dataAuthKey.data().equals(DataAuth.PRODUCT)) {
                nodeIdSql = getSqlString(dataAuthKey.paramKey(), nodeIds);
            }
            if (dataAuthKey.data().equals(DataAuth.PRICE)) {
                serviceInstanceIdSql = getSqlString(dataAuthKey.paramKey(), serviceInstanceIds);
            }
        }
        String newSql = sql;
        if (newSql.toLowerCase().contains(WHERE_NAME)) {
            if (StringUtils.isNotBlank(nodeIdSql)) {
                newSql = newSql + " and " + nodeIdSql;
            }
            if (StringUtils.isNotBlank(serviceInstanceIdSql)) {
                newSql = newSql + " and " + serviceInstanceIdSql;
            }
        } else {
            if (StringUtils.isNotBlank(nodeIdSql)) {
                newSql = newSql + " where " + nodeIdSql;
            }
            if (newSql.toLowerCase().contains(WHERE_NAME)) {
                if (StringUtils.isNotBlank(serviceInstanceIdSql)) {
                    newSql = newSql + " and " + serviceInstanceIdSql;
                }
            } else {
                if (StringUtils.isNotBlank(serviceInstanceIdSql)) {
                    newSql = newSql + " where " + serviceInstanceIdSql;
                }
            }
        }
        return newSql;
    }

    @Override
    void filterParam(Set<Integer> productIds, Set<Integer> priceIds, String userName, Object[] args) {
        MappedStatement ms = (MappedStatement) args[0];
        CountSpliceSql countSpliceSql = paraseAop(ms);
        Object  param = args[1];
        if (countSpliceSql == null) {
            if (param instanceof MapperMethod.ParamMap) {

            }
        } else {
        }
    }
}
