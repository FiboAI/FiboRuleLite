package com.fibo.rule.core.util;

import cn.hutool.core.util.IdUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *<p>地址生成工具</p>
 *
 *@author JPX
 *@since 2022/12/5 13:38
 */
public final class FiboAddressUtils {

    public static String getAddress(Long app) {
        if (app == null) {
            throw new RuntimeException("初始化地址时app不能为空");
        }
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            if ("127.0.0.1".equals(host)) {
                host = InetAddress.getLocalHost().getHostName();
            }
        } catch (UnknownHostException e) {
            //ignore
        }
        return host == null ? (app + "/" + IdUtil.fastSimpleUUID()) : (host + "/" + app + "/" + IdUtil.fastSimpleUUID());
    }

    public static String getAddress() {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            if ("127.0.0.1".equals(host) || "localhost".equals(host)) {
                host = getHostIp();
            }
        } catch (UnknownHostException e) {
            //ignore
        }
        return host;
    }

    public static String getHostIp() {
        String result = null;
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            // handle error
        }

        if (interfaces != null) {
            while (interfaces.hasMoreElements() && result == null) {
                NetworkInterface i = interfaces.nextElement();
                Enumeration<InetAddress> addresses = i.getInetAddresses();
                while (addresses.hasMoreElements() && (result == null || result.isEmpty())) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() &&
                            address.isSiteLocalAddress()) {
                        result = address.getHostAddress();
                    }
                }
            }
        }
        return result;
    }
}