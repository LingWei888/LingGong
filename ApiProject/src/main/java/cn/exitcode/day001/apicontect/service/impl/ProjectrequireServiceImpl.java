package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Project;
import cn.exitcode.day001.apicontect.entity.Projectrequire;
import cn.exitcode.day001.apicontect.entity.dto.EditRequireDTO;
import cn.exitcode.day001.apicontect.entity.dto.INTDTO;

import cn.exitcode.day001.apicontect.mapper.ProjectrequireMapper;
import cn.exitcode.day001.apicontect.service.ProjectService;
import cn.exitcode.day001.apicontect.service.ProjectrequireService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-19
 */
@Service
public class ProjectrequireServiceImpl extends ServiceImpl<ProjectrequireMapper, Projectrequire> implements ProjectrequireService {



}
