package com.ceresdata.insert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * JNDIl����ݿ⣬��ȡ��ݿ��t��
 * 
 * @author ytwps
 * 
 */
public class DBManager {

	/**
	 * MySql
	 */
	static final String driver = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost/white_jotter?characterEncoding=utf-8";
	static final String uName = "root";
	static final String uPwd = "root";

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = getJNDIConnection();
		if (conn == null) {
			conn = getJDBCConnection();
		} 
		return conn;
	}

	/**
	 * 获取数据库连接,JDBC的方式
	 * 
	 * @return
	 */
	public static Connection getJDBCConnection() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, uName, uPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 获取数据库连接,JNDI的方式
	 * 
	 * @return
	 */
	public static Connection getJNDIConnection() {
		Connection conn = null;
		try {
			String dsname = "java:comp/env/jdbc/dbservice";
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(dsname);
			conn = ds.getConnection();
		} catch (SQLException e) {
			System.out.println("DBManager SQLException");
			e.printStackTrace();
		} catch (NamingException e) {
			System.out.println("DBManager NamingException");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param con
	 *            连接对象
	 */
	public static void closeConection(Connection con) {
		try {
			if (con != null) {
				if (!con.isClosed()) {
					con.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
		}
	}

	/**
	 * 关闭Statement 对象
	 * 
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			stmt = null;
		}
	}

	/**
	 * 关闭结果集
	 * 
	 * @param rs
	 *            结果集对象
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs = null;
		}
	}
}
