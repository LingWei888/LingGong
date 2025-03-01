package cn.exitcode.day001.apicontect.entity.dto;

import cn.exitcode.day001.apicontect.entity.Projectrequire;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data

public class UProjectDTO implements Serializable {


    private Integer id;

    private String uid;

    private String money;

    private String title;

    private String content;

    private String acid;

    private String addtime;

    private String endtime;

    private String uevalute;

    private String acevalute;

    private Integer state;//-1未接单0:未开始 1-8:进行中 9:已完成


    private List<Projectrequire> require;


}