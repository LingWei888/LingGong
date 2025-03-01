package cn.exitcode.day001.apicontect.entity.vo;

import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import lombok.Data;

import java.util.List;

@Data
public class UserIndexVO {
    private int projectCount;
    private int ContactCount;
    private int waitCount;
    private Float money;
    private int roleid;
    private List<Project> projectList;
    private List<Projectrequire> projectrequireList;

}
