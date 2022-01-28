package com.topideal.common.tools.excel;

import java.util.List;

public class ExportExcelSheet {

	private String sheetNames ;
	
	private String[] keys ;
	
	private String[] colums ;
	
	private List resultList ;

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String[] getColums() {
		return colums;
	}

	public void setColums(String[] colums) {
		this.colums = colums;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public String getSheetNames() {
		return sheetNames;
	}

	public void setSheetNames(String sheetNames) {
		this.sheetNames = sheetNames;
	}
	
	
}
