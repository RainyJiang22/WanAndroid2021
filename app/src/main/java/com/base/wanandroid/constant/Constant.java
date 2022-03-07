package com.base.wanandroid.constant;

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
public interface Constant {


    interface URI {

        String BASE_URI = "https://www.wanandroid.com";

  // ----------------  首页相关 ---------------

        //首页文章列表
        String HOME_LIST = "/article/list/{page}/json";
        //首页banner
        String HOME_BANNER = "/banner/json";
        //常用网站
        String FRIEND_LIST = "/friend/json";
        //搜索热词
        String HOT_KEY = "/hotkey/json";
        //置顶文章
        String TOP_ARTICLE = "/top/json";

  // ---------------  体系相关 -----------------

        //体系数据
        String TREE_LIST = "/tree/json";

        //体系知识下的文章
        String TREE_ARTICLE_LIST = "/article/list/{page}/json";

   // ------------ 导航 --------------

        //导航数据
        String NAVI = "/navi/json";


        //搜素
        String SEARCH = "/article/query/{page}/json";

   // ------------ 项目 --------------

        //项目分类
        String TYPE_PROJECT = "/project/tree/json";

        //项目列表数据
        String PROJECT_LIST = "/project/list/1/json";

   //  -------------登录与注册 ------------

        //登录
        String LOGIN = "/user/login";

       //注册
        String REGISTER = "/user/register";

       //退出
        String LOGOUT = "/user/logout/json";


    // ------------收藏 -------------------

        //收藏文章列表
        String COLLECT_LIST = "/lg/collect/list/{page}/json";

        //收藏站内文章
        String COLLECT_INNER_ARTICLE = "/lg/collect/{articleId}/json";

        //收藏站外文章
        String COLLECT_OUT_ARTICLE = "/lg/collect/add/json";

        String UN_COLLECT = "lg/uncollect_originId/{articleId}/json";


    // ---------------广场 -----------------

        String SQUARE_ARTICLE = "user_article/list/{page}/json";


    }

}
