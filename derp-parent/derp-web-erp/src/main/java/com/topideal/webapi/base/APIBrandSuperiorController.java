package com.topideal.webapi.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.service.base.BrandSuperiorService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 母品牌控制层
 */

@RestController
@RequestMapping("/webapi/system/brandSuperior")
@Api(tags = "母品牌列表")
public class APIBrandSuperiorController {

	@Autowired
	private BrandSuperiorService brandSuperiorService;


	/**
	 * 访问列表页面
	 * @throws SQLException 
	 */
/*	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws SQLException {
		return "/derp/base/brand-superior-list";
	}
*/

	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "获取列表分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "name", value = "母品牌名称"),
	})
	@PostMapping(value="/list.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BrandSuperiorDTO> list(String name,int begin,int pageSize) {
		try {
			BrandSuperiorDTO dto=new BrandSuperiorDTO();
			dto.setName(name);
			dto.setBegin(begin);
			dto.setPageSize(pageSize);
            dto = brandSuperiorService.listByPage(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 新增
	 */
	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "name", value = "母品牌名称"),
			@ApiImplicitParam(name = "ncCode", value = "NC编码"),
			@ApiImplicitParam(name = "priceDeclareRatio", value = "申报系数" ,required = true)
	})
	@PostMapping(value="/save.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean save(String token,String name,String ncCode,Double priceDeclareRatio) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			BrandSuperiorModel model=new BrandSuperiorModel();
			model.setName(name);
			model.setNcCode(ncCode);
			model.setPriceDeclareRatio(priceDeclareRatio);
			brandSuperiorService.save(model,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 根据id获取数据
	 */
	@ApiOperation(value = "根据id获取数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/findById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BrandSuperiorModel> findById(Long id) {		
		try {
			BrandSuperiorModel model = brandSuperiorService.findById(id);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 编辑
	 */
	@ApiOperation(value = "编辑")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "name", value = "母品牌名称",required = true),
			@ApiImplicitParam(name = "ncCode", value = "NC编码"),
			@ApiImplicitParam(name = "priceDeclareRatio", value = "申报系数" ,required = true)

	})
	@PostMapping(value="/modify.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modify(String token,Long id,String name,String ncCode,Double priceDeclareRatio) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			BrandSuperiorModel model=new BrandSuperiorModel();
			model.setId(id);
			model.setName(name);
			model.setNcCode(ncCode);
			model.setPriceDeclareRatio(priceDeclareRatio);
			brandSuperiorService.modify(model,user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 删除
	 */
	@ApiOperation(value = "编辑")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "id集合,多个用英文逗号隔开")
	})
	@PostMapping(value="/delete.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delete(String ids) {
		try {
			List<Long> list = StrUtils.parseIds(ids);
			brandSuperiorService.delete(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取下拉列表
	 * @return
	 */
	@ApiOperation(value = "获取下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getSelectBean.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			result = brandSuperiorService.getSelectBean();
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

}
