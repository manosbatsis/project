package com.topideal.mongo.entity;

import java.sql.Timestamp;
/**
 * 仓库附表
 * @author lian_
 *
 */
public class DepotScheduleMongo {
	 /**
	    * 
	    */
	    private Long depotScheduleId;
	    /**
	    * 仓库ID
	    */
	    private Long depotId;
	    /**
	    * 仓库名称
	    */
	    private String depotName;
	    /**
	    * 详细地址
	    */
	    private String address;
	   
	    
	    
	
		public Long getDepotId() {
			return depotId;
		}
		public void setDepotId(Long depotId) {
			this.depotId = depotId;
		}
		public String getDepotName() {
			return depotName;
		}
		public void setDepotName(String depotName) {
			this.depotName = depotName;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}

		public Long getDepotScheduleId() {
			return depotScheduleId;
		}
		public void setDepotScheduleId(Long depotScheduleId) {
			this.depotScheduleId = depotScheduleId;
		}
	    
	    
}
