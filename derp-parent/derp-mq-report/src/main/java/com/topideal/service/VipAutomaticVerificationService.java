package com.topideal.service;

import java.sql.SQLException;

public interface VipAutomaticVerificationService {

	void saveSummaryReport(String json, String keys, String topics, String tags) throws SQLException;

}
