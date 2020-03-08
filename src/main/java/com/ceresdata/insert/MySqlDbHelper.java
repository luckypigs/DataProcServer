package com.ceresdata.insert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySqlDbHelper extends OracleDbHelper{
	
	public MySqlDbHelper(){}
	
	public  MySqlDbHelper(Connection con){
		super(con);
	}
	public MySqlDbHelper(Connection con,String tableName,String pkName ){
		super(con,tableName,pkName);
	}
	
	
	public String  getCreateTableSql(String tablename, String pkName,List<TableFiled> fileds) {
		sb.delete(0, sb.length());
		for (int i = 0; i < fileds.size(); i++) {
			// TS列下面添加
			TableFiled filed = fileds.get(i);
			if (filed.getFiledName().equalsIgnoreCase("TS")) {
				continue;
			}
			sb.append("`");
			sb.append(filed.getFiledName());
			sb.append("` ");
			// 以后要改，现在是字符串类型改成了 4000
			String coltype = (filed.getFiledType());
			if (filed.getFiledName().equalsIgnoreCase(pkName)) {
				sb.append(coltype);
				if(coltype.equalsIgnoreCase("char")||coltype.equalsIgnoreCase("varchar")){
					sb.append("("+filed.getLength()+")");
				}
				sb.append(" not null primary key ");
			}else {
				if(coltype.equalsIgnoreCase("char")){
					sb.append(coltype);
					sb.append("("+filed.getLength()+")");
					sb.append(" CHARACTER SET utf8");
				}else if(coltype.equalsIgnoreCase("varchar")){
					if(filed.getLength() == 21845){
						sb.append("TEXT");
					}else if(filed.getLength() == 5592405){
						sb.append("MEDIUMTEXT");
					}else if(filed.getLength() == 715827882){
						sb.append("LONGTEXT");
					}else if(filed.getLength() > 32767){
						sb.append("TEXT");
					}else{
						sb.append(coltype);
						sb.append("("+filed.getLength()+")");
						sb.append(" CHARACTER SET utf8");
					}
				}else{
					sb.append(coltype);
				}
			}
			sb.append(",");
		}

		String temp = sb.toString();
		sb.delete(0, sb.length());
		sb.append("Create Table `");
		sb.append(tablename);
		sb.append("` (");
		sb.append(temp);
		sb.append("TS TimeStamp NULL DEFAULT NULL");
		sb.append(") ENGINE=InnoDB DEFAULT  CHARSET=utf8");
		
		return sb.toString();
	}

	public String getCreateTempTableSql(){
		sb.delete(0, sb.length());
		sb.append("CREATE TABLE `");
		sb.append(this.tempTableName);
		sb.append("`(SELECT ");
		for(TableFiled fi : fileds){
			sb.append("`");
			sb.append(fi.getFiledName());
			sb.append("`,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" FROM `");
		sb.append(this.tableName);
		sb.append("` WHERE 1=2");
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 构建从临时表中往数据表中插入的SQL
	 * 
	 * @return 插入的SQL
	 */
	private String buildToTableInsetSql(){
		sb.delete(0, sb.length());
		sb.append("INSERT INTO `");
		sb.append(tableName);
		sb.append("` (");
		String temp = "";
		for(TableFiled fi : fileds){
			temp += "`"+fi.getFiledName() + "`,";
		}
		temp = temp.substring(0,temp.length()-1);
		sb.append(temp);
		sb.append(") ");
		sb.append("SELECT ");
		sb.append(temp);
		sb.append(" FROM `");
		sb.append(tempTableName);
		sb.append("` ON DUPLICATE KEY UPDATE `");
		sb.append(pkName);
		sb.append("` = `");
		sb.append(tempTableName);
		sb.append("`.`");
		sb.append(pkName);
		sb.append("`");
		return sb.toString();
	}
	/**
	 * 构建从临时表中往数据表中更新的SQL
	 * 
	 * @return 跟新SQL
	 */
	private String buildToTableUpdateSql(){
		sb.delete(0, sb.length());
		sb.append("update `");
		sb.append(tableName);
		sb.append("`,`");
		sb.append(tempTableName);
		sb.append("` Set ");
		for(TableFiled fi : fileds){
			sb.append("`"+tableName+"`.`"+fi.getFiledName()+"`=`"+tempTableName+"`.`"+fi.getFiledName()+"`,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" where `"+tableName+"`.`"+pkName+"`=`"+tempTableName+"`.`"+pkName+"`");
		return sb.toString();
	}
	
	protected void combineDataTable()throws SQLException{
		// 先将id相同的，跟新过去，
		String sql = this.buildToTableUpdateSql();
		pre = con.prepareStatement(sql);
		pre.executeUpdate();
		// id 没有的插入
		sql = this.buildToTableInsetSql();
		pre = con.prepareStatement(sql);
		pre.executeUpdate();
		pre.close();
	}
	
	
	/**
	 * 构建从临时表中往数据表中插入的SQL,这种语句会将原来的删掉，在进行插入的。
	 * 
	 * @return 插入的SQL
	 */
	private String replaceTempTableToDataTableSql(){
		sb.append("replace INTO ");
		sb.append(tableName);
		sb.append(" (");
		String temp = "";
		for(TableFiled fi : fileds){
			temp += fi.getFiledName() + ",";
		}
		temp = temp.substring(0,temp.length()-1);
		sb.append(temp);
		sb.append(") ");
		sb.append("SELECT ");
		sb.append(temp);
		sb.append(" FROM ");
		sb.append(tempTableName);
		return sb.toString();
	}
	
	public String buildInsertSql(String tablename, String pkname,List<TableFiled> fileds ){
		sb.delete(0, sb.length());
		StringBuilder valueSb = new StringBuilder();
		sb.append("INSERT INTO `" );
		sb.append(tablename);
		sb.append("`(");
		for(int i = 0 ; i < fileds.size(); i++){
			TableFiled col = fileds.get(i);
			sb.append("`");
			sb.append(col.getFiledName()).append("`,");
			valueSb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") VALUES(");
		sb.append(valueSb.substring(0, valueSb.length()-1));
		sb.append(")");
		return sb.toString();
	}
	
	public String buildTableExistsSql(String tablename){
	    	String sql = "Select * from `" + tablename + "` Where 1=2";
	    	return sql;
	}
	
	public String buildAddColumnSql(final TableFiled tf){
		String sql = "ALTER TABLE `"+this.tableName+"` ADD COLUMN `"+tf.getFiledName()+"`";
		String coltype = tf.getFiledType();

		if("CHAR".equalsIgnoreCase(coltype)){
			sql += " CHAR("+tf.getLength()+")";
		}else if("VARCHAR".equalsIgnoreCase(coltype)){
			sql += " VARCHAR("+tf.getLength()+")";
		}else{
			sql += " "+coltype;
		}
		return sql;
	}
}
