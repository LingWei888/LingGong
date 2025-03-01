package cn.exitcode.day001.apicontect.entity.dto;

import lombok.Data;

@Data
public class CommunicateDTO {
    private String username;
    private String avatar;
    private String content;
    private String type;
    private Boolean isSelf;
}
