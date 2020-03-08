package com.ceresdata.insert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class OracleDbHelper{
	private static final Logger logger = LoggerFactory.getLogger(OracleDbHelper.class);
	private static final String te = "TMP_";
	private static Random random = new Random();

	/**
	 * 批量插入时的数据量
	 */
	private static final int ADD_BATCH_COUNT = 100;
	Connection con = null;
	PreparedStatement pre = null;
	DataBaseFieldType adapter = null;
	int index = 0 ;
	StringBuilder sb = new StringBuilder();
	String tableName = "";
	String tempTableName = "";
	String pkName = "";
	Map<String, String> map = null;
	List<TableFiled> fileds = new ArrayList<TableFiled>();
	
	public OracleDbHelper(){
	}
	
	public  OracleDbHelper(Connection con){
		this.con = con;
		adapter = new DataBaseFieldType();
	}
	
	public OracleDbHelper(Connection con,String tableName,String pkName ){
		this(con);
		this.pkName = stringToUpCase(pkName);
		this.setTableName(stringToUpCase(tableName));
	}
	
	public void setTableName(String tableName){
		this.tableName = stringToUpCase(tableName);
		this.tempTableName = te + this.tableName + random.nextInt(100);
		this.tempTableName = tableName;
	}
	
	public String getTempTableName(){
		return tempTableName;
	}
	
	public void setPkName(String pkName){
		this.pkName = stringToUpCase(pkName);
	}
	
	public void setMap(Map<String, String> map){
		this.map = map;
	}
	
	public void setFileds(List<TableFiled> fileds){
		this.fileds = new ArrayList<TableFiled>(fileds);
		for(TableFiled fi : this.fileds){
			fi.setFiledName(stringToUpCase(fi.getFiledName()));
		}
	}
	
	/**
	 *  构建临时表的sql语句
	 *  
	 * @param tablename 表名
	 * @param pkName 主键名
	 * @param fileds 字段结合
	 * 
	 * @return 构建好的临时表的sql
	 */
	public String  getCreateTableSql(String tablename, String pkName,List<TableFiled> fileds) {
		sb.delete(0, sb.length());
		sb.append("CREATE TABLE \"");
		sb.append(tablename);
		sb.append("\" (");
		for (int i = 0; i < fileds.size(); i++) {
			// TS列下面添加
			TableFiled filed = fileds.get(i);
			if (filed.getFiledName().equalsIgnoreCase("TS")) {
				continue;
			}
			sb.append("\"");
			sb.append(filed.getFiledName());
			sb.append("\" ");
			// 以后要改，现在是字符串类型改成了 4000
			String coltype = filed.getFiledType();
			
			if (filed.getFiledName().equalsIgnoreCase(pkName)) {
				sb.append(coltype);
				if(coltype.equalsIgnoreCase("CHAR")||coltype.equalsIgnoreCase("VARCHAR")){
					sb.append("("+filed.getLength()+")");
				}
				sb.append(" not null primary key ");
			}else {
				if(filed.getFiledType().equalsIgnoreCase("GRAPHIC") ||filed.getFiledType().equalsIgnoreCase("VARGRAPHIC") ){
					int len = filed.getLength()*2;
					len = len >4000 ? 4000 : len;
					sb.append(coltype);
					sb.append("("+len+")");
				}else if(coltype.equalsIgnoreCase("CHAR")){
					sb.append(coltype);
					int len = filed.getLength() == 0 ? 10 : filed.getLength();
					sb.append("("+len+")");
				}else if(coltype.equalsIgnoreCase("VARCHAR")){
					int len = filed.getLength() >4000 ? 4000 : filed.getLength();
					sb.append(coltype);
					sb.append("("+len+")");
				}else{
					sb.append(coltype);
				}
			}
			sb.append(",");
		}
		sb.append("TS TimeStamp NULL default null");
		sb.append(")");
		
		return sb.toString();
	}
	/**
	 * 检查背地表是否存在，并且创建临时表
	 * @throws SQLException
	 */
	public void createRecvTableAndTempTable() {
		String sql = "";
		try{
			if(checkTableExists(this.tableName) == false){
				//创建数据库表
				sql= getCreateTableSql(this.tableName, this.pkName, this.fileds);
				logger.info("数据库表不存在，创建数据库表:"+sql);
				pre = con.prepareStatement(sql);
				pre.execute();
				pre.close();
			}
			// 初始化话本地服务
			this.initMap(this.tableName);
			//给表添加新列
			this.addColmn();
			if(checkTableExists(this.tempTableName)){
				sql = deleteTempTable();
				credateTableToDB(sql);
				logger.info("Temp table exists,Delete Temp table:"+sql);
				//删除临时表
				sql= this.getDropTempTable();
				credateTableToDB(sql);
				logger.info("Temp table exists,drop Temp table:"+sql);
			}
			//创建临时表
			sql = this.getCreateTempTableSql();
			pre = con.prepareStatement(sql);
			pre.execute();
			pre.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 给表添加主键列
	 * 
	 * @param tableName 表名
	 * @param pkName 主键名
	 * 
	 * @return 添加主键列SQL语句
	 */
	public String getAddPrimaryKeySql(String tableName,String pkName){
		String sql = " alter table "+tableName+" add primary key ("+pkName+")";
		return sql;
	}
	
	/**
	 *  构建临时表的sql
	 *  
	 * @return 构建好后的临时表的sql语句
	 */
	public String getCreateTempTableSql(){
		String sql = getCreateTableSql(this.tableName, this.pkName, this.fileds);
		sql = "Create Table " + this.tempTableName + sql.substring(sql.indexOf("("));
		return sql;
	}
	
	public void credateTableToDB(String sql){
		try {
			pre = con.prepareStatement(sql);
			// create table;
			pre.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.closeStatement(pre);
		}
	}
	/**
	 * 构建批量插入的sql，每5000条执行一次，
	 * 
	 * @param fileds 需要构建的sql列名和值
	 * 
	 * @throws SQLException
	 */
	public void addBatchSql(List<TableFiled> fileds) throws SQLException{
		if (index == 0){
			String sql = buildInsertSql(tempTableName, pkName, fileds);
			pre = con.prepareStatement(sql);
		}
		index ++ ;
		for(int i = 0 ;  i < fileds.size() ; i++){
			TableFiled filed = fileds.get(i);
			//本地数据库的列的类型
			String localColType = filed.getFiledType();
			if(adapter.isIntegerType(filed.getFiledType())){
				if(adapter.isDBNull(filed.getFiledValue())){
					pre.setNull(i+1, java.sql.Types.INTEGER);
				}else{
					pre.setInt(i+1, Integer.parseInt(filed.getFiledValue().trim()));
				}
			}else if(adapter.isDoubleType(filed.getFiledType())){
				if(adapter.isDBNull(filed.getFiledValue())){
					pre.setNull(i+1, java.sql.Types.DOUBLE);
				}else{
					pre.setDouble(i+1, Double.parseDouble(filed.getFiledValue().trim()));
				}
			}else if(adapter.isDateType(filed.getFiledType())){
				if(adapter.isDBNull(filed.getFiledValue())){
					pre.setNull(i+1, java.sql.Types.TIMESTAMP);
				}else{
					pre.setTimestamp(i+1,java.sql.Timestamp.valueOf(filed.getFiledValue().trim()));
				}
			}else {
				if(adapter.isDBNull(filed.getFiledValue())){
					pre.setNull(i+1,java.sql.Types.VARCHAR);
				}else{
					pre.setString(i+1, filed.getFiledValue().trim());
				}
			}
		}
		// add Batch;
		pre.addBatch();
		try{
			if(index % ADD_BATCH_COUNT == 0){
				pre.executeBatch();
				pre.clearBatch();
			}
		}catch(SQLException e){
			System.out.println("add batch error!"+fileds.get(0).getFiledName()+":"+fileds.get(0).getFiledValue());
			e.printStackTrace();
		}
	}
	
	/**
	 *  xml 文件解析完成，从临时表导入数据表，
	 *  
	 * @throws SQLException
	 */
	public void executeBatchSql()throws SQLException{
		pre.executeBatch();
		pre.clearBatch();
		pre.close();
	}
	/**
	 * 删除临时表
	 * 
	 * @throws SQLException
	 */
	protected void dropTempTable()throws SQLException{
		String sql = getDropTempTable();
		pre = con.prepareStatement(sql);
		pre.executeUpdate();
	}

	protected void combineDataTable()throws SQLException{
		String sql = tempTableToDataTable();
		pre = con.prepareStatement(sql);
		pre.executeUpdate();
	}
	
	/**
	 * 删除临时表结构:drop
	 */
	public String getDropTempTable(){
		String sql = "Drop table " + tempTableName;
		return sql;
	}
	/**
	 * 删除临时表数据：delete
	 */
	public String deleteTempTable(){
		String sql = "Delete From " + tempTableName;
		return sql;
	}
	
	/**
	 *  构建MERGE,实现有则更新，无则插入的sql
	 *  
	 * @return 构建好的sql
	 * 
	 */
	public String tempTableToDataTable() {
		sb.delete(0, sb.length());
		sb.append("merge into \"");
		sb.append(tableName);
		sb.append("\" p  using \"");
		sb.append(tempTableName);
		sb.append("\" np  on (");
		sb.append("p.\""+pkName+"\"="+"np.\""+pkName+"\"");
		sb.append(")");
		sb.append("  when matched then  update set ");
		for(TableFiled fi : fileds){
			if(fi.getFiledName().equalsIgnoreCase(pkName)){
				continue;
			}
			sb.append("p.\""+fi.getFiledName()+"\"=np.\""+fi.getFiledName()+"\",");
		}
		sb.deleteCharAt(sb.length()-1);
		StringBuilder temp1 = new StringBuilder();
		StringBuilder temp2 = new StringBuilder();
		for(TableFiled fi : fileds){
			temp1.append("p.\"").append(fi.getFiledName()).append("\",");
			temp2.append("np.\"").append(fi.getFiledName()).append("\",");
		}
		sb.append(" when not matched then  insert (");
		sb.append(temp1);
		sb.deleteCharAt(sb.length()-1);
		sb.append(") values(");
		sb.append(temp2);
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		//执行操作
		return sb.toString();
	}

	/**
	 * 构建InSert语句（Insert into table1(id,name) values(?,?)）
	 * @param tablename 表名
	 * @param pkname 主键名
	 * @param fileds 字段的结合
	 * 
	 * @return 构建好的Insert语句
	 */
	public String buildInsertSql(String tablename, String pkname,List<TableFiled> fileds ){
		sb.delete(0, sb.length());
		StringBuilder valueSb = new StringBuilder();
		sb.append("insert into \"" );
		sb.append(tablename);
		sb.append("\"(");
		for(int i = 0 ; i < fileds.size(); i++){
			TableFiled col = fileds.get(i);
			sb.append("\"");
			sb.append(col.getFiledName()).append("\",");
			valueSb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") values(");
		sb.append(valueSb.substring(0, valueSb.length()-1));
		sb.append(")");
		return sb.toString();
	}

	public void initMap(String tableName) {
		String sql = buildTableExistsSql(tableName);
		ResultSet rs = null;
		if (map == null) {
			map = new HashMap<String, String>();
			try {
				pre = con.prepareStatement(sql);
				rs = pre.executeQuery();
				ResultSetMetaData data = rs.getMetaData();
				
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnName(i);
					String columnTypeName = data.getColumnTypeName(i);
					map.put(columnName.toUpperCase(), columnTypeName.toUpperCase());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBManager.closeResultSet(rs);
				DBManager.closeStatement(pre);
			}
		}
	}
    public String buildTableExistsSql(String tablename){
    	String sql = "Select * from \"" + tablename + "\" Where 1=2";
    	return sql;
    }
	public boolean checkTableExists(String tablename) {
		boolean isExists = true;
		String sql = buildTableExistsSql(tablename);;
		Statement sta = null;
		ResultSet rs = null;
		try {
			pre = con.prepareStatement(sql);
			rs = pre.executeQuery();
		} catch (SQLException e) {
			isExists = false;
		} finally {
			DBManager.closeResultSet(rs);
			DBManager.closeStatement(sta);
		}
		return isExists;
	}
	/**
	 * 将字符串换成大写格式
	 * 
	 * @param str 要转换成大写的字符
	 * 
	 * @return 大写的格式
	 */
	public String stringToUpCase(String str){
		if(str == null)
			return null;
		return str.toUpperCase();
	}

	protected void addColmn(){
		//找出原表中不存在列
		if(this.map!= null){
			for(TableFiled tf : this.fileds){
				if(map.get(tf.getFiledName()) == null){
					String sql  = buildAddColumnSql(tf);
					System.out.println("ADD New Column :"+sql);
					//给表添加新列
					credateTableToDB(sql);
					map.put(tf.getFiledName(), tf.getFiledType());
				}
			}
		}
	}
	
	/**
	 * 构建添加列的SQL语句
	 * 
	 * @param tf 数据库中表的列
	 * @return 构建的SQL语句
	 */
	public String buildAddColumnSql(final TableFiled tf){
		String sql = "ALTER TABLE \""+this.tableName+"\" ADD ( \"" + tf.getFiledName() +"\"";
		String coltype = (tf.getFiledType());
		if("CHAR".equalsIgnoreCase(coltype)){
			sql += " CHAR("+tf.getLength()+")";
		}else if("VARCHAR".equalsIgnoreCase(coltype)){
			if(tf.getLength()>4000){
				sql += " BLOB";
			}else{
				sql += " VARCHAR("+tf.getLength()+")";
			}
		}else{
			sql += " "+coltype;
		}
		sql += " )";
		return sql;
	}


}
