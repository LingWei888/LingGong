package cn.exitcode.day001.apicontect.mapper;

import cn.exitcode.day001.apicontect.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

}
