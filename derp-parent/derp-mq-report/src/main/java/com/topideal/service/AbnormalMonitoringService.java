package com.topideal.service;

import java.sql.SQLException;

public interface AbnormalMonitoringService {

	void sendMonitoringMail(String json, String keys, String topics, String tags) throws SQLException, Exception;

}
