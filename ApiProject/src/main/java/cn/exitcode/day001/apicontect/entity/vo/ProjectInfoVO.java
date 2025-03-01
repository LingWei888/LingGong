package cn.exitcode.day001.apicontect.entity.vo;

import cn.exitcode.day001.apicontect.entity.Projectrequire;
import lombok.Data;

import java.util.List;

@Data
public class ProjectInfoVO {
    private String title;
    private String content;
    private String endtime;
    private List<Projectrequire> projectrequire;
}
