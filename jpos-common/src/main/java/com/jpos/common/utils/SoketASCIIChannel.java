package com.jpos.common.utils;

import org.jpos.iso.channel.ASCIIChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoketASCIIChannel {
    private static final Logger logger = LogManager.getLogger(SoketASCIIChannel.class);

    private static ASCIIChannel socketStatic;

    private ASCIIChannel loadSocket(String host, String port) {
        try {
            ASCIIChannel channel = new ASCIIChannel();

            channel.setHost(host);
            channel.setPort(Integer.parseInt(port));
            channel.setPackager(new Packager());
            channel.setTimeout(400*1000);
            channel.connect();
            channel.sendKeepAlive();
            socketStatic = channel;
        } catch (Exception ex) {
            logger.info("Exception SoketASCIIChannelBEN loadSocket: " + ex.toString());
        }
        return socketStatic;
    }

    public ASCIIChannel getSocket(String host, String port) {
        if (socketStatic == null||!socketStatic.isConnected()) {
            socketStatic = null;
            loadSocket(host, port);
        }
        return socketStatic;
    }
}