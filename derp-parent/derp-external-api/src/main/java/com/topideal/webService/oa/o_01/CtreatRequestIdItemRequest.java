package com.topideal.webService.oa.o_01;

import java.math.BigDecimal;

/**
 * OA流程创建接口表体
 * 2021-05-11
 * 杨创
 */
public class CtreatRequestIdItemRequest {
	private String zy;//摘要
	private String poh;//PO号
	private String szfx;//收支费项
	private String jebhs;//浮点数-2位
	private String se;//税额
	private String jehs;//金额（含税）
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getPoh() {
		return poh;
	}
	public void setPoh(String poh) {
		this.poh = poh;
	}
	public String getSzfx() {
		return szfx;
	}
	public void setSzfx(String szfx) {
		this.szfx = szfx;
	}
	public String getJebhs() {
		return jebhs;
	}
	public void setJebhs(String jebhs) {
		this.jebhs = jebhs;
	}
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	public String getJehs() {
		return jehs;
	}
	public void setJehs(String jehs) {
		this.jehs = jehs;
	}
	
	
	
	
}
