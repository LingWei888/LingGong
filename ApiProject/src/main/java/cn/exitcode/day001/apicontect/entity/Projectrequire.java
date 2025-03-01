package cn.exitcode.day001.apicontect.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2025-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Projectrequire对象", description="")
public class Projectrequire implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String proid;

    private String content;

    private Integer state;


}
