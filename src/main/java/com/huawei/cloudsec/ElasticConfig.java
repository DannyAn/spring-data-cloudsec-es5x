package com.huawei.cloudsec;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 态势感知配置文件
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticConfig.class);
    //由于项目从2.2.4配置的升级到 5.5.3版本 原配置文件不想动还是指定原来配置参数
    private String clusterNodes;
    //集群名称
    private String clusterName;
    //xpack鉴权配置插件，default= elastic:change
    private String xpackSecurityUser;

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        Settings.Builder builder = Settings.builder();
        builder.put("cluster.name", clusterName);
        if (!StringUtils.isEmpty(xpackSecurityUser)) {
            builder.put("xpack.security.user", xpackSecurityUser);
        }
        TransportClient client = new PreBuiltXPackTransportClient(builder.build());

        String[] aryClusterNodes = clusterNodes.split(",");
        for (String nodes : aryClusterNodes) {
            String inetSocket[] = nodes.split(":");
            if (inetSocket.length != 2) {
                continue;
            }
            String address = inetSocket[0];
            Integer port = Integer.valueOf(inetSocket[1]);
            client.addTransportAddress(new
                    InetSocketTransportAddress(InetAddress.getByName(address), port));
        }
        return client;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getXpackSecurityUser() {
        return xpackSecurityUser;
    }

    public void setXpackSecurityUser(String xpackSecurityUser) {
        this.xpackSecurityUser = xpackSecurityUser;
    }
}
