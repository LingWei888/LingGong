package cn.exitcode.day001.apicontect.entity.vo;

import lombok.Data;

@Data
public class IUserInfoVO {
    private String name;
    private String tips;
    private String mail;
    private String Qq;
    private String card="****************";
    private Float BZMoney;
}
