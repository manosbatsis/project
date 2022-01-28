package com.topideal.webapi.user;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.TreeBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.user.PermissionInfoDTO;
import com.topideal.entity.vo.user.PermissionInfoModel;
import com.topideal.entity.vo.user.RolePermissionRelModel;
import com.topideal.service.user.PermissionService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.PermissionInfoAddForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixiaolei on 2018/5/2.
 */

@RequestMapping("/webapi/system/permission")
@RestController
@Api(tags = "权限")
public class APIPermissionController {

    @Autowired
    private PermissionService service;

    /**
     * 跳转到用户列表页面
     * @return
     */
    /*@RequestMapping("/toPage.html")
    public String toPage(){
        return "/derp/user/permission-list";
    }*/

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
			@ApiImplicitParam(name = "authorityName", value = "权限名称")
	})
	@PostMapping(value="/list.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<PermissionInfoDTO> listPermission(String authorityName,int begin,int pageSize){
        try{
        	PermissionInfoDTO dto=new PermissionInfoDTO();
        	dto.setAuthorityName(authorityName);
        	dto.setBegin(begin);
        	dto.setPageSize(pageSize);
            dto=service.listPermissionInfo(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 保存权限数据
     * @param model
     * @return
     */
	@ApiOperation(value = "新增")
	@PostMapping(value="/save.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean savePermission(PermissionInfoAddForm from){
        try{
        	PermissionInfoModel model=new PermissionInfoModel();  
        	model.setAuthorityName(from.getAuthorityName());
        	model.setParentId(from.getParentId());
        	model.setType(from.getType());
        	model.setUrl(from.getUrl());
        	model.setServerAddr(from.getServerAddr());
        	model.setModule(from.getModule());
        	model.setPermission(from.getPermission());
        	model.setSeq(from.getSeq());
        	model.setRemark(from.getRemark());
        	model.setIsEnabled(from.getIsEnabled());
            //空值校验
            boolean isNull = new EmptyCheckUtils().
                    addObject(model.getAuthorityName()).
                    empty();
            if(isNull){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            if (model.getType() != null && model.getType() == 2) {
                if (StringUtils.isBlank(model.getPermission())) {
                	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
                }
            }
            //session 获取用户信息
            User user = ShiroUtils.getUserByToken(from.getToken());
            //创建人
            model.setCreater(user.getId());
            boolean b = service.savePermission(model);
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
     * 列表所需数据
     * @return
     */
	@ApiOperation(value = "获取权限树结构数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/listTree.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<List<TreeBean>> listTree()throws SQLException{
		List<TreeBean>list=new ArrayList<>();
		try {
			list=service.listTree();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
        
    }

    /**
     * 列表所需数据
     * @return
     */
	@ApiOperation(value = "获取权限列表树结构数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/listTreeAll.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<List<TreeBean>> listTreeAll(String token)throws SQLException{
		try {
			//session 获取用户信息
			User user = ShiroUtils.getUserByToken(token);
	        //假设平台
	        Long userId=null;
	        //普通用户，取超管用户id
	        if("3".equals(user.getUserType())){
	            userId=user.getParentId();
	        }else if("2".equals(user.getUserType())){
	            userId=user.getId();
	        }
	        List<TreeBean> list=service.listTreeAll(userId);
	        //若子节点只有一个，判断是否为空，为空不加载子节点
	        if(list != null && list.size() > 0){
	            for(TreeBean tr: list){
	                if(tr.getChildren() != null && tr.getChildren().size() == 1){
	                    if(tr.getChildren().get(0).getId() == null){
	                        tr.getChildren().remove(0);
	                    }
	                }
	            }
	        }
	        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
        
    }

    /**
     *删除用户数据
     * @param id
     * @return
     */
	@ApiOperation(value = "删除用户数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/del.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delUser(Long id){
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(id);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//成功
            }
            List<Long> ids = new ArrayList<Long>();
            ids.add(id);
            boolean b = service.delPermission(ids);
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
     *通过角色ID查询用户信息
     * @param roleId
     * @return
     */
	@ApiOperation(value = "通过角色ID查询用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "roleId", value = "角色id", required = true)
	})
	@PostMapping(value="/searchByRoleId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<List<RolePermissionRelModel>> searchByRoleId(Long roleId){
        List<RolePermissionRelModel> list=null;
        try{
            //校验id是否正确
            boolean isRight = StrUtils.validateId(roleId);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//成功
            }
             list= service.searchRelByRoleId(roleId);
             return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

	/**
	 * 保存权限数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "编辑权限")
	@PostMapping(value="/modifyPermission.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyPermission(PermissionInfoAddForm form){
		try{
			//空值校验
			boolean isNull = new EmptyCheckUtils().addObject(form.getAuthorityName()).empty();
			if(isNull){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			PermissionInfoModel model=new PermissionInfoModel();
			BeanUtils.copyProperties(form, model);

			if (model.getType() != null && model.getType() == 2) {
				if (StringUtils.isBlank(model.getPermission())) {
					return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
				}
			}
			//session 获取用户信息
			User user = ShiroUtils.getUserByToken(form.getToken());
			service.modifyPermission(model,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	/**
	 * 保存权限数据
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "主键id", required = true)
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean detail(Long id, String token){
		try{
			//空值校验
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if(isNull){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			PermissionInfoModel model = service.detail(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);//成功
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
}
