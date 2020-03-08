package com.ceresdata.insert;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
* @author xielijun
* 支持断点续传的FTP实用类  
* @version 0.1 实现基本断点上传下载  
* @version 0.2 实现上传下载进度汇报  
* @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持  
*/  
public class FtpContinueUpload {
	public static final String ControlEncoding = "GBK";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String sendPercent;
	private FTPClient ftpClient = new FTPClient();
    private String hostname,username,pwd,ftpport;
	
	public FtpContinueUpload(){
    }

	/**
	 *
	 * @param hostname
	 * @param username
	 * @param pwd
	 * @param ftpport
	 */
	public FtpContinueUpload(String hostname, String username, String pwd, String ftpport){
		 this.hostname = hostname;
		 this.username = username;
		 this.pwd = pwd;
		 this.ftpport = ftpport;
		//设置将过程中使用到的命令输出到控制台   
//		 this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));   
    }   
       
    /**
     * 连接到FTP服务器  
     * @param hostname 主机名  
     * @param port 端口  
     * @param username 用户名  
     * @param password 密码  
     * @return 是否连接成功  
     * @throws IOException  
     */
    public boolean connect(String hostname,int port,String username,String password) throws IOException{
        ftpClient.connect(hostname, port);
        ftpClient.setControlEncoding(ControlEncoding);//在调用此方法之前要加这一句
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
            if(ftpClient.login(username, password)){
                return true;   
            }   
        }
        return false;   
    }

	/**
	 * 连接ftp 服务器
	 *
	 * @return
	 * @throws IOException
	 */
	public boolean connect() throws IOException{
		ftpClient.connect(this.hostname, Integer.parseInt(this.ftpport));
		ftpClient.setControlEncoding(ControlEncoding);//在调用此方法之前要加这一句
		if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
			if(ftpClient.login(username, this.pwd)){
				return true;
			}
		}
		return false;
	}

	/**
     * 上传文件到FTP服务器，支持断点续传  
     * @param localFile 本地文件名称，
     * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext或是 http://www.guihua.org /subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果  
     * @throws IOException  
     */  
    public UploadStatus upload(File localFile,String remote) throws Exception{
        //设置PassiveMode传输   
        ftpClient.enterLocalPassiveMode();   
        //设置以二进制流的方式传输   
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);   
        UploadStatus result = null;
        //对远程目录的处理   
        String remoteFileName = remote;   
        if(remote.lastIndexOf("/") != 0){   
            remoteFileName = remote.substring(remote.lastIndexOf("/")+1);   
            //创建服务器远程目录结构，创建失败直接返回   
            if(CreateDirecroty(remote, ftpClient)==UploadStatus.Create_Directory_Fail){   
                return UploadStatus.Create_Directory_Fail;   
            }   
        }
        //检查远程是否存在文件   
        FTPFile[] files = ftpClient.listFiles();   
        
        FTPFile existFtpFile = null;
        for(int i = 0 ; i < files.length; i++){
        	if(remoteFileName.startsWith("/")){
        		remoteFileName = remoteFileName.substring(1, remoteFileName.length());
        	}
        	if(("/"+files[i].getName()).equals("/"+remoteFileName)){
        		existFtpFile = files[i];
        		break;
        	}
        }
		long nStartP=0,nEndP= localFile.length();
        // 说明远程服务器已经存在这个文件
    	if(existFtpFile != null){
    		long remoteSize = existFtpFile.getSize();
    		long localSize = nEndP-nStartP+1;
    		// 远程文件小于本地文件则端点续传
    		if(remoteSize < localSize){
	    		//尝试移动文件内读取指针,实现断点续传   
	    		result = uploadFileSplit(localFile,remoteFileName, nStartP,nEndP, ftpClient, remoteSize);   
	    		//如果断点续传没有成功，则删除服务器上文件，重新上传   
	    		if(result == UploadStatus.Upload_From_Break_Failed){
	    			ftpClient.deleteFile(remoteFileName);
	    			result = uploadFileSplit(localFile,remoteFileName,nStartP,nEndP,ftpClient, 0);   
	    		}
    		}else{
    			logger.info("远程FTP文件已经存在,ftp://"+this.hostname +":" + this.ftpport + remote);
    			sendPercent = "100";
    		}
    	}else {
    		result = uploadFileSplit(localFile,remoteFileName,nStartP,nEndP,ftpClient, 0);
    	}
        return result;   
    }   
    /**
     * 上传文件到ftp 服务器，本地文件
     * @param localFile
     * @param remoteFile
     * @param nStartP
     * @param nEndP
     * @param ftpClient
     * @param remoteSize
     * @return
     * @throws IOException
     */
    public UploadStatus uploadFileSplit(File localFile,String remoteFile,long nStartP,long nEndP,FTPClient ftpClient,long remoteSize) throws Exception{
    	UploadStatus status;   
    	//显示进度的上传   
    	long localreadbytes = nStartP;   
    	RandomAccessFile randomAccessFile = null;
    	OutputStream out = null;
    	try{
    		randomAccessFile = new RandomAccessFile(localFile,"r");
    		out = ftpClient.appendFileStream(remoteFile);
    		// 每次发送文件的进度的步长
    		int percent = 20;
    		long step = localFile.length() / percent;
    		// 传输的进度
    		long process = 0;
        	//断点续传   
        	if(remoteSize > 0){
        		process = remoteSize / step;
        		ftpClient.setRestartOffset(remoteSize);   
        		localreadbytes += remoteSize;
        		randomAccessFile.seek(localreadbytes);   
        	}else{
        		randomAccessFile.seek(nStartP);
        	}
        	byte[] bytes = new byte[4096];
        	int index = -1;   
        	while(localreadbytes < nEndP && (index = randomAccessFile.read(bytes))!= -1){   
        		// 如果开始坐标+读取字节数组 大于 文件切割结束索引时
        		if(localreadbytes + index > nEndP){
        			index = (int)(nEndP - localreadbytes)+1;
        		}
        		out.write(bytes,0,index);   
        		localreadbytes += index;   
        		
    			if (localreadbytes / step != process) {
    				process = localreadbytes / step;
    				process = process > 100 ? 100 : process;
    				sendPercent = (process * (100 /percent))+"";
					if(logger.isDebugEnabled()){
						logger.debug("上传进度 {"+localFile.getPath()+"}:" + sendPercent);
					}
    			}
        	}
        	out.flush();
    	}catch(Exception ex){
    		logger.error("ftp 上传失败",ex);
    		throw ex;
    	}finally{
    		if(out != null){
    			out.close();
    		}
    		if(randomAccessFile != null){
    			randomAccessFile.close();
    		}
    	}
    	boolean result = ftpClient.completePendingCommand();   
    	if(remoteSize > 0){   
    		status = result?UploadStatus.Upload_From_Break_Success:UploadStatus.Upload_From_Break_Failed;   
    	}else {   
    		status = result?UploadStatus.Upload_New_File_Success:UploadStatus.Upload_New_File_Failed;   
    	}   
    	return status;   
    }
    /**
     * 断开与远程服务器的连接  
     * @throws IOException  
     */  
    public void disconnect() throws IOException{
    	// 退出登录
		ftpClient.logout();
		// 断开连接
        if(ftpClient != null && ftpClient.isConnected()){   
            ftpClient.disconnect();   
        } 
        ftpClient = null;
    }   
       
    /** *//**  
     * 递归创建远程服务器目录  
     * @param remote 远程服务器文件绝对路径  
     * @param ftpClient FTPClient 对象  
     * @return 目录创建是否成功  
     * @throws IOException  
     */  
    public UploadStatus CreateDirecroty(String remote,FTPClient ftpClient) throws IOException{   
        UploadStatus status = UploadStatus.Create_Directory_Success;   
        String directory = remote.substring(0,remote.lastIndexOf("/")+1);  
        //boolean changeWorkDir = ftpClient.changeWorkingDirectory(new String(directory.getBytes(ControlEncoding),"iso-8859-1"));
        boolean changeWorkDir = ftpClient.changeWorkingDirectory(directory);
        if(!changeWorkDir){   
            //如果远程目录不存在，则递归创建远程服务器目录   
            int start=0;   
            int end = 0;   
            if(directory.startsWith("/")){   
                start = 1;   
            }else{   
                start = 0;   
            }   
            end = directory.indexOf("/",start);   
            while(true){   
            	String subDirectory = new String(remote.substring(start,end));
                if(!ftpClient.changeWorkingDirectory(subDirectory)){   
                    if(ftpClient.makeDirectory(subDirectory)){   
                        ftpClient.changeWorkingDirectory(subDirectory);   
                    }else {   
                        logger.warn("创建目录失败");   
                        return UploadStatus.Create_Directory_Fail;   
                    }   
                }   
                   
                start = end + 1;   
                end = directory.indexOf("/",start);
                //检查所有目录是否创建完毕   
                if(end <= start){   
                    break;   
                }   
            }   
        }
        return status;   
    }

	public static void main(String[] args) {
		FtpContinueUpload upload = new FtpContinueUpload("127.0.0.1","xielijun","123456","21");
		File file = new File("c:/temp/srs2856_part1of2.mdb");

		File[] files = file.getParentFile().listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".mdb");
			}
		});
		try {
			// 建立FTP 连接
			upload.connect();
			for(int i = 0 ; i < files.length; i++){
				// 开始上传文件
				upload.upload(files[i],"/demo/"+files[i].getName());
			}
			// 断开ftp连接
			upload.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}  