package com.topideal.dao.purchase.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.dao.purchase.PurchasingReportsDao;
import com.topideal.entity.dto.purchase.PurchasingReportsDTO;


@Repository
public class PurchasingReportsDaoImpl implements PurchasingReportsDao {
	/**
	 *  列表分页查询
	 */
	@Override
	public PurchasingReportsDTO getListByPage(PurchasingReportsDTO dto) throws Exception {
		Class.forName(ApolloUtilDB.jdbforName);
		String url=ApolloUtilDB.scmUrl;
		String username=ApolloUtilDB.scmUserName;
		String pwd = ApolloUtilDB.scmPassword;

		Connection conn = DriverManager.getConnection(url,username,pwd);
		if(conn == null) {
			throw new RuntimeException("连接采购计划报表失败");
		}
		// 关闭事务自动提交
		conn.setAutoCommit(false);

		try {			
			Integer countNum = getTotalNum(dto, conn);// 总行数

			List<PurchasingReportsDTO> dtoList = getList(dto, conn, true);

			dto.setTotal(countNum);
			dto.setPageNo(dto.getPageNo());
			dto.setPageSize(dto.getPageSize());
			dto.setList(dtoList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			conn.close();

		}
		return dto;

	}

	/**
	 * 导出
	 */
	@Override
	public List<PurchasingReportsDTO> exportList(PurchasingReportsDTO dto) throws Exception {
		Class.forName(ApolloUtilDB.jdbforName);
		String url=ApolloUtilDB.scmUrl;
		String username=ApolloUtilDB.scmUserName;
		String pwd = ApolloUtilDB.scmPassword;
		Connection conn = DriverManager.getConnection(url,username,pwd);
		if(conn == null) {
			throw new RuntimeException("连接采购计划报表失败");
		}
		// 关闭事务自动提交
		conn.setAutoCommit(false);

		List<PurchasingReportsDTO> dtoList = getList(dto, conn, false);

		return dtoList;
	}
	/**
	 * 获取总行数
	 */
	@Override
	public Integer getTotalNum(PurchasingReportsDTO dto, Connection conn) throws Exception{			
		if(conn == null) {
			Class.forName(ApolloUtilDB.jdbforName);
			String url=ApolloUtilDB.scmUrl;
			String username=ApolloUtilDB.scmUserName;
			String pwd = ApolloUtilDB.scmPassword;
			conn = DriverManager.getConnection(url,username,pwd);
			if(conn == null) {
				throw new RuntimeException("连接采购计划报表失败");
			}
		}		
		Statement countPst = null;
		ResultSet countRs = null;
		Integer countNum = 0;// 总行数
		try {
			// 统计总行数
			String countSql = "select count(*) as countNum from purchasing_reports_90days_sku where 1=1 ";
			if (dto.getRunDate() != null) {
				countSql += " and run_date = '" + dto.getRunDate() + "'";
			}
			if (StringUtils.isNotBlank(dto.getWarehouse())) {
				countSql += " and warehouse = '" + dto.getWarehouse() + "'";
			}
			if (StringUtils.isNotBlank(dto.getPlatform())) {
				countSql += " and platform = '" + dto.getPlatform() + "'";
			}
			if (StringUtils.isNotBlank(dto.getWareId())) {
				countSql += " and ware_id = '" + dto.getWareId() + "'";
			}
			if (dto.getMerchantId() != null) {
				countSql += " and merchant_id = '" + dto.getMerchantId() + "'";
			}
			countPst = conn.createStatement();
			countRs = countPst.executeQuery(countSql);
			
			if (countRs.next())
				countNum = countRs.getInt("countNum");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (countRs != null) {
				countRs.close();
				countRs = null;
			}
			if (countPst != null) {
				countPst.close();
				countPst = null;
			}

		}
		return countNum;
	}
	
	/**
	 * 获取列表
	 * @param dto
	 * @param conn
	 * @param isByPage
	 * @return
	 * @throws Exception
	 */
	private List<PurchasingReportsDTO> getList(PurchasingReportsDTO dto, Connection conn, boolean isByPage)
			throws Exception {
		Statement pst = null;
		ResultSet rs = null;
		List<PurchasingReportsDTO> dtoList = new ArrayList<PurchasingReportsDTO>();
		try {
			String sql = "select id,ware_id,upc,name,merchant_id,merchant_name,customer_id,customer_name,platform,warehouse,business_date,transit_stock,stock,avg_90day_num,stock_sell_days,purchase_num,supply_sell_days,supply_warning,carton_size,run_date from purchasing_reports_90days_sku where 1=1 ";
			if (dto.getRunDate() != null) {
				sql += " and run_date = '" + dto.getRunDate() + "'";
			}
			if (StringUtils.isNotBlank(dto.getWarehouse())) {
				sql += " and warehouse = '" + dto.getWarehouse() + "'";
			}
			if (StringUtils.isNotBlank(dto.getPlatform())) {
				sql += " and platform = '" + dto.getPlatform() + "'";
			}
			if (StringUtils.isNotBlank(dto.getWareId())) {
				sql += " and ware_id = '" + dto.getWareId() + "'";
			}
			if (dto.getMerchantId() != null) {
				sql += " and merchant_id = '" + dto.getMerchantId() + "'";
			}
			sql = sql + " order by run_date desc";
			
			if (isByPage) {
				sql += " limit " + dto.getBegin() + "," + dto.getPageSize() + ";";
			}
			System.out.println("sql=" + sql);
			pst = conn.createStatement();
			rs = pst.executeQuery(sql);
			while (rs.next()) {// -----------start-002
				PurchasingReportsDTO purchasingReportsDTO = new PurchasingReportsDTO();
				purchasingReportsDTO.setId(rs.getLong("id"));
				purchasingReportsDTO.setWareId(rs.getString("ware_id"));
				purchasingReportsDTO.setUpc(rs.getString("upc"));
				purchasingReportsDTO.setName(rs.getString("name"));
				purchasingReportsDTO.setMerchantId(rs.getLong("merchant_id"));
				purchasingReportsDTO.setMerchantName(rs.getString("merchant_name"));
				purchasingReportsDTO.setCustomerId(rs.getLong("customer_id"));
				purchasingReportsDTO.setCustomerName(rs.getString("customer_name"));
				purchasingReportsDTO.setPlatform(rs.getString("platform"));
				purchasingReportsDTO.setWarehouse(rs.getString("warehouse"));
				purchasingReportsDTO.setBusinessDate(rs.getDate("business_date"));
				purchasingReportsDTO.setTransitStock(rs.getBigDecimal("transit_stock"));
				purchasingReportsDTO.setStock(rs.getBigDecimal("stock"));
				purchasingReportsDTO.setAvg90dayNum(rs.getBigDecimal("avg_90day_num"));
				purchasingReportsDTO.setStockSellDays(rs.getBigDecimal("stock_sell_days"));
				purchasingReportsDTO.setPurchaseNum(rs.getBigDecimal("purchase_num"));
				purchasingReportsDTO.setSupplySellDays(rs.getBigDecimal("supply_sell_days"));
				purchasingReportsDTO.setSupplyWarning(rs.getString("supply_warning"));
				purchasingReportsDTO.setCartonSize(rs.getInt("carton_size"));
				purchasingReportsDTO.setRunDate(rs.getDate("run_date"));

				dtoList.add(purchasingReportsDTO);

			}
			rs.close();
			pst.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		}
		return dtoList;
	}

}