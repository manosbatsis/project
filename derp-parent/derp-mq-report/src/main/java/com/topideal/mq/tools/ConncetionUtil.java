package com.topideal.mq.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mongodb.MongoClient;

/**
 * jdbc
 * @author zhanghx
 */
public class ConncetionUtil {

	/**
	 * 获取jdbc连接
	 * @param databaseName 数据库名称
	 * @return
	 */
	public static Connection getJDBCConnection(String url, String username, String password, String databaseName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url + "/" + databaseName + "?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true", username, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static MongoClient getMongoClient() {
		try {
			return new MongoClient("10.10.102.179", 27017);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closed(Connection conn, Statement stmt, ResultSet rs, MongoClient mongoClient) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			if (mongoClient != null)
				mongoClient.close();
			conn.close();
		} catch (Exception e2) {
		}
	}

}
