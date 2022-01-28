/*
 * package com.topideal.report.web.reporting;
 * 
 * import java.sql.SQLException; import java.util.HashMap; import
 * java.util.List; import java.util.Map;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * com.topideal.common.constant.DERP_SYS; import
 * com.topideal.common.system.auth.User; import
 * com.topideal.common.system.bean.SelectBean; import
 * com.topideal.common.system.web.ResponseFactory; import
 * com.topideal.common.system.web.StateCodeEnum; import
 * com.topideal.common.system.web.ViewResponseBean; import
 * com.topideal.common.tools.StrUtils; import
 * com.topideal.entity.dto.SettlementPriceWarnconfigDTO; import
 * com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel; import
 * com.topideal.entity.vo.system.MerchantBuRelModel; import
 * com.topideal.entity.vo.system.MerchantInfoModel; import
 * com.topideal.mongo.dao.MerchantBuRelMongoDao; import
 * com.topideal.mongo.entity.MerchantBuRelMongo; import
 * com.topideal.report.service.reporting.MerchantInfoService; import
 * com.topideal.report.service.reporting.SettlementPriceWarnconfigService;
 * import com.topideal.report.shiro.ShiroUtils;
 * 
 *//**
	 * 标准成本单价预警配置
	 * 
	 * @author wenyan
	 */
/*
 * @Controller
 * 
 * @RequestMapping("/settlementPriceWarnconfig") public class
 * SettlementPriceWarnconfigConroller {
 * 
 * @Autowired private SettlementPriceWarnconfigService
 * settlementPriceWarnconfigService;// 标准成本单价预警配置信息service
 * 
 * @Autowired private MerchantInfoService merchantInfoService ;
 *//**
	 * 访问列表页面
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(Model model) throws
 * SQLException { User user = ShiroUtils.getUser(); MerchantInfoModel merchant =
 * new MerchantInfoModel() ; List<SelectBean> merchantList =
 * merchantInfoService.getSelectBean(merchant);
 * model.addAttribute("merchantList", merchantList);
 * if("1".equals(user.getUserType())) { // 超级管理员 model.addAttribute("userType",
 * 1); }else{ model.addAttribute("userType", 2); } return
 * "/derp/reporting/settlement-price-warnconfig-list"; }
 * 
 *//**
	 * 访问新增页面
	 * 
	 * @throws SQLException
	 */
/*
 * @RequestMapping("/toAddPage.html") public String toAddPage(Model model, Long
 * id) throws SQLException { User user = ShiroUtils.getUser(); MerchantInfoModel
 * merchant = new MerchantInfoModel() ; List<SelectBean> merchantList =
 * merchantInfoService.getSelectBean(merchant);
 * model.addAttribute("merchantList", merchantList);
 * if("1".equals(user.getUserType())) { // 超级管理员 model.addAttribute("userType",
 * 1); }else{ model.addAttribute("userType", 2); } return
 * "/derp/reporting/settlement-price-warnconfig-add"; }
 * 
 * 
 *//**
	 * 访问详情页面
	 */
/*
 * @RequestMapping("/toDetailsPage.html") public String toDetailsPage(Model
 * model, Long id) throws Exception { SettlementPriceWarnconfigDTO detail =
 * settlementPriceWarnconfigService.searchDetail(id);
 * model.addAttribute("detail", detail); return
 * "/derp/reporting/settlement-price-warnconfig-details"; }
 * 
 *//**
	 * 访问编辑页面
	 */
/*
 * @RequestMapping("/toEditPage.html") public String toEditPage(Model model,
 * Long id) throws Exception { User user = ShiroUtils.getUser();
 * MerchantInfoModel merchant = new MerchantInfoModel() ; List<SelectBean>
 * merchantList = merchantInfoService.getSelectBean(merchant);
 * model.addAttribute("merchantList", merchantList); // 查询邮件发送配置详情
 * SettlementPriceWarnconfigDTO detail =
 * settlementPriceWarnconfigService.searchDetail(id);
 * model.addAttribute("detail", detail); if("1".equals(user.getUserType())) { //
 * 超级管理员 model.addAttribute("userType", 1); }else{
 * model.addAttribute("userType", 2); } return
 * "/derp/reporting/settlement-price-warnconfig-edit"; }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/listSettlementPriceWarnconfig.asyn")
 * 
 * @ResponseBody private ViewResponseBean
 * listSettlementPriceWarnconfig(SettlementPriceWarnconfigDTO dto) { try { //
 * 响应结果集 User user = ShiroUtils.getUser(); if(!"1".equals(user.getUserType())) {
 * dto.setMerchantId(user.getMerchantId()); } dto =
 * settlementPriceWarnconfigService.listEmail(dto); } catch (SQLException e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception
 * e) { return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
 * ResponseFactory.success(dto); }
 * 
 * 
 *//**
	 * 新增
	 */
/*
 * @RequestMapping("/saveSettlementPriceWarnconfig.asyn")
 * 
 * @ResponseBody public ViewResponseBean
 * saveSettlementPriceWarnconfig(SettlementPriceWarnconfigModel model) {
 * Map<String, Object> saveEmail=new HashMap<>(); try { User user =
 * ShiroUtils.getUser();
 * 
 * saveEmail = settlementPriceWarnconfigService.saveEmail(model,user); } catch
 * (Exception e) { return ResponseFactory.error(StateCodeEnum.ERROR_302, e); }
 * return ResponseFactory.success(saveEmail); }
 * 
 *//**
	 * 删除
	 */
/*
 * @RequestMapping("/delSettlementPriceWarnconfig.asyn")
 * 
 * @ResponseBody public ViewResponseBean delSettlementPriceWarnconfig(String
 * ids) { try { // 校验id是否正确 boolean isRight = StrUtils.validateIds(ids); if
 * (!isRight) { // 输入信息不完整 return
 * ResponseFactory.error(StateCodeEnum.ERROR_303); } List list =
 * StrUtils.parseIds(ids); boolean
 * b=settlementPriceWarnconfigService.delEmail(list); if (!b) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_301); } } catch (SQLException e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_302, e); } catch
 * (NullPointerException e) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
 * ResponseFactory.success(); }
 * 
 *//**
	 * 修改
	 */
/*
 * @RequestMapping("/modifySettlementPriceWarnconfig.asyn")
 * 
 * @ResponseBody public ViewResponseBean
 * modifySettlementPriceWarnconfig(SettlementPriceWarnconfigModel model) {
 * Map<String, Object> modifyEmail=new HashMap<>(); try { // 校验id是否正确 boolean
 * isRight = StrUtils.validateId(model.getId()); if (!isRight) { // 输入信息不完整
 * return ResponseFactory.error(StateCodeEnum.ERROR_303); } User user =
 * ShiroUtils.getUser(); modifyEmail =
 * settlementPriceWarnconfigService.modifyEmail(model,user);
 * 
 * } catch (SQLException e) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_302, e); } catch
 * (NullPointerException e) { return
 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
 * ResponseFactory.success(modifyEmail); }
 * 
 *//**
	 * 根据id禁用和启用 状态(1启用,0禁用)
	 * 
	 * @return
	 *//*
		 * @RequestMapping("/auditSettlementPriceWarnconfig.asyn")
		 * 
		 * @ResponseBody public ViewResponseBean auditSettlementPriceWarnconfig(Long id
		 * , String status) { try { User user = ShiroUtils.getUser(); //校验id是否正确 boolean
		 * isRight = StrUtils.validateId(id); if(!isRight){ //输入信息不完整 return
		 * ResponseFactory.error(StateCodeEnum.ERROR_303); } boolean b =
		 * settlementPriceWarnconfigService.audit(id,status,user); if (!b) { return
		 * ResponseFactory.error(StateCodeEnum.ERROR_301); } } catch (SQLException e) {
		 * return ResponseFactory.error(StateCodeEnum.ERROR_302, e); } catch
		 * (NullPointerException e) { return
		 * ResponseFactory.error(StateCodeEnum.ERROR_304, e); } catch (Exception e) {
		 * return ResponseFactory.error(StateCodeEnum.ERROR_305, e); } return
		 * ResponseFactory.success(); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 */