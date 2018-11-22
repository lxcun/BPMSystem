package com.atw.bpmsystem.Common;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
@Slf4j
public class RequestUtil {
    /**
     * 获取IP地址
     *
     * @return String IP地址
     */
    public String getLocalhostIP() {
        InetAddress address = null;//获取的是本地的IP地址
        try {
            address = InetAddress.getLocalHost();
            log.info("获取本地ip"+address);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取服务器未知！");
            e.printStackTrace();
        }

        try {
            String ip = Inet4Address.getLocalHost().getHostAddress();
            log.warn("获取服务器ip"+ip);
            return ip;
        } catch (UnknownHostException e) {
            log.error("IP获取错误");
            e.printStackTrace();
        }

        return "127.0.0.1";
    }
}
