package cn.exitcode.day001.apicontect.entity.dto;

import lombok.Data;

@Data
public class RechargeDTO {
    private String user;
    private int money;
    private String type;
}
