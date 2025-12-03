// File: core-navigation/src/main/java/com/template/core/navigation/AppRoutes.kt
package com.template.core.navigation

import java.net.URLEncoder

object AppRoutes {

    // 登录
    const val LOGIN_ROUTE = "login"
    const val LOGIN_WITH_VPN_ROUTE = "LOGIN_WITH_VPN_ROUTE"

    // webview
    const val WEBVIEW_ROUTE = "webview/{url}"
    const val WEBSOCKET_ROUTE = "websocket_route"
    const val WEBVIEW_SERVER = "server_webview"
    const val WEBVIEW_LOCAL = "local_webview"
    const val AI_TEST_ROUTE = "AI_TEST_ROUTE"


    // 单个设置项
    const val SETTING_ONE_ITEM = "setting_one_item"
    // 带参数
    const val SETTING_ONE_ITEM_ROUTE = "setting_one_item/{title}/{value}"
    // 设置列表
    const val SETTING_LIST_ROUTE = "setting_list"


    const val SETTING_MAP_ROUTER = "SETTING_MAP_ROUTER"

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
    const val CUSTOM_TEST_VANT_ROUTE = "CUSTOM_TEST_VANT_ROUTE"
    const val CUSTOM_TEST_VANT_DETAIL_ROUTE = "custom_test_vant_detail/{path}"


    // 还可以定义更复杂的路由，比如带参数的
    // const val POST_DETAIL_ROUTE = "post/{postId}"
    // fun postDetail(postId: Int) = "post/$postId"
    fun buildNavigateRoute(
        route: String,
        vararg params: String
    ):String {
        val encoded = params.joinToString("/") { param ->
            URLEncoder.encode(param, "UTF-8")
        }
        val finalRoute = if (encoded.isEmpty()) route else "$route/$encoded"
        return finalRoute
    }
}