package com.ceresdata.schedule;

import com.ceresdata.dao.SystemStatusLogDao;
import com.ceresdata.multiThreadServer.DataProcessServer;
import com.ceresdata.multiThreadServer.MultiThreadOfflineClient;
import com.ceresdata.multiThreadServer.MultiThreadSocketClient;
import com.ceresdata.pojo.Connect;
import com.ceresdata.pojo.SystemStatusLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * created by hsk on 2020/3/22
 */
@Component
public class StaticScheduleTask {
    @Autowired
    private SystemStatusLogDao systemStatusLogDao;
    @Autowired
    private DataProcessServer dataProcessServer;

    @Value("${server.port}")
    private int server_port;

    @Scheduled(fixedRate=60000)
    public void SystemStatusScheduleTask(){
        SystemStatusLog log=new SystemStatusLog();
        String ipList="";
        List<String> list=getLocalIPList();
        for(int i=0;i<list.size();i++){
            if(i==list.size()-1){
                ipList+=list.get(i);
            }else {
                ipList+=list.get(i)+";";
            }
        }
        log.setEqpIP(ipList);
        log.setPosition(dataProcessServer.getConfig().getPosition());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
        log.setReportTime(simpleDateFormat.format(System.currentTimeMillis()));
        log.setWorkStat(dataProcessServer.getConfig().getFileServerState()==1||dataProcessServer.getConfig().getStreamState()==1?1:0);
        String workparam=getWorkparam();
        log.setWorkparam(workparam);
        log.setTcpUse(getTcpUse());

        systemStatusLogDao.addSystemStatusLog(log);
    }

    public  List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    public String getWorkparam(){
        String res="当前数据处理方式：";
        if(dataProcessServer.getConfig().getStreamState()==1){
            res+="数据流，当前存储路径:";
            res+=dataProcessServer.getConfig().getFilePath()+",当前处理数据总条数：";
            res+= MultiThreadSocketClient.getCountPa()+",当前处理数据总量：";
            res+=MultiThreadSocketClient.getCountByte();
        }else if(dataProcessServer.getConfig().getFileServerState()==1){
            res+="文件，当前存储路径：";
            res+=dataProcessServer.getConfig().getDescRootDir()+",当前处理数据总条数：";
            res+=MultiThreadOfflineClient.getCountPa()+",当前处理数据总量：";
            res+=MultiThreadOfflineClient.getCountByte();
        }else{
            res+="无";
        }
        return res;

    }

    public String getTcpUse(){
        String res="";
        List<Connect> l=dataProcessServer.getConfig().getConnectList();
        for(int i=0;i<l.size();i++){
            Connect c=l.get(i);
            String tmp = server_port + ":" + c.getPort();
            if(i!=l.size()-1) {
                res+=tmp+";";
            }else{
                res+=tmp;
            }
        }
        return res;
    }

}
