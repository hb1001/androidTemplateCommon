// File: core-navigation/src/main/java/com/template/core/navigation/AppRoutes.kt
package com.template.core.navigation

object AppRoutes {

    // 登录
    const val LOGIN_ROUTE = "login"
    const val LOGIN_WITH_VPN_ROUTE = "LOGIN_WITH_VPN_ROUTE"

    // 测试app用的
    const val HOME_ROUTE = "home"
    const val DEBUG_ROUTE = "debug"


    const val HOME_LIST_ROUTE = "home_list"
    const val PAGING_AND_REFRESH_ROUTE = "paging_and_refresh"
    const val PAGING_ONLY_ROUTE = "paging_only"
    const val PULL_REFRESH_ONLY_ROUTE = "pull_refresh_only"

    // 地图（用不上）
    const val MAP_ROUTER_ROUTE = "map_router"

    // 自定义路由
    const val CUSTOM_ROUTER_ROUTE = "custom_router"
    const val CUSTOM_POST_DETAIL_ROUTE = "CUSTOM_POST_DETAIL_ROUTE"

    // 还可以定义更复杂的路由，比如带参数的
    // const val POST_DETAIL_ROUTE = "post/{postId}"
    // fun postDetail(postId: Int) = "post/$postId"
}