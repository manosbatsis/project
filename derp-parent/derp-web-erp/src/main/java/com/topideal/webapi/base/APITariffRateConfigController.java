package com.topideal.webapi.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.base.TariffRateConfigDTO;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.service.base.TariffRateConfigService;
import com.topideal.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 税率配置 Controller
 */
@RequestMapping("/webapi/system/tariffRate")
@RestController
@Api(tags = "税率配置列表")
public class APITariffRateConfigController {
	
	private static final Logger LOG = Logger.getLogger(APITariffRateConfigController.class) ;

    @Autowired
    private TariffRateConfigService tariffRateConfigService;

    /**
     * 获取分页数据
     * @return
     */
    @ApiOperation(value = "获取列表分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "begin", value = "开始位置", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
            @ApiImplicitParam(name = "rate", value = "税率")
    })
    @PostMapping(value="/listTariffRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TariffRateConfigDTO> listTariffRate(String token, BigDecimal rate, Integer begin, Integer pageSize) throws Exception{
        TariffRateConfigDTO dto=new TariffRateConfigDTO();
        User user = ShiroUtils.getUserByToken(token);
        try {
            dto.setRate(rate);
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            // 响应结果集
            dto = tariffRateConfigService.getListByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 新增税率配置信息
     * @return
     */
    @ApiOperation(value = "新增税率配置信息")
    @PostMapping(value="/saveTariffRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveTariffRate(String token,BigDecimal rate) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            TariffRateConfigModel model=new TariffRateConfigModel();
            model.setRate(rate);
            model.setCreateName(user.getName());
            model.setCreater(user.getId());
            Map<String, Object> map = tariffRateConfigService.saveTariffRate(model);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常);
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开", required = true)
    })
    @PostMapping(value="/delTarffRate.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delTarffRate(String ids) {
        try{
            Map<String,Object> map=tariffRateConfigService.delReptile(ids);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 下拉选择
     * @param ids
     * @return
     */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "获取下拉选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/getSelectBean.asyn")
    public ResponseBean<List<SelectBean>> getSelectBean(String token) {
        try{
            
        	List<SelectBean> selectList = tariffRateConfigService.getSelectBean() ;
        	
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, selectList);
        	
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
}
