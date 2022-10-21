# WanAndroid2021

Kotlin版WanAndroid客户端,项目中集成了很多优秀的开源库和UI组件,正在开发中

## 接口

[玩Android接口](http://www.wanandroid.com/blog/show/2)

## 项目功能

- [x] 首页
- [x] 知识体系
- [x] 知识体系专题
- [x] 广场(每日一问，项目,导航)
- [x] 公众号列表
- [x] 登录、注册、注销
- [x] 我的收藏、取消收藏、添加收藏
- [x] 文章内容
- [x] 分享文章、专题
- [x] 常用网站
- [x] 搜索
- [x] 热词搜索
- [x] 关于我们
- [x] Banner
- [x] 搜索记录
- [x] 离线数据
- [x] 积分及排名页
- [x] 历史记录页

## 开发计划

#### v1.0.0(已开发完成)

- 提交完成wanAndroid第一版

#### v1.0.1(已开发完成)

- 完成离线数据功能
- 完成夜间模式功能
- 增加splash
- 增加文章作者界面，浏览作者发表文章列表
- Fix1.0.0的bug

#### v1.0.2(已开发完成)

- 扫描二维码下载
- 昵称生成头像
- Fix1.0.2的bug

#### v1.1.0(已开发完成)

- 增加今日诗词功能(完成)
- Fix之前版本的遗留bug(已完成)
- Fix刷新列表闪退问题 (完成)

#### v1.1.1(已开发完成)

- 整体界面优化，美化
- FIX之前版本的bug

#### v2.0.0(开发完成)

- 整体界面UI细节改动
- 重新调整项目架构，采用MVVM模式集成谷歌官方推荐的JetPack组件库,感谢开源框架(https://github.com/hegaojian/JetpackMvvm) (初步完成)
- FIX主页切换卡顿闪退问题 (已解决)
- 增加离线数据，已经加载的数据持久化存储 (已完成)

#### v2.1.0(开发完成)

- 全局主题切换FIX(已完成)
- 增加todo列表管理功能(已完成)

## 主要使用的开源框架

- [协程](https://github.com/Kotlin/kotlinx.coroutines)
- [Gson解析](https://github.com/google/gson)
- [网络请求](https://github.com/liangjingkanji/Net)
- [序列化数据](https://github.com/liangjingkanji/Serialize)
- [消息事件](https://github.com/liangjingkanji/Channel)
- [RecyclerView](https://github.com/liangjingkanji/BRV)
- [Adapter](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [侧滑返回](https://github.com/goweii/SwipeBack)
- [Retrofit](https://github.com/square/retrofit)
- [okhttp](https://github.com/square/okhttp)
- [Glide](https://github.com/bumptech/glide)
- [LiveEventBus](https://github.com/JeremyLiao/LiveEventBus)
- [UnpeekLiveData](https://github.com/KunMinX/UnPeek-LiveData)
- [AgentWeb](https://github.com/Justson/AgentWeb)
- [FlowLayout](https://github.com/hongyangAndroid/FlowLayout)
- [BGABanner-Android](https://github.com/bingoogolapple/BGABanner-Android)
- [leakcanary](https://github.com/square/leakcanary)
- [AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)

**还用到了很多开源框架就不写上了，感谢所有优秀的开源项目**

## License

```
MIT License

Copyright (c) 2022 Ricardo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```