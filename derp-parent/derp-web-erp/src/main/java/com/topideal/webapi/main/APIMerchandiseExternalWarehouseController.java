package com.topideal.webapi.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.ActionTypeEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.main.MerchandiseExternalWarehouseDTO;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.service.main.MerchandiseCatService;
import com.topideal.service.main.MerchandiseExternalWarehouseService;
import com.topideal.shiro.ShiroUtils;
import com.topideal.webapi.form.MerchandiseExternalWarehouseEditForm;
import com.topideal.webapi.form.MerchandiseExternalWarehouseForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/system/merchandiseExternalWarehouse")
@Api(tags = "外仓备案商品列表")
public class APIMerchandiseExternalWarehouseController {
    @Autowired
    private MerchandiseExternalWarehouseService merchandiseExternalWarehouseService;
    // 商品分类
    @Autowired
    private MerchandiseCatService merchandiseCatService;
    /**
     * 获取分页数据
     * @param form
     * @return
     */
    @ApiOperation(value = "获取分页数据")
    @PostMapping(value="/lisMerchandiseExternalWarehouse.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean listMerchandiseExternalWarehouse(MerchandiseExternalWarehouseForm form) {
        MerchandiseExternalWarehouseDTO dto=new MerchandiseExternalWarehouseDTO();
        try {
            BeanUtils.copyProperties(form,dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            dto = merchandiseExternalWarehouseService.getListByPage(dto,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 访问编辑页面
     * @param id st_merchandise_external_warehouse主键
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "访问编辑页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value="/toEditPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toEditPage(Long id) throws Exception {
        try {
            Map<String, Object> resultMap=new HashMap<String, Object>();
            //获取外仓备案商品
            MerchandiseExternalWarehouseDTO detailDTO = merchandiseExternalWarehouseService.getDetailById(id);
            resultMap.put("detail", detailDTO);
            // 获取一级分类
            MerchandiseCatModel merchandiseCatModel1 =new MerchandiseCatModel();
            merchandiseCatModel1.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_0);
            List<SelectBean> oneCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel1);
            resultMap.put("oneCatBean", oneCatBean);
            // 根据一级分类id获取二级分类
            MerchandiseCatModel merchandiseCatModel2 =new MerchandiseCatModel();
            merchandiseCatModel2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);
            merchandiseCatModel2.setParentId(detailDTO.getProductTypeId0());
            List<SelectBean> twoCatBean = merchandiseCatService.getSelectBeanByModel(merchandiseCatModel2);
            resultMap.put("twoCatBean", twoCatBean);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 修改外仓备案商品
     * @return
     */
    @ApiOperation(value = "修改外仓备案商品")
    @PostMapping(value="/modify.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean modify(MerchandiseExternalWarehouseEditForm form) {
        try{
            // 实例化JSON对象
//            JSONObject jsonData=JSONObject.fromObject(json);
//            MerchandiseExternalWarehouseDTO dto = (MerchandiseExternalWarehouseDTO) JSONObject.toBean(jsonData, MerchandiseExternalWarehouseDTO.class);
            MerchandiseExternalWarehouseDTO dto = new MerchandiseExternalWarehouseDTO();
            BeanUtils.copyProperties(form, dto);
            User user = ShiroUtils.getUserByToken(form.getToken());
            //校验id是否正确
            boolean isRight = StrUtils.validateId(form.getId());
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            dto.setModifiyName(user.getName());//操作人员
            //修改时间
            dto.setModifyDate(TimeUtils.getNow());
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            //设置商家名称
            dto.setMerchantName(user.getMerchantName());
            ResponseBean result = merchandiseExternalWarehouseService.saveOrUpdateByDTO(dto, ActionTypeEnum.UPDATE);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            //未知异常
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    /**
     * 根据id删除
     * @param token
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping(value="/deleteById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean deleteById(String token,String ids) {
        try {
            User user= ShiroUtils.getUserByToken(token);
            // 校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//未知异常
            }
            List<Long> list = StrUtils.parseIds(ids);
            Map map=merchandiseExternalWarehouseService.deleteById(user,list);
            String message=(String)map.get("message");
            String code=(String)map.get("code");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);//成功
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);//失败
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 导入商品
     */
    @ApiOperation(value = "导入平台备案商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value="/importMerchandiseWarehouse.asyn",headers = "content-type=multipart/form-data")
    public ResponseBean importMerchandiseWarehouse(String token, @RequestParam(value = "file", required = false) MultipartFile file) {
        Map resultMap = new HashMap();// 返回的结果集

        try{
            if (file == null) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
                    file.getOriginalFilename(), 1);
            if (data == null) {// 数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            resultMap=merchandiseExternalWarehouseService.importMerchandiseWarehouse(data,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }


    /**
     * 根据id查看详情
     * @param id
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据id查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)
    })
    @PostMapping(value="/toDetailsById.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<MerchandiseExternalWarehouseDTO> toDetailsById(Long id,String token) throws Exception {
        try{

            User user = ShiroUtils.getUserByToken(token);
            MerchandiseExternalWarehouseDTO dto=merchandiseExternalWarehouseService.getDetailById(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
        }catch (Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }



    /**
     * 导出商品信息
     * @param response
     * @param request
     * @throws Exception
     */
    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportMerchandiseExternalWarehouse.asyn")
    public void exportMerchandiseExternalWarehouse(MerchandiseExternalWarehouseForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 设置商家id
            MerchandiseExternalWarehouseDTO model=new MerchandiseExternalWarehouseDTO();
            BeanUtils.copyProperties(form,model);

            List<MerchandiseExternalWarehouseDTO> list=merchandiseExternalWarehouseService.exportMerchandiseExternalWarehouse(model,user);
            String[] COLUMNS= {"所属公司",
                    "平台商品货号","商品条形码","平台备案关区","商品名称","商品英文名称","经分销货号","SKU编码","售卖商品名称（中文）","账册备案料号",
                                "海关商品备案号","计量单位","商品毛重","商品净重","备案单价","原产国","原产地区","保质天数",
                    "数据来源","一级品类","二级品类","工厂编码",
                               "生产企业名称","生产厂家地址","商品描述","商品品牌","HS编码","规格型号","商品成分","金二项号","申报要素","箱规","托规","长","宽","高","体积",
                                "包装方式","第一法定单位","第二法定单位","品牌类型","EMS编码"};
            String[] KEYS= {"merchantName","goodsNo","barcode","customsAreaName","goodsName","englishGoodsName", "derpGoodsNo","skuCode","saleGoodNames", "materialAccount" ,"customsGoodsRecordNo","unit","grossWeight",
                            "netWeight","filingPrice","countyName","countyArea","shelfLifeDays", "sourceLabel","productTypeName0","productTypeName","factoryNo","manufacturer","manufacturerAddr","describe",
                            "brandName","hsCode","spec","component","jinerxiang","declareFactor","boxToUnit","torrToUnit","length","width","height","volume","packType","unitNameOne","unitNameTwo","brandType","emsCode"};

            String sheetName = "商品信息导出";
            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , list);
            //导出文件
            FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
