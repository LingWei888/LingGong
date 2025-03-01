package cn.exitcode.day001.apicontect.service;

import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
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
public interface ProjectService extends IService<Project> {

    Result listAll(QueryPageParam param);

    Result addproject(ProjectAddDTO project);



    Result selectHandler(SelectHandler selectHandler);

    Result completeProject(INTDTO id);

    Result rateHandler(RateDTO rateDTO);

    Result projectList(QueryPageParam param);



    Result cxProjectInfo(INTDTO pid);

    Result enterProject(INTDTO pid);

    Result listU(INTDTO pid);

    Result editRequire(EditRequireDTO editRequireDTO);

    Result outProject(INTDTO pid);

    Result completeProjectRequire(RateDTO id);
}
