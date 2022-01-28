package com.topideal.webapi.system;

import com.topideal.common.constant.*;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.entity.dto.main.LoginPermissionsDTO;
import com.topideal.entity.dto.main.MerchantMinDTO;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.PermissionService;
import com.topideal.service.user.UserInfoService;
import com.topideal.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录成功后加载权限菜单
 */
@RestController
@RequestMapping(value="/webapi/system")
@Api(tags = "登录模块")
public class APIIndexController {

    @Autowired
    public PermissionService permissionService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    public UserInfoService userInfoService;
    
    /**
     * 加载权限菜单
     */
    @ApiOperation(value = "加载登录用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="LoadTreeMenuList",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<LoginPermissionsDTO> LoadTreeMenuList(String token){
        LoginPermissionsDTO retDTO = new LoginPermissionsDTO();
        try{
            //登陆用户信息
            User user= ShiroUtils.getUserByToken(token);
            //获取菜单权限
            List<TreeBean> treeMenuList=permissionService.treeMenuList(user.getId());
            for(TreeBean treeParent:treeMenuList) {
                List<TreeBean> childrenList = treeParent.getChildren();
                if (childrenList != null && childrenList.size() > 0) {
                    for(int i = 0;i< childrenList.size();i++){
                        if(childrenList.get(i).getId() == null){
                            childrenList.remove(childrenList.get(i));
                            continue;
                        }
                    }
                }
            }
            //获取用户的按钮权限字符
            List<String> btnList = userInfoService.getBtnPersonInfo(user.getId());
            //查询用户关联的公司列表
            UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
            userMerchantRelModel.setUserId(user.getId());
            List<MerchantInfoModel> merchantSelectBeans = merchantInfoService.getUserMerchantList(userMerchantRelModel);
            List<MerchantMinDTO> merchantList = new ArrayList<>();
            for(MerchantInfoModel merchant : merchantSelectBeans){
                MerchantMinDTO dto = new MerchantMinDTO();
                BeanUtils.copyProperties(merchant, dto);
                merchantList.add(dto);
            }
            retDTO.setMerchantId(user.getMerchantId());
            retDTO.setMerchantName(user.getMerchantName());
            retDTO.setTopidealCode(user.getTopidealCode());
            retDTO.setUserId(user.getId());
            retDTO.setUserType(user.getUserType());
            retDTO.setUsername(user.getUsername());
            retDTO.setName(user.getName());
            retDTO.setMerchantList(merchantList);
            retDTO.setTreeMenuList(treeMenuList);
            retDTO.setBtnList(btnList);
            retDTO.setSystemName(ApolloUtil.systemName);
        }catch(Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retDTO);//成功
    }

    /**
     * 根据名称获取常量集合
     * */
    @ApiOperation(value = "获取常量集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "listName", value = "常量集合名称", required = true)
    })
    @PostMapping(value="/getConstantListByName.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<DerpBasic>> getConstantListByName(String token,String listName){
    	ArrayList<DerpBasic> list = DERP.getConstantListByName(listName);
    	if(list==null) list = DERP_SYS.getConstantListByName(listName);
    	if(list==null) list = DERP_ORDER.getConstantListByName(listName);
    	if(list==null) list = DERP_STORAGE.getConstantListByName(listName);
    	if(list==null) list = DERP_INVENTORY.getConstantListByName(listName);
    	if(list==null) list = DERP_REPORT.getConstantListByName(listName);

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
    }

}
