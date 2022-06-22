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
        String PROJECT_LIST = "/project/list/{page}/json";

        /**
         * 最新项目数据
         */
        String Project_NEW = "/article/listproject/{page}/json";

        //  -------------登录与注册 ------------

        //登录
        String LOGIN = "/user/login";

        //注册
        String REGISTER = "/user/register";

        /**
         * 用户积分路径
         */
        String CoinInfo = "lg/coin/userinfo/json";

        //退出
        String LOGOUT = "/user/logout/json";


        // ------------收藏 -------------------

        //收藏文章列表
        String COLLECT_LIST = "/lg/collect/list/{page}/json";

        //收藏站内文章
        String COLLECT_INNER_ARTICLE = "/lg/collect/{articleId}/json";

        //收藏站外文章
        String COLLECT_OUT_ARTICLE = "/lg/collect/add/json";

        /**
         * 取消收藏文章
         */
        String UN_COLLECT = "/lg/uncollect_originId/{id}/json";

        /**
         * 收藏页面取消收藏文章路径
         */
        String UserUnCollectArticleAPI = "/lg/uncollect/{id}/json";


        // ---------------广场 -----------------

        String SQUARE_ARTICLE = "/user_article/list/{page}/json";


        // ---------------公众号 -----------------

        //公众号历史数据
        String PLATFORM_HISTORY = "/wxarticle/list/{cid}/{page}/json";

        //公众号列表数据
        String PLATFORM_LIST = "/wxarticle/chapters/json";

        //问答数据路径
        String InquiryAnswerAPI = "/wenda/list/{page}/json";

        // ---------------积分 -----------------
        /**
         * 积分获取列表路径
         */
        String IntegralListAPI = "/lg/coin/list/{page}/json";

        /**
         * 积分排行路径
         */
        String LeaderboardAPI = "/coin/rank/{page}/json";


        //-------------分享----------------
        String ShareListAPI = "/user/lg/private_articles/{page}/json";

        /**
         * 删除分享文章路径
         */
        String DeleteShareAPI = "/lg/user_article/delete/{id}/json";

        /**
         * 分享文章路径
         */
        String ShareArticleAPI = "/lg/user_article/add/json";

        /**
         * 按照作者昵称搜索文章路径
         */
        String SearchArticleByNameAPI = "/article/list/{page}/json?author={name}";

        /**
         * 分享人对应列表数据路径
         */
        String SearchArticleByIdAPI = "/user/{userId}/share_articles/{page}/json";

    }

}
