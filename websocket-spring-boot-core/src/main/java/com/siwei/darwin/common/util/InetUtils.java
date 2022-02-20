package com.siwei.darwin.common.util;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.siwei.darwin.config.properties.InetUtilsProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InetUtils {
    private final InetUtilsProperties properties;

    public InetUtils.HostInfo findFirstNonLoopbackHostInfo() {
        InetAddress address = this.findFirstNonLoopbackAddress();
        if (address != null) {
            return this.convertAddress(address);
        } else {
            InetUtils.HostInfo hostInfo = new InetUtils.HostInfo();
            hostInfo.setHostname(this.properties.getDefaultHostname());
            hostInfo.setIpAddress(this.properties.getDefaultIpAddress());
            return hostInfo;
        }
    }

    public InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;

        try {
            int lowest = 2147483647;
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();

            label61:
            while (true) {
                NetworkInterface ifc;
                do {
                    while (true) {
                        do {
                            if (!nics.hasMoreElements()) {
                                break label61;
                            }

                            ifc = nics.nextElement();
                        } while (!ifc.isUp());

                        log.trace("Testing interface: " + ifc.getDisplayName());
                        if (ifc.getIndex() >= lowest && result != null) {
                            continue;
                        }

                        lowest = ifc.getIndex();
                        break;
                    }
                } while (this.ignoreInterface(ifc.getDisplayName()));

                Enumeration<InetAddress> addrs = ifc.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress address = addrs.nextElement();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress() && this.isPreferredAddress(address)) {
                        log.trace("Found non-loopback interface: " + ifc.getDisplayName());
                        result = address;
                    }
                }
            }
        } catch (IOException var8) {
            log.error("Cannot get first non-loopback address", var8);
        }

        if (result != null) {
            return result;
        } else {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException var7) {
                log.warn("Unable to retrieve localhost");
                return null;
            }
        }
    }

    boolean isPreferredAddress(InetAddress address) {
        if (this.properties.isUseOnlySiteLocalInterfaces()) {
            boolean siteLocalAddress = address.isSiteLocalAddress();
            if (!siteLocalAddress) {
                log.trace("Ignoring address: " + address.getHostAddress());
            }

            return siteLocalAddress;
        } else {
            List<String> preferredNetworks = this.properties.getPreferredNetworks();
            if (preferredNetworks.isEmpty()) {
                return true;
            } else {
                Iterator var3 = preferredNetworks.iterator();

                String regex;
                String hostAddress;
                do {
                    if (!var3.hasNext()) {
                        log.trace("Ignoring address: " + address.getHostAddress());
                        return false;
                    }

                    regex = (String) var3.next();
                    hostAddress = address.getHostAddress();
                } while (!hostAddress.matches(regex) && !hostAddress.startsWith(regex));

                return true;
            }
        }
    }

    boolean ignoreInterface(String interfaceName) {
        Iterator var2 = this.properties.getIgnoredInterfaces().iterator();

        String regex;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            regex = (String) var2.next();
        } while (!interfaceName.matches(regex));

        log.trace("Ignoring interface: " + interfaceName);
        return true;
    }

    public InetUtils.HostInfo convertAddress(final InetAddress address) {
        InetUtils.HostInfo hostInfo = new InetUtils.HostInfo();
        address.getClass();
        Future result = ExecutorTool.Instance().submit(address::getHostName);

        String hostname;
        try {
            hostname = (String) result.get(this.properties.getTimeoutSeconds(), TimeUnit.SECONDS);
        } catch (Exception var6) {
            log.info("Cannot determine local hostname");
            hostname = "localhost";
        }

        hostInfo.setHostname(hostname);
        hostInfo.setIpAddress(address.getHostAddress());
        return hostInfo;
    }

    @Data
    public static class HostInfo {
        public boolean override;
        private String ipAddress;
        private String hostname;

        public HostInfo(String hostname) {
            this.hostname = hostname;
        }

        public HostInfo() {
        }

        public int getIpAddressAsInt() {
            InetAddress inetAddress = null;
            String host = this.ipAddress;
            if (host == null) {
                host = this.hostname;
            }

            try {
                inetAddress = InetAddress.getByName(host);
            } catch (UnknownHostException var4) {
                throw new IllegalArgumentException(var4);
            }

            return ByteBuffer.wrap(inetAddress.getAddress()).getInt();
        }

    }
}

