package com.topideal.web.derp.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.service.main.DepartmentInfoService;
import com.topideal.service.main.MerchantBuRelService;
import com.topideal.service.main.MerchantDepotBuRelService;
import com.topideal.service.user.UserBuRelService;
import com.topideal.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.service.main.BusinessUnitService;

@Controller
@RequestMapping("/businessUnit")
public class BusinessUnitController {

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

	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/main/business-unit-list";
	}

	/**
	 * 新增时自动生成code
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/getCode.asyn")
	@ResponseBody
	public ViewResponseBean getCode() {
		String code = null;
		try {
			code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_SYB);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}

		return ResponseFactory.success(code);
	}

	/**
	 * 列表所需数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/listBusinessUnits.asyn")
	@ResponseBody
	private ViewResponseBean listBusinessUnits(BusinessUnitDTO dto) {
		try {
			dto = businessUnitService.listBusinessUnits(dto);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);

		}

		return ResponseFactory.success(dto);
	}


	/**
	 * 获取部门下拉
	 * @return
	 */
	@RequestMapping("/getDepartSelectBean.asyn")
	@ResponseBody
	private ViewResponseBean listSelectBean() {
		try {
			List<SelectBean> list=departmentInfoService.getSelectBean();
			return ResponseFactory.success(list);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}


	/**
	 * 保存、修改数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveBusinessUnit.asyn")
	@ResponseBody
	private ViewResponseBean saveBusinessUnit(BusinessUnitDTO modelDTO) {
		try {

			Map<String, Object> retMap = new HashMap<String, Object>();
			// 非空校验
			if (StringUtils.isBlank(modelDTO.getCode()) || StringUtils.isBlank(modelDTO.getName())) {
				retMap.put("code", "99");
				retMap.put("message", "输入信息不完整！");
				return ResponseFactory.success(retMap);
			}

			// 校验编码是否已存在
			BusinessUnitDTO queryModel = new BusinessUnitDTO();
			queryModel.setId(modelDTO.getId());
			queryModel.setCode(modelDTO.getCode());
			List<BusinessUnitDTO> list = businessUnitService.getcheckCodeName(queryModel);
			if (list != null && list.size() > 0) {
				retMap.put("code", "99");
				retMap.put("message", "编码已存在！");
				return ResponseFactory.success(retMap);
			}
			// 校验名称是否已存在
			queryModel = new BusinessUnitDTO();
			queryModel.setId(modelDTO.getId());
			queryModel.setName(modelDTO.getName());
			list = businessUnitService.getcheckCodeName(queryModel);
			if (list != null && list.size() > 0) {
				retMap.put("code", "99");
				retMap.put("message", "名称已存在！");
				return ResponseFactory.success(retMap);
			}
			BusinessUnitModel saveModel = new BusinessUnitModel();
			saveModel.setId(modelDTO.getId());
			saveModel.setName(modelDTO.getName());
			saveModel.setDepartmentId(modelDTO.getDepartmentId());
			saveModel.setDepartmentName(modelDTO.getDepartmentName());
			if (modelDTO.getId() == null) {
				saveModel.setCode(modelDTO.getCode());
				businessUnitService.save(saveModel);
			} else {
				businessUnitService.modify(saveModel);
			}
			retMap.put("code", "00");
			retMap.put("message", "成功");
			return ResponseFactory.success(retMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}

	}

	/**
	 * 查询详情
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail.asyn")
	@ResponseBody
	private ViewResponseBean detail(BusinessUnitModel model) {
		try {

			model = businessUnitService.query(model);

			return ResponseFactory.success(model);

		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}

	}

	/**
	 * 删除
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/delBusinessUnits.asyn")
	@ResponseBody
	private ViewResponseBean delBusinessUnits(BusinessUnitModel model) {
		try {

			// 判断是否引用
			boolean flag = businessUnitService.judgeQuote(model);

			if (flag) {
				return ResponseFactory.error(StateCodeEnum.ERROR_313);
			}

			flag = businessUnitService.delBusinessUnits(model);

			if (!flag) {
				ResponseFactory.error(StateCodeEnum.ERROR_301);
			}

		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}

		return ResponseFactory.success();
	}

	/**
	 * 根据页面传入关联仓库获取此商家下仓库关联的事业部下拉框，为空时则获取商家关联的所有事业部
	 */
	@RequestMapping("/getBUSelectBeanByMerchantDepotRel.asyn")
	@ResponseBody
	public ViewResponseBean getBUSelectBeanByMerchantDepotRel(MerchantDepotBuRelDTO dto) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {

			User user = ShiroUtils.getUser();
			if ("1".equals(user.getUserType()) && dto.getMerchantId() == null) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}

			MerchantBuRelModel merchantBuRelModel = new MerchantBuRelModel();

			if (dto.getMerchantId() == null) {
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
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 根据页面传入多个关联仓库获取此商家下仓库关联的事业部下拉框，为空时则获取商家关联的所有事业部（多个仓库的情况）
	 */
	@RequestMapping("/getBUSelectBeanByManyDepot.asyn")
	@ResponseBody
	public ViewResponseBean getBUSelectBeanByManyDepot(MerchantDepotBuRelDTO dto) {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {

			User user = ShiroUtils.getUser();
			if ("1".equals(user.getUserType()) && dto.getMerchantId() == null) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
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
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	@RequestMapping("getAllSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getAllSelectBean() {
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
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
}
