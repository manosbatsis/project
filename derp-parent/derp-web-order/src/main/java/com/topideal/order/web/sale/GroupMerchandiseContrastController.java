package com.topideal.order.web.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailModel;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.sale.GroupMerchandiseContrastService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 组合商品对照表 控制层
 * @Author: Chen Yiluan
 * @Date: 2019/09/16 16:42
 **/
@Controller
@RequestMapping("/groupMerchandiseContrast")
public class GroupMerchandiseContrastController {

    /** 标题  */
    private static final String[] COLUMNS= {"店铺编码","店铺名称","状态","商家","商品ID","商品名称","单品条形码","单品商品货号","单品商品名称","数量","创建时间","修改时间"};

    private static final String[] KEYS = {"shopCode", "shopName", "statusLabel", "merchantName", "skuId", "groupGoodsName", "barcode", "goodsNo", "goodsName", "num", "createDate", "modifyDate"} ;
    @Autowired
    private GroupMerchandiseContrastService groupMerchandiseContrastService;

    /**
     * 访问列表页面
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        List<SelectBean> merchantSelectBean = groupMerchandiseContrastService.getMerchantSelectBean();
        model.addAttribute("merchantSelectBean", merchantSelectBean);
        return "/derp/sale/groupMerchandiseContrast-list";
    }

    /**
     * 访问新增页面
     */
    @RequestMapping("/toAddPage.html")
    public String toAddPage(Model model,Long id) throws Exception {
        return "/derp/sale/groupMerchandiseContrast-add";
    }

    /**
     * 访问详情页面
     */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Model model, Long id) throws Exception {
        GroupMerchandiseContrastDTO dto = groupMerchandiseContrastService.getDTODetail(id);
        model.addAttribute("detail", dto);
        return "/derp/sale/groupMerchandiseContrast-details";
    }

    /**
     * 访问编辑页面
     */
    @RequestMapping("/toEditPage.html")
    public String toEditPage(Model model, Long id) throws Exception {
        GroupMerchandiseContrastDTO dto = groupMerchandiseContrastService.getDTODetail(id);
        model.addAttribute("detail", dto);
        return "/derp/sale/groupMerchandiseContrast-edit";
    }

    /**
     * 保存
     */
    @RequestMapping("/saveGroupMerchandiseContrast.asyn")
    @ResponseBody
    public ViewResponseBean saveGroupMerchandiseContrast(GroupMerchandiseContrastModel model, String goodsItem) {
        try {
            User user = ShiroUtils.getUser();
            boolean isNull = new EmptyCheckUtils().addObject(model.getShopId()).addObject(model.getShopName())
                    .addObject(model.getMerchantId()).addObject(model.getSkuId()).addObject(model.getGroupGoodsName())
                    .empty();
            if (isNull) {
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }


            JSONArray jsonArray = JSONArray.fromObject(goodsItem);
            List<GroupMerchandiseContrastDetailModel> detailModelList = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) object;
                if (!(json.containsKey("goodsNo") || json.containsKey("goodsId") || json.containsKey("goodsName")
                        || json.containsKey("barcode")|| json.containsKey("num") || json.containsKey("price"))) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_303);
                }

                String goodsNo = json.getString("goodsNo");
                Long goodsId = json.getLong("goodsId");
                String goodsName = json.getString("goodsName");
                String barcode = json.getString("barcode");
                String brandName = json.containsKey("brandName") ? json.getString("brandName") : null;
                Integer num = json.getInt("num");
                double price = json.getDouble("price");
                GroupMerchandiseContrastDetailModel detailModel = new GroupMerchandiseContrastDetailModel();
                detailModel.setGoodsId(goodsId);
                detailModel.setGoodsName(goodsName);
                detailModel.setGoodsNo(goodsNo);
                detailModel.setBarcode(barcode);
                detailModel.setBrand(brandName);
                detailModel.setNum(num);
                detailModel.setPrice(new BigDecimal(price));
                detailModelList.add(detailModel);
            }

            model.setDetailList(detailModelList);
            groupMerchandiseContrastService.saveGroupMerchandiseContrast(model, user);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }

    /**
     * 获取分页数据
     */
    @RequestMapping("/listGroupMerchandiseContrast.asyn")
    @ResponseBody
    private ViewResponseBean listGroupMerchandiseContrast(GroupMerchandiseContrastDTO dto) {
        try {
            // 响应结果集
            dto = groupMerchandiseContrastService.listGroupMerchandiseContrast(dto);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 判断同一店铺编码、同一商家、同一组合品ID是否存在
     */
    @RequestMapping("/getGroupMerchantContrast.asyn")
    @ResponseBody
    private ViewResponseBean getGroupMerchantContrast(GroupMerchandiseContrastModel model) {
        try {
            model = groupMerchandiseContrastService.getGroupMerchandiseContrast(model);
            if (model != null) {
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }


    /**
     * 保存
     */
    @RequestMapping("/modifyGroupMerchandiseContrast.asyn")
    @ResponseBody
    public ViewResponseBean modifyGroupMerchandiseContrast(GroupMerchandiseContrastModel model, String goodsItem) {
        try {
            User user = ShiroUtils.getUser();
            boolean isNull = new EmptyCheckUtils().addObject(model.getId()).addObject(model.getShopId()).addObject(model.getShopName())
                    .addObject(model.getMerchantId()).addObject(model.getSkuId()).addObject(model.getGroupGoodsName())
                    .empty();
            if (isNull) {
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }

            JSONArray jsonArray = JSONArray.fromObject(goodsItem);
            List<GroupMerchandiseContrastDetailModel> detailModelList = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONObject json = (JSONObject) object;
                if (!(json.containsKey("goodsNo") || json.containsKey("goodsId") || json.containsKey("goodsName")
                        || json.containsKey("barcode")|| json.containsKey("num") || json.containsKey("price"))) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_303);
                }

                String goodsNo = json.getString("goodsNo");
                Long goodsId = json.getLong("goodsId");
                String goodsName = json.getString("goodsName");
                String barcode = json.getString("barcode");
                String brandName = json.containsKey("brandName") ? json.getString("brandName") : null;
                Integer num = json.getInt("num");
                double price = json.getDouble("price");
                GroupMerchandiseContrastDetailModel detailModel = new GroupMerchandiseContrastDetailModel();
                detailModel.setGoodsId(goodsId);
                detailModel.setGoodsName(goodsName);
                detailModel.setGoodsNo(goodsNo);
                detailModel.setBarcode(barcode);
                detailModel.setBrand(brandName);
                detailModel.setNum(num);
                detailModel.setPrice(new BigDecimal(price));
                detailModelList.add(detailModel);
            }

            model.setDetailList(detailModelList);
            groupMerchandiseContrastService.modifyGroupMerchandiseContrast(model, user);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }

    /**
     * 导出信息
     * @param response
     * @param request
     * @param
     * @throws Exception
     */
    @RequestMapping("/exportInfo.asyn")
    public void exportInfo(HttpServletResponse response, HttpServletRequest request, GroupMerchandiseContrastDTO dto) throws Exception{

        String sheetName = "组合对照表导出";
        List<GroupMerchandiseContrastDTO> list = groupMerchandiseContrastService.getexportList(dto);
        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list) ;
        //导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }

    /**
     * 访问导入页面
     * @throws SQLException
     * */
    @RequestMapping("/toImportPage.html")
    public String toImportPage(Model model) throws SQLException {
        return "/derp/sale/groupMerchandiseContrast-import";
    }

    /**
     * 批量导入
     * @param
     * @return int
     * @throws IOException
     */
    @RequestMapping("/importExcel.asyn")
    @ResponseBody
    public ViewResponseBean importExcel(@RequestParam(value = "file", required = false) MultipartFile file,
                                        HttpSession session) throws IOException {
        Map resultMap = new HashMap();// 返回的结果集
        try {
            if (file == null) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
                    file.getOriginalFilename(), 2);
            if (data == null) {// 数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            resultMap = groupMerchandiseContrastService.batchImport(data);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(resultMap);
    }
    
    /**
     * 获取店铺
     * @return
     */
    @RequestMapping("getShop.asyn")
    @ResponseBody
    public ViewResponseBean getShop(String shopName) {
    	try {
			List<MerchantShopRelMongo> shopList = groupMerchandiseContrastService.getShopList(shopName) ;
			return ResponseFactory.success(shopList) ;
    	} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
    }

    /**
     * 批量删除商品对照信息
     * @return
     */
    @RequestMapping("/deleteGroupMerchandise.asyn")
    @ResponseBody
    public ViewResponseBean deleteGroupMerchandise(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
            }
            List<Long> idList =  Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            groupMerchandiseContrastService.batchDel(idList);
            return ResponseFactory.success() ;
        } catch (RuntimeException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
        }
    }
}
