package com.topideal.tools;

public class OrderSourceUtils {
	public static String getSource(String orderCode){
		String tag = null;//1 来自采购  2:来自调拨   3:来自销售 4.来自销售推退
		if (orderCode.contains("CGO")) {//1 来自采购 
			tag="1";
		}else if (orderCode.contains("DBO")) {//2:来自调拨
			tag="2";
		}else if (orderCode.contains("XSO")) {//3:来自销售 
			tag="3";
		}else if (orderCode.contains("XSTO")) {//4.来自销售退货
			tag="4";
		}    
		
		return tag;
	}
}
