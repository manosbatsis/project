package com.topideal.web.derp.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.ProductInfoDTO;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.base.BrandService;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.ProductInfoService;
import com.topideal.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

/**
 * 产品管理 控制层
 */
@RequestMapping("/product")
@Controller
public class ProductInfoController {

	//品牌信息service
	@Autowired
	private BrandService brandService;
	//货品
	@Autowired
	private ProductInfoService productInfoService;
	// 商品分类
	@Autowired
	private MerchandiseCatService merchandiseCatService;
	
	/**
	 * 访问列表页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		// 获取二级类目
		MerchandiseCatModel merchandiseCatModel =new MerchandiseCatModel();
		merchandiseCatModel.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
		List<SelectBean> productTypeBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel);
		model.addAttribute("productTypeBean", productTypeBean);
		return "/derp/main/product-list";
	}
	
	/**
	 * 访问详情页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		ProductInfoDTO merchandiseInfo = productInfoService.searchDetail(id);
		model.addAttribute("detail", merchandiseInfo);
		return "/derp/main/product-details";
	}
	
	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id)throws Exception{
		ProductInfoDTO merchandiseInfo = productInfoService.searchDetail(id);
		model.addAttribute("detail", merchandiseInfo);
		// 获取一级分类
		MerchandiseCatModel merchandiseCatModel1 =new MerchandiseCatModel();
		merchandiseCatModel1.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
		List<SelectBean> oneCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel1);
		model.addAttribute("oneCatBean", oneCatBean);
		// 根据一级分类id获取二级分类
		MerchandiseCatModel merchandiseCatModel2 =new MerchandiseCatModel();
		merchandiseCatModel2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
		merchandiseCatModel2.setParentId(merchandiseInfo.getProductTypeId0());
		List<SelectBean> twoCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel2);
		model.addAttribute("twoCatBean", twoCatBean);	
		/*List<SelectBean> productTypeBean = merchandiseCatService.getSelectBean();
		model.addAttribute("productTypeBean", productTypeBean);*/
		List<SelectBean> brandBean = brandService.getSelectBean();
		model.addAttribute("brandBean", brandBean);
		return "/derp/main/product-edit";
	}
	
	
	/**
	 * 获取分页数据 
	 * @param model 商品信息
	 * @return
	 */
	@RequestMapping("/listProduct.asyn")
	@ResponseBody
	private ViewResponseBean listProduct(MerchandiseInfoDTO dto) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = productInfoService.getListByPage(dto);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 修改
	 * */
	@RequestMapping("/modifyProduct.asyn")
	@ResponseBody
	public ViewResponseBean modifyProduct(ProductInfoModel model) throws SQLException {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateId(model.getId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user= ShiroUtils.getUser();
            //修改时间
            model.setModifyDate(TimeUtils.getNow());
            boolean b = productInfoService.modifyProduct(model);
            if(!b){
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
	
	/**
	 * 批量修改
	 * */
	@RequestMapping("/modifyBatchProduct.asyn")
	@ResponseBody
	public ViewResponseBean modifyBatchProduct(String ids, String productTypeId) throws SQLException {
		try{
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			User user= ShiroUtils.getUser();
			List <Long>list = StrUtils.parseIds(ids);
			boolean b = productInfoService.updateBatch(list,productTypeId);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
	}
	
	/**
	 * 根据ID获取货品详情
	 * @param id  产品ID
	 * @return
	 */
	@RequestMapping("/getProductDetails.asyn")
	@ResponseBody
	public ViewResponseBean getProductDetails(Long id) {
		//校验id是否正确
        boolean isRight = StrUtils.validateId(id);
        if(!isRight){
            //输入信息不完整
            return ResponseFactory.error(StateCodeEnum.ERROR_303);
        }
        ProductInfoDTO model = new ProductInfoDTO();
		try{
			model = productInfoService.searchDetail(id);
        }catch(SQLException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
        }catch(NullPointerException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(model);
	}
	
}
