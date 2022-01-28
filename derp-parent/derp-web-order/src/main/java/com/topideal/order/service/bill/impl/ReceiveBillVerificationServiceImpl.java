package com.topideal.order.service.bill.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topideal.mongo.dao.UserBuRelMongoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.bill.ReceiveBillVerificationDao;
import com.topideal.dao.bill.ReceivePaymentNotesDao;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceivePaymentNotesModel;
import com.topideal.order.service.bill.ReceiveBillVerificationService;

/**
 * @Description: 应收账单service实现类
 * @Author: 杨创
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillVerificationServiceImpl implements ReceiveBillVerificationService {

    @Autowired
    private ReceiveBillVerificationDao receiveBillVerificationDao;  
    @Autowired
    private ReceivePaymentNotesDao receivePaymentNotesDao;// 回款备注
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;


    
    /**
     * 分页查询
     */
    @Override
    public ReceiveBillVerificationDTO listReceiveBillVerificationByPage(ReceiveBillVerificationDTO dto, User user) throws SQLException {

		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}

    	ReceiveBillVerificationDTO receiveBillVerificationDTO = receiveBillVerificationDao.listReceiveBillVerificationByPage(dto);
        return receiveBillVerificationDTO;
    }


    /**
     * 查询回款备注
     */
	@Override
	public List<ReceivePaymentNotesModel> getNotesList(ReceivePaymentNotesModel model) throws SQLException {		
		return receivePaymentNotesDao.list(model);
	}


	/**
	 * 保存备注
	 */
	@Override
	public boolean saveNotes(User user, String receiveCode, String notes) throws SQLException {
		ReceivePaymentNotesModel model=new ReceivePaymentNotesModel();
		model.setReceiveCode(receiveCode);
		model.setNotesId(user.getId());
		model.setNotesName(user.getName());
		model.setNotes(notes);
		Long id = receivePaymentNotesDao.save(model);
		if (id!=null) {
			return true;
		}
		
		return false;
	}


	/**
	 * 导出
	 */
	@Override
	public List<Map<String, Object>> exportBillVerification(ReceiveBillVerificationDTO dto, User user) throws SQLException {

		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				return new ArrayList<>();
			}
			dto.setBuList(buList);
		}

		return receiveBillVerificationDao.exportBillVerification(dto);
	}

 
}
