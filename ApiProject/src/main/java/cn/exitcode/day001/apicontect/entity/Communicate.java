package cn.exitcode.day001.apicontect.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
@ApiModel(value="Communicate对象", description="")
public class Communicate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private int uid;

    private int toid;

    private long addtime;

    private String type;

    private String content;


}
