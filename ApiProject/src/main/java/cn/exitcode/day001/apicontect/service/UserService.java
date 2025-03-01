package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.User;
import cn.exitcode.day001.apicontect.entity.dto.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
public interface UserService extends IService<User> {

    Result cxInfo() ;

    Result Ulist(QueryPageParam param);

    User login(LoginDTO res);

    Result check(INTDTO res);

    Result userList(QueryPageParam param);

    Result isUser(String user);

    Result cxUserInfo(INTDTO uid);

    Result infoEdit(UserDTO user);

    Result PwdEdit(PwdDTO user);

    Result AgreeList(INTDTO id);

    Result projectHall(QueryPageParam param);

    Result getConfig();

    Result AClist(QueryPageParam param);


    Result selectHandler(SelectHandler selectHandler);

    Result completeProject(INTDTO id);
}
