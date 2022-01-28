package com.topideal.report.webapi.reporting;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.dto.SettlementPriceExamineDTO;
import com.topideal.entity.dto.SettlementPriceRecordDTO;
import com.topideal.entity.vo.reporting.SettlementPriceModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.report.service.reporting.BrandService;
import com.topideal.report.service.reporting.SettlementPriceService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SettlementPriceExamineForm;
import com.topideal.report.webapi.form.SettlementPriceForm;
import com.topideal.report.webapi.form.SettlementPriceSearchForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 成本单价列表
 */
@RestController
@RequestMapping("/webapi/report/settlementPrice")
@Api(tags = "成本单价列表")
public class APISettlementPriceController {
    @Autowired
    private SettlementPriceService service;//结算价格
    @Autowired
    private BrandService brandService;	//品牌


    /**
     * 访问列表页面
     */
    @ApiOperation(value = "访问列表页面")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true)})
    @PostMapping(value = "/toPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toPage(String token) {
        try {
            List<SelectBean> brandBean = brandService.getSelectBean();
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, brandBean);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 访问编辑页面
     * @throws Exception
     * */
    @ApiOperation(value = "访问编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value = "/toDetailsPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toDetailsPage(Long id) throws Exception {
        SettlementPriceModel sPrice = service.searchDetail(id);
        SettlementPriceModel settlementPrice = new SettlementPriceModel();
        settlementPrice.setMerchantId(sPrice.getMerchantId());
        settlementPrice.setBarcode(sPrice.getBarcode());
        settlementPrice.setBuId(sPrice.getBuId());
        List<SettlementPriceModel> settlementPriceList = service.list(settlementPrice);
        List<SettlementPriceModel> list = new ArrayList<SettlementPriceModel>();
        for(SettlementPriceModel spModel:settlementPriceList){
            String month = service.getMaxMonthByParam(spModel.getMerchantId());
            if(StringUtils.isNotBlank(month)){
                spModel.setStartDate(month);
            }
            list.add(spModel);
        }
        Map map=new HashMap();
        map.put("detail",sPrice);
        map.put("itemList",list);
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);//成功
    }


    /**
     * 获取分页数据
     * @param form
     * @return
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listPrice.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<SettlementPriceDTO> listPrice(SettlementPriceSearchForm form) {
        try {
            SettlementPriceDTO dto=new SettlementPriceDTO();
            BeanUtils.copyProperties(form,dto);

            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto = service.listPriceDTO(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }


    /**
     * 新增
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "/saveSettlementPrice.asyn")
    public ResponseBean saveSettlementPrice(@RequestBody  SettlementPriceForm form) {
        try {
            User user= ShiroUtils.getUserByToken(form.getToken());
            if(form.getItemList().size()==0){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
            // 存储商品信息
            Map<String,Object> map=service.saveSettlementPrice(form.getBuId(),form.getItemList(),user.getMerchantId(),user.getMerchantName() , user);
            String code=(String)map.get("code");
            String message=(String)map.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping(value = "/delPrice.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean delPrice(String ids) {
        try {
            // 校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = service.delPrice(list);
            if (!b) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (SQLException e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015,e);
        }catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }

    /**
     * 修改
     * @param form
     */
    @ApiOperation(value = "修改")
    @PostMapping(value="/modifySettlementPrice.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean modifySettlementPrice(@RequestBody SettlementPriceForm form) {
        try {
            User user= ShiroUtils.getUserByToken(form.getToken());
            if(form == null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
            // 存储商品信息
            boolean b =service.modifySettlementPrice(form.getItemList(), user, form.getBuId(), form.getId());
            if (!b) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }


    /**
     * 获取导出的数量
     */
    @ApiOperation(value = "获取导出的数量")
    @PostMapping(value="/getSettlementPriceCount.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getSettlementPriceCount(HttpServletResponse response, HttpServletRequest request, SettlementPriceSearchForm form) throws Exception{
        Map<String,Object> data=new HashMap<String,Object>();
        try{
            SettlementPriceDTO dto=new SettlementPriceDTO();
            BeanUtils.copyProperties(form,dto);

            User user= ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            List<SettlementPriceDTO> result = service.listSettlementPrice(user,dto);
            data.put("total",result.size());
        }catch(SQLException e){
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015,e);
        }catch(Exception e){
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,data);
    }


    /**
     * 导出
     * */
    @ApiOperation(value = "导出")
    @GetMapping("/exportSettlementPrice.asyn")
    private void exportSettlementPrice(HttpServletResponse response, HttpServletRequest request,SettlementPriceSearchForm form) throws Exception{
        User user= ShiroUtils.getUserByToken(form.getToken());

        SettlementPriceDTO dto=new SettlementPriceDTO();
        BeanUtils.copyProperties(form,dto);

        // 设置商家id
        dto.setMerchantId(user.getMerchantId());

        // 响应结果集
        List<SettlementPriceDTO> result = service.listSettlementPrice(user,dto);
        String sheetName = "标准成本单价列表";
        String[] columns={"商家","事业部","品牌","品类","商品名称","条形码","生效年月","标准成本单价","结算币种","创建日期","修改日期","调价原因","状态"};
        String[] keys = {"merchantName" , "buName","brandName" , "productTypeName" , "goodsName" , "barcode" , "effectiveDate" , "price" , "currencyLabel" , "createDate" , "createDateRecord" , "adjustPriceResult","statusLabel"} ;
        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result);
        //导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }

    /**
     *  导入结算价格信息--有问题，获取不到单位名称、原产国名称
     */
    @ApiOperation(value = "导入结算价格")
    @SuppressWarnings("rawtypes")
    @PostMapping("/importPrice.asyn")
    public ResponseBean importPrice(@RequestParam(value = "file", required = false) MultipartFile file,String token) {
        Map resultMap = new HashMap();// 返回的结果集
        try {
            if (file == null) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
                    file.getOriginalFilename(), 1);
            if (data == null) {// 数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            resultMap = service.importPrice(data, user);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
    }


    /**
     * 获取未关账的最早日期
     */
    @ApiOperation(value = "获取未关账的最早日期")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "barcode", value = "标准条码", required = true),
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @GetMapping("/getMinMonthByStatus.asyn")
    public ResponseBean getMinMonthByStatus(String barcode,String token) {
        String month = "";
        try {
            User user= ShiroUtils.getUserByToken(token);
            if(barcode == null && StringUtils.isBlank(barcode)){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            month = service.getMaxMonthByParam(user.getMerchantId());
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,month);
    }

    /**
     * 获取变更记录分页数据
     * */
    @ApiOperation(value = "获取变更记录分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "begin", value = "开始位置", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
            @ApiImplicitParam(name = "settlementPriceId", value = "结算价格id")
    })
    @PostMapping(value="/listRecordPrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listRecordPrice(Long settlementPriceId,int begin, int pageSize,String token) {
        SettlementPriceRecordDTO dto = new SettlementPriceRecordDTO();
        try {
            dto.setSettlementPriceId(settlementPriceId);
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            // 响应结果集
            dto = service.listRecordPriceDTO(dto);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
    }

    /**
     * 根据商品ID查看组合商品的详情
     * @throws SQLException
     * */
    @ApiOperation(value = "根据商品ID查看组合商品的详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true)
    })
    @PostMapping(value="listAllGroupMerchandiseByGroupId.asyn")
    private ResponseBean listAllGroupMerchandiseByGroupId(Long goodsId) throws SQLException {
        List<MerchandiseInfoModel> groupList=new ArrayList<>();
        // 响应结果集
        //groupList = service.getAllGroupMerchandiseByGroupId(goodsId);

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,groupList);
    }

    /**
     * 获取审批记录分页数据
     * */
    @ApiOperation(value = "获取审批记录分页数据")
    @PostMapping(value="/listExamineList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listExamineList(SettlementPriceExamineForm form) {
        SettlementPriceExamineDTO model=new SettlementPriceExamineDTO();
        try{
            User user= ShiroUtils.getUserByToken(form.getToken());
            BeanUtils.copyProperties(form,model);
            // 设置商家id
            model.setMerchantId(user.getMerchantId());
            // 响应结果集
            model = service.listExamineList(model, user);
        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, model);
    }



    /**
     * 提交审核
     * */
    @ApiOperation(value = "提交审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "ids", required = true)
    })
    @PostMapping(value="/toExamine.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toExamine(String ids,String token) {
        try {
            // 校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                // 输入信息不完整
                return  WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);

            User user= ShiroUtils.getUserByToken(token);

            service.saveAudit(list, user) ;

            return  WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        } catch (SQLException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015);
        } catch (NullPointerException e) {
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
	 * 提交审核
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "ids", required = true)
    })
    @PostMapping(value="/examine.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean examine(String ids , String status,String token) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			if(!(DERP_REPORT.SETTLEMENTPRICE_STATUS_032.equals(status) ||
					DERP_REPORT.SETTLEMENTPRICE_STATUS_033.equals(status))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"单据不为已生效或审核不通过");
			}
			
			List list = StrUtils.parseIds(ids);
			
			User user= ShiroUtils.getUserByToken(token);
			
			service.examine(list, status , user) ;
			
			return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), e.getMessage());
		}
	}
}

