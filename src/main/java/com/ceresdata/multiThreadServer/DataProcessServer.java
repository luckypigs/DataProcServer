package com.ceresdata.multiThreadServer;

import com.ceresdata.pojo.Connect;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.util.FileUtil;
import com.ceresdata.util.FastJSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据整理服务端
 */
public class DataProcessServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String initConfigFile;
    // 服务启动配置实体
    private ServerConfig config = new ServerConfig();
    /**
     * 客户端连接
     */
    private List<MultiThreadSocketClient> clients = new ArrayList<MultiThreadSocketClient>();

    /**
     * 通过配置文件初始化server，并重新启动
     */
    public void initServer(){
        this.roladByFile();
        logger.info("数据整理软件流处理服务从配置文件加载完成");
        if(this.config.getStreamState() == 1){
            this.startServer();
        }
    }
    /**
     * 启动所有客户端
     */
    public void startServer(){
        for(int i = 0 ; i < clients.size(); i++){
            MultiThreadSocketClient client = clients.get(i);
            if(client.isRunning() == false){
                Thread thread = new Thread(client);
                thread.start();
            }
        }
        // 状态修改为开启状态，并对文件进行保存
        this.config.setStreamState(1);
        this.saveToFile();
        logger.info("数据整理软件流处理服务启动");
    }

    /**
     * 建立socket 连接
     * @param connect
     * @return
     */
    public boolean connect(Connect connect){
        try {
            Socket socket = new Socket(connect.getIp(),connect.getPort());
            MultiThreadSocketClient client = new MultiThreadSocketClient(connect.getIp(),connect.getPort(),socket);
            this.clients.add(client);
            this.config.addConnect(connect);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 断开连接
     * @param connect
     * @return
     */
    public boolean disconnect(Connect connect){
        try {
            // 找到连接并
            for(int i = 0; i < this.clients.size(); i++){
                MultiThreadSocketClient client = this.clients.get(i);
                // 客户端断开连接并退出
                if(client.getIp().equals(connect.getIp()) && connect.getPort() == client.getPort()){
                    client.exit();
                }
            }
            // 从配置对象中移除
            this.config.removeConnect(connect);
            this.saveToFile();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 停止客户端
     */
    public void stopServer(){
        for(int i =0 ; i < clients.size(); i++){
            MultiThreadSocketClient client = clients.get(i);
            client.exit();
        }
        this.clients.clear();
        this.config.setStreamState(0);
        // 重新持久到文件中
        this.saveToFile();
    }

    /**
     * 从配置文件中重新加载配置信息到内存中
     */
    public void roladByFile(){
        try{
            File file = new File(this.initConfigFile);
            if(!file.exists()){
                this.saveToFile();
            }else{
                String content = FileUtil.readStringByFile(this.initConfigFile);
                this.config.reset(content);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 保存并持久化到文件中
     */
    public void saveToFile(){
        try{
            String json = FastJSONUtil.toJSONString(this.config);
            FileUtil.writeFile(this.initConfigFile,json);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void setInitConfigFile(String initConfigFile) {
        this.initConfigFile = initConfigFile;
    }
}
