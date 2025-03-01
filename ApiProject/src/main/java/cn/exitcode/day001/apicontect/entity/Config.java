package cn.exitcode.day001.apicontect.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

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
@ApiModel(value="Config对象", description="")
@Setter
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String k;

    private String v;




}
