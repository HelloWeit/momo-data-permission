package cn.weit.happymo.controller;

import cn.weit.happymo.annotation.AuthData;
import cn.weit.happymo.annotation.DataAuthKey;
import cn.weit.happymo.annotation.PermissionMeta;
import cn.weit.happymo.constants.DataAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weitong
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/product/{productId:[0-9]*}")
    @AuthData(value = @PermissionMeta(type = Integer.class, dataAuthKey = @DataAuthKey(data = DataAuth.PRODUCT,  paramKey = "productId")))

    public int queryAllCluster(@PathVariable Integer productId) {
        return 0;
    }
}
