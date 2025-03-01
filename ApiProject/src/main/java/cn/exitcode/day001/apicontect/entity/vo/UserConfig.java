package cn.exitcode.day001.apicontect.entity.vo;

import lombok.Data;

@Data
public class UserConfig {
    private int userId;
    private String userName;
    private int roleId;
    private String qq;
    private String token;
}
