package com.topideal.enums;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Excel 模板
 * Created by weixiaolei on 2018/5/18.
 */
public enum ExcelTemplateEnum {

    M_001(101,"/template/excel/101.xls","仓库导入模板.xls"),
	M_002(102,"/template/excel/102.xlsx","供应商导入模板.xlsx"),
	M_003(103,"/template/excel/103.xlsx","采购价格导入模版.xlsx"),
	M_004(104,"/template/excel/104.xlsx","客户导入模板.xlsx"),
	M_005(105,"/template/excel/105.xlsx","库存期初导入模板.xlsx"),
	M_006(106,"/template/excel/106.xlsx","供应商询价池模板.xlsx"),
	M_007(107,"/template/excel/107.xlsx","采购订单导入模板.xlsx"),
	M_008(108,"/template/excel/108.xlsx","关联采购单导入模板.xlsx"),
	M_009(109,"/template/excel/109.xlsx","入库单导入模板.xlsx"),
	M_010(110,"/template/excel/110.xlsx","代销退货订单导入模板.xlsx"),
	M_011(111,"/template/excel/111.xlsx","调拨单导入模板.xlsx"),
	M_012(112,"/template/excel/112.xlsx","库存调整导入模板.xlsx"),
	M_013(113,"/template/excel/113.xlsx","商品导入模板.xlsx"),
	M_014(114,"/template/excel/114.xlsx","销售订单导入模板.xlsx"),
	M_015(115,"/template/excel/115.xlsx","mq重推导入模板.xlsx"),
	M_016(116,"/template/excel/116.xlsx","销售价格导入模板.xlsx"),
	M_017(117,"/template/excel/117.xlsx","结算价格导入模板.xlsx"),
	M_018(118,"/template/excel/118.xlsx","唯品PO单与出库仓库关系导入模板.xlsx"),
	M_019(119,"/template/excel/119.xlsx","汇率导入模板.xlsx"),
	M_020(120,"/template/excel/120.xlsx","消费者退货订单导入模板.xlsx"),
	M_022(122,"/template/excel/122.xlsx","宝洁商品货期导入模板.xlsx"),
	M_023(123,"/template/excel/123.xlsx","标准品牌管理导入模板.xlsx"),
	M_024(124,"/template/excel/124.xlsx","组合品对照表导入模板.xlsx"),
	M_025(125,"/template/excel/125.xlsx","电商订单导入模板.xlsx"),
	M_026(126,"/template/excel/126.xlsx","电商退货导入模板.xlsx"),
	M_027(127,"/template/excel/127.xlsx","销售出库单导入模板.xlsx"),
	M_028(128,"/template/excel/128.xlsx","盘盈盘亏导入模板.xlsx"),
	M_030(130,"/template/excel/130.xlsx","卓志保税仓导入模板.xlsx"),
	M_031(131,"/template/excel/131.xlsx","菜鸟仓导入模板.xlsx"),
	M_032(132,"/template/excel/132.xlsx","账单出库导入模板.xlsx"),
	M_033(133,"/template/excel/133.xlsx","采购退货出库导入模板.xlsx"),
	M_034(134,"/template/excel/134.xlsx","事业部库存期初导入模板.xlsx"),
	M_035(135,"/template/excel/135.xlsx","仓储类型调整导入模板.xlsx"),
    M_036(136,"/template/excel/136.xlsx","事业部移库单导入模板.xlsx"),
	M_037(137,"/template/excel/137.xlsx","销售目标导入模板.xlsx"),
    M_038(138,"/template/excel/138.xlsx","预售单导入模板.xlsx"),
    M_039(139,"/template/excel/139.xlsx","协议单价导入模板.xlsx"),
    M_040(140,"/template/excel/140.xlsx","库位调整单导入模板.xlsx"),
	M_041(141,"/template/excel/141.xlsx","库位映射导入模板.xlsx"),
	M_042(142,"/template/excel/142.xlsx","上架导入模板.xlsx"),
	M_043(143,"/template/excel/143.xlsx","商品导入模板.xlsx"),
	M_044(144,"/template/excel/144.xlsx","电商订单金额自校验模板.xlsx"),
    M_045(145,"/template/excel/145.xlsx","月度销售额目标导入模板.xlsx"),
    M_046(146,"/template/excel/146.xlsx","商品导入模板.xlsx"),
    M_047(147,"/template/excel/147.xlsx","箱规导入模板.xlsx"),
	M_048(148,"/template/excel/148.xlsx","采购SD配置多比例导入模板.xlsx"),
	M_049(149,"/template/excel/149.xlsx","平台费用映射导入.xlsx"),
	M_050(150,"/template/excel/150.xlsx","SD金额调整单导入模板.xlsx"),
	M_051(151,"/template/excel/151.xlsx","平台结算单导入（通用模板）.xlsx"),
    M_052(152,"/template/excel/152.xlsx","关区配置信息导入（通用模板）.xlsx"),
	M_053(153,"/template/excel/153.xlsx","销售退出库单导入模板.xlsx"),
	M_054(154,"/template/excel/154.xlsx","销售退入库单导入模板.xlsx"),
	M_055(155,"/template/excel/155.xlsx","调拨入库单导入模板.xlsx"),
	M_056(156,"/template/excel/156.xlsx","调拨出库单导入模板.xlsx"),
	M_057(157,"/template/excel/157.xlsx","箱装明细导入模板.xlsx"),
	M_058(158,"/template/excel/158.xlsx","平台品类配置导入模板.xlsx"),
	M_059(159,"/template/excel/159.xlsx","平台商品品类导入模板.xlsx"),
	M_060(160,"/template/excel/160.zip","图片上传.zip"),
	M_061(161,"/template/excel/161.xlsx","领料单导入模版.xlsx"),
	M_062(162,"/template/excel/162.xlsx","外仓备案商品导入模版.xlsx"),
	M_063(163,"/template/excel/163.xlsx","销售SD配置多比例导入模板.xlsx"),
	M_064(164,"/template/excel/164.xlsx","销售SD调整单导入模板.xlsx"),
	M_065(165,"/template/excel/165.xlsx","领料出库单导入模板.xlsx"),
	M_066(166,"/template/excel/166.xlsx","电商退款单导入模板.xlsx"),
	M_067(167,"/template/excel/167.xlsx","商品关联仓库导入.xlsx"),
	M_068(168,"/template/excel/168.xlsx","采购商品导入模板.xlsx"),
	M_069(169,"/template/excel/169.xlsx","采购装箱明细导入模板.xlsx"),
	M_070(170,"/template/excel/170.xlsx","库位类型调整导入模板.xlsx"),
	M_071(171,"/template/excel/171.xlsx","固定成本价盘导入模板.xlsx");


    //编码
    private int code;
    //路径
    private String path;
    //描述
    private String desc;

    private ExcelTemplateEnum(Integer code,String path,String desc){
        this.code=code;
        this.path=path;
        this.desc=desc;
    }


    public static String getPath(int code){
        for (ExcelTemplateEnum model : ExcelTemplateEnum.values()) {
               if(model.code==code){
                   return model.path;
               }
        }
        return null;
    }

    public static String getDesc(int code){
        for (ExcelTemplateEnum model : ExcelTemplateEnum.values()) {
               if(model.code==code){
                   return model.desc;
               }
        }
        return null;
    }

    public static InputStream getFile(String path)throws IOException{
        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource("classpath:"+path);
        return resource.getInputStream();
    }

    public static void main(String[] args){
        System.out.println(ExcelTemplateEnum.getPath(001));
    }



}
