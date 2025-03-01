package cn.exitcode.day001.apicontect.entity.vo;

import cn.exitcode.day001.apicontect.entity.Introduce;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
    private String user;
    private String tips;
    private String mail;
    private List<Introduce> introduce;
    private List<ProRateVO> prorate;
}
