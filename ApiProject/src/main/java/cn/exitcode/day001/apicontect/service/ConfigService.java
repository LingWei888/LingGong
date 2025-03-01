package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Config;
import cn.exitcode.day001.apicontect.entity.dto.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
public interface ConfigService extends IService<Config> {


    boolean Login(LoginDTO res);

    String getValueByKey(String key);

    List<HashMap<String, Object>> getAllConfig();



    boolean updateValueByKey(String key, String value);

    Result getConfig();

    Result setPwd(PwdDTO res);

    Result setApi(ApiDTO res);

    Result cxApi();

    Result setConfig(WebConfigDTO res);

    Result cxConfig();

    Result checkinfo(CheckInfoDTO user);
}
