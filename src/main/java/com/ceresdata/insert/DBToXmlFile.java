package com.ceresdata.insert;

import com.ceresdata.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;

/**
 * 
 * @author xielijun
 *
 *  数据库表格解析
 *
 * */
public class DBToXmlFile {
	private static final Logger logger = LoggerFactory.getLogger(DBToXmlFile.class);
	private static final int buffLength = 1024;
	protected Connection con = null;
	protected PreparedStatement pre = null;

	// 分别存放数据库的二进制形式和数字类型
	private String[] column_types_Binary = new String[] { "CLOB", "BLOB",
			"DBCLOB", "MEDIUMBLOB", "TINYBLOB", "LONGBLOB","NCLOB","BFILE","LONG","LONG RAW"};
	private String[] column_types_Numer = new String[] { "BIGINT", "DECIMAL",
			"INTEGER", "SMALLINT", "TINYINT", "MEDIUMINT", "FLOAT", "DOUBLE",
			"BINARY_FLOAT", "BINARY_DOUBLE", "NUMBER", "INT",
			"INTEGER UNSIGNED", "DOUBLE UNSIGNED" };
	private String[] column_types_Date = new String[] { "DATE", "DATETIME","TIME","TIMESTAMP" };
	
	protected String sql ;
	
	public DBToXmlFile(){}
	/**
	 * 
	 * @param con 数据库连接
	 * @param sql 执行脚本
	 */
	public DBToXmlFile(Connection con , String sql){
		this.con = con;
		this.sql = sql;
	}
	

	/**
	 * 验证type 是否为数字类型的列
	 * 
	 * @param type  列的类型名
	 *           
	 * @return true 代表是数字类型列，false代表不是数字类型的
	 */
	public boolean colunmTypeIsNumber(String type) {
		boolean val = false;
		for (int i = 0; i < column_types_Numer.length; i++) {
			if (column_types_Numer[i].equalsIgnoreCase(type)) {
				val = true;
				break;
			}
		}
		return val;
	}

	/**
	 * 验证type 是否为二进制类型的列
	 * 
	 * @param type  列的类型名
	 *           
	 * @return true 代表是二进制类型列，false代表不是二进制类型的列
	 * 
	 */
	public boolean colunmTypeIsBinary(String type) {
		boolean val = false;
		for (int i = 0; i < column_types_Binary.length; i++) {
			if (column_types_Binary[i].equalsIgnoreCase(type)) {
				val = true;
				break;
			}
		}
		return val;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	/**
	 * 执行SQL 查询 并将提取的数据写入到指定的目录中
	 * @param sql sql语句
	 * @param outdir 输出文件目录
	 * @return
	 */
	public String excuteSQLToFile(String sql,File outdir) {
		// 输出文件名称
		String fname = System.nanoTime()+".xml";
		// 输出文件
		File outfile = new File(outdir,fname);
		// 获取执行SQL的表名称
		String tablename = this.getMainTableName(sql);
		logger.info("执行SQL查询 :" + sql);
		ResultSet rs = null;
		try {
			pre = con.prepareStatement(sql);
			rs = pre.executeQuery();
			// 根据结果集，输出到文件中
            this.writeFileFiled(rs,tablename, outfile);
		} catch (SQLException ex) {
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBManager.closeResultSet(rs);
			DBManager.closeStatement(pre);
			DBManager.closeConection(con);
		}
		return outfile.getPath();
	}
	
	public String writeFileFiled(ResultSet rs,String tableName,File outFile) {
		int j = 0;
		StringBuffer sb = new StringBuffer();
		try {
			// 写文件的留
			OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8");
			// 工具类负责写入文件
			try {
				// build head xml
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				sb.append("<DataInfo>\n");
				sb.append("   <TableName>" + tableName + "</TableName>\n");
				sb.append("   <Records>\n");
				// 写入一部分
				fout.write(sb.toString());
				sb.delete(0, sb.length());
				ResultSetMetaData  rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				// 循环每一列写入xml文件
				while (rs.next()) {
					j++;// 记录需要推送记录的条数
					sb.append("      <Record>\n");
					// 取出每一列的值
					for (int i = 1; i <= columnCount; i++) {
						String name = rsmd.getColumnLabel(i);
						String type = rsmd.getColumnTypeName(i);
						int length = rsmd.getColumnDisplaySize(i);
						String value = "";
						// 代表列的类型是二进制类型的
						if (colunmTypeIsBinary(type)) {
							InputStream is = rs.getBinaryStream(name);
							if (is != null) {
								//  这个地方处理lob 类型
							}
						}else if (isDate(type)) {
							//日期类型的话要格式话日期
							Timestamp timeStamp = rs.getTimestamp(name);
							if (timeStamp == null) {
								value = "NULL";
							} else {
								value = timeStamp.toString();
							}
						} else {
							value = rs.getString(name);
						}
						// 消息内容和定义列的长度相同时，扩展列的长度
						if(value != null && value.length() == length){
							length = length*2;
						}
						sb.append("          <Field name=\"" + name
								+ "\" type=\"" + type + "\" length =\""+length+"\">" + "<![CDATA["
								+ StringUtil.replaceXmlChar(value) + "]]>" + "</Field>\n");
					
					}
					sb.append("      </Record>\n");
					// 每50条记录合并一次
					if (j % 100 == 0) {
						fout.write(sb.toString());
						sb.delete(0, sb.length());
					}
				}
				sb.append("   </Records>\n");
				sb.append("</DataInfo>");
				// xml格式的末尾内容，写入文件
				fout.write(sb.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				j = 0;
			}
			fout.flush();
			fout.close();
			// 如果没有更新的记录，则删除写入的文件。
			if (j == 0) {
				outFile.delete();
				logger.info("数据表中没有更新的记录！删除生成的文件！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String fileOutURL = outFile.getAbsolutePath();
		return fileOutURL;
	}


	/**
	 * 获取主表的
	 * @return
	 */
	public String getMainTableName(String sql){
		String tableName = "";
		int index = sql.toUpperCase().lastIndexOf(" FROM ");
		int endIndex = sql.indexOf(" ", index+6);
		//
		if(endIndex != -1){
			tableName = sql.substring(index+6,endIndex);
		}else{
			tableName = sql.substring(index+6,sql.length());
		}
		return tableName;
	}

	
	/**
	 * 
	 * @param colType
	 *            数据列的类型
	 * @return 如果数据列是日期类型或是timeStamp类型 则返回true
	 */
	public boolean isDate(String colType) {
		for (int i = 0; i < column_types_Date.length; i++) {
			if (column_types_Date[i].equalsIgnoreCase(colType)) {
				return true;
			}
		}
		return false;
	}


	public static void main(String[] args) {
		String sql = "select * from data_dictionary";
		File outdir = new File("d:/tmp");
		DBToXmlFile helper = new DBToXmlFile();
		helper.setConnection(DBManager.getJDBCConnection());
		helper.excuteSQLToFile(sql,outdir);
	}
}
