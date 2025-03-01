package cn.exitcode.day001.apicontect.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String user;

    private String pwd;

    private String mail;

    private String card;

    private Integer roleid;//0为自由，1为雇主

    private String tips;

    @TableField("BZMoney")
    private float bzmoney;

    private String xinyu;

    private String state;

    private String addtime;

    private String qq;



}
