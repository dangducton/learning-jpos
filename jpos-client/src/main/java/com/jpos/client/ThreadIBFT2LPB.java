package com.jpos.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.jpos.common.utils.Packager;
import com.jpos.common.utils.SoketASCIIChannel;
import org.apache.logging.log4j.LogManager;
import org.jpos.iso.ISOMsg;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;

public class ThreadIBFT2LPB extends Thread {
    private static final Logger logger = LogManager.getLogger(MainTest.class);
    private String host;
    private String port;

    public ThreadIBFT2LPB(String host, String port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            SoketASCIIChannel socket = new SoketASCIIChannel();
            ASCIIChannel channel = socket.getSocket(host, port);
            while (true) {
                try {
                    if (!channel.isConnected()) {
                        channel.reconnect();
                        ISOUtil.sleep(10 * 1000);
                    } else {
                        ISOMsg receive = channel.receive();

                        if (receive.getMTI().equals("0800")) {
                            channel.send(gen0810(receive));
                        } else if (receive.getMTI().equals("0810")) {
                        } else {
                            ThreadCallLPBASCIIChannel thread = new ThreadCallLPBASCIIChannel(
                                    channel, host, receive);
                            thread.start();
                            Thread.sleep(50);
                        }
                    }

                } catch (Exception e) {
                    logger.info("Exception: " + e.toString(), "INFO");
                    // channel = socket.getSocket(host, port);
                }

            }

        } catch (Exception e) {
            logger.info("Exception: " + e.toString());
        }
    }

    public ISOMsg gen0810(ISOMsg isoMsg0800) throws Exception {
        ISOMsg isoMsg = isoMsg0800;
        isoMsg.setMTI("0810");
        isoMsg.set(39, "00");
        isoMsg.setPackager(new Packager());
        return isoMsg;
    }

    public ISOMsg gen0800() throws Exception {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0800");
        Calendar calendar = Calendar.getInstance();
        TimeZone time_zone = TimeZone.getTimeZone("GMT");
        java.util.Date date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("MMddHHmmss");// F07_Transmission_Date_Time
        format1.setTimeZone(time_zone);
        String F07 = format1.format(date);
        SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
        String F11 = format2.format(date);
        isoMsg.set(7, F07);
        isoMsg.set(11, F11);
        isoMsg.set(32, "970449");
        isoMsg.set(70, "301");

        isoMsg.setPackager(new Packager());
        return isoMsg;
    }
}
