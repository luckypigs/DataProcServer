package com.ceresdata.insert;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过解析XML文档内容
 */
public class XmlHandler extends DefaultHandler {
	private StringBuffer value = new StringBuffer();
	private int index = 0;
	private String tableName = null;
	private Connection m_conn;
	private boolean isCommit = true;
	private int listIndex = 0 ;
	private List<TableFiled> tableFileds = new ArrayList<TableFiled>(10);
	private OracleDbHelper dbHelper = null;

	public boolean isCommit() {
		return isCommit;
	}

	public XmlHandler(Connection connection) {
		this.m_conn = connection;
		dbHelper = new MySqlDbHelper(this.m_conn);
	}

	public void startDocument() throws SAXException {
		try {
			m_conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void endDocument() throws SAXException {
		try {
			//执行SQL，从临时表中导入数据库表中
			dbHelper.executeBatchSql();
		} catch (SQLException e) {
			e.printStackTrace();
			isCommit = false;
		} 
		tableFileds.clear();
		tableFileds = null;
		try {
			if(isCommit){
				m_conn.commit();
			}else{
				m_conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.closeConection(m_conn);
		}
	}

	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		//clear temp value
		value.delete(0, value.length());
		if (qName.equalsIgnoreCase("Field")) {
			if(index == 0){
				TableFiled filed = new TableFiled();
				filed.setFiledName(attributes.getValue("name"));
				filed.setFiledType(attributes.getValue("type"));
				filed.setLength(Integer.parseInt(attributes.getValue("length")));
				tableFileds.add(filed);
			}
		}
	}
	
	public void characters(char[] chars, int start, int length)
			throws SAXException {
		value.append(chars, start, length);
	}

	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("record")) {
			if(index == 0){
				dbHelper.setTableName(this.tableName);
				dbHelper.setFileds(tableFileds);
				//数据库表不存在，创建数据库接收表和临时表
//				dbHelper.createRecvTableAndTempTable();
			}
			try {
				// 临时表中插入数据
				dbHelper.addBatchSql(tableFileds);
			} catch (SQLException e) {
				isCommit = false;
				e.printStackTrace();
			}
			// list reset  zero
			index ++ ;
			listIndex = 0 ;
		} else if (qName.equalsIgnoreCase("Field")) {
			TableFiled filed = null ;
			if(index == 0){
				filed = tableFileds.get(tableFileds.size()-1);
			}else{
				 filed = tableFileds.get(listIndex);
			}
			//设置字段的值
			filed.setFiledValue(value.toString());
			// list index ++ 
			listIndex ++ ;
		} else if (qName.equalsIgnoreCase("TableName")) {
			this.tableName = value.toString();
		}
	}

	private String convToSql(String strValue) {
		if (null == strValue) {
			return "NULL";
		}
		if (strValue.length() == 0) {
			return "''";
		}
		return "'" + replace(strValue, "'", "''") + "'";
	}

	private String replace(String maintext, String oldstring, String newstring) {
		if (maintext != null && oldstring != null && newstring != null) {
			int iIx0 = maintext.indexOf(oldstring);
			if (iIx0 == -1) {
				return maintext;
			}
			int iLength = oldstring.length();
			int iStart = 0;
			StringBuffer fsb = new StringBuffer();
			while (iIx0 != -1) {
				fsb.append(maintext.substring(iStart, iIx0));
				fsb.append(newstring);
				iStart = iIx0 + iLength;
				iIx0 = maintext.indexOf(oldstring, iStart);
			}
			fsb.append(maintext.substring(iStart));
			return fsb.toString();
		}
		return null;
	}

}
