package cn.exitcode.day001.apicontect.entity.dto;

import lombok.Data;

@Data
public class ImSingleDTO {
    private String user;
    private String touser;
    private String content;
    private String type;
    private Long time;
}
