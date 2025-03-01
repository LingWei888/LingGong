package cn.exitcode.day001.apicontect.entity.dto;

import lombok.Data;

@Data
public class RegDTO {
    private String username;
    private String password;
    private String email;
    private String code;
}
