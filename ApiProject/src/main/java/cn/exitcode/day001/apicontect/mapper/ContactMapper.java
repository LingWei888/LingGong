package cn.exitcode.day001.apicontect.mapper;

import cn.exitcode.day001.apicontect.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Mapper
public interface ContactMapper extends BaseMapper<Contact> {

}
