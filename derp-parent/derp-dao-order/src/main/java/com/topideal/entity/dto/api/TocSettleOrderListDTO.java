package com.topideal.entity.dto.api;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/2 18:56
 * @Description:
 */
public class TocSettleOrderListDTO implements Serializable {

    //当前页
    private Integer pageNo;

    //每页显示的记录数
    private Integer pageSize;

    //总页数
    private Integer total;

    //开始位置
    private Integer begin;

    //结束位置
    private Integer end;

    private List<TocSettleBillDTO> list;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public List<TocSettleBillDTO> getList() {
        return list;
    }

    public void setList(List<TocSettleBillDTO> list) {
        this.list = list;
    }
}
