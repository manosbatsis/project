package com.topideal.webService.oa.o_01;

import java.math.BigDecimal;
import java.util.List;

/**
 * OA流程创建接口表头
 * 2021-05-11
 * 杨创
 */
public class CreatRequestIdRequest {
	private String wbxtdjh;//外部系统单据号
	private String sqbm;//申请部门
	private String sqr;//申请人
	private String sqsj;//申请时间
	private String cwzz;// 财务组织
	private String gysxz;//供应商性质 0：境内供应商 1：境外供应商
	private String gys; // 供应商 主数据客商编码
	private String syb; //事业部编码
	private String skyhzh; // 收款银行账户
	private String skyhkhh;// 收款银行开户行
	private String skyhzh1;//收款银行账号
	private String swiftcode;// Swift Code
	private String hth;// 合同号
	private String jsbz;// 结算币种
	private String ybjehz;// 原币金额汇总
	private String ybjedx;// 原币金额大写
	private String yjfksj;// 预计付款时间
	private String qkyy;// 请款原因
	private String fj;// 附件
	private String title;// 推送OA标题
	private String sfdz;// 是否垫资  0 是  1 否
	private String lyxt;//来源系统 #系统来源 KJT-跨境测试环境 KJU--跨境UAT  NMT--内贸测试环境
	
	private List<CtreatRequestIdItemRequest> itemList;

	public String getWbxtdjh() {
		return wbxtdjh;
	}

	public void setWbxtdjh(String wbxtdjh) {
		this.wbxtdjh = wbxtdjh;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getCwzz() {
		return cwzz;
	}

	public void setCwzz(String cwzz) {
		this.cwzz = cwzz;
	}

	public String getGysxz() {
		return gysxz;
	}

	public void setGysxz(String gysxz) {
		this.gysxz = gysxz;
	}

	public String getGys() {
		return gys;
	}

	public void setGys(String gys) {
		this.gys = gys;
	}

	public String getSyb() {
		return syb;
	}

	public void setSyb(String syb) {
		this.syb = syb;
	}

	public String getSkyhzh() {
		return skyhzh;
	}

	public void setSkyhzh(String skyhzh) {
		this.skyhzh = skyhzh;
	}

	public String getSkyhkhh() {
		return skyhkhh;
	}

	public void setSkyhkhh(String skyhkhh) {
		this.skyhkhh = skyhkhh;
	}

	public String getSwiftcode() {
		return swiftcode;
	}

	public void setSwiftcode(String swiftcode) {
		this.swiftcode = swiftcode;
	}

	public String getHth() {
		return hth;
	}

	public void setHth(String hth) {
		this.hth = hth;
	}

	public String getJsbz() {
		return jsbz;
	}

	public void setJsbz(String jsbz) {
		this.jsbz = jsbz;
	}

	public String getYbjehz() {
		return ybjehz;
	}

	public void setYbjehz(String ybjehz) {
		this.ybjehz = ybjehz;
	}

	public String getYbjedx() {
		return ybjedx;
	}

	public void setYbjedx(String ybjedx) {
		this.ybjedx = ybjedx;
	}

	public String getYjfksj() {
		return yjfksj;
	}

	public void setYjfksj(String yjfksj) {
		this.yjfksj = yjfksj;
	}

	public String getQkyy() {
		return qkyy;
	}

	public void setQkyy(String qkyy) {
		this.qkyy = qkyy;
	}

	public String getFj() {
		return fj;
	}

	public void setFj(String fj) {
		this.fj = fj;
	}

	public List<CtreatRequestIdItemRequest> getItemList() {
		return itemList;
	}

	public void setItemList(List<CtreatRequestIdItemRequest> itemList) {
		this.itemList = itemList;
	}

	public String getSkyhzh1() {
		return skyhzh1;
	}

	public void setSkyhzh1(String skyhzh1) {
		this.skyhzh1 = skyhzh1;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSfdz() {
		return sfdz;
	}

	public void setSfdz(String sfdz) {
		this.sfdz = sfdz;
	}

	public String getLyxt() {
		return lyxt;
	}

	public void setLyxt(String lyxt) {
		this.lyxt = lyxt;
	}



	
	
}
