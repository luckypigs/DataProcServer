package com.ceresdata.multiThreadServer;

import com.alibaba.fastjson.JSONObject;
import com.ceresdata.pojo.Connect;
import com.ceresdata.pojo.FileSize;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.util.FileUtil;
import com.ceresdata.util.FastJSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据整理服务端
 */
@Component
public class DataProcessServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String initConfigFile;
    // 服务启动配置实体
    private ServerConfig config = new ServerConfig();

    @Autowired
    PcapDataService pcapDataService;
    /**
     * 客户端连接
     */
    private List<MultiThreadSocketClient> clients = new ArrayList<MultiThreadSocketClient>();

    private MultiThreadOfflineClient offlineClient;

    /**
     * 通过配置文件初始化server，并重新启动
     */
    public void initServer(){
        this.roladByFile();
        logger.info("数据整理软件流处理服务从配置文件加载完成");
        if(this.config.getStreamState() == 1){
            this.startServer();
        }
        if(this.config.getFileServerState()==1){
            this.startOfflineServer(getFileSize());
        }
    }
    /**
     * 启动所有客户端
     */
    public void startServer(){
        List<Connect> l=config.getConnectList();
        try {
            for (int i = 0; i < l.size(); i++) {
                Connect c = l.get(i);
                Socket socket = new Socket(c.getIp(), c.getPort());
                clients.add(new MultiThreadSocketClient(c.getIp(), c.getPort(), socket, this));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0 ; i < clients.size(); i++){
            MultiThreadSocketClient client = clients.get(i);
            Connect connect=l.get(i);
            if(client.isRunning() == false&&connect.isSocktState()){
                Thread thread = new Thread(client);
                thread.start();
            }
        }
        // 状态修改为开启状态，并对文件进行保存
        this.config.setStreamState(1);
        this.saveToFile();
        logger.info("数据整理软件流处理服务启动");
    }

    public void startOfflineServer(FileSize fileSize){
        config.setFileMaxMinute(fileSize.getFileMaxMinute());
        config.setFileMaxSize(fileSize.getFileMaxSize());
        config.setDescRootDir(fileSize.getDescDir());
        config.setSrcRootDir(fileSize.getSrcDir());
        offlineClient=new MultiThreadOfflineClient(this);
        Thread thread = new Thread(offlineClient);
        thread.start();
        this.config.setFileServerState(1);
        this.saveToFile();
    }

    public void stopOfflineServer(){
        offlineClient.setRunning(false);
        this.config.setFileServerState(0);
        this.saveToFile();

    }

    /**
     * 建立socket 连接
     * @param connect
     * @return
     */
    public boolean connect(Connect connect){
        int haveConnected=0; //0从未连接过，1正在连接，2连接过但现在断开了
        List<Connect> l=config.getConnectList();
        try {
            for(int i = 0; i < l.size(); i++){
                Connect c = l.get(i);
                if(c.getIp().equals(connect.getIp()) && connect.getPort() == c.getPort()){
                    if(c.isSocktState()){
                        haveConnected=1;
                    }else{
                        c.setSocktState(true);
                        haveConnected=2;
                    }
                    break;
                }
            }
            if (haveConnected==0) {
                Socket socket = new Socket(connect.getIp(), connect.getPort());
                MultiThreadSocketClient client = new MultiThreadSocketClient(connect.getIp(), connect.getPort(), socket, this);
                connect.setSocktState(true);
                this.clients.add(client);
                this.config.addConnect(connect);
                saveToFile();
            }else if(haveConnected==2){
                saveToFile();
            }
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
        List<Connect>l=config.getConnectList();
        try {
            for(int i = 0; i < l.size(); i++){
                Connect c = l.get(i);
                if(c.getIp().equals(connect.getIp()) && connect.getPort() == c.getPort()&&c.isSocktState()){
                    c.setSocktState(false);
                    break;
                }
            }
            // 找到连接并断开
            for(int i = 0; i < this.clients.size(); i++){
                MultiThreadSocketClient client = this.clients.get(i);
                // 客户端断开连接并退出
                if(client.getIp().equals(connect.getIp()) && connect.getPort() == client.getPort()&&client.isRunning()){
                    connect.setSocktState(false);
                    client.exit();
                }
            }
            // 从配置对象中移除
            //this.config.removeConnect(connect);
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
                //this.config.reset(content);
                this.config= JSONObject.parseObject(content, ServerConfig.class);
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

    public ServerConfig getConfig() {
        return config;
    }

    public void setConfig(ServerConfig config) {
        this.config = config;
    }

    public PcapDataService getPcapDataService() {
        return pcapDataService;
    }

    public void setPcapDataService(PcapDataService pcapDataService) {
        this.pcapDataService = pcapDataService;
    }

    public FileSize getFileSize(){
        return new FileSize(config.getFileMaxSize(),config.getFileMaxMinute(),config.getFilePath(),config.getSrcRootDir(),config.getDescRootDir());
    }
}
