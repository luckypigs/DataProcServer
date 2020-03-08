package com.ceresdata.insert;

/**
 * 解析数据库表字段名称和 value 值
 */
public class TableFiled {
	public TableFiled(){
	}
	private String filedName;
	private String filedType;
	private String filedValue;
	private int length = 0;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getFiledName() {
		return filedName;
	}
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	public String getFiledType() {
		return filedType;
	}
	public void setFiledType(String filedType) {
		this.filedType = filedType;
	}
	public String getFiledValue() {
		return filedValue;
	}
	public void setFiledValue(String filedValue) {
		this.filedValue = filedValue;
	}
	
}
