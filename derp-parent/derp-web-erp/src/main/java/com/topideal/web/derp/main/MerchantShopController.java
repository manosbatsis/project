package com.topideal.web.derp.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.enums.ShopTypeEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.dto.main.MerchantShopShipperDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.entity.vo.main.MerchantShopShipperModel;
import com.topideal.service.main.MerchantShopService;
import com.topideal.service.main.MerchantShopShipperService;
import com.topideal.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家店铺 controller层
 * @author lian_
 */
@RequestMapping("/merchantShop")
@Controller
public class MerchantShopController {

	@Autowired
	private MerchantShopService service;
	@Autowired
	private MerchantShopShipperService shopShipperService;
	
	/**
	 * 访问列表页面
	 */
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/main/merchant-shop-list";
	}
	/**
	 * 访问新增页面
	 * @throws SQLException 
	 * */
	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, Long merchantId) throws SQLException {
		List<MerchantInfoModel> merchantBean = service.getMerchant();
		model.addAttribute("merchantBean", merchantBean);
		// 店铺类型
		List<Map<String, Object>> shopTypeList = new ArrayList<>() ;
	       for (ShopTypeEnum shopType : ShopTypeEnum.values()) {
	           Map<String, Object> map = new HashMap<String, Object>();
	           map.put("selectValue", shopType.getKey());
	           map.put("selectLable", shopType.getValue());
	           shopTypeList.add(map);
	       }
	       model.addAttribute("shopTypeList",shopTypeList);
	       // 数据来源
	       
		return "/derp/main/merchant-shop-add";
	}
	/**
	 * 访问编辑页面
	 * @param merchantId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id, Long merchantId) throws Exception {
		
		List<MerchantInfoModel> merchantBean = service.getMerchant();
		MerchantShopRelDTO shopModel = service.searchDetail(id);

		//表体货主公司
		MerchantShopShipperModel shipperModel = new MerchantShopShipperModel();
		shipperModel.setShopId(shopModel.getId());
		List<MerchantShopShipperModel> shipperList = shopShipperService.getListByModel(shipperModel);
		
		//销售类型list
		ArrayList<DerpBasic> saleTypeList = DERP_SYS.shopShipperSaleTypeList;

		int isMerchantRequired=0;//0:非必填 1：必填
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(shopModel.getStoreTypeCode())){
			isMerchantRequired=1;
		}
		
		model.addAttribute("isMerchantRequired", isMerchantRequired);
		model.addAttribute("merchantBean", merchantBean);
		model.addAttribute("detail", shopModel);
		model.addAttribute("shipperList", shipperList);
		model.addAttribute("shipperListSize", shipperList.size());
		model.addAttribute("saleTypeList", saleTypeList);
		return "/derp/main/merchant-shop-edit";
	}
	/**
	 * 访问详情页面
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id) throws Exception {
		MerchantShopRelDTO shopModel = service.searchDetail(id);
		//表体货主公司
		MerchantShopShipperModel shipperModel = new MerchantShopShipperModel();
		shipperModel.setShopId(shopModel.getId());
		List<MerchantShopShipperDTO> shipperList = shopShipperService.getListDTOByModel(shipperModel);
		
		model.addAttribute("detail", shopModel);
		model.addAttribute("shipperList", shipperList);
		return "/derp/main/merchant-shop-details";
	}
	
	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@RequestMapping("/listShop.asyn")
	@ResponseBody
	private ViewResponseBean listShop(MerchantShopRelDTO dto) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = service.getListByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 新增
	 * */
	@RequestMapping("/saveShop.asyn")
	@ResponseBody
	public ViewResponseBean saveShop(String json) {
		try {
			// 实例化JSON对象
	        JSONObject jsonData=JSONObject.fromObject(json);
	        Map classMap = new HashMap();
			classMap.put("shipperList",Map.class);
			MerchantShopRelDTO model = (MerchantShopRelDTO) JSONObject.toBean(jsonData, MerchantShopRelDTO.class,classMap);
			
			User user= ShiroUtils.getUser();
			model.setOperator(user.getName());//操作人员
			service.saveShop(model);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyShop.asyn")
	@ResponseBody
	public ViewResponseBean modifyShop(String json) {
		try{
			// 实例化JSON对象
	        JSONObject jsonData=JSONObject.fromObject(json);
	        Map classMap = new HashMap();
			classMap.put("shipperList",Map.class);
			MerchantShopRelDTO model = (MerchantShopRelDTO) JSONObject.toBean(jsonData, MerchantShopRelDTO.class,classMap);
			
			User user= ShiroUtils.getUser();
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            model.setOperator(user.getName());//操作人员
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = service.modifyShop(model);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(Exception e){
        	e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}

	/**
	 * 根据ID获取商品详情
	 */
	@RequestMapping("/getShopDetails.asyn")
	@ResponseBody
	public ViewResponseBean getShopDetails(Long id) {
		// 校验id是否正确
		boolean isRight = StrUtils.validateId(id);
		if (!isRight) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		MerchantShopRelDTO model = new MerchantShopRelDTO();
		try {
			model = service.searchDetail(id);
		
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(model);
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delShop.asyn")
	@ResponseBody
	public ViewResponseBean delShop(String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = service.delShop(list);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}

	/**
	 * 获取下拉列表
	 * @return
	 */
	@RequestMapping("/getSelectBean.asyn")
	@ResponseBody
	public ViewResponseBean getSelectBean(MerchantShopRelModel model) {
		List<MerchantShopRelModel> result = new ArrayList<MerchantShopRelModel>();
		try {
			result = service.getSelectMerchantShopRelBean(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 根据平台获取店铺下拉列表
	 * @return
	 */
	@RequestMapping("/getShopByPlatformCodeUrl.asyn")
	@ResponseBody
	public ViewResponseBean getShopByPlatformCodeUrl(MerchantShopRelModel model) {
		List<MerchantShopRelModel> result = new ArrayList<MerchantShopRelModel>();
		try {
			User user = ShiroUtils.getUser();
			if (!"1".equals(user.getUserType())) {
				model.setMerchantId(user.getMerchantId());
			}
			model.setStatus(DERP_SYS.MERCHANTSHOPREL_STATUS_1);
			result = service.getListByModel(model);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	/**
	 * 根据查询条件导出店铺信息
	 */
	@RequestMapping("/exportShop.asyn")
	public void exportShop(MerchantShopRelDTO dto, HttpServletResponse response, HttpServletRequest request) {
		//表头信息
		List<Map<String,Object>> shopList = service.getExportListByDTO(dto);

		//店铺信息
		String headSheetName = "店铺信息";
		String[] headColumns = {"店铺类型","店铺统一ID","店铺编码","店铺名称","电商平台","运营类型","开店公司","开店事业部","客户名称","仓库名称","状态"};
		String[] headKeys = {"storeTypeName","shopUnifyId","shopCode","shopName","storePlatformName","shopTypeName","merchantName","buName","customerName","depotName", "status"};

		//店铺货主信息
		List<Map<String,Object>> itemList = shopShipperService.listForExportShipper(dto);

		String itemSheetName = "店铺货主信息";
		String[] itemColumns = {"店铺编码","店铺名称","货主公司","货主事业部"};
		String[] itemKeys = {"shopCode","shopName","merchantName","buName"};
		//生成表格
		ExportExcelSheet headSheet = ExcelUtilXlsx.createSheet(headSheetName, headColumns, headKeys, shopList);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(headSheet) ;
		sheets.add(itemSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		String fileName = "店铺信息";
		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
	}

}
