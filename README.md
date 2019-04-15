# momo-data-permission

在上家公司时候做过一个大的权限和sso系统，这种系统也被称为IAM系统，权限方面做的是简单的功能权限，没有涉足数据权限。两者相比功能权限级别上有相应的理论模型如RBAC，因此可以做成一个通用框架模板，也较为简单。而数据权限和业务耦合的较深，很多解决方案只能参考，不能直接拿来用，因此没有一个通用的方案。但不管是哪种方式实现的数据权限，最终归根到底都会在数据访问层进行sql的处理。   
所以利用闲暇时间，尝试搞一套通用的框架。  
基本思路：aop切面拦截一部分参数中带有明显权限点的请求，再一个在mybatis的层面设置一个拦截器，在执行前执行后进行相关拦截，执行前增加where的处理，执行后对结果进行过滤。简单解释：用aop拦截的好处在于1.如果url不是很规范，可以通过反射获取，2.内部设置一个开关，只有加了注解的请求在处理时，mybatis层面会做拦截判断
注释中的代码
```

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        if (!RequestHolder.isDataHolderExist()) {
//            return invocation.proceed();
//        }

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
```
