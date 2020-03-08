package com.ceresdata.insert;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;

/**
 *  解析XML文件到数据库文件
 */
public class XMLParse{
	// xml 文件名称
	private String xmlFileName;
	// 数据库连接
	private Connection connection;

	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void parse(){
		try(FileInputStream fileInputStream = new FileInputStream(this.xmlFileName)) {
			this.parse(fileInputStream);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * @param in
	 * @throws Exception
	 */
	public void parse(InputStream in){
    	XmlHandler handler = new XmlHandler(this.connection);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        SAXParser parser=null;
        try {
            parser = factory.newSAXParser();
            parser.parse(in, handler);
        }catch (Exception ex){
        	ex.printStackTrace();
		}
        finally{
            factory=null;
            parser=null;
            handler=null;
        }
    }
	
	public static void main(String[] args) throws Exception{
		XMLParse xmlParse = new XMLParse();
		Connection conn = DBManager.getConnection();
		String fname = "d:/tmp/406241577186200.xml";
		xmlParse.setXmlFileName(fname);
		xmlParse.setConnection(conn);

		long s = System.currentTimeMillis();
		xmlParse.parse();
		long e = System.currentTimeMillis();
		System.out.print("共用总时间："+(e-s));
	}
    
}


