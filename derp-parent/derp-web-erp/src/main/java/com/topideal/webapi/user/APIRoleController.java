package com.topideal.webapi.user;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.user.RoleInfoDTO;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.service.user.RoleInfoService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.RoleSearchUserResponseDTO;
import com.topideal.webapi.form.RoleUserMapResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/5/2.
 */
@RequestMapping("/webapi/system/role")
@RestController
@Api(tags = "角色部")
public class APIRoleController {

    @Autowired
    private RoleInfoService service;

    /**
     * 跳转到用户列表页面
     * @return
     */
   /* @RequestMapping("/toPage.html")
    public String toPage(){
        return "/derp/user/role-list";
    }
*/
    /**
     * 列表所需数据
     * @param model
     * @return
     */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "name", value = "角色名称")
	})
	@PostMapping(value="/list.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<RoleInfoDTO> listRole(String token,String name,int begin,int pageSize){
        try{
        	RoleInfoDTO dto=new RoleInfoDTO();
        	dto.setName(name);
        	dto.setBegin(begin);
        	dto.setPageSize(pageSize);
            //session 获取用户信息
        	User user = ShiroUtils.getUserByToken(token);
            //普通用户，取超管用户id
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                dto.setUserId(user.getId());
            }
            dto=service.listRoleInfo(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

    }

    /**
     * 新增角色数据
     * @param model
     * @return
     */
   	@ApiOperation(value = "新增角色数据")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "name", value = "角色名称",required = true),
   			@ApiImplicitParam(name = "remark", value = "备注")
   	})
   	@PostMapping(value="/saveRole.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveRole(String token,String name,String remark){
        try{
        	RoleInfoModel model=new RoleInfoModel();
        	model.setName(name);
        	model.setRemark(remark);
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(model.getName()).
                    empty();
            if(isNull){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            //session 获取用户信息
            User user = ShiroUtils.getUserByToken(token);
            //绑定用户信息
            model.setUserId(user.getId());
            //存储用户信息
            /*if("3".equals(user.getUserType())){
                model.setUserId(user.getParentId());
            }*/
            //创建人
            model.setCreater(user.getId());
            Long id = service.saveRole(model);
            if(id==null){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,id);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     *删除角色数据
     * @param id
     * @return
     */
    @ApiOperation(value = "删除角色数据")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "id", value = "id",required = true)
   	})
   	@PostMapping(value="/del.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delUser(Long id){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            //查询该角色下是否已分配用户，若分配则不能删除
            if (service.isExistUsersByRoleId(id)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10016);//未知异常
            }
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            boolean b = service.delRole(ids);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 分配权限
     * @param ids
     * @param roleId 
     * @return
     */
    @ApiOperation(value = "分配权限")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "ids", value = "id",required = true),
   			@ApiImplicitParam(name = "roleId", value = "角色id",required = true)
   	})
   	@PostMapping(value="/addPermission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean addPermission(String ids, Long roleId){
        try{
            //校验id是否正确
            boolean isRight =StrUtils.validateId(roleId);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            //转成list
            List<Long> idList=new ArrayList<Long>();
            if (StringUtils.isNotBlank(ids)) {
            	idList= StrUtils.parseIds(ids);
			}
            
            //新增
            boolean b = service.addPermission(idList,roleId);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 绑定用户信息
     * @return
     */
    @ApiOperation(value = "绑定用户信息")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "roleId", value = "角色id",required = true)
   	})
   	@PostMapping(value="/searchUserInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<RoleSearchUserResponseDTO> searchUserInfo(String token,Long roleId){
        Map map=null;
        try{
            //session 获取用户信息
        	User user = ShiroUtils.getUserByToken(token);
            //后台管理员取全部用户/普通用户取自己创建的用户
            Long userId=user.getId();
            //普通用户，取超管用户id
            /*if("3".equals(user.getUserType())){
                userId=user.getParentId();
            }*/
            map=service.seachUserInfoByRoleId(roleId,userId,user.getUserType());
            
            List<Map> allUserMap= (List<Map>) map.get("allUser");
            List<RoleUserMapResponseDTO> allUser=new ArrayList<>();
            for (Map map2 : allUserMap) {
            	RoleUserMapResponseDTO userMapResponse=new RoleUserMapResponseDTO();
            	
            	userMapResponse.setText((String)map2.get("text"));
            	userMapResponse.setValue((Long)map2.get("value"));
            	allUser.add(userMapResponse);
			}
            List<String> bindUser= (List<String>) map.get("bindUser");
            RoleSearchUserResponseDTO response=new RoleSearchUserResponseDTO();
            response.setAllUser(allUser);
            response.setBindUser(bindUser);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,response);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }
    /**
     * 获取启用用户
     * @return
     */
    @ApiOperation(value = "获取启用用户")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true)
   	})
   	@PostMapping(value="/getUserInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<RoleSearchUserResponseDTO> getUserInfo(String token){

        try{
            //session 获取用户信息
        	User user = ShiroUtils.getUserByToken(token);
            //后台管理员取全部用户/普通用户取自己创建的用户
            Long userId=user.getId();
            Map map=service.getUserInfo(userId,user.getUserType(), false);
            List<Map> allUserMap= (List<Map>) map.get("allUser");
            List<RoleUserMapResponseDTO> allUser=new ArrayList<>();
            for (Map map2 : allUserMap) {
            	RoleUserMapResponseDTO userMapResponse=new RoleUserMapResponseDTO();
            	userMapResponse.setText((String)map2.get("text"));
            	userMapResponse.setValue((Long)map2.get("value"));
            	allUser.add(userMapResponse);
			}
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,allUser);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

    }

    /**
     * 获取启用用户
     * @return
     */
    @ApiOperation(value = "获取员工编号不为空的启用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/getUserInfoHasCode.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<RoleSearchUserResponseDTO> getUserInfoHasCode(String token){

        try{
            //session 获取用户信息
            User user = ShiroUtils.getUserByToken(token);
            //后台管理员取全部用户/普通用户取自己创建的用户
            Long userId=user.getId();
            List<Map<String, Object>> list=service.getUserInfoByCode(true);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 分配用户角色权限
     * @param ids
     * @param roleId
     * @return
     */
    @ApiOperation(value = "分配用户角色权限")
   	@ApiImplicitParams({
   			@ApiImplicitParam(name = "token", value = "令牌", required = true),
   			@ApiImplicitParam(name = "ids", value = "id的集合,多个用逗号隔开", required = true),
   			@ApiImplicitParam(name = "token", value = "令牌", required = true)
   	})
   	@PostMapping(value="/bindUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean bindUser(String ids, Long roleId){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids)&& StrUtils.validateId(roleId);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            //转成list
            List<Long> idList= null;
            if(StringUtils.isNotBlank(ids)){
                idList=StrUtils.parseIds(ids);
            }

            if (roleId.equals(1001L)) {
                if (StringUtils.isBlank(ids) || idList == null) {
                	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"该角色为平台管理员角色，至少绑定一个用户！");
                }
            }

            //新增
            boolean b = service.addBindUser(idList,roleId);
            if(!b){
            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,b);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }









}
