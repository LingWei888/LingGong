package cn.exitcode.day001.apicontect.entity.vo;

import cn.exitcode.day001.apicontect.entity.Projectrequire;
import lombok.Data;

import java.util.List;

@Data
public class AgreeListVO {
    private int id;
    private String title;
    private String content;
    private int state;
    private List<Projectrequire> projectrequireList;
    private List<UserVO> userVOList;
}
