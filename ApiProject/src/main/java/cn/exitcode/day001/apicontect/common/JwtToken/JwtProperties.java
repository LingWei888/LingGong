package cn.exitcode.day001.apicontect.common.JwtToken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spark.jwt")
@Data
public class JwtProperties {

    /**
     * 管理Admin生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户User生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;
    private Long userId;
    private Long roleId;
    private String userName;


}