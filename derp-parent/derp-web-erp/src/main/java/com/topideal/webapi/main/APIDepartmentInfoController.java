package com.topideal.webapi.main;


import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.main.DepartmentInfoDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import com.topideal.service.main.DepartmentInfoService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.DepartmentInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/webapi/system/departmentInfo")
@Api(tags = "部门管理")
public class APIDepartmentInfoController {

    @Autowired
    private DepartmentInfoService departmentInfoService;


    /**
     * 访问列表页面
     * @param form
     * @return
     * @throws SQLException
     */
    @ApiOperation(value = "访问列表页面")
    @PostMapping(value="/listDepartmentInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean listDepartmentInfo(DepartmentInfoForm form) throws SQLException {
        DepartmentInfoDTO departmentInfoDTO=new DepartmentInfoDTO();
        try {
            BeanUtils.copyProperties(form,departmentInfoDTO);
            //当前用户
            User user = ShiroUtils.getUserByToken(form.getToken());
            departmentInfoDTO=departmentInfoService.getListByPage(departmentInfoDTO);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,departmentInfoDTO);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 编辑
     * @param form
     * @return
     * @throws SQLException
     */
    @ApiOperation(value = "编辑")
    @PostMapping(value="/modifyDepartmentInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean modifyDepartmentInfo(DepartmentInfoForm form) throws SQLException {
        try {
            DepartmentInfoModel model=new DepartmentInfoModel();
            model.setId(form.getId());
            model.setName(form.getName());
            //当前用户
            User user = ShiroUtils.getUserByToken(form.getToken());
            Map map=departmentInfoService.modifyDepartmentInfo(model,user);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }



    /**
     * 新增
     * @param form
     * @return
     * @throws SQLException
     */
    @ApiOperation(value = "新增")
    @PostMapping(value="/saveDepartmentInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveDepartmentInfo(DepartmentInfoForm form) throws SQLException {
        try {
            DepartmentInfoModel model=new DepartmentInfoModel();
            model.setCode(form.getCode());
            model.setName(form.getName());
            //当前用户
            User user = ShiroUtils.getUserByToken(form.getToken());
            Map map=departmentInfoService.saveDepartmentInfo(model,user);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }



}
