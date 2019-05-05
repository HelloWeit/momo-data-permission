package cn.weit.happymo.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author weitong
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Component
public class OtherPermissionInterceptor extends BasePermissionInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(OtherPermissionInterceptor.class);





    @Override
    Object filterProductResult(Object obj, Map<String, Object> map, Set<Integer> productIds) {

        return null;
    }

    @Override
    Object filterPriceResult(Object obj, Map<String, Object> map, Set<Integer> priceIds) {

        return null;
    }


    @Override
    void filterParam(Set<Integer> productIds, Set<Integer> priceIds, String userName, Object[] args) {

    }
}
