package com.topideal.webService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.topideal.webService.oa.o_01.CreatRequestIdRequest;
import com.topideal.webService.oa.o_01.CtreatRequestIdItemRequest;

public class main2 {
	public static void main(String[] args) throws Exception {
		
		CreatRequestIdRequest model=new  CreatRequestIdRequest();
		model.setWbxtdjh("YStest000001");
		model.setSqbm("1271");
		model.setSqr("1404");
		model.setSqsj("2021-05-17");
		model.setCwzz("1000005406");
		model.setGysxz("1");
		model.setGys("1000000432");
		model.setSyb("E030000");
		model.setSkyhzh("");
		model.setSkyhkhh("中国银行");
		model.setSkyhzh1("天河");
		model.setSwiftcode("swiftcode");
		model.setHth("20210517");
		model.setJsbz("CNY");
		model.setYbjehz("100.00");
		model.setYbjedx("一百");
		model.setYjfksj("2021-05-17");
		model.setQkyy("测试");
		String fjs="https://www.baidu.com/,https://www.baidu.com/";
        if (!StringUtils.isEmpty(fjs)) {
        	String[] split = fjs.split(",");
        	String html="<p>";
        	html=html+"<a href='https://www.baidu.com/' target='_blank'>附件</a>";
        	html=html+"<a href='https://www.baidu.com/' target='_blank'>附件</a>";
        	html=html+"</p>";
        	model.setFj(html);
		}


		List<CtreatRequestIdItemRequest>itemList=new ArrayList<CtreatRequestIdItemRequest>();
		CtreatRequestIdItemRequest item=new CtreatRequestIdItemRequest();
		item.setZy("摘要");
		item.setPoh("po号");
		item.setSzfx("预付");
		item.setJebhs("202.22");
		item.setSe("20.00");
		item.setJehs("182.22");
		itemList.add(item);
		
		CtreatRequestIdItemRequest item1=new CtreatRequestIdItemRequest();
		item1.setZy("摘要");
		item1.setPoh("po号");
		item1.setSzfx("预付");
		item1.setJebhs("202.22");
		item1.setSe("20.00");
		item1.setJehs("182.22");
		itemList.add(item1);
		model.setItemList(itemList);
		
		String oaRequestId = OAUtils.getOARequestId(model, "003817");
		
		System.out.println(oaRequestId);
		
		

		
		
	}
}
