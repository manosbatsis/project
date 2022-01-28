package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;

import java.sql.SQLException;


public interface ReceivePaymentSubjectDao extends BaseDao<ReceivePaymentSubjectModel>{



    /**
     * 根据条件查询（分页）
     * @return
     */
    ReceivePaymentSubjectModel searchByPage(ReceivePaymentSubjectModel dto)throws SQLException;

    ReceivePaymentSubjectModel searchByName(String name);

    ReceivePaymentSubjectModel searchByCode(String code);
}
