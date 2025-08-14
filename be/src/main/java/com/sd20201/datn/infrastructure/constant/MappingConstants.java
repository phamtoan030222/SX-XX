package com.sd20201.datn.infrastructure.constant;

public class MappingConstants {

    /* API BASE ROLE */
    public static final String MANAGE = "/manage";
    public static final String STAFF = "/staff";
    public static final String CUSTOMER = "/customer";

    /* API VERSION PREFIX */
    public static final String API_VERSION_PREFIX = "/api/v1";

    /* AUTHENTICATION */
    public static final String API_AUTH_PREFIX = API_VERSION_PREFIX + "/auth";

    /* API COMMON */
    public static final String API_COMMON = API_VERSION_PREFIX + "/common";
    public static final String API_LOGIN = API_VERSION_PREFIX + "/login";

    /* API FOR MANAGE */
    public static final String API_PREFIX_MANAGE = API_VERSION_PREFIX + "/manage";
    public static final String API_TEST = API_PREFIX_MANAGE + "/product";


}
