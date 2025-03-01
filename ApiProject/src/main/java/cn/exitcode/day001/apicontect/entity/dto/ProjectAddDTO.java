package cn.exitcode.day001.apicontect.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectAddDTO {
    private String title;
    private String content;
    private float amount;
    private List<String> requirements;
    private String deadline;
}
