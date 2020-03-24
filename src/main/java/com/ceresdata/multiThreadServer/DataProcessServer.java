package com.ceresdata.multiThreadServer;

import com.alibaba.fastjson.JSONObject;
import com.ceresdata.pojo.Connect;
import com.ceresdata.pojo.EventLog;
import com.ceresdata.pojo.FileSize;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.service.EventLogService;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.util.FileUtil;
import com.ceresdata.util.FastJSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
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
    private ServerConfig config;
    @Autowired
    EventLog eventLog;

    @Autowired
    PcapDataService pcapDataService;
    @Autowired
    EventLogService eventLogService;
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
        eventLog.setPosition(getConfig().getPosition());
        if(this.config.getStreamState() == 1){
            this.startServer(getFileSize());
        }
        if(this.config.getFileServerState()==1){
            this.startOfflineServer(getFileSize());
        }
    }
    /**
     * 启动所有客户端
     */
    public void startServer(FileSize fileSize){
        config.setFileMaxMinute(fileSize.getFileMaxMinute());
        config.setFileMaxSize(fileSize.getFileMaxSize());
        config.setFilePath(fileSize.getFilePath());
        List<Connect> l=config.getConnectList();
        for (int i = 0; i < l.size(); i++) {
            Connect c = l.get(i);
            clients.add(new MultiThreadSocketClient(c.getIp(), c.getPort(), this));
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
        eventLog.setEventName("启动流处理服务");
        eventLog.setEventInfo("启动流处理服务成功");
        eventLog.setEventParams("文件存储路径："+config.getFilePath()+",文件大小："+config.getFileMaxSize()+",文件间隔时间："+config.getFileMaxMinute());
        eventLogService.addInfoLog(eventLog);

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
        logger.info("数据整理软件文件处理服务启动");
        eventLog.setEventName("启动文件处理服务");
        eventLog.setEventInfo("启动文件处理服务成功");
        eventLog.setEventParams("数据源："+config.getSrcRootDir()+",文件存储路径："+config.getDescRootDir()+",文件大小："+config.getFileMaxSize()+",文件间隔时间："+config.getFileMaxMinute());
        eventLogService.addInfoLog(eventLog);
    }

    public void stopOfflineServer(){
        offlineClient.setRunning(false);
        this.config.setFileServerState(0);
        this.saveToFile();
        logger.info("数据整理软件文件处理服务停止");
        eventLog.setEventName("停止文件处理服务");
        eventLog.setEventInfo("停止文件处理服务成功");
        eventLog.setEventParams("停止文件处理服务成功,已处理数据包数："+MultiThreadOfflineClient.getCountPa()+",已处理字节数："+MultiThreadOfflineClient.getCountByte());
        eventLogService.addInfoLog(eventLog);

    }

    /**
     * 建立socket 连接
     * @param connect
     * @return
     */
    public boolean connect(Connect connect){
        int haveConnected=0; //0从未连接过，1正在连接，2连接过但现在断开了
        List<Connect> l=config.getConnectList();
        for(int i = 0; i < l.size(); i++){
            Connect c = l.get(i);
            if(c.getIp().equals(connect.getIp()) && connect.getPort() == c.getPort()){
                if(c.isSocktState()){
                    haveConnected=1;
                }else{
                    c.setSocktState(true);
                    haveConnected=2;
                    if(config.getStreamState()==1) {
                        new Thread(clients.get(i)).start();
                    }
                }
                break;
            }
        }
        if (haveConnected==0) {
            connect.setSocktState(true);
            this.config.addConnect(connect);
            saveToFile();
            if(config.getStreamState()==1){
                MultiThreadSocketClient client = new MultiThreadSocketClient(connect.getIp(), connect.getPort(), this);
                this.clients.add(client);
                new Thread(client).start();
            }
        }else if(haveConnected==2){
            saveToFile();
        }
        if(haveConnected==0){
            //logger.info("数据整理软件文件处理服务启动");
            eventLog.setEventName("建立连接");
            eventLog.setEventInfo("建立连接成功");
            eventLog.setEventParams("IP："+connect.getIp()+",Port"+connect.getPort());
            eventLogService.addInfoLog(eventLog);
        }else if(haveConnected==1){
            eventLog.setEventName("建立连接");
            eventLog.setEventInfo("重复建立连接");
            eventLog.setEventParams("IP："+connect.getIp()+",Port"+connect.getPort());
            eventLogService.addWarnLog(eventLog);
        }else{
            eventLog.setEventName("建立连接");
            eventLog.setEventInfo("重新连接成功");
            eventLog.setEventParams("IP："+connect.getIp()+",Port"+connect.getPort());
            eventLogService.addInfoLog(eventLog);
        }
        return true;
    }

    /**
     * 断开连接
     * @param connect
     * @return
     */
    public boolean disconnect(Connect connect){
        List<Connect>l=config.getConnectList();
        int tmp=0;
        for (int i = 0; i < l.size(); i++) {
            Connect c = l.get(i);
            if (c.getIp().equals(connect.getIp()) && connect.getPort() == c.getPort() && c.isSocktState()) {
                c.setSocktState(false);
                tmp=1;
                break;
            }
        }
        // 找到连接并断开
        for (int i = 0; i < this.clients.size(); i++) {
            MultiThreadSocketClient client = this.clients.get(i);
            // 客户端断开连接并退出
            if (client.getIp().equals(connect.getIp()) && connect.getPort() == client.getPort() && client.isRunning()) {
                connect.setSocktState(false);
                client.exit();
            }
        }
        // 从配置对象中移除
        //this.config.removeConnect(connect);
        this.saveToFile();
        if(tmp==1) {
            eventLog.setEventName("断开连接");
            eventLog.setEventInfo("断开连接成功");
            eventLog.setEventParams("IP："+connect.getIp()+",Port"+connect.getPort());
            eventLogService.addInfoLog(eventLog);
        }else{
            eventLog.setEventName("断开连接");
            eventLog.setEventInfo("指定连接不存在");
            eventLog.setEventParams("IP："+connect.getIp()+",Port"+connect.getPort());
            eventLogService.addWarnLog(eventLog);
        }
        return true;

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
        logger.info("数据整理软件流处理服务停止");
        eventLog.setEventName("停止流处理服务");
        eventLog.setEventInfo("停止流处理服务成功");
        eventLog.setEventParams("停止流处理服务成功,已处理数据包数："+MultiThreadSocketClient.getCountPa()+",已处理字节数："+MultiThreadSocketClient.getCountByte());
        eventLogService.addInfoLog(eventLog);
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
            json=json.replaceAll(",",",\n");
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
