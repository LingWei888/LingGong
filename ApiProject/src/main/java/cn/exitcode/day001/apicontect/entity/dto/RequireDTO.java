package cn.exitcode.day001.apicontect.entity.dto;

import cn.exitcode.day001.apicontect.entity.Projectrequire;
import lombok.Data;

import java.util.List;

@Data
public class RequireDTO {
    private int state;
    private String title;
    private String content;
    private String endtime;
    private List<Projectrequire> todo;
    private List<Projectrequire> doing;
    private List<Projectrequire> done;
}
