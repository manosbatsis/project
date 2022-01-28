package com.topideal.order.tools;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.common.CustomsDeclareItemDTO;
import com.topideal.entity.dto.common.CustomsPackingDetailsDTO;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xwpf.usermodel.*;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Excel工具类 功能：提供各种Excel操作的方法
 */
public class DownloadExcelUtil {
    /**
     * 一线清关资料导出excel
     *
     * @param firstCustomsInfoDTOMap 封装的清关资料实体
     * @param type                   仓库类型 : 1-出仓 2-入仓
     * @return
     */
    public static Map<String, Workbook> createMultipartCustomsDeclares(Map<String, Object> firstCustomsInfoDTOMap, String type) throws Exception{
        Map<String, Workbook> map = new HashMap<>();
        String fileNamePrefix = "出仓仓库";

        if ("2".equals(type)) {
            fileNamePrefix = "入仓仓库";
        }

        for (String key : firstCustomsInfoDTOMap.keySet()) {
            Map<String, Object> firstCustomsInfoDTO = (Map<String, Object>) firstCustomsInfoDTOMap.get(key);
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NANSHA.equals(key)) {
                String fileName = fileNamePrefix + "南沙综合仓模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NANSHA,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG.equals(key)) {
                String fileName = fileNamePrefix + "综合1仓模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPTIANJIN.equals(key)) {
                String fileName = fileNamePrefix + "唯品天津模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPTIANJIN,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPONLINE.equals(key)) {
                String fileName = fileNamePrefix + "唯品线上模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPONLINE,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPQINGDAO.equals(key)) {
                String fileName = fileNamePrefix + "唯品青岛模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPQINGDAO,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPCHONGQING.equals(key)) {
                String fileName = fileNamePrefix + "唯品重庆模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPCHONGQING,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU.equals(key)) {
                String fileName = fileNamePrefix + "唯品郑州模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPNANSHA.equals(key)) {
                String fileName = fileNamePrefix + "唯品南沙仓模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPNANSHA,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHUANGPU.equals(key)) {
                String fileName1 = fileNamePrefix + "京东黄埔仓进仓单模板清关资料.xlsx";
                Workbook wb1 = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHUANGPU, firstCustomsInfoDTO);
                map.put(fileName1, wb1);

                String fileName2 = fileNamePrefix + "黄埔自营仓模板清关资料.xlsx";
                Workbook wb2 = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_HPZIYING, firstCustomsInfoDTO);
                map.put(fileName2, wb2);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDSHANGHAI.equals(key)) {
                String fileName = fileNamePrefix + "京东上海仓进仓单模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDSHANGHAI,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNANSHA.equals(key)) {
                String fileName = fileNamePrefix + "京东南沙模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNANSHA,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDQINGDAO.equals(key)) {
                String fileName = fileNamePrefix + "京东青岛模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDQINGDAO,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDZHENGZHOU.equals(key)) {
                String fileName = fileNamePrefix + "京东郑州模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDZHENGZHOU,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNHUAZENG.equals(key)) {
                String fileName = fileNamePrefix + "骅增菜鸟模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNHUAZENG,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNWTXYICANG.equals(key)) {
                String fileName = fileNamePrefix + "沃天下菜鸟一仓模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNWTXYICANG,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNWTXDB.equals(key)) {
                String fileName = fileNamePrefix + "沃天下菜鸟调拨模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_CNWTXDB,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNINGBO.equals(key)) {
                String fileName = fileNamePrefix + "京东宁波模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNINGBO,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDXIAMEN.equals(key)) {
                String fileName = fileNamePrefix + "京东厦门模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDXIAMEN,firstCustomsInfoDTO);
                map.put(fileName, wb);

                String fileName1 = fileNamePrefix + "京东自营采购订单模板-香港.xlsx";
                Workbook wb1 = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDZIYINGHK,firstCustomsInfoDTO);
                map.put(fileName1, wb1);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDTIANJIN.equals(key)) {
                String fileName = fileNamePrefix + "京东天津模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDTIANJIN,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDJINYI.equals(key)) {
                String fileName = fileNamePrefix + "京东金义模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDJINYI,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEI.equals(key)) {
                String fileName = fileNamePrefix + "京东河北廊坊箱单发票.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEI,firstCustomsInfoDTO);
                map.put(fileName, wb);

                fileName = fileNamePrefix + "京东河北廊坊入库明细.xlsx";
                Workbook wb1 = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEIDETAIL,firstCustomsInfoDTO);
                map.put(fileName, wb1);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDINANSHA.equals(key)) {
                String fileName = fileNamePrefix + "天猫南沙模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDINANSHA,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDININGBO.equals(key)) {
                String fileName = fileNamePrefix + "天猫宁波模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDININGBO,firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDITIANJIN.equals(key)) {
                String fileName = fileNamePrefix + "天猫天津模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDITIANJIN, firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDIZHENGZHOU.equals(key)) {
                String fileName = fileNamePrefix + "天猫郑州模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDIZHENGZHOU, firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDCHONGQING.equals(key)) {
                String fileName = fileNamePrefix + "京东重庆模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDCHONGQING, firstCustomsInfoDTO);
                map.put(fileName, wb);
            }else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NBCIXI.equals(key)) {
                String fileName = fileNamePrefix + "宁波慈溪模板清关资料.xlsx";
                Workbook wb = DownloadExcelUtil.exportTemplateExcel(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NBCIXI, firstCustomsInfoDTO);
                map.put(fileName, wb);
            }

        }
        return map;
    }

    /**
     * 一线清关资料导出word
     *
     * @param firstCustomsInfoDTOMap 封装的清关资料实体
     * @param type                   仓库类型 : 1-出仓 2-入仓
     * @return
     */
    public static Map<String, XWPFDocument> createMultipartCustomsDeclaresWord(Map<String, Object> firstCustomsInfoDTOMap, String type) throws Exception{
        Map<String, XWPFDocument> map = new HashMap<>();
        String fileNamePrefix = "出仓仓库";

        if ("2".equals(type)) {
            fileNamePrefix = "入仓仓库";
        }

        for (String key : firstCustomsInfoDTOMap.keySet()) {
            Map<String, Object> firstCustomsInfoDTO = (Map<String, Object>) firstCustomsInfoDTOMap.get(key);
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU.equals(key)) {
                String fileName = fileNamePrefix + "唯品郑州模板清关资料.docx";
                XWPFDocument xd = DownloadExcelUtil.exportCustomsWord(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU,firstCustomsInfoDTO);
                map.put(fileName, xd);
            }
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDTIANJIN.equals(key)) {
                String fileName = fileNamePrefix + "京东天津模板清关资料.docx";
                //运期限 导出时间+一个星期
                Calendar orderDateCal = Calendar.getInstance();
                orderDateCal.add(Calendar.DATE, 7);
                Date orderDate = new Timestamp(orderDateCal.getTime().getTime());
				firstCustomsInfoDTO.put("orderDate",orderDate);// 运期限
                XWPFDocument xd = DownloadExcelUtil.exportCustomsWord(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDTIANJIN,firstCustomsInfoDTO);
                map.put(fileName, xd);
            }
            if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEI.equals(key)) {
                String fileName = fileNamePrefix + "京东河北模板清关资料.docx";
                //运期限 导出时间+一个星期
                Calendar orderDateCal = Calendar.getInstance();
                orderDateCal.add(Calendar.DATE, 7);
                Date orderDate = new Timestamp(orderDateCal.getTime().getTime());
                firstCustomsInfoDTO.put("orderDate",orderDate);// 运期限
                XWPFDocument xd = DownloadExcelUtil.exportCustomsWord(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEI,firstCustomsInfoDTO);
                map.put(fileName, xd);
            }
        }
        return map;
    }

    /**
     * 一线清关资料导出excel-专用(南沙仓模板)
     *
     * @param model
     * @return
     */
    public static SXSSFWorkbook createCustomsDeclareExcel(FirstCustomsInfoDTO model) {
        // 日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        CellStyle headStyle = workbook.createCellStyle();
        CellStyle bodyStyle = workbook.createCellStyle();
        CellStyle centerStyle = workbook.createCellStyle();
        CellStyle rightStyle = workbook.createCellStyle();
        CellStyle tableThStyle = workbook.createCellStyle();
        CellStyle tableTrStyle = workbook.createCellStyle();
        CellStyle nStyle = workbook.createCellStyle();
        // 设置字体
        Font font1 = workbook.createFont();
        font1.setBold(true); // 粗体
        font1.setFontHeightInPoints((short) 15);
        font1.setUnderline((byte) 1);

        Font font2 = workbook.createFont();
        font2.setBold(true); // 粗体
        font2.setFontHeightInPoints((short) 10);
        headStyle.setFont(font1);

        Font underLineFont = workbook.createFont();
        underLineFont.setUnderline((byte) 1);

        // 设置背景颜色
        headStyle.setFillBackgroundColor(HSSFColorPredefined.GREY_40_PERCENT.getIndex());
        headStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        headStyle.setWrapText(true);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        nStyle.setWrapText(true);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        tableThStyle.setBorderBottom(BorderStyle.THIN);// 底部边框线样式(细实线)
        tableThStyle.setBorderLeft(BorderStyle.THIN);// 左边框线样式(细实线)
        tableThStyle.setBorderRight(BorderStyle.THIN);// 右边框线样式(细实线)
        tableThStyle.setBorderTop(BorderStyle.THIN);// 顶部边框线样式(细实线)
        tableThStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        tableThStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        tableThStyle.setFont(font2);
        tableThStyle.setWrapText(true);
        tableTrStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        tableTrStyle.setBorderBottom(BorderStyle.THIN);// 底部边框线样式(细实线)
        tableTrStyle.setBorderLeft(BorderStyle.THIN);// 左边框线样式(细实线)
        tableTrStyle.setBorderRight(BorderStyle.THIN);// 右边框线样式(细实线)
        tableTrStyle.setBorderTop(BorderStyle.THIN);// 顶部边框线样式(细实线)
        tableTrStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        tableTrStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        tableTrStyle.setWrapText(true);
        int fontHeight = 300;

        //单元格右对齐
        rightStyle.setAlignment(HorizontalAlignment.RIGHT);

        /**
         * 合同
         */
        String sheet1_name = "合同";
        Sheet sheet1 = workbook.createSheet(sheet1_name);
        //addMergedRegion 合并一些单元格
        //new CellRangeAddress 起始行号，终止行号， 起始列号，终止列号
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));
        sheet1.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));
        sheet1.addMergedRegion(new CellRangeAddress(3, 3, 5, 7));
        sheet1.addMergedRegion(new CellRangeAddress(4, 4, 0, 5));
        sheet1.addMergedRegion(new CellRangeAddress(5, 5, 0, 5));
        sheet1.addMergedRegion(new CellRangeAddress(6, 6, 0, 5));
        // 设置标题和列宽
        Row headrow = sheet1.createRow(0);
        headrow.setHeight((short) 500);
        Cell cell0 = headrow.createCell(0);
        sheet1.setColumnWidth(0, 4 * 256);
        sheet1.setColumnWidth(1, 37 * 256);
        sheet1.setColumnWidth(2, 15 * 256);
        sheet1.setColumnWidth(3, 10 * 256);
        sheet1.setColumnWidth(4, 10 * 256);
        sheet1.setColumnWidth(5, 10 * 256);
        cell0.setCellStyle(headStyle);
        cell0.setCellValue(sheet1_name);
        // 合同编码
        String contractNo = "";
        if (model.getContractNo() != null) {
            contractNo = model.getContractNo();
        }
        Row bodyRow1 = sheet1.createRow(1);
        Cell cell1_1 = bodyRow1.createCell(5);//bodyRow1 获取第几个单元格
        cell1_1.setCellValue("合同编码：" + contractNo);
        // 签订日期
        String signingDate = "";
        if (model.getSigningDate() == null) {
            signingDate = sdf.format(new Date());
        } else {
            signingDate = sdf.format(model.getSigningDate());
        }
        Row bodyRow2 = sheet1.createRow(2);
        Cell cell2_1 = bodyRow2.createCell(5);
        cell2_1.setCellValue("签订日期：" + signingDate);
        // 签订地点
        String signingAddr = "";
        if (model.getSigningAddr() != null) {
            signingAddr = model.getSigningAddr();
        }
        Row bodyRow3 = sheet1.createRow(3);
        Cell cell3_1 = bodyRow3.createCell(5);
        cell3_1.setCellValue("签订地点：" + signingAddr);
        // 甲方
        String firstParty = "";
        if (model.getFirstParty() != null) {
            firstParty = model.getFirstParty();
        }
        Row bodyRow4 = sheet1.createRow(4);
        Cell cell4_1 = bodyRow4.createCell(0);
        cell4_1.setCellValue("甲　　方：" + firstParty);
        // 甲方地址
        String firstPartyAddr = "";
        if (model.getFirstPartyAddr() != null) {
            firstPartyAddr = model.getFirstPartyAddr();
        }
        Row bodyRow5 = sheet1.createRow(5);
        Cell cell5_1 = bodyRow5.createCell(0);
        cell5_1.setCellValue("甲方地址：" + firstPartyAddr);
        // 乙方
        String secondParty = "";
        if (model.getSecondParty() != null) {
            secondParty = model.getSecondParty();
        }
        Row bodyRow6 = sheet1.createRow(6);
        Cell cell6_1 = bodyRow6.createCell(0);

        String startStr = "乙　　方：";
        //部分内容添加下划线
        RichTextString richString = new XSSFRichTextString(startStr + secondParty);

        richString.applyFont(startStr.length(), startStr.length() + secondParty.length(), underLineFont);
        cell6_1.setCellValue(richString);

        // 协议
        Row bodyRow7 = sheet1.createRow(8);
        Cell cell7_1 = bodyRow7.createCell(1);

        String signNo = "";
        if (StringUtils.isNotBlank(model.getSignNo())) {
            signNo = model.getSignNo();
        }
        //部分内容添加下划线
        startStr = "基于甲方与";
        richString = new XSSFRichTextString(startStr + " " + secondParty + " 签订的编号为：" + signNo);
        richString.applyFont(startStr.length(), startStr.length() + secondParty.length(), underLineFont);
        cell7_1.setCellValue(richString);

        Row bodyRow8 = sheet1.createRow(9);
        Cell cell8_1 = bodyRow8.createCell(0);
        cell8_1.setCellValue("的《委托服务协议》（下称“《委托服务协议》”），甲方现委托乙方办理如下商品在中国南沙保税");

        Row bodyRow9 = sheet1.createRow(10);
        Cell cell9_1 = bodyRow9.createCell(0);
        cell9_1.setCellValue("港区的申报通关、仓储服务。");

        // 商品资料明细
        Row bodyRow10 = sheet1.createRow(12);
        Cell cell10_1 = bodyRow10.createCell(0);
        cell10_1.setCellValue("一、");
        Cell cell10_2 = bodyRow10.createCell(1);
        cell10_2.setCellValue("商品资料明细");
        // 商品资料明细标题
        Row bodyRow11 = sheet1.createRow(13);
        bodyRow11.setHeight((short) 300);
        Cell cell11_1 = bodyRow11.createCell(1);
        cell11_1.setCellValue("商品名称");
        cell11_1.setCellStyle(tableThStyle);
        Cell cell11_2 = bodyRow11.createCell(2);
        cell11_2.setCellValue("货号");
        cell11_2.setCellStyle(tableThStyle);
        Cell cell11_3 = bodyRow11.createCell(3);
        cell11_3.setCellValue("单位");
        cell11_3.setCellStyle(tableThStyle);
        Cell cell11_4 = bodyRow11.createCell(4);
        cell11_4.setCellValue("数量");
        cell11_4.setCellStyle(tableThStyle);
        Cell cell11_5 = bodyRow11.createCell(5);
        cell11_5.setCellValue("单价(RMB)");
        cell11_5.setCellStyle(tableThStyle);
        Cell cell11_6 = bodyRow11.createCell(6);
        cell11_6.setCellValue("总价(RMB)");
        cell11_6.setCellStyle(tableThStyle);
        Cell cell11_7 = bodyRow11.createCell(7);
        cell11_7.setCellValue("原产地");
        cell11_7.setCellStyle(tableThStyle);

        int cell1_num = 13;
        int count_num1 = 0;

        BigDecimal count_amount = new BigDecimal(0);
        for (CustomsDeclareItemDTO item : model.getItemList()) {
            Row bodyRow12 = sheet1.createRow(++cell1_num);
            //获取字符串总宽度
            double width = item.getGoodsName().getBytes().length * 256;
            double needsRows = width / (37 * 256);
            if (needsRows < 1.0) {
                needsRows = 1.0;
            }
            short height = (short) (fontHeight * Math.ceil(needsRows));
            bodyRow12.setHeight(height);
            Cell cell12_1 = bodyRow12.createCell(1);
            cell12_1.setCellValue(item.getGoodsName());
            cell12_1.setCellStyle(tableTrStyle);
            Cell cell12_2 = bodyRow12.createCell(2);
            cell12_2.setCellValue(item.getGoodsNo());
            cell12_2.setCellStyle(tableTrStyle);
            Cell cell12_3 = bodyRow12.createCell(3);
            cell12_3.setCellValue(item.getUnit());
            cell12_3.setCellStyle(tableTrStyle);
            Cell cell12_4 = bodyRow12.createCell(4);
            if (item.getNum() == null) {
                cell12_4.setCellValue("");
            } else {
                cell12_4.setCellValue(item.getNum());
                count_num1 += item.getNum();
            }
            cell12_4.setCellStyle(tableTrStyle);
            Cell cell12_5 = bodyRow12.createCell(5);
            cell12_5.setCellValue(item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            cell12_5.setCellStyle(tableTrStyle);
            Cell cell12_6 = bodyRow12.createCell(6);
            cell12_6.setCellValue(item.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            cell12_6.setCellStyle(tableTrStyle);
            Cell cell12_7 = bodyRow12.createCell(7);
            cell12_7.setCellValue(item.getCountryName());
            cell12_7.setCellStyle(tableTrStyle);
            count_amount = count_amount.add(item.getAmount());
        }
        Row bodyRow13 = sheet1.createRow(++cell1_num);
        bodyRow13.setHeight((short) 300);
        Cell cell13_1 = bodyRow13.createCell(1);
        cell13_1.setCellValue("TOTAL：");
        cell13_1.setCellStyle(tableTrStyle);
        Cell cell13_2 = bodyRow13.createCell(2);
        cell13_2.setCellStyle(tableTrStyle);
        Cell cell13_3 = bodyRow13.createCell(3);
        cell13_3.setCellStyle(tableTrStyle);
        Cell cell13_4 = bodyRow13.createCell(4);
        cell13_4.setCellValue(count_num1);
        cell13_4.setCellStyle(tableTrStyle);
        Cell cell13_5 = bodyRow13.createCell(5);
        cell13_5.setCellStyle(tableTrStyle);
        Cell cell13_6 = bodyRow13.createCell(6);
        cell13_6.setCellValue(count_amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        cell13_6.setCellStyle(tableTrStyle);
        Cell cell13_7 = bodyRow13.createCell(7);
        cell13_7.setCellStyle(tableTrStyle);
        cell1_num++;
        // 商品原产地
        Row bodyRow14 = sheet1.createRow(++cell1_num);
        Cell cell14_1 = bodyRow14.createCell(1);
        cell14_1.setCellValue("商品原产地：" + model.getCountry());
        // 特殊操作要求
        Row bodyRow15 = sheet1.createRow(++cell1_num);
        Cell cell15_1 = bodyRow15.createCell(1);
        cell15_1.setCellValue("特殊操作要求：");
        cell1_num++;
        // 其他
        Row bodyRow16 = sheet1.createRow(++cell1_num);
        Cell cell16_1 = bodyRow16.createCell(0);
        cell16_1.setCellValue("二、");
        Cell cell16_2 = bodyRow16.createCell(1);
        cell16_2.setCellValue("其他");
        sheet1.addMergedRegion(new CellRangeAddress(cell1_num + 1, cell1_num + 1, 0, 5));
        Row bodyRow17 = sheet1.createRow(++cell1_num);
        Cell cell17_1 = bodyRow17.createCell(0);
        cell17_1.setCellValue("1.本协议一式贰份，甲、乙双方各持一份，具有同等法律效力。");
        sheet1.addMergedRegion(new CellRangeAddress(cell1_num + 1, cell1_num + 1, 0, 5));
        Row bodyRow18 = sheet1.createRow(++cell1_num);
        Cell cell18_1 = bodyRow18.createCell(0);
        cell18_1.setCellValue("2.本协议经甲、乙双方签章后生效，通过传真件或扫描件方式签订同样具备法律效力。");
        sheet1.addMergedRegion(new CellRangeAddress(cell1_num + 1, cell1_num + 1, 0, 5));
        Row bodyRow19 = sheet1.createRow(++cell1_num);
        Cell cell19_1 = bodyRow19.createCell(0);
        cell19_1.setCellValue("3.本协议作为《委托服务协议》不可分割的组成部分，未尽之处，适用《委托服务协议》的约定。");
        cell1_num++;
        Row bodyRow20 = sheet1.createRow(++cell1_num);
        Cell cell20_1 = bodyRow20.createCell(1);
        cell20_1.setCellValue("（以下无正文）");
        cell1_num++;
        // 签章
        Row bodyRow21 = sheet1.createRow(++cell1_num);
        Cell cell21_1 = bodyRow21.createCell(1);
        cell21_1.setCellValue("甲方（签章） ");
        Cell cell21_2 = bodyRow21.createCell(3);
        cell21_2.setCellValue("乙方（签章） ");
        /**
         * 发票
         */
        String sheet2_name = "INVOICE\n发票";
        Sheet sheet2 = workbook.createSheet("發票");
        sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet2.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
        sheet2.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
        sheet2.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));
        sheet2.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
        sheet2.addMergedRegion(new CellRangeAddress(3, 3, 4, 5));
        sheet2.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));
        sheet2.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));
        sheet2.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
        sheet2.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
        sheet2.addMergedRegion(new CellRangeAddress(8, 8, 5, 6));
        // 设置标题和列宽
        Row headrow2 = sheet2.createRow(0);
        headrow2.setHeight((short) 1000);
        Cell cell0_2 = headrow2.createCell(0);
        sheet2.setColumnWidth(0, 4 * 256);
        sheet2.setColumnWidth(1, 28 * 256);
        sheet2.setColumnWidth(2, 18 * 256);
        sheet2.setColumnWidth(3, 10 * 256);
        sheet2.setColumnWidth(4, 12 * 256);
        sheet2.setColumnWidth(5, 12 * 256);
        sheet2.setColumnWidth(6, 15 * 256);
        cell0_2.setCellStyle(headStyle);
        RichTextString ts = new XSSFRichTextString(sheet2_name);
        ts.applyFont(0, ts.length(), font1);// 下划线的
        cell0_2.setCellValue(ts);
        // date时间
        Row bodyRow2_1 = sheet2.createRow(1);
        Cell cell2_1_1 = bodyRow2_1.createCell(4);
        cell2_1_1.setCellStyle(rightStyle);
        cell2_1_1.setCellValue("DATE时间：");
        Cell cell2_1_2 = bodyRow2_1.createCell(6);
        cell2_1_2.setCellValue(signingDate);

        // 发票号
        String invoiceNo = "";
        if (model.getInvoiceNo() != null) {
            invoiceNo = model.getInvoiceNo();
        }
        Row bodyRow2_2 = sheet2.createRow(2);
        Cell cell2_2_1 = bodyRow2_2.createCell(4);
        cell2_2_1.setCellStyle(rightStyle);
        cell2_2_1.setCellValue("INVOICE NO发票号：");
        Cell cell2_2_2 = bodyRow2_2.createCell(6);
        cell2_2_2.setCellValue(invoiceNo);
        // 合同号
        Row bodyRow2_3 = sheet2.createRow(3);
        Cell cell2_3_1 = bodyRow2_3.createCell(4);
        cell2_3_1.setCellValue("CONTRACT NO合同号：");
        cell2_3_1.setCellStyle(rightStyle);
        Cell cell2_3_2 = bodyRow2_3.createCell(6);
        cell2_3_2.setCellValue(contractNo);
        // Consignee
        String consignee = "";
        if (model.geteAddressee() != null) {
            consignee = model.geteAddressee();
        }
        Row bodyRow2_4 = sheet2.createRow(4);
        Cell cell2_4_1 = bodyRow2_4.createCell(0);
        cell2_4_1.setCellValue("Consignee：");
        cell2_4_1.setCellStyle(rightStyle);
        Cell cell2_4_2 = bodyRow2_4.createCell(1);
        cell2_4_2.setCellValue(consignee);
        // 收货人
        String addressee = "";
        if (model.getAddressee() != null) {
            addressee = model.getAddressee();
        }
        Row bodyRow2_6 = sheet2.createRow(5);
        Cell cell2_6_1 = bodyRow2_6.createCell(0);
        cell2_6_1.setCellValue("收货人：");
        cell2_6_1.setCellStyle(rightStyle);
        Cell cell2_6_2 = bodyRow2_6.createCell(1);
        cell2_6_2.setCellValue(addressee);
        Row bodyRow2_7 = sheet2.createRow(6);
        Cell cell2_7_1 = bodyRow2_7.createCell(1);
        cell2_7_1.setCellValue("广州市南沙区港荣一街3号401-2房");
        // 商品明细
        Row bodyRow2_8 = sheet2.createRow(7);
        bodyRow2_8.setHeight((short) 1200);
        Cell cell2_8_1 = bodyRow2_8.createCell(1);
        cell2_8_1.setCellValue(new XSSFRichTextString("COMMODITY.\n商品名称"));
        cell2_8_1.setCellStyle(tableThStyle);
        Cell cell2_8_2 = bodyRow2_8.createCell(2);
        cell2_8_2.setCellValue(new XSSFRichTextString("Article number\n货号"));
        cell2_8_2.setCellStyle(tableThStyle);
        Cell cell2_8_3 = bodyRow2_8.createCell(3);
        cell2_8_3.setCellValue(new XSSFRichTextString("Unit\n单位"));
        cell2_8_3.setCellStyle(tableThStyle);
        Cell cell2_8_4 = bodyRow2_8.createCell(4);
        cell2_8_4.setCellValue(new XSSFRichTextString("QUANTITY\n( PCS )\n数量"));
        cell2_8_4.setCellStyle(tableThStyle);
        Cell cell2_8_5 = bodyRow2_8.createCell(5);
        cell2_8_5.setCellValue(new XSSFRichTextString("UNITY PRICE\n单价(RMB)"));
        cell2_8_5.setCellStyle(tableThStyle);
        Cell cell2_8_6 = bodyRow2_8.createCell(6);
        cell2_8_6.setCellValue(new XSSFRichTextString("AMOUNT\n总价(RMB)"));
        cell2_8_6.setCellStyle(tableThStyle);
        Cell cell2_8_7 = bodyRow2_8.createCell(7);
        cell2_8_7.setCellValue(new XSSFRichTextString("原产国"));
        cell2_8_7.setCellStyle(tableThStyle);

        Row bodyRow2_9 = sheet2.createRow(8);
        short heightRow1 = (short) (fontHeight * Math.ceil(1.0));
        bodyRow2_9.setHeight(heightRow1);
        Cell cell2_9_1 = bodyRow2_9.createCell(1);
        cell2_9_1.setCellStyle(tableThStyle);
        Cell cell2_9_2 = bodyRow2_9.createCell(2);
        cell2_9_2.setCellStyle(tableThStyle);
        Cell cell2_9_3 = bodyRow2_9.createCell(3);
        cell2_9_3.setCellStyle(tableThStyle);
        Cell cell2_9_4 = bodyRow2_9.createCell(4);
        cell2_9_4.setCellStyle(tableThStyle);
        Cell cell2_9_5 = bodyRow2_9.createCell(5);
        cell2_9_5.setCellValue(new XSSFRichTextString("CIF NANSHA"));
        cell2_9_5.setCellStyle(tableThStyle);
        Cell cell2_9_7 = bodyRow2_9.createCell(7);
        cell2_9_7.setCellStyle(tableThStyle);

        int cell2_num = 8;
        int count_num2 = 0;
        BigDecimal count_amount2 = new BigDecimal(0);
        for (CustomsDeclareItemDTO item : model.getItemList()) {
            Row bodyRow2_10 = sheet2.createRow(++cell2_num);
            //获取字符串总宽度
            double width = item.getGoodsName().getBytes().length * 256;
            double needsRows = width / (28 * 256);
            if (needsRows < 1.0) {
                needsRows = 1.0;
            }
            short height = (short) (fontHeight * Math.ceil(needsRows));
            bodyRow2_10.setHeight(height);
            Cell cell2_10_1 = bodyRow2_10.createCell(1);
            cell2_10_1.setCellValue(item.getGoodsName());
            cell2_10_1.setCellStyle(tableTrStyle);
            Cell cell2_10_2 = bodyRow2_10.createCell(2);
            cell2_10_2.setCellValue(item.getGoodsNo());
            cell2_10_2.setCellStyle(tableTrStyle);
            Cell cell2_10_3 = bodyRow2_10.createCell(3);
            cell2_10_3.setCellValue(item.getUnit());
            cell2_10_3.setCellStyle(tableTrStyle);
            Cell cell2_10_4 = bodyRow2_10.createCell(4);
            if (item.getNum() == null) {
                cell2_10_4.setCellValue("");
            } else {
                cell2_10_4.setCellValue(item.getNum());
                count_num2 += item.getNum();
            }
            cell2_10_4.setCellStyle(tableTrStyle);
            Cell cell2_10_5 = bodyRow2_10.createCell(5);
            cell2_10_5.setCellValue(item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            cell2_10_5.setCellStyle(tableTrStyle);
            Cell cell2_10_6 = bodyRow2_10.createCell(6);
            cell2_10_6.setCellValue(item.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            cell2_10_6.setCellStyle(tableTrStyle);
            Cell cell2_10_7 = bodyRow2_10.createCell(7);
            cell2_10_7.setCellValue(item.getCountryName());
            cell2_10_7.setCellStyle(tableTrStyle);

            count_amount2 = count_amount2.add(item.getAmount());
        }
        Row bodyRow2_10 = sheet2.createRow(++cell2_num);
        bodyRow2_10.setHeight((short) 300);
        Cell cell2_10_1 = bodyRow2_10.createCell(1);
        cell2_10_1.setCellValue("TOTAL：");
        cell2_10_1.setCellStyle(tableTrStyle);
        Cell cell2_10_2 = bodyRow2_10.createCell(2);
        cell2_10_2.setCellStyle(tableTrStyle);
        Cell cell2_10_3 = bodyRow2_10.createCell(3);
        cell2_10_3.setCellStyle(tableTrStyle);
        Cell cell2_10_4 = bodyRow2_10.createCell(4);
        cell2_10_4.setCellValue(count_num2);
        cell2_10_4.setCellStyle(tableTrStyle);
        Cell cell2_10_5 = bodyRow2_10.createCell(5);
        cell2_10_5.setCellStyle(tableTrStyle);
        Cell cell2_10_6 = bodyRow2_10.createCell(6);
        cell2_10_6.setCellValue(count_amount2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        cell2_10_6.setCellStyle(tableTrStyle);
        Cell cell2_10_7 = bodyRow2_10.createCell(7);
        cell2_10_7.setCellStyle(tableTrStyle);

        cell2_num++;

        // 装船时间
        Row bodyRow2_11 = sheet2.createRow(++cell2_num);
        Cell cell2_11_1 = bodyRow2_11.createCell(0);
        cell2_11_1.setCellValue("1、");
        Cell cell2_11_2 = bodyRow2_11.createCell(1);
        cell2_11_2.setCellValue("Shipment before：");
        Cell cell2_11_3 = bodyRow2_11.createCell(2);
        if (model.getShipDate() == null) {
            cell2_11_3.setCellValue("");
        } else {
            cell2_11_3.setCellValue(sdf.format(model.getShipDate()));
        }

        Row bodyRow2_12 = sheet2.createRow(++cell2_num);
        Cell cell2_12_1 = bodyRow2_12.createCell(1);
        cell2_12_1.setCellValue("装船时间：");

        // 付款方式
        String payment = "";
        if (model.getPayment() != null) {
            payment = model.getPayment();
        }
        Row bodyRow2_13 = sheet2.createRow(++cell2_num);
        Cell cell2_13_1 = bodyRow2_13.createCell(0);
        cell2_13_1.setCellValue("2、");
        Cell cell2_13_2 = bodyRow2_13.createCell(1);
        cell2_13_2.setCellValue("Payment：");
        Cell cell2_13_3 = bodyRow2_13.createCell(2);
        cell2_13_3.setCellValue(payment);

        Row bodyRow2_14 = sheet2.createRow(++cell2_num);
        Cell cell2_14_1 = bodyRow2_14.createCell(1);
        cell2_14_1.setCellValue("付款方式：");

        // 包装
        Row bodyRow2_15 = sheet2.createRow(++cell2_num);
        Cell cell2_15_1 = bodyRow2_15.createCell(0);
        cell2_15_1.setCellValue("3、");
        Cell cell2_15_2 = bodyRow2_15.createCell(1);
        cell2_15_2.setCellValue("Packing：");
        Cell cell2_15_3 = bodyRow2_15.createCell(2);
        cell2_15_3.setCellValue(model.getPack());

        Row bodyRow2_16 = sheet2.createRow(++cell2_num);
        Cell cell2_16_1 = bodyRow2_16.createCell(1);
        cell2_16_1.setCellValue("包装：");

        // 装货港
        Row bodyRow2_17 = sheet2.createRow(++cell2_num);
        Cell cell2_17_1 = bodyRow2_17.createCell(0);
        cell2_17_1.setCellValue("4、");
        Cell cell2_17_2 = bodyRow2_17.createCell(1);
        cell2_17_2.setCellValue("Port of Loading：");
        Cell cell2_17_3 = bodyRow2_17.createCell(2);
        cell2_17_3.setCellValue(model.getPortLoading());

        Row bodyRow2_18 = sheet2.createRow(++cell2_num);
        Cell cell2_18_1 = bodyRow2_18.createCell(1);
        cell2_18_1.setCellValue("装货港：");

        // 卸货港
        Row bodyRow2_19 = sheet2.createRow(++cell2_num);
        Cell cell2_19_1 = bodyRow2_19.createCell(0);
        cell2_19_1.setCellValue("5、");
        Cell cell2_19_2 = bodyRow2_19.createCell(1);
        cell2_19_2.setCellValue("Port of Discharge：");
        Cell cell2_19_3 = bodyRow2_19.createCell(2);
        cell2_19_3.setCellValue(model.getPortDest());

        Row bodyRow2_20 = sheet2.createRow(++cell2_num);
        Cell cell2_20_1 = bodyRow2_20.createCell(1);
        cell2_20_1.setCellValue("卸货港：");

        // 付款条约
        Row bodyRow2_21 = sheet2.createRow(++cell2_num);
        Cell cell2_21_1 = bodyRow2_21.createCell(0);
        cell2_21_1.setCellValue("6、");
        Cell cell2_21_2 = bodyRow2_21.createCell(1);
        cell2_21_2.setCellValue("Payment RULES：");
        Cell cell2_21_3 = bodyRow2_21.createCell(2);
        cell2_21_3.setCellValue(model.getPayRules());

        Row bodyRow2_22 = sheet2.createRow(++cell2_num);
        Cell cell2_22_1 = bodyRow2_22.createCell(1);
        cell2_22_1.setCellValue("付款条约：");

        // 原产国
        Row bodyRow2_23 = sheet2.createRow(++cell2_num);
        Cell cell2_23_1 = bodyRow2_23.createCell(0);
        cell2_23_1.setCellValue("7、");
        Cell cell2_23_2 = bodyRow2_23.createCell(1);
        cell2_23_2.setCellValue("Country of Origin：");
        Cell cell2_23_3 = bodyRow2_23.createCell(2);
        cell2_23_3.setCellValue(model.getCountry());

        Row bodyRow2_24 = sheet2.createRow(++cell2_num);
        Cell cell2_24_1 = bodyRow2_24.createCell(1);
        cell2_24_1.setCellValue("原产国：");

        // 境外发货人
        Row bodyRow2_25 = sheet2.createRow(++cell2_num);
        Cell cell2_25_1 = bodyRow2_25.createCell(0);
        cell2_25_1.setCellValue("8、");
        Cell cell2_25_2 = bodyRow2_25.createCell(1);
        cell2_25_2.setCellValue("Shipper：");
        Cell cell2_25_3 = bodyRow2_25.createCell(2);
        cell2_25_3.setCellValue(model.getShipper());

        Row bodyRow2_26 = sheet2.createRow(++cell2_num);
        Cell cell2_26_1 = bodyRow2_26.createCell(1);
        cell2_26_1.setCellValue("境外发货人：");

        // 唛头
        Row bodyRow2_27 = sheet2.createRow(++cell2_num);
        Cell cell2_27_1 = bodyRow2_27.createCell(0);
        cell2_27_1.setCellValue("9、");
        Cell cell2_27_2 = bodyRow2_27.createCell(1);
        cell2_27_2.setCellValue("Marks and Numbers：");
        Cell cell2_27_3 = bodyRow2_27.createCell(2);
        cell2_27_3.setCellValue(model.getMark());

        Row bodyRow2_28 = sheet2.createRow(++cell2_num);
        Cell cell2_28_1 = bodyRow2_28.createCell(1);
        cell2_28_1.setCellValue("唛头：");

        cell2_num++;
        Row bodyRow2_29 = sheet2.createRow(++cell2_num);
        Cell cell2_29_1 = bodyRow2_29.createCell(0);
        cell2_29_1.setCellValue("注：本发票通过扫描件方式出具同样具有法律效力。");

        /**
         * 装箱单
         */
        String sheet3_name = "PACKING LIST\n装箱单";
        Sheet sheet3 = workbook.createSheet("装箱单");
        sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet3.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
        sheet3.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));
        sheet3.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
        sheet3.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));
        sheet3.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));
        sheet3.addMergedRegion(new CellRangeAddress(3, 3, 5, 7));
        sheet3.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));
        sheet3.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
        // 设置标题和列宽
        Row headrow3 = sheet3.createRow(0);
        headrow3.setHeight((short) 1000);
        Cell cell0_3 = headrow3.createCell(0);
        sheet3.setColumnWidth(0, 4 * 256);
        sheet3.setColumnWidth(1, 28 * 256);
        sheet3.setColumnWidth(2, 16 * 256);
        sheet3.setColumnWidth(3, 10 * 256);
        sheet3.setColumnWidth(4, 8 * 256);
        sheet3.setColumnWidth(5, 12 * 256);
        sheet3.setColumnWidth(6, 12 * 256);
        sheet3.setColumnWidth(7, 12 * 256);
        cell0_3.setCellStyle(headStyle);
        RichTextString ts3 = new XSSFRichTextString(sheet3_name);
        ts3.applyFont(0, ts3.length(), font1);// 下划线的
        cell0_3.setCellValue(ts3);
        // date时间
        Row bodyRow3_1 = sheet3.createRow(1);
        Cell cell3_1_1 = bodyRow3_1.createCell(3);
        cell3_1_1.setCellValue("DATE时间：");
        cell3_1_1.setCellStyle(rightStyle);
        Cell cell3_1_2 = bodyRow3_1.createCell(5);
        cell3_1_2.setCellValue(signingDate);
        // 发票号
        Row bodyRow3_2 = sheet3.createRow(2);
        Cell cell3_2_1 = bodyRow3_2.createCell(3);
        cell3_2_1.setCellValue("INVOICE NO发票号：");
        Cell cell3_2_2 = bodyRow3_2.createCell(5);
        cell3_2_2.setCellValue(invoiceNo);
        // 合同号
        Row bodyRow3_3 = sheet3.createRow(3);
        Cell cell3_3_1 = bodyRow3_3.createCell(3);
        cell3_3_1.setCellValue("CONTRACT NO合同号：");
        cell3_3_1.setCellStyle(rightStyle);
        Cell cell3_3_2 = bodyRow3_3.createCell(5);
        cell3_3_2.setCellValue(contractNo);
        // Consignee
        Row bodyRow3_4 = sheet3.createRow(4);
        Cell cell3_4_1 = bodyRow3_4.createCell(0);
        cell3_4_1.setCellValue("Consignee：");
        cell3_4_1.setCellStyle(rightStyle);
        Cell cell3_4_2 = bodyRow3_4.createCell(1);
        cell3_4_2.setCellValue(consignee);
        // 收货人
        Row bodyRow3_6 = sheet3.createRow(5);
        Cell cell3_6_1 = bodyRow3_6.createCell(0);
        cell3_6_1.setCellValue("收货人：");
        cell3_6_1.setCellStyle(rightStyle);
        Cell cell3_6_2 = bodyRow3_6.createCell(1);
        cell3_6_2.setCellValue(addressee);
        Row bodyRow3_7 = sheet3.createRow(6);
        Cell cell3_7_1 = bodyRow3_7.createCell(1);
        cell3_7_1.setCellValue("广州市南沙区港荣一街3号401-2房 ");
        // 商品明细
        Row bodyRow3_8 = sheet3.createRow(7);
        bodyRow3_8.setHeight((short) 1200);
        Cell cell3_8_1 = bodyRow3_8.createCell(1);
        cell3_8_1.setCellValue(new XSSFRichTextString("COMMODITY.\r商品名称"));
        cell3_8_1.setCellStyle(tableThStyle);
        Cell cell3_8_2 = bodyRow3_8.createCell(2);
        cell3_8_2.setCellValue(new XSSFRichTextString("Specifications\r规格"));
        cell3_8_2.setCellStyle(tableThStyle);
        Cell cell3_8_3 = bodyRow3_8.createCell(3);
        cell3_8_3.setCellValue(new XSSFRichTextString("Article number\r货号"));
        cell3_8_3.setCellStyle(tableThStyle);
        Cell cell3_8_4 = bodyRow3_8.createCell(4);
        cell3_8_4.setCellValue(new XSSFRichTextString("Unit\r单位"));
        cell3_8_4.setCellStyle(tableThStyle);
        Cell cell3_8_5 = bodyRow3_8.createCell(5);
        cell3_8_5.setCellValue(new XSSFRichTextString("QUANTITY\r( PCS )\r数量"));
        cell3_8_5.setCellStyle(tableThStyle);
        Cell cell3_8_6 = bodyRow3_8.createCell(6);
        cell3_8_6.setCellValue(new XSSFRichTextString("NET WEIGHT\r（KGS）\r净重"));
        cell3_8_6.setCellStyle(tableThStyle);
        Cell cell3_8_7 = bodyRow3_8.createCell(7);
        cell3_8_7.setCellValue(new XSSFRichTextString("GROSS WEIGHT\r（KGS）\r毛重"));
        cell3_8_7.setCellStyle(tableThStyle);
        int cell3_num = 7;
        int count_num3 = 0;
        Double count_GrossWeight = 0.0;
        Double count_NetWeight = 0.0;
        for (CustomsDeclareItemDTO item : model.getItemList()) {
            Row bodyRow3_9 = sheet3.createRow(++cell3_num);
            //获取字符串总宽度
            double width = item.getGoodsName().getBytes().length * 256;
            double needsRows = width / (28 * 256);
            if (needsRows < 1.0) {
                needsRows = 1.0;
            }
            short height = (short) (fontHeight * Math.ceil(needsRows));
            bodyRow3_9.setHeight(height);
            Cell cell3_9_1 = bodyRow3_9.createCell(1);
            cell3_9_1.setCellValue(item.getGoodsName());
            cell3_9_1.setCellStyle(tableTrStyle);
            Cell cell3_9_2 = bodyRow3_9.createCell(2);
            cell3_9_2.setCellValue(item.getGoodsSpec());
            cell3_9_2.setCellStyle(tableTrStyle);
            Cell cell3_9_3 = bodyRow3_9.createCell(3);
            cell3_9_3.setCellValue(item.getGoodsNo());
            cell3_9_3.setCellStyle(tableTrStyle);
            Cell cell3_9_4 = bodyRow3_9.createCell(4);
            cell3_9_4.setCellValue(item.getUnit());
            cell3_9_4.setCellStyle(tableTrStyle);
            Cell cell3_9_5 = bodyRow3_9.createCell(5);
            if (item.getNum() == null) {
                cell3_9_5.setCellValue("");
            } else {
                cell3_9_5.setCellValue(item.getNum());
                count_num3 += item.getNum();
            }
            cell3_9_5.setCellStyle(tableTrStyle);
            Cell cell3_9_6 = bodyRow3_9.createCell(6);
            if (item.getNetWeight() == null) {
                cell3_9_6.setCellValue("");
            } else {
                cell3_9_6.setCellValue(item.getNetWeight());
                count_NetWeight += item.getNetWeight();
            }
            cell3_9_6.setCellStyle(tableTrStyle);
            Cell cell3_9_7 = bodyRow3_9.createCell(7);
            if (item.getGrossWeight() == null) {
                cell3_9_7.setCellValue("");
            } else {
                cell3_9_7.setCellValue(item.getGrossWeight());
                count_GrossWeight += item.getGrossWeight();
            }
            cell3_9_7.setCellStyle(tableTrStyle);
        }
        Row bodyRow3_10 = sheet3.createRow(++cell3_num);
        bodyRow3_10.setHeight((short) 300);
        Cell cell3_10_1 = bodyRow3_10.createCell(1);
        cell3_10_1.setCellValue("TOTAL：");
        cell3_10_1.setCellStyle(tableTrStyle);
        Cell cell3_10_2 = bodyRow3_10.createCell(2);
        cell3_10_2.setCellStyle(tableTrStyle);
        Cell cell3_10_3 = bodyRow3_10.createCell(3);
        cell3_10_3.setCellStyle(tableTrStyle);
        Cell cell3_10_4 = bodyRow3_10.createCell(4);
        cell3_10_4.setCellStyle(tableTrStyle);
        Cell cell3_10_5 = bodyRow3_10.createCell(5);
        cell3_10_5.setCellValue(count_num3);
        cell3_10_5.setCellStyle(tableTrStyle);
        Cell cell3_10_6 = bodyRow3_10.createCell(6);
        cell3_10_6.setCellValue(count_NetWeight);
        cell3_10_6.setCellStyle(tableTrStyle);
        Cell cell3_10_7 = bodyRow3_10.createCell(7);
        cell3_10_7.setCellValue(count_GrossWeight);
        cell3_10_7.setCellStyle(tableTrStyle);

        cell3_num++;
        // 装船时间
        Row bodyRow3_11 = sheet3.createRow(++cell3_num);
        Cell cell3_11_1 = bodyRow3_11.createCell(0);
        cell3_11_1.setCellValue("1、");
        Cell cell3_11_2 = bodyRow3_11.createCell(1);
        cell3_11_2.setCellValue("Shipment before：");
        Cell cell3_11_3 = bodyRow3_11.createCell(2);
        if (model.getShipDate() == null) {
            cell3_11_3.setCellValue("");
        } else {
            cell3_11_3.setCellValue(sdf.format(model.getShipDate()));
        }

        Row bodyRow3_12 = sheet3.createRow(++cell3_num);
        Cell cell3_12_1 = bodyRow3_12.createCell(1);
        cell3_12_1.setCellValue("装船时间：");

        // 付款方式
        payment = "";
        if (model.getPayment() != null) {
            payment = model.getPayment();
        }
        Row bodyRow3_13 = sheet3.createRow(++cell3_num);
        Cell cell3_13_1 = bodyRow3_13.createCell(0);
        cell3_13_1.setCellValue("2、");
        Cell cell3_13_2 = bodyRow3_13.createCell(1);
        cell3_13_2.setCellValue("Payment：");
        Cell cell3_13_3 = bodyRow3_13.createCell(2);
        cell3_13_3.setCellValue(payment);

        Row bodyRow3_14 = sheet3.createRow(++cell3_num);
        Cell cell3_14_1 = bodyRow3_14.createCell(1);
        cell3_14_1.setCellValue("付款方式：");

        // 包装
        Row bodyRow3_15 = sheet3.createRow(++cell3_num);
        Cell cell3_15_1 = bodyRow3_15.createCell(0);
        cell3_15_1.setCellValue("3、");
        Cell cell3_15_2 = bodyRow3_15.createCell(1);
        cell3_15_2.setCellValue("Packing：");
        Cell cell3_15_3 = bodyRow3_15.createCell(2);
        cell3_15_3.setCellValue(model.getPack());

        Row bodyRow3_16 = sheet3.createRow(++cell3_num);
        Cell cell3_16_1 = bodyRow3_16.createCell(1);
        cell3_16_1.setCellValue("包装：");

        // 装货港
        Row bodyRow3_17 = sheet3.createRow(++cell3_num);
        Cell cell3_17_1 = bodyRow3_17.createCell(0);
        cell3_17_1.setCellValue("4、");
        Cell cell3_17_2 = bodyRow3_17.createCell(1);
        cell3_17_2.setCellValue("Port of Loading：");
        Cell cell3_17_3 = bodyRow3_17.createCell(2);
        cell3_17_3.setCellValue(model.getPortLoading());

        Row bodyRow3_18 = sheet3.createRow(++cell3_num);
        Cell cell3_18_1 = bodyRow3_18.createCell(1);
        cell3_18_1.setCellValue("装货港：");

        // 卸货港
        Row bodyRow3_19 = sheet3.createRow(++cell3_num);
        Cell cell3_19_1 = bodyRow3_19.createCell(0);
        cell3_19_1.setCellValue("5、");
        Cell cell3_19_2 = bodyRow3_19.createCell(1);
        cell3_19_2.setCellValue("Port of Discharge：");
        Cell cell3_19_3 = bodyRow3_19.createCell(2);
        cell3_19_3.setCellValue(model.getPortDest());

        Row bodyRow3_20 = sheet3.createRow(++cell3_num);
        Cell cell3_20_1 = bodyRow3_20.createCell(1);
        cell3_20_1.setCellValue("卸货港：");

        // 付款条约
        Row bodyRow3_21 = sheet3.createRow(++cell3_num);
        Cell cell3_21_1 = bodyRow3_21.createCell(0);
        cell3_21_1.setCellValue("6、");
        Cell cell3_21_2 = bodyRow3_21.createCell(1);
        cell3_21_2.setCellValue("Payment RULES：");
        Cell cell3_21_3 = bodyRow3_21.createCell(2);
        cell3_21_3.setCellValue(model.getPayRules());

        Row bodyRow3_22 = sheet3.createRow(++cell3_num);
        Cell cell3_22_1 = bodyRow3_22.createCell(1);
        cell3_22_1.setCellValue("付款条约：");

        // 原产国
        Row bodyRow3_23 = sheet3.createRow(++cell3_num);
        Cell cell3_23_1 = bodyRow3_23.createCell(0);
        cell3_23_1.setCellValue("7、");
        Cell cell3_23_2 = bodyRow3_23.createCell(1);
        cell3_23_2.setCellValue("Country of Origin：");
        Cell cell3_23_3 = bodyRow3_23.createCell(2);
        cell3_23_3.setCellValue(model.getCountry());

        Row bodyRow3_24 = sheet3.createRow(++cell3_num);
        Cell cel3_24_1 = bodyRow3_24.createCell(1);
        cel3_24_1.setCellValue("原产国：");

        // 境外发货人
        Row bodyRow3_25 = sheet3.createRow(++cell3_num);
        Cell cell3_25_1 = bodyRow3_25.createCell(0);
        cell3_25_1.setCellValue("8、");
        Cell cell3_25_2 = bodyRow3_25.createCell(1);
        cell3_25_2.setCellValue("Shipper：");
        Cell cell3_25_3 = bodyRow3_25.createCell(2);
        cell3_25_3.setCellValue(model.getShipper());

        Row bodyRow3_26 = sheet3.createRow(++cell3_num);
        Cell cell3_26_1 = bodyRow3_26.createCell(1);
        cell3_26_1.setCellValue("境外发货人：");

        // 唛头
        Row bodyRow3_27 = sheet3.createRow(++cell3_num);
        Cell cell3_27_1 = bodyRow3_27.createCell(0);
        cell3_27_1.setCellValue("9、");
        Cell cell3_27_2 = bodyRow3_27.createCell(1);
        cell3_27_2.setCellValue("Marks and Numbers：");
        Cell cell3_27_3 = bodyRow3_27.createCell(2);
        cell3_27_3.setCellValue(model.getMark());

        Row bodyRow3_28 = sheet3.createRow(++cell3_num);
        Cell cell3_28_1 = bodyRow3_28.createCell(1);
        cell3_28_1.setCellValue("唛头：");
        cell3_num++;

        Row bodyRow3_29 = sheet3.createRow(++cell3_num);
        Cell cell3_29_1 = bodyRow3_29.createCell(0);
        cell3_29_1.setCellValue("注：本装箱单通过扫描件方式出具同样具有法律效力。");
        /**
         * 装箱明细
         */
        String sheet4_name = "PACKING DETAIL\n装箱明细";
        Sheet sheet4 = workbook.createSheet("装箱明细");
        sheet4.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet4.addMergedRegion(new CellRangeAddress(2, 2, 1, 7));
        sheet4.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));
        sheet4.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));
        // 设置标题和列宽
        Row headrow4 = sheet4.createRow(0);
        headrow4.setHeight((short) 1000);
        Cell cell0_4 = headrow4.createCell(0);
        sheet4.setColumnWidth(0, 20 * 256);
        sheet4.setColumnWidth(1, 25 * 256);
        sheet4.setColumnWidth(2, 13 * 256);
        sheet4.setColumnWidth(3, 10 * 256);
        sheet4.setColumnWidth(4, 10 * 256);
        sheet4.setColumnWidth(5, 12 * 256);
        sheet4.setColumnWidth(6, 12 * 256);
        sheet4.setColumnWidth(7, 12 * 256);
        sheet4.setColumnWidth(8, 10 * 256);
        cell0_4.setCellStyle(headStyle);
        XSSFRichTextString ts4 = new XSSFRichTextString(sheet4_name);
        ts4.applyFont(0, ts4.length(), font1);// 下划线的
        cell0_4.setCellValue(ts4);
        // Consignee
        Row bodyRow4_1 = sheet4.createRow(2);
        Cell cell4_1_1 = bodyRow4_1.createCell(0);
        cell4_1_1.setCellValue("Consignee：");
        cell4_1_1.setCellStyle(rightStyle);
        Cell cell4_1_2 = bodyRow4_1.createCell(1);
        cell4_1_2.setCellValue(consignee);
        // 收货人
        Row bodyRow4_3 = sheet4.createRow(3);
        Cell cell4_3_1 = bodyRow4_3.createCell(0);
        cell4_3_1.setCellValue("收货人：");
        cell4_3_1.setCellStyle(rightStyle);
        Cell cell4_3_2 = bodyRow4_3.createCell(1);
        cell4_3_2.setCellValue(addressee);
        Row bodyRow4_4 = sheet4.createRow(4);

        // 商品明细
        Row bodyRow4_5 = sheet4.createRow(5);
        bodyRow4_5.setHeight((short) 1200);
        Cell cell4_5_1 = bodyRow4_5.createCell(0);
        cell4_5_1.setCellValue(new XSSFRichTextString("The pallet no.\r托盘号"));
        cell4_5_1.setCellStyle(tableThStyle);
        Cell cell4_5_2 = bodyRow4_5.createCell(1);
        cell4_5_2.setCellValue(new XSSFRichTextString("COMMODITY.\r商品名称"));
        cell4_5_2.setCellStyle(tableThStyle);
        Cell cell4_5_3 = bodyRow4_5.createCell(2);
        cell4_5_3.setCellValue(new XSSFRichTextString("Article number\r商品货号"));
        cell4_5_3.setCellStyle(tableThStyle);
        Cell cell4_5_4 = bodyRow4_5.createCell(3);
        cell4_5_4.setCellValue(new XSSFRichTextString("QUANTITY\r( PCS )\r数量"));
        cell4_5_4.setCellStyle(tableThStyle);
        Cell cell4_5_5 = bodyRow4_5.createCell(4);
        cell4_5_5.setCellValue(new XSSFRichTextString("Cartons\r箱数"));
        cell4_5_5.setCellStyle(tableThStyle);
        Cell cell4_5_6 = bodyRow4_5.createCell(5);
        cell4_5_6.setCellValue(new XSSFRichTextString("NET WEIGHT\r（KGS）\r净重"));
        cell4_5_6.setCellStyle(tableThStyle);
        Cell cell4_5_7 = bodyRow4_5.createCell(6);
        cell4_5_7.setCellValue(new XSSFRichTextString("GROSS WEIGHT\r（KGS）\r毛重"));
        cell4_5_7.setCellStyle(tableThStyle);
        Cell cell4_5_8 = bodyRow4_5.createCell(7);
        cell4_5_8.setCellValue(new XSSFRichTextString(" Container No.\r柜号"));
        cell4_5_8.setCellStyle(tableThStyle);

        int cell4_num = 5;
        int count_num4 = 0;// 数量统计
        int count_cartons = 0;// 箱数统计
        Double count_NetWeight4 = 0.0;// 净重统计
        Double count_GrossWeight4 = 0.0;// 毛重统计

        List<CustomsPackingDetailsDTO> detailsDTOList = calculateWeights(model.getDetailsDTOList());

        if (detailsDTOList.isEmpty()) {
            for (CustomsDeclareItemDTO item : model.getItemList()) {
                Row bodyRow4_6 = sheet4.createRow(++cell4_num);
                //获取字符串总宽度
                double width = item.getGoodsName().getBytes().length * 256;
                double needsRows = width / (25 * 256);
                if (needsRows < 1.0) {
                    needsRows = 1.0;
                }
                short height = (short) (fontHeight * Math.ceil(needsRows));
                bodyRow4_6.setHeight(height);
                String palletNo = "";
                if (StringUtils.isNotBlank(item.getPalletNo())) {
                    palletNo = item.getPalletNo();
                }
                Cell cell4_6_1 = bodyRow4_6.createCell(0);
                cell4_6_1.setCellValue(palletNo);
                cell4_6_1.setCellStyle(tableTrStyle);
                Cell cell4_6_2 = bodyRow4_6.createCell(1);
                cell4_6_2.setCellValue(item.getGoodsName());
                cell4_6_2.setCellStyle(tableTrStyle);
                Cell cell4_6_3 = bodyRow4_6.createCell(2);
                cell4_6_3.setCellValue(item.getGoodsNo());
                cell4_6_3.setCellStyle(tableTrStyle);
                Cell cell4_6_4 = bodyRow4_6.createCell(3);
                if (item.getNum() == null) {
                    cell4_6_4.setCellValue("");
                } else {
                    cell4_6_4.setCellValue(item.getNum());
                    count_num4 += item.getNum();
                }
                cell4_6_4.setCellStyle(tableTrStyle);
                Cell cell4_6_5 = bodyRow4_6.createCell(4);
                cell4_6_5.setCellStyle(tableTrStyle);
                Integer cartons = 0;
                if (item.getCartons() != null) {
                    cartons = item.getCartons();
                    count_cartons += item.getCartons();
                }
                cell4_6_5.setCellValue(cartons);
                Cell cell4_6_6 = bodyRow4_6.createCell(5);
                if (item.getNetWeight() == null) {
                    cell4_6_6.setCellValue("");
                } else {
                    cell4_6_6.setCellValue(item.getNetWeight());
                    count_NetWeight4 += item.getNetWeight();
                }
                cell4_6_6.setCellStyle(tableTrStyle);
                Cell cell4_6_7 = bodyRow4_6.createCell(6);
                if (item.getGrossWeight() == null) {
                    cell4_6_7.setCellValue("");
                } else {
                    cell4_6_7.setCellValue(item.getGrossWeight());
                    count_GrossWeight4 += item.getGrossWeight();
                }
                cell4_6_7.setCellStyle(tableTrStyle);

                Cell cell4_6_8 = bodyRow4_6.createCell(7);
                String cabinetNo = "/";
                cell4_6_8.setCellValue(cabinetNo);
                cell4_6_8.setCellStyle(tableTrStyle);
            }
        } else {
            for (CustomsPackingDetailsDTO item : detailsDTOList) {
                Row bodyRow4_6 = sheet4.createRow(++cell4_num);
                //获取字符串总宽度
                double width = item.getGoodsName().getBytes().length * 256;
                double needsRows = width / (25 * 256);
                if (needsRows < 1.0) {
                    needsRows = 1.0;
                }
                short height = (short) (fontHeight * Math.ceil(needsRows));
                bodyRow4_6.setHeight(height);
                String palletNo = "";
                if (StringUtils.isNotBlank(item.getTorrNo())) {
                    palletNo = item.getTorrNo();
                }
                Cell cell4_6_1 = bodyRow4_6.createCell(0);
                cell4_6_1.setCellValue(palletNo);
                cell4_6_1.setCellStyle(tableTrStyle);
                Cell cell4_6_2 = bodyRow4_6.createCell(1);
                cell4_6_2.setCellValue(item.getGoodsName());
                cell4_6_2.setCellStyle(tableTrStyle);
                Cell cell4_6_3 = bodyRow4_6.createCell(2);
                cell4_6_3.setCellValue(item.getGoodsNo());
                cell4_6_3.setCellStyle(tableTrStyle);
                Cell cell4_6_4 = bodyRow4_6.createCell(3);
                if (item.getPiecesNum() == null) {
                    cell4_6_4.setCellValue("");
                } else {
                    cell4_6_4.setCellValue(item.getPiecesNum());
                    count_num4 += item.getPiecesNum();
                }
                cell4_6_4.setCellStyle(tableTrStyle);
                Cell cell4_6_5 = bodyRow4_6.createCell(4);
                cell4_6_5.setCellStyle(tableTrStyle);
                Integer cartons = 0;
                if (item.getBoxNum() != null) {
                    cartons = item.getBoxNum();
                    count_cartons += item.getBoxNum();
                }
                cell4_6_5.setCellValue(cartons);
                Cell cell4_6_6 = bodyRow4_6.createCell(5);
                if (item.getNetWeight() == null) {
                    cell4_6_6.setCellValue("");
                } else {
                    cell4_6_6.setCellValue(item.getNetWeight());
                    count_NetWeight4 += item.getNetWeight();
                }
                cell4_6_6.setCellStyle(tableTrStyle);
                Cell cell4_6_7 = bodyRow4_6.createCell(6);
                if (item.getGrossWeight() == null) {
                    cell4_6_7.setCellValue("");
                } else {
                    cell4_6_7.setCellValue(item.getGrossWeight());
                    count_GrossWeight4 += item.getGrossWeight();
                }
                cell4_6_7.setCellStyle(tableTrStyle);

                Cell cell4_6_8 = bodyRow4_6.createCell(7);
                String cabinetNo = "";
                if (StringUtils.isNotBlank(item.getCabinetNo())) {
                    cabinetNo = item.getCabinetNo();
                }
                cell4_6_8.setCellValue(cabinetNo);
                cell4_6_8.setCellStyle(tableTrStyle);
            }
        }


		Row bodyRow4_7 = sheet4.createRow(++cell4_num);
		bodyRow4_7.setHeight((short) 300);
		Cell cell4_7_1 = bodyRow4_7.createCell(0);
		if (model.getTorrNum() == null) {
			cell4_7_1.setCellValue("");
		} else {
			cell4_7_1.setCellValue("TOTAL：" + model.getTorrNum() + "托");
		}

		cell4_7_1.setCellStyle(tableTrStyle);
		Cell cell4_7_2 = bodyRow4_7.createCell(1);
		cell4_7_2.setCellStyle(tableTrStyle);
		Cell cell4_7_3 = bodyRow4_7.createCell(2);
		cell4_7_3.setCellStyle(tableTrStyle);
		Cell cell4_7_4 = bodyRow4_7.createCell(3);
		cell4_7_4.setCellValue(count_num4);
		cell4_7_4.setCellStyle(tableTrStyle);
		Cell cell4_7_5 = bodyRow4_7.createCell(4);
		cell4_7_5.setCellValue(count_cartons);
		cell4_7_5.setCellStyle(tableTrStyle);
		Cell cell4_7_6 = bodyRow4_7.createCell(5);
		cell4_7_6.setCellStyle(tableTrStyle);
		cell4_7_6.setCellValue(count_NetWeight4);
		Cell cell4_7_7 = bodyRow4_7.createCell(6);
		cell4_7_7.setCellValue(count_GrossWeight4);
		cell4_7_7.setCellStyle(tableTrStyle);
		Cell cell4_7_8 = bodyRow4_7.createCell(7);
		cell4_7_8.setCellStyle(tableTrStyle);

		Row bodyRow4_10 = sheet4.createRow(cell4_num+2);
		Cell cell4_10_1 = bodyRow4_10.createCell(4);
		cell4_10_1.setCellValue("电商企业盖章");
		cell4_10_1.setCellStyle(tableTrStyle);

        /**
         * 原料成分占比
         */
        String sheet5_name = "原料成分占比";
        Sheet sheet5 = workbook.createSheet(sheet5_name);
        sheet5.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        // 设置标题和列宽
        Row headrow5 = sheet5.createRow(0);
        headrow5.setHeight((short) 700);
        Cell cell0_5 = headrow5.createCell(0);
        sheet5.setColumnWidth(0, 46 * 256);
        sheet5.setColumnWidth(1, 36 * 256);
        sheet5.setColumnWidth(2, 4 * 256);
        cell0_5.setCellStyle(headStyle);
        XSSFRichTextString ts5 = new XSSFRichTextString(sheet5_name);
        ts5.applyFont(0, ts5.length(), font1);// 下划线的
        cell0_5.setCellValue(ts5);
        // 商品明细
        Row bodyRow5_1 = sheet5.createRow(2);
        bodyRow5_1.setHeight((short) 300);
        Cell cell5_1_1 = bodyRow5_1.createCell(0);
        cell5_1_1.setCellValue(new XSSFRichTextString("商品名称"));
        cell5_1_1.setCellStyle(tableThStyle);
        Cell cell5_1_2 = bodyRow5_1.createCell(1);
        cell5_1_2.setCellValue(new XSSFRichTextString("成分占比"));
        cell5_1_2.setCellStyle(tableThStyle);
        int cell5_num = 2;
        for (CustomsDeclareItemDTO item : model.getItemList()) {
            Row bodyRow5_2 = sheet5.createRow(++cell5_num);
            //获取字符串总宽度
            double width = item.getGoodsName().getBytes().length * 256;
            double needsRows = width / (46 * 256);
            if (needsRows < 1.0) {
                needsRows = 1.0;
            }
            short height = (short) (fontHeight * Math.ceil(needsRows));
            bodyRow5_2.setHeight(height);
            Cell cell5_2_1 = bodyRow5_2.createCell(0);
            cell5_2_1.setCellValue(item.getGoodsName());
            cell5_2_1.setCellStyle(tableTrStyle);
            Cell cell5_2_2 = bodyRow5_2.createCell(1);
            cell5_2_2.setCellValue(item.getConstituentRatio());
            cell5_2_2.setCellStyle(tableTrStyle);
        }
        cell5_num++;
        Row bodyRow5_3 = sheet5.createRow(++cell5_num);
        Cell cell5_3_1 = bodyRow5_3.createCell(0);
        cell5_3_1.setCellValue("注：本成分占比通过扫描件方式出具同样具有法律效力。");
        return workbook;
    }

    /**
     * 计算毛重：商品毛重=以重算后的商品净重占比摊分原有单据对应商品的商品毛重，最后一个倒减；
     */
    public static List<CustomsPackingDetailsDTO> calculateWeights(List<CustomsPackingDetailsDTO> customsPackingDetailsDTOS) {
        List<CustomsPackingDetailsDTO> resultDTOs = new ArrayList<>();

        //货号对应的总净重
        Map<String, Double> goodsNetWeightMap = new HashMap<>();
        //货号对应的总毛重
        Map<String, Double> goodsGrossWeightMap = new HashMap<>();
        //货号对应的箱装明细集合
        Map<String, List<CustomsPackingDetailsDTO>> customsPackingDetailsMap = new HashMap<>();

        Map<String, CustomsPackingDetailsDTO> detailsDTOMap = new HashMap<>();
        if (customsPackingDetailsDTOS != null) {
            //合并相同货号+相同托盘号+相同柜号的商品信息
            for (CustomsPackingDetailsDTO detailsDTO : customsPackingDetailsDTOS) {
                String key = detailsDTO.getGoodsNo() + "_" + detailsDTO.getCabinetNo();
                if (detailsDTOMap.containsKey(key)) {
                    CustomsPackingDetailsDTO existDto = detailsDTOMap.get(key);
                    detailsDTO.setPiecesNum(existDto.getPiecesNum() + detailsDTO.getPiecesNum());
                    detailsDTO.setBoxNum(existDto.getBoxNum() + detailsDTO.getBoxNum());

                    String torrNo = existDto.getTorrNo();
                    String newTorrNo = detailsDTO.getTorrNo();
                    String[] torrNoArr = torrNo.split("&");
                    String[] newTorrNoArr = newTorrNo.split("&");
                    List<String> torrNoList = new ArrayList<>();
                    List<String> newTorrNoList = new ArrayList<>();
                    for (int i = 0; i < torrNoArr.length; i++) {
                        torrNoList.add(torrNoArr[i]);
                    }

                    for (int i = 0; i < newTorrNoArr.length; i++) {
                        newTorrNoList.add(newTorrNoArr[i]);
                    }

                    List<String> collect = Stream.of(torrNoList, newTorrNoList)
                            .flatMap(Collection::stream)
                            .distinct()
                            .collect(Collectors.toList());

                    detailsDTO.setTorrNo(StringUtils.join(collect.toArray(), "&"));
                }
                detailsDTOMap.put(key, detailsDTO);
            }

            for (String key : detailsDTOMap.keySet()) {
                CustomsPackingDetailsDTO detailsDTO = detailsDTOMap.get(key);
                Double existNetWeight = 0.0;
                Double existGrossWeight = 0.0;
                if (goodsNetWeightMap.containsKey(detailsDTO.getGoodsNo())) {
                    existNetWeight = goodsNetWeightMap.get(detailsDTO.getGoodsNo());
                }
                existNetWeight += detailsDTO.getNetWeight() * detailsDTO.getPiecesNum();
                goodsNetWeightMap.put(detailsDTO.getGoodsNo(), existNetWeight);

				if (goodsGrossWeightMap.containsKey(detailsDTO.getGoodsNo())) {
					existGrossWeight = goodsGrossWeightMap.get(detailsDTO.getGoodsNo());
				}
				existGrossWeight += detailsDTO.getTotalGrossWeight();
                goodsGrossWeightMap.put(detailsDTO.getGoodsNo(), existGrossWeight);

                List<CustomsPackingDetailsDTO> existDetails = new ArrayList<>();
                existDetails.add(detailsDTO);
                if (customsPackingDetailsMap.containsKey(detailsDTO.getGoodsNo())) {
                    existDetails.addAll(customsPackingDetailsMap.get(detailsDTO.getGoodsNo()));
                }
                customsPackingDetailsMap.put(detailsDTO.getGoodsNo(), existDetails);
            }
        }

        for (String goodsNo : customsPackingDetailsMap.keySet()) {
            Double totalNetWeight = goodsNetWeightMap.get(goodsNo);
            Double totalGrossWeight = goodsGrossWeightMap.get(goodsNo);
            List<CustomsPackingDetailsDTO> detailsDTOS = customsPackingDetailsMap.get(goodsNo);

            Double alreadyCalWeight = 0.0;

            for (int i = 0; i < detailsDTOS.size(); i++) {
                CustomsPackingDetailsDTO detailsDTO = detailsDTOS.get(i);
                if (i == detailsDTOS.size() - 1) {
                    Double grossWeight = totalGrossWeight - alreadyCalWeight;
                    detailsDTO.setNetWeight(detailsDTO.getNetWeight() * detailsDTO.getPiecesNum());
                    if (totalNetWeight.compareTo(0.0) != 0) {
                        detailsDTO.setGrossWeight(grossWeight);
                    } else {
                        detailsDTO.setGrossWeight(0.0);
                    }
                } else {
                    if (totalNetWeight.compareTo(0.0) != 0) {
                        Double grossWeight = detailsDTO.getNetWeight() * detailsDTO.getPiecesNum() / totalNetWeight * totalGrossWeight;
                        BigDecimal grossWeightBD = new BigDecimal(grossWeight.toString());
                        Double newGrossWight = grossWeightBD.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        detailsDTO.setGrossWeight(newGrossWight);
                        alreadyCalWeight += newGrossWight;
                    } else {
                        detailsDTO.setGrossWeight(0.0);
                    }
                    detailsDTO.setNetWeight(detailsDTO.getNetWeight() * detailsDTO.getPiecesNum());
                }
                resultDTOs.add(detailsDTO);
            }
        }

        //相同柜号的放在一起
        List<CustomsPackingDetailsDTO> nullsLastCollect = resultDTOs.stream()
                .sorted(Comparator.comparing(CustomsPackingDetailsDTO::getCabinetNo, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());

        return nullsLastCollect;
    }

    //唯品郑州word模板
    public static XWPFDocument createCustomsDeclareWord(FirstCustomsInfoDTO model) {
        XWPFDocument document = new XWPFDocument();
        //添加一级标题
        XWPFParagraph para1 = document.createParagraph();
        para1.setAlignment(ParagraphAlignment.CENTER);
        para1.setStyle("1");
        XWPFRun run1 = para1.createRun();
        run1.setText("订购合同");
        run1.setColor("000000");
        run1.setFontSize(20);

        // 标题2
        XWPFParagraph para2 = document.createParagraph();
        para2.setAlignment(ParagraphAlignment.CENTER);
        para2.setStyle("2");
        XWPFRun run2 = para2.createRun();
        run2.setText("PURCHASE CONTRACT");
        run2.setColor("000000");
        run2.setFontSize(16);

        //段落
        XWPFParagraph contractNoParagraph = document.createParagraph();
        XWPFRun contractNoParagraphRun = contractNoParagraph.createRun();
        contractNoParagraphRun.setText("合同号（CONTRACT NO）:" + model.getPoNo());
        contractNoParagraphRun.setColor("222222");
        contractNoParagraphRun.setFontSize(12);

        XWPFParagraph dateParagraph = document.createParagraph();
        XWPFRun dateParagraphRun = dateParagraph.createRun();
        dateParagraphRun.setText("日期（DATE）:" + TimeUtils.formatDay(model.getOrderDate()));
        dateParagraphRun.setColor("222222");
        dateParagraphRun.setFontSize(12);

        XWPFParagraph buyerParagraph = document.createParagraph();
        XWPFRun buyerParagraphRun = buyerParagraph.createRun();
        buyerParagraphRun.setText("买方：Vipshop International Holdings Limited");
        buyerParagraphRun.setColor("222222");
        buyerParagraphRun.setFontSize(12);

        XWPFParagraph cnAddrParagraph = document.createParagraph();
        XWPFRun cnAddrParagraphRun = cnAddrParagraph.createRun();
        cnAddrParagraphRun.setText("地址：香港湾仔皇后大道东213号胡忠大厦22楼2209室");
        cnAddrParagraphRun.setColor("222222");
        cnAddrParagraphRun.setFontSize(12);

        XWPFParagraph enAddrParagraph = document.createParagraph();
        XWPFRun enAddrParagraphRun = enAddrParagraph.createRun();
        enAddrParagraphRun.setText("ADDRESS: Unit 2209, 22/F, Wu Chung House 213 Queen's Road East Wanchai, Hong Kong");
        enAddrParagraphRun.setColor("222222");
        enAddrParagraphRun.setFontSize(12);

        XWPFParagraph telParagraph = document.createParagraph();
        XWPFRun telParagraphRun = telParagraph.createRun();
        telParagraphRun.setText("电话：020-22330000");
        telParagraphRun.setColor("222222");
        telParagraphRun.setFontSize(12);

        XWPFParagraph telNumParagraph = document.createParagraph();
        XWPFRun telNumParagraphRun = telNumParagraph.createRun();
        telNumParagraphRun.setText("TEL: 020-22330000");
        telNumParagraphRun.setColor("222222");
        telNumParagraphRun.setFontSize(12);

        XWPFParagraph sellerParagraph = document.createParagraph();
        XWPFRun sellerParagraphRun = sellerParagraph.createRun();
        sellerParagraphRun.setText("卖方（SELLER）:" + model.getMerchantEnName());
        sellerParagraphRun.setColor("222222");
        sellerParagraphRun.setFontSize(12);

        XWPFParagraph sellerAddrParagraph = document.createParagraph();
        XWPFRun sellerAddrParagraphRun = sellerAddrParagraph.createRun();
        sellerAddrParagraphRun.setText("地址：" + model.getSecondPartyAddr());
        sellerAddrParagraphRun.setColor("222222");
        sellerAddrParagraphRun.setFontSize(12);

        XWPFParagraph textParagraph = document.createParagraph();
        XWPFRun textParagraphRun = textParagraph.createRun();
        textParagraphRun.setText("兹经买卖双方同意由买方购进卖方售出以下商品：");
        textParagraphRun.setColor("222222");
        textParagraphRun.setFontSize(12);

        XWPFParagraph enTextParagraph = document.createParagraph();
        XWPFRun enTextParagraphRun = enTextParagraph.createRun();
        enTextParagraphRun.setText("THIS CONTRACT IS MADE BY AND BETWEEN THE BUYER AND THE SELLER, WHEREBY THE BUYER AGREE TO BUY AND THE SELLER AGREE TO SELL THE UNDERMENTIONED COMMODITIES AS SIPULATED HEREINAFTER");
        enTextParagraphRun.setColor("222222");
        enTextParagraphRun.setFontSize(12);

        XWPFParagraph firstParagraph = document.createParagraph();
        XWPFRun firstParagraphRun = firstParagraph.createRun();
        firstParagraphRun.setText("(1)商品名称及规格（NAME OF COMMODITIES AND SPECIFICATION）:");
        firstParagraphRun.setColor("222222");
        firstParagraphRun.setFontSize(12);

        XWPFTable infoTable = document.createTable(model.getItemList().size() + 1, 5);
        //表格第一行
        XWPFTableRow infoTableRowOne = infoTable.getRow(0);
        infoTableRowOne.getCell(0).setText("条码");
        infoTableRowOne.getCell(1).setText("Description\n（货物名称）");
        infoTableRowOne.getCell(2).setText("Qty\n(数量)");
        infoTableRowOne.getCell(3).setText("Unit price\n（单价)");
        infoTableRowOne.getCell(4).setText("Total amount\n（总价）");

        int totalNum = 0;
        BigDecimal totalAmount = new BigDecimal("0");
        Set<String> brandNameSet = new HashSet<>();
        int index = 1;
        for (CustomsDeclareItemDTO itemDTO : model.getItemList()) {
            XWPFTableRow infoTableRow = infoTable.getRow(index++);
            infoTableRow.getCell(0).setText(itemDTO.getBarcode());
            infoTableRow.getCell(1).setText(itemDTO.getGoodsName());
            String num = "";
            if (itemDTO.getNum() != null) {
                num = String.valueOf(itemDTO.getNum());
                totalNum += itemDTO.getNum();
            }
            infoTableRow.getCell(2).setText(num);
            String price = "";
            if (itemDTO.getPrice() != null) {
                price = String.valueOf(itemDTO.getPrice());
            }
            infoTableRow.getCell(3).setText(price);
            String amount = "";
            if (itemDTO.getAmount() != null) {
                amount = String.valueOf(itemDTO.getAmount());
                totalAmount = totalAmount.add(itemDTO.getAmount());
            }
            infoTableRow.getCell(4).setText(amount);
            if (StringUtils.isNotBlank(itemDTO.getBrandName())) {
                brandNameSet.add(itemDTO.getBrandName());
            }
        }
        String country = "";
        if (StringUtils.isNotBlank(model.getCountry())) {
            country = model.getCountry();
        }
        XWPFParagraph countryParagraph = document.createParagraph();
        XWPFRun countryParagraphRun = countryParagraph.createRun();
        countryParagraphRun.setText("原产国:" + country);
        countryParagraphRun.setColor("222222");
        countryParagraphRun.setFontSize(12);

        XWPFParagraph brandParagraph = document.createParagraph();
        XWPFRun brandParagraphRun = brandParagraph.createRun();
        brandParagraphRun.setText("品牌名:" + StringUtils.join(brandNameSet, ","));
        brandParagraphRun.setColor("222222");
        brandParagraphRun.setFontSize(12);

        XWPFParagraph totalNumParagraph = document.createParagraph();
        XWPFRun totalNumParagraphRun = totalNumParagraph.createRun();
        totalNumParagraphRun.setText("总数量:" + totalNum);
        totalNumParagraphRun.setColor("222222");
        totalNumParagraphRun.setFontSize(12);

        XWPFParagraph totalAmountParagraph = document.createParagraph();
        XWPFRun totalAmountParagraphRun = totalAmountParagraph.createRun();
        totalAmountParagraphRun.setText("(2)总值（TOTAL VALUE）: TOTAL 人民币:" + totalAmount);
        totalAmountParagraphRun.setColor("222222");
        totalAmountParagraphRun.setFontSize(12);

        String pack = "";
        if (StringUtils.isNotBlank(model.getPack())) {
            pack = model.getPack();
        }
        XWPFParagraph packParagraph = document.createParagraph();
        XWPFRun packParagraphRun = packParagraph.createRun();
        packParagraphRun.setText("(3)包装（PACKAGE）:" + pack);
        packParagraphRun.setColor("222222");
        packParagraphRun.setFontSize(12);

        XWPFParagraph manufactureParagraph = document.createParagraph();
        XWPFRun manufactureParagraphRun = manufactureParagraph.createRun();
        manufactureParagraphRun.setText("(4)生产国别及制造厂商(COUNTRY OF ORIGIN & MANUFACTURER):");
        manufactureParagraphRun.setColor("222222");
        manufactureParagraphRun.setFontSize(12);

        XWPFParagraph paymentParagraph = document.createParagraph();
        XWPFRun paymentParagraphRun = paymentParagraph.createRun();
        paymentParagraphRun.setText("(5)付款条件（TERMS OF PAYMENT）:CIF");
        paymentParagraphRun.setColor("222222");
        paymentParagraphRun.setFontSize(12);

        XWPFParagraph portParagraph = document.createParagraph();
        XWPFRun portParagraphRun = portParagraph.createRun();
        portParagraphRun.setText("(6)装运口岸（PORT OF LOADING）: ");
        portParagraphRun.setColor("222222");
        portParagraphRun.setFontSize(12);

        XWPFParagraph destinationParagraph = document.createParagraph();
        XWPFRun destinationParagraphRun = destinationParagraph.createRun();
        destinationParagraphRun.setText("(7)目的口岸（PORT OF DESTINATION ）:zhengzhou China");
        destinationParagraphRun.setColor("222222");
        destinationParagraphRun.setFontSize(12);

        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun paragraph1Run = paragraph1.createRun();
        paragraph1Run.setText("买方（THE BUYER）:                卖方（THE SELLER）：");
        paragraph1Run.setColor("222222");
        paragraph1Run.setFontSize(12);

        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun paragraph2Run = paragraph2.createRun();
        paragraph2Run.setText("Vipshop International Holdings Limited    "+ model.getMerchantEnName());
        paragraph2Run.setColor("222222");
        paragraph2Run.setFontSize(12);

        return document;
    }

//    public static Workbook exportCustomsExcel(String templateName, FirstCustomsInfoDTO model) throws Exception{
//    	String templatePath = "classpath:/customsTemplate/" +templateName +".xlsx";
//    	ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
//        Resource resource=resolver.getResource(templatePath);
//    	InputStream is = resource.getInputStream();
//    	Context context = new Context();
//		context.putVar("dto", model);
//
//        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
//        PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
//        transformer.setFullFormulaRecalculationOnOpening(true);//打开文件时将重新计算所有公式
//        //添加自定义功能
//        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
//        Map<String, Object> funcs = new HashMap<String, Object>();
//        funcs.put("timeUtils", new TimeUtils());
//        JexlBuilder jb = new JexlBuilder();
//        jb.namespaces(funcs);
//        JexlEngine je = jb.create();
//        evaluator.setJexlEngine(je);
//
//        AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
//        areaBuilder.setTransformer(transformer);
//        List<Area> xlsAreaList = areaBuilder.build();
//        jxlsHelper.setHideTemplateSheet(true);
//        for (Area xlsArea : xlsAreaList) {
//            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
//            xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
//            xlsArea.processFormulas();
//        }
//        return transformer.getWorkbook();
//    }

//    public static Workbook exportCustomsExcel(String templateName, FinancingOrderDTO dto) throws Exception{
//        String templatePath = "classpath:/customsTemplate/" +templateName +".xlsx";
//        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
//        Resource resource=resolver.getResource(templatePath);
//        InputStream is = resource.getInputStream();
//        Context context = new Context();
//        context.putVar("dto", dto);
//
//        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
//        PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
//        transformer.setFullFormulaRecalculationOnOpening(true);
//        //添加自定义功能
//
//        AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
//        areaBuilder.setTransformer(transformer);
//        List<Area> xlsAreaList = areaBuilder.build();
//        jxlsHelper.setHideTemplateSheet(true);
//        for (Area xlsArea : xlsAreaList) {
//            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
//            xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
//            xlsArea.processFormulas();
//        }
//
//        return transformer.getWorkbook();
//    }

    public static Workbook exportTemplateExcel(String templateName, Object dto) throws Exception{
        String templatePath = "classpath:/customsTemplate/" +templateName +".xlsx";
        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource(templatePath);
        InputStream is = resource.getInputStream();
        Context context = new Context();
        context.putVar("dto", dto);

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
        transformer.setFullFormulaRecalculationOnOpening(true);
        //添加自定义功能
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("timeUtils", new TimeUtils());
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
        areaBuilder.setTransformer(transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        jxlsHelper.setHideTemplateSheet(true);
        jxlsHelper.setUseFastFormulaProcessor(false);
        for (Area xlsArea : xlsAreaList) {
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
            xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
            xlsArea.processFormulas();
        }

        return transformer.getWorkbook();
    }

    public static XWPFDocument exportCustomsWord(String templateName, Object model) throws Exception{
    	String templatePath = "classpath:/customsTemplate/" +templateName +".docx";
    	ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource(templatePath);
        InputStream is = resource.getInputStream();
        HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy();
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL();//启用el表达式
        builder.buildGramer("${", "}");
        builder.bind("itemList", hackLoopTableRenderPolicy).build();
        XWPFTemplate template = XWPFTemplate.compile(is, builder.build()).render(model);

        return template.getXWPFDocument();
    }

    //删除目录下的所有子文件
    public static void deleteFile(String dir) {
        File file = new File(dir);
        if (file.exists()) {//目录存在
            //删除子文件
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    f.delete();
                }
            }
        }
    }

    public static void main(String[] args) {
        InputStream is = null;
        OutputStream os = null;
        try{
            is = new FileInputStream("E:\\in-2.xlsx");
            os = new FileOutputStream("E:\\out.xlsx");

            Map<String,Object> dto = new HashMap<>();
            List<Map<String,Object>> list = new ArrayList<>();
            for(int i = 5 ; i>0 ;i--){
                Integer num = 12 + i;
                BigDecimal price = new BigDecimal(i).multiply(new BigDecimal(10.131));
                BigDecimal amount = price.multiply(new BigDecimal(num));
                Double totalNetWeight = new BigDecimal(i).multiply(new BigDecimal(1.0118)).doubleValue();
                Double totalGrossWeight = new BigDecimal(i).multiply(new BigDecimal(2.213)).doubleValue();

                Map<String,Object> item = new HashMap<>();
                item.put("goodsNo","32-"+i);
                item.put("goodsName","test31-"+i);
                item.put("num",num);
                item.put("price",price);
                item.put("amount",amount);
                item.put("totalNetWeight",totalNetWeight);
                item.put("totalGrossWeight",totalGrossWeight);

                list.add(item);
            }
            dto.put("poNo","558232-123");
            dto.put("itemList",list);
            Context context = new Context();
            context.putVar("dto", dto);

            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
            transformer.setFullFormulaRecalculationOnOpening(true);
            //添加自定义功能
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> funcs = new HashMap<String, Object>();
            funcs.put("timeUtils", new TimeUtils());
            JexlBuilder jb = new JexlBuilder();
            jb.namespaces(funcs);
            JexlEngine je = jb.create();
            evaluator.setJexlEngine(je);

            AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
            areaBuilder.setTransformer(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            jxlsHelper.setHideTemplateSheet(true);
            jxlsHelper.setUseFastFormulaProcessor(false);
            for (Area xlsArea : xlsAreaList) {
                xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
                xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
                xlsArea.processFormulas();
            }
            Workbook workbook = transformer.getWorkbook();
            workbook.write(os);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
                os.close();
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
}
