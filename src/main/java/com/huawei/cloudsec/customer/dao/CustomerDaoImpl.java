package com.huawei.cloudsec.customer.dao;

import com.huawei.cloudsec.customer.elasticsearch.document.Customer;
import com.huawei.cloudsec.customer.elasticsearch.repository.CustomerRepository;
import com.huawei.cloudsec.customer.vo.CustomerVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CustomerDaoImpl implements ICustomerDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDaoImpl.class);

    @Autowired
    private CustomerRepository esRepo;

    @Override
    public Iterable<Customer> findAll() {
        return this.esRepo.findAll();
    }

    @Override
    public void deleteAll() {
        this.esRepo.deleteAll();
    }

    @Override
    public void save(Customer info) {
        this.esRepo.save(info);
    }

    @Override
    public Customer findByFirstName(String firstName) {
        return this.esRepo.findByFirstName(firstName);
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        return this.esRepo.findByLastName(lastName);
    }

    private void assetScores()
    {
        List<Customer> pageLists=null;
        //List<Asset> assets = findAssetByPage();
        //List<String> eip4assets = parseIpAddress(assets);
        //FIXME: 释放的EIP被其他人使用，历史的EIP攻击事件将不存在
        // 其他人的使用了别人释放的EIP，别人被攻击的事件将被搜索出来
        //按照弹性ECS的ID进行查找，将不会出现此问题。
        //searchFromElasticSearch(eip4assets);
    }

    @Override
    public Page<Customer> search(CustomerVO vo, Pageable pageable) {
        SumAggregationBuilder sumDeviceIpExists = AggregationBuilders.sum("sumDeviceIpExists").field("deviceIp");
        MaxAggregationBuilder maxAttackLevel = AggregationBuilders.max("maxAttackLevel").field("attackLevel");
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        //term是代表完全匹配，即不进行分词器分析
        queryBuilder.must(QueryBuilders.termQuery("firstName","Bob"));
        queryBuilder.should(QueryBuilders.termQuery("lastName","Smith"));
        queryBuilder.must(QueryBuilders.rangeQuery("age").from(10).to(20));
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("name").order(SortOrder.ASC);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withSort(sortBuilder).withPageable(pageable)
                .build();
        LOGGER.debug(queryBuilder.toString());
        return this.esRepo.search(searchQuery);
    }

    @Override
    public long count() {
        return this.esRepo.count();
    }
    /*
    DSL语言：一段时间内订单的总金额
   {
      "query": {
        "bool": {
          "must": [
            {
              "range": {
                "orderTime": {
                  "gte": 20180313,
                  "lte": 20180314
                }
              }
            },
            {
              "match": {
                "skuNo": "888"
              }
            }
          ]
        }
      },
      "aggs": {
        "sum_value": {
          "sum_sales": {
            "field": "goodsSales"
          }
        }
      }
    }

    //一段时间内订单的总金额，延伸1：一段时间内新增客户总量，延伸2：一段时间内告警总数量
     QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("orderTime").gte(from).lte(to));
        // 聚合查询。goodsSales是要统计的字段，sum_sales是自定义的别名
        SumAggregationBuilder sumBuilder = AggregationBuilders.sum("sum_sales").field("goodsSales");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .addAggregation(sumBuilder)
                .build();

        double saleAmount = elasticsearchTemplate.query(searchQuery, response -> {
            InternalSum sum = (InternalSum)response.getAggregations().asList().get(0);
            return sum.getValue();
        });
     */
    /**
      常用聚合查询（参考http://blog.csdn.net/u010454030/article/details/63266035）：
     curl验证参考命令：https://www.cnblogs.com/duanxz/p/6528161.html
     （1）统计某个字段的数量
     ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");
     （2）去重统计某个字段的数量（有少量误差）
     CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");
     （3）聚合过滤
     FilterAggregationBuilder fab = AggregationBuilders.filter("uid_filter").filter(QueryBuilders.queryStringQuery("uid:001"));
     （4）按某个字段分组
     TermsBuilder tb =  AggregationBuilders.terms("group_name").field("name");
     （5）求和
     SumBuilder  sumBuilder = AggregationBuilders.sum("sum_price").field("price");
     （6）求平均
     AvgBuilder ab = AggregationBuilders.avg("avg_price").field("price");
     （7）求最大值
     MaxBuilder mb = AggregationBuilders.max("max_price").field("price");
     （8）求最小值
     MinBuilder min =	AggregationBuilders.min("min_price").field("price");
     （9）按日期间隔分组
     DateHistogramBuilder dhb = AggregationBuilders.dateHistogram("dh").field("date");
     （10）获取聚合里面的结果
     TopHitsBuilder thb =  AggregationBuilders.topHits("top_result");
     （11）嵌套的聚合
     NestedBuilder nb = AggregationBuilders.nested("negsted_path").path("quests");
     （12）反转嵌套
     AggregationBuilders.reverseNested("res_negsted").path("kps ");
     */
    /*
    //聚合查询，分组求和（对应sql的group by）
    public Map getEsCashSummaryInfo(String appId) throws Exception {
        Map map = new HashMap<>();
        TermsBuilder tb = AggregationBuilders.terms("cash").field("appId");//appId 是分组字段名，cash是查询结果的别名
        SumBuilder sb = AggregationBuilders.sum("amount").field("paid");//paid是求和字段名称，amount是结果别名
        tb.subAggregation(sb);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("settled",SuperAppConstant.CASH_STATUS_CANCLED));
        boolQueryBuilder.must(QueryBuilders.termQuery("appId",appId));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(boolQueryBuilder)
        .withIndices(SuperAppConstant.ES_INDEX_NAME)
        .withTypes(SuperAppConstant.ES_CASH_TYPE)
        .withSearchType(SearchType.DEFAULT)
        .addAggregation(tb)
        .build();
        Aggregations aggregations = esTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        Terms term = aggregations.get("cash");//获取结果后进行解析
        if(term.getBuckets().size()>0){
            for (Bucket bk : term.getBuckets()) {
                long count = bk.getDocCount();
                //得到所有子聚合
                Map subaggmap = bk.getAggregations().asMap();
                //sum值获取方法
                double amount = ((InternalSum) subaggmap.get("amount")).getValue();
                map.put("count", count);
                map.put("amount", amount);
            }
            return map;
        }else{
            return null;
        }
    }
     */
/*
   //批量插入100万数据
    public void bulkIndex(List<IndexQuery> queries) {
        BulkRequestBuilder bulkRequest = this.client.prepareBulk();
        Iterator var3 = queries.iterator();

        while(var3.hasNext()) {
            IndexQuery query = (IndexQuery)var3.next();
            bulkRequest.add(this.prepareIndex(query));
        }

        BulkResponse bulkResponse = (BulkResponse)bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            Map<String, String> failedDocuments = new HashMap();
            BulkItemResponse[] var5 = bulkResponse.getItems();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                BulkItemResponse item = var5[var7];
                if (item.isFailed()) {
                    failedDocuments.put(item.getId(), item.getFailureMessage());
                }
            }

            throw new ElasticsearchException("Bulk indexing has failures. Use ElasticsearchException.getFailedDocuments() for detailed messages [" + failedDocuments + "]", failedDocuments);
        }
    }
*/
}
