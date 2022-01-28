package com.topideal.common.system.ibatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页
 */
public class PageDataModel<T> extends ArrayList implements Serializable{
    //数据集
    private PageModel<T > model;

    public PageDataModel(int offset,int limit,int total,List<T> rows,Object o){
        super();
        model=(PageModel)o;
        model.setPageNo(offset/limit+1);
        model.setTotal(total);
        model.setPageSize(limit);
        model.setList(rows);
    }

    public PageModel<T> getModel() {
        return model;
    }

    public void setModel(PageModel<T> model) {
        this.model = model;
    }
}
