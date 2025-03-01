package cn.exitcode.day001.apicontect.service.impl;

import cn.exitcode.day001.apicontect.common.JwtToken.JwtProperties;
import cn.exitcode.day001.apicontect.common.QueryPageParam;
import cn.exitcode.day001.apicontect.common.Result;
import cn.exitcode.day001.apicontect.entity.Introduce;
import cn.exitcode.day001.apicontect.mapper.IntroduceMapper;
import cn.exitcode.day001.apicontect.service.IntroduceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LingWei
 * @since 2025-01-13
 */
@Service
public class IntroduceServiceImpl extends ServiceImpl<IntroduceMapper, Introduce> implements IntroduceService {
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public Result MyIntroduce(QueryPageParam param) {
        Page<Introduce> page = new Page<>(param.getPageNum(),param.getPageSize());
        String title = param.getKeyword();
        IPage res;
        if(title!=null){
            res=this.lambdaQuery().eq(Introduce::getUid,jwtProperties.getUserId()).like(Introduce::getTitle,title).page(page);
        }else{
            res=this.lambdaQuery().eq(Introduce::getUid,jwtProperties.getUserId()).page(page);
        }
        return Result.result(200,"成功",res.getTotal(),res);
    }
    @Override
    public Result addIntroduce(Introduce introduce) {
        if(!introduce.getUid().equals(jwtProperties.getUserId().toString())) return Result.failstr("非法操作");
        introduce.setUid(jwtProperties.getUserId().toString());
        if(this.save(introduce)){
            return Result.result(200,"成功",null,null);
        }else{
            return Result.result(400,"失败",null,null);
        }
    }
    @Override
    public Result updateIntroduce(Introduce introduce) {
        if(!this.lambdaQuery().eq(Introduce::getId,introduce.getId()).one().getUid().equals(jwtProperties.getUserId().toString()))return Result.failstr("非法操作");
        if(this.updateById(introduce)){
            return Result.result(200,"成功",null,null);
        }else{
            return Result.result(400,"失败",null,null);
        }
    }
    @Override
    public Result deleteIntroduce(Introduce introduce) {
        if(!this.lambdaQuery().eq(Introduce::getId,introduce.getId()).one().getUid().equals(jwtProperties.getUserId().toString()))return Result.failstr("非法操作");
        if(this.removeById(introduce.getId())){
            return Result.result(200,"成功",null,null);
        }else{
            return Result.result(400,"失败",null,null);
        }
    }
}
