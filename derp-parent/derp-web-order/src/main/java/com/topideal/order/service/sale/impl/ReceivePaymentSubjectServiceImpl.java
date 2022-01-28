package com.topideal.order.service.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.ReceivePaymentSubjectDao;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.order.exception.NCException;
import com.topideal.order.service.sale.ReceivePaymentSubjectService;

/**
 * NC收支费项service实现类
 */
@Service
public class ReceivePaymentSubjectServiceImpl implements ReceivePaymentSubjectService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(ReceivePaymentSubjectServiceImpl.class);

	@Autowired
	private ReceivePaymentSubjectDao receivePaymentSubjectDao;


	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ReceivePaymentSubjectModel listReceivePaymentSubjectByPage(ReceivePaymentSubjectModel model)
			throws SQLException {
		return receivePaymentSubjectDao.searchByPage(model);
	}

	/**
	 * 新增
	 * @param name
	 * @param code
	 * @param subCode
	 * @param subName
	 * @return
	 * @throws Exception
	 */
	@Override
	public String saveReceivePaymentSubject(String name,String code, String subCode, String subName,User user) throws Exception {
		ReceivePaymentSubjectModel receivePaymentSubjectModel1 = receivePaymentSubjectDao.searchByName(name.trim());
		if (receivePaymentSubjectModel1 != null){
			throw new NCException("该NC收支费项已存在");
		}
		ReceivePaymentSubjectModel receivePaymentSubjectModel2 = receivePaymentSubjectDao.searchByCode(code.trim());
		if (receivePaymentSubjectModel2 != null){
			throw new NCException("该NC收支费项编码已存在");
		}
		if(StringUtils.isBlank(name)){
			throw new RuntimeException("NC收支费项不能为空");
		}
		if(StringUtils.isBlank(code)){
			throw new RuntimeException("收支费项编码不能为空");
		}
		if(StringUtils.isBlank(subCode)){
			throw new RuntimeException("科目编码不能为空");
		}
		if(StringUtils.isBlank(subName)){
			throw new RuntimeException("科目名称不能为空");
		}

		ReceivePaymentSubjectModel model = new ReceivePaymentSubjectModel();
		model.setCode(code);
		model.setName(name);
		model.setSubCode(subCode);
		model.setSubName(subName);
		model.setStatus(DERP_ORDER.RECEIVE_PAYMENT_SUBJECT_TYPE_1);
		model.setCreater(user.getId());
		model.setCreaterName(user.getName());
		model.setCreateDate(TimeUtils.getNow());
		receivePaymentSubjectDao.save(model);

		return "新增成功";
	}

		@Override
		public boolean enableNcPay(Long id, String type) throws SQLException {
			//获取用户信息
			ReceivePaymentSubjectModel userInfo = new ReceivePaymentSubjectModel();
			userInfo.setId(id);
			userInfo.setStatus(type);
			int num = receivePaymentSubjectDao.modify(userInfo);
			if (num > 0) {
				return true;
			}
			return false;
		}

    @Override
    public ReceivePaymentSubjectModel getById(Long id) throws SQLException {
		return receivePaymentSubjectDao.searchById(id);
    }

    @Override
    public String modifyReceivePaymentSubject(String id, String name, String code, String subCode, String subName)  throws SQLException {
		ReceivePaymentSubjectModel receivePaymentSubjectModel1 = receivePaymentSubjectDao.searchByName(name.trim());
		ReceivePaymentSubjectModel receivePaymentSubjectModel2 = receivePaymentSubjectDao.searchByCode(code.trim());
		if (receivePaymentSubjectModel1 !=null){
            if (!id.equals(receivePaymentSubjectModel1.getId()+"")){
                throw new NCException("该NC收支费项已存在");
            }
        }
        if(receivePaymentSubjectModel2 != null){
            if (!id.equals(receivePaymentSubjectModel2.getId()+"")){
                throw new NCException("该NC收支费项编码已存在");
            }
        }
		if(StringUtils.isBlank(name)){
			throw new RuntimeException("NC收支费项不能为空");
		}
		if(StringUtils.isBlank(code)){
			throw new RuntimeException("收支费项编码不能为空");
		}
		if(StringUtils.isBlank(subCode)){
			throw new RuntimeException("科目编码不能为空");
		}
		if(StringUtils.isBlank(subName)){
			throw new RuntimeException("科目名称不能为空");
		}

		ReceivePaymentSubjectModel model = new ReceivePaymentSubjectModel();
		model.setId(Long.valueOf(id));
		model.setCode(code);
		model.setName(name);
		model.setSubCode(subCode);
		model.setSubName(subName);
		model.setModifyDate(TimeUtils.getNow());
		receivePaymentSubjectDao.modify(model);

		return "修改成功";
    }

    /**
     * NC收支费项及科目关系
     */
	@Override
	public List<ReceivePaymentSubjectModel> getReceivePaymentSubjectList() throws SQLException {
		ReceivePaymentSubjectModel model=new  ReceivePaymentSubjectModel();
		model.setStatus("1");
		return receivePaymentSubjectDao.list(model);
	}

}
