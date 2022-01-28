package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.NoticeDao;
import com.topideal.entity.dto.main.NoticeDTO;
import com.topideal.entity.vo.main.NoticeModel;
import com.topideal.entity.vo.main.NoticeUserRelModel;
import com.topideal.mapper.main.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class NoticeDaoImpl implements NoticeDao {

    @Autowired
    private NoticeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<NoticeModel> list(NoticeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(NoticeModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(NoticeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public NoticeModel  searchByPage(NoticeModel  model) throws SQLException{
        PageDataModel<NoticeModel> pageModel=mapper.listByPage(model);
        return (NoticeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public NoticeModel  searchById(Long id)throws SQLException {
        NoticeModel  model=new NoticeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public NoticeModel searchByModel(NoticeModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public NoticeDTO getListByPage(NoticeDTO dto) throws SQLException{
		PageDataModel<NoticeDTO> pageModel=mapper.getListByPage(dto);
        return (NoticeDTO ) pageModel.getModel();
	}
	@Override
	public NoticeDTO searchDTOById(Long id) throws SQLException{
		return mapper.searchDTOById(id);
	}
	@Override
	public List<NoticeDTO> getNoticeByUser(Map<String,Object> params) throws SQLException{
		return mapper.getNoticeByUser(params);
	}
	@Override
	public Integer getUnReadAmountByUser(Long userId) {
		return mapper.getUnReadAmountByUser(userId);
	}
	@Override
	public Integer getNoticeAccountByUser(Long userId) {
		return mapper.getNoticeAccountByUser(userId);
	}
	@Override
	public NoticeModel getBeforeNotice(Long noticeId) throws SQLException{
		return mapper.getBeforeNotice(noticeId);
	}
	@Override
	public NoticeModel getAfterNotice(Long noticeId) throws SQLException{
		return mapper.getAfterNotice(noticeId);
	}
}