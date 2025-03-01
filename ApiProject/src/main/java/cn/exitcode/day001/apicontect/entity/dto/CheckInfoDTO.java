package cn.exitcode.day001.apicontect.entity.dto;

import lombok.Data;

@Data
public class CheckInfoDTO {
    private String name;
    private String id_card;
    private int role_id;
    private float money;
}
