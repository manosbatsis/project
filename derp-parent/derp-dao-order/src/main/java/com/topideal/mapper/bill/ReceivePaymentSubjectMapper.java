package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface ReceivePaymentSubjectMapper extends BaseMapper<ReceivePaymentSubjectModel> {


    PageDataModel<ReceivePaymentSubjectModel> queryByPage(ReceivePaymentSubjectModel model);

    ReceivePaymentSubjectModel searchByName(String name);

    ReceivePaymentSubjectModel searchByCode(String code);
}
