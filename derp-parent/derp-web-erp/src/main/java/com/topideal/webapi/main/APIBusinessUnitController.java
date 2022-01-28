package com.topideal.webapi.main;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.service.main.BusinessUnitService;
import com.topideal.service.main.DepartmentInfoService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantDepotBuRelService;
import com.topideal.service.user.UserBuRelService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.MerchantDepotBuForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/system/businessUnit")
@Api(tags = "事业部")
public class APIBusinessUnitController {

	@Autowired
	private BusinessUnitService businessUnitService;

	@Autowired
	private MerchantBuRelService merchantBuRelService;

	@Autowired
	private MerchantDepotBuRelService merchantDepotBuRelService;

	@Autowired
	private UserBuRelService userBuRelService;

	@Autowired
	private DepartmentInfoService departmentInfoService;

	/**
	 * 新增时自动生成code
	 * 
	 * @return
	 * @throws SQLException
	 */
	@ApiOperation(value = "新增时自动生成code")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getCode.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getCode() {
		String code = null;
		try {
			code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_SYB);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,code);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 列表所需数据
	 * 
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "code", value = "事业部编码"),
			@ApiImplicitParam(name = "name", value = "事业部名称"),
			@ApiImplicitParam(name = "departmentName", value = "部门名称")
	})
	@PostMapping(value="/listBusinessUnits.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BusinessUnitDTO> listBusinessUnits(String code,String name,String departmentName, Integer begin,Integer pageSize) {
		try {
			BusinessUnitDTO dto=new BusinessUnitDTO();
			dto.setCode(code);
			dto.setName(name);
			dto.setDepartmentName(departmentName);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
			dto = businessUnitService.listBusinessUnits(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取部门下拉
	 * @return
	 */
	@RequestMapping("/getDepartSelectBean.asyn")
	@ResponseBody
	private ResponseBean<List<SelectBean>> listSelectBean() {
		try {
			List<SelectBean> list=departmentInfoService.getSelectBean();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 保存、修改数据
	 * 
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "保存、修改数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "code", value = "事业部编码", required = true),
			@ApiImplicitParam(name = "name", value = "事业部名称", required = true),
			@ApiImplicitParam(name = "departmentId", value = "部门id"),
			@ApiImplicitParam(name = "departmentName", value = "部门名称"),
			@ApiImplicitParam(name = "id", value = "id新增id为空,修改id不能为空")

	})
	@PostMapping(value="/saveBusinessUnit.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean saveBusinessUnit(String code,String name,Long id,Long departmentId,String departmentName) {
		try {			
			// 非空校验
			if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//数据为空
			}
			BusinessUnitDTO modelDTO=new BusinessUnitDTO();
			modelDTO.setId(id);
			modelDTO.setName(name);
			modelDTO.setCode(code);
			
			// 校验编码是否已存在
			BusinessUnitDTO queryModel = new BusinessUnitDTO();
			queryModel.setId(modelDTO.getId());
			queryModel.setCode(modelDTO.getCode());
			List<BusinessUnitDTO> list = businessUnitService.getcheckCodeName(queryModel);
			if (list != null && list.size() > 0) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10012);//数据应存在
			}
			// 校验名称是否已存在
			queryModel = new BusinessUnitDTO();
			queryModel.setId(modelDTO.getId());
			queryModel.setName(modelDTO.getName());
			list = businessUnitService.getcheckCodeName(queryModel);
			if (list != null && list.size() > 0) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10012);//数据应存在
			}
			BusinessUnitModel saveModel = new BusinessUnitModel();
			saveModel.setId(modelDTO.getId());
			saveModel.setName(modelDTO.getName());
			saveModel.setDepartmentId(departmentId);
			saveModel.setDepartmentName(departmentName);
			if (modelDTO.getId() == null) {
				saveModel.setCode(modelDTO.getCode());
				businessUnitService.save(saveModel);
			} else {
				businessUnitService.modify(saveModel);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}

	/**
	 * 查询详情
	 * 
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "查询详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/detail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BusinessUnitModel> detail(Long id) {
		try {
			BusinessUnitModel model=new BusinessUnitModel();
			model.setId(id);
			model = businessUnitService.query(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 删除
	 * 
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id", required = true)
	})
	@PostMapping(value="/delBusinessUnits.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean delBusinessUnits(Long id) {
		try {
			BusinessUnitModel model=new BusinessUnitModel();
			model.setId(id);
			// 判断是否引用
			boolean flag = businessUnitService.judgeQuote(model);
			if (flag) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10013);//未知异常
			}

			flag = businessUnitService.delBusinessUnits(model);
			if (!flag) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	@ApiOperation(value = "获取所有数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "事业部id"),
			@ApiImplicitParam(name = "code", value = "事业部编码"),
			@ApiImplicitParam(name = "name", value = "事业部名称"),
			@ApiImplicitParam(name = "departmentId", value = "部门id"),
			@ApiImplicitParam(name = "departmentName", value = "部门名称"),
			@ApiImplicitParam(name = "departmentIds", value = "部门id集合")
	})
	@PostMapping(value="/listDTO.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<BusinessUnitDTO>> listDTO(String token ,String code,String name,Long id,Long departmentId,String departmentName,String departmentIds) {
		try {
			BusinessUnitDTO dto=new BusinessUnitDTO();
			dto.setCode(code);
			dto.setName(name);
			dto.setDepartmentName(departmentName);
			dto.setDepartmentId(departmentId);
			dto.setId(id);
			dto.setDepartmentIds(departmentIds);
			User user = ShiroUtils.getUserByToken(token);
			List<BusinessUnitDTO> list = businessUnitService.listDTO(dto, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 根据页面传入关联仓库获取此商家下仓库关联的事业部下拉框，为空时则获取商家关联的所有事业部
	 */
	@ApiOperation(value = "根据商家仓库加载事业部",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@PostMapping("/getBUSelectBeanByMerchantDepotRel.asyn")
	public ResponseBean<List<SelectBean>> getBUSelectBeanByMerchantDepotRel(MerchantDepotBuForm form) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
            //参数
			MerchantDepotBuRelDTO dto = new MerchantDepotBuRelDTO();
			if(StringUtils.isNotBlank(form.getMerchantId())) dto.setMerchantId(Long.valueOf(form.getMerchantId()));
			if(StringUtils.isNotBlank(form.getDepotId1())) dto.setDepotId1(Long.valueOf(form.getDepotId1()));
			if(StringUtils.isNotBlank(form.getDepotId2())) dto.setDepotId2(Long.valueOf(form.getDepotId2()));

			User user = ShiroUtils.getUserByToken(form.getToken());
			//用户是管理员时需指定公司id
			/*
			 * if(DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType()) &&
			 * StringUtils.isBlank(form.getMerchantId())) { return
			 * WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10010);//公司id为空 }
			 */

			MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();

			if (dto.getMerchantId()==null) {
				dto.setMerchantId(user.getMerchantId());
			}
			merchantBuRelModel.setMerchantId(dto.getMerchantId());

			List<UserBuRelModel> userBuRelList = userBuRelService.getBuByUserId(user.getId());
			List<SelectBean> tempResult = merchantBuRelService.getSelectBean(merchantBuRelModel);

			// 取当前用户事业部和公司事业部交集
			result = new ArrayList<SelectBean>();

			if (!userBuRelList.isEmpty()) {
				
				Iterator<SelectBean> iterator = tempResult.iterator();

				while (iterator.hasNext()) {
					SelectBean selectBean = iterator.next();
					
					for (UserBuRelModel userBuRelModel : userBuRelList) {
						if (selectBean.getValue().equals(userBuRelModel.getBuId().toString())) {
							result.add(selectBean) ;
						}
					}

				}
			}
			// 管理员
			if(DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())){
				Iterator<SelectBean> iteratorTemp = tempResult.iterator();
				while (iteratorTemp.hasNext()) {
					SelectBean selectBean = iteratorTemp.next();
					result.add(selectBean) ;
				}
				if (dto.getDepotId1() != null || dto.getDepotId2() != null) {
					tempResult = merchantDepotBuRelService.getSelectBean(dto);
						Iterator<SelectBean> iterator = result.iterator();
						while (iterator.hasNext()) {
							SelectBean selectBean = iterator.next();
							boolean flag = false;
							for (SelectBean tempBean : tempResult) {
								if (selectBean.getValue().equals(tempBean.getValue())) {
									flag = true;
									break;
								}
							}
							if (!flag) {
								iterator.remove();
							}
						}
				}
			}else{
				if (dto.getDepotId1() != null || dto.getDepotId2() != null) {

					tempResult = merchantDepotBuRelService.getSelectBean(dto);

					// 若用户事业部关联为空，返回空
					if (userBuRelList.isEmpty()) {
						result = new ArrayList<SelectBean>();
					} else {
						Iterator<SelectBean> iterator = result.iterator();

						while (iterator.hasNext()) {
							SelectBean selectBean = iterator.next();

							boolean flag = false;
							for (SelectBean tempBean : tempResult) {
								if (selectBean.getValue().equals(tempBean.getValue())) {
									flag = true;
									break;
								}
							}

							if (!flag) {
								iterator.remove();
							}

						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
	}
	

	/**
	 * 根据页面传入多个关联仓库获取此商家下仓库关联的事业部下拉框，为空时则获取商家关联的所有事业部（多个仓库的情况）
	 */
	@ApiOperation(value = "根据页面传入多个关联仓库获取此商家下仓库关联的事业部下拉框，为空时则获取商家关联的所有事业部（多个仓库的情况）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "merchantId", value = "商家id"),
			@ApiImplicitParam(name = "depotIdList", value = "仓库id,多个用逗号隔开")
	})
	@PostMapping(value="/getBUSelectBeanByManyDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean> > getBUSelectBeanByManyDepot(String token,Long merchantId,@RequestParam(value = "depotIdList")List<Long> depotIdList) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			MerchantDepotBuRelDTO dto=new MerchantDepotBuRelDTO();
			dto.setMerchantId(merchantId);
			dto.setDepotIdList(depotIdList);
			User user = ShiroUtils.getUserByToken(token);
			if ("1".equals(user.getUserType()) && dto.getMerchantId() == null) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}

			MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();

			if (dto.getMerchantId() == null) {
				dto.setMerchantId(user.getMerchantId());
			}
			merchantBuRelModel.setMerchantId(dto.getMerchantId());

			List<UserBuRelModel> userBuRelList = userBuRelService.getBuByUserId(user.getId());
			if (dto.getDepotIdList() == null || dto.getDepotIdList().size() == 0) {
				result = merchantBuRelService.getSelectBean(merchantBuRelModel);

				// 如果不为管理员，取当前用户事业部
				if (!"1".equals(user.getUserType())) {
					result = new ArrayList<SelectBean>();

					if (!userBuRelList.isEmpty()) {
						for (UserBuRelModel userBuRelModel : userBuRelList) {
							SelectBean selectBean = new SelectBean();
							selectBean.setLabel(userBuRelModel.getBuName());
							selectBean.setValue(userBuRelModel.getBuId().toString());

							result.add(selectBean);
						}
					}
				}

			} else {
				result = merchantDepotBuRelService.getSelectBean2(dto);

				// 如果不为管理员，与当前用户事业部取交集
				if (!"1".equals(user.getUserType())) {
					// 若用户事业部关联为空，返回空
					if (userBuRelList.isEmpty()) {
						result = new ArrayList<SelectBean>();
					} else {
						Iterator<SelectBean> iterator = result.iterator();

						while (iterator.hasNext()) {
							SelectBean selectBean = iterator.next();

							boolean flag = false;
							for (UserBuRelModel userBuRelModel : userBuRelList) {
								if (selectBean.getValue().equals(userBuRelModel.getBuId().toString())) {
									flag = true;
									break;
								}
							}

							if (!flag) {
								iterator.remove();
							}

						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		
	}

	@ApiOperation(value = "根据页面传入多个关联仓库获取此商家下仓库关联的事业部下拉框，为空时则获取商家关联的所有事业部（多个仓库的情况）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getAllSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean> > getAllSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			List<BusinessUnitModel> list = businessUnitService.getAllList();

			for (BusinessUnitModel businessUnitModel : list) {
				SelectBean selectBean = new SelectBean();

				selectBean.setLabel(businessUnitModel.getName());
				selectBean.setValue(businessUnitModel.getId().toString());

				result.add(selectBean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
	}
	
	@ApiOperation(value = "获取用户关联事业部下拉")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getSelectBeanByUserId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getSelectBeanByUserId(String token) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			List<UserBuRelModel> userBuRelList = userBuRelService.getBuByUserId(user.getId());	
			userBuRelList = userBuRelList.stream().sorted(Comparator.comparing(UserBuRelModel::getBuCode)).collect(Collectors.toList());
			for (UserBuRelModel model : userBuRelList) {
				SelectBean selectBean = new SelectBean();
				selectBean.setLabel(model.getBuName());
				selectBean.setValue(model.getBuId().toString());
				result.add(selectBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
	}
	@ApiOperation(value = "根据用户关联事业部获取部门下拉")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getDepartSelectBeanByUserId.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Set<SelectBean>> getDepartSelectBeanByUserId(String token) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			List<UserBuRelModel> userBuRelList = userBuRelService.getBuByUserId(user.getId());
			List<Long> buIds = userBuRelList.stream().map(UserBuRelModel :: getBuId).collect(Collectors.toList());
			result = businessUnitService.getDepartSelectBeanByUserId(buIds);

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
	}
}
