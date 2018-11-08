
复杂示例代码参考：
https://www.javatips.net/api/org.elasticsearch.index.query.querybuilder

数据分析：
一个周内，攻击者排名

应用场景：
1. 维基百科,百度百科
2. The Guardian蕾丝国外新网网站
3. 每天新闻文章的作者，让他知道文章的公众反馈、鄙视、崇拜等都是用ES做的数据分析
4. 国外的StackOverflow也是用ES搜索上千亿行代码
5. 电商里面搜索商品信息
6. ELk系统
7. BI系统 Business Intelligence 商业智能系统，商业数据进行分析和挖掘，作为进行管理层决策依据
辅助支撑商业级别的决策
某某区域最近三年的消费金额趋势
XX区，最近三年，每年消费金额呈现100%的增长，而且用户群体的85%都是白领
8. OA，CRM，都可以这样的
9. 可以做上百台的ES集群，大部分情况下，中小型公司跑在两三台机器上就行了。
不是新技术，分布式，搜索，数据分析结合在一起了。
10. 三分中一部署就行了
分布式，搜索，分析是三个基本功能
flume自定义Serializer收集日志入elasticsearch
https://blog.csdn.net/yujimoyouran/article/details/59104131