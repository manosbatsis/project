package com.topideal.common.system.ibatis;

import java.io.Serializable;
import java.util.List;

/**
 * 分页
 * @author 魏小磊  2015-03-11
 *
 */ 
public class PageModel<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int total;//记录总数
	
	private int pageNo=1;//当前页
	
	private int pageSize=10;//每一页记录数
	
	private int begin ;//开始位置
	
	private int end;   //结束位置

	private List<T> list;//结果集

	public PageModel(){

	}

	public PageModel(int pageNo, int pageSize, int total){
		 this.pageNo=pageNo;
		 this.pageSize=pageSize;
		 this.total=total;

	}


	/*开始位置*/
	public int getBegin() {
		return begin;
	}
	/*结束位置 */
	public int getEnd(){
        return begin+pageSize;
	}

	/**
	 * 获取当前页
	 * @return
     */
	public int getPageNo() {
		return begin/pageSize+1;
	}


	public void setBegin(int begin) {
		this.begin=begin;
	}

	
	/**get() set()方法*/
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
