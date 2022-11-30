package com.jpos.client;

import com.jpos.common.utils.Packager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.ASCIIChannel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ThreadCallLPBASCIIChannel extends Thread {
    private static final Logger logger = LogManager.getLogger(ThreadCallLPBASCIIChannel.class);
    private ASCIIChannel channel;
    private String ipSocket;
    private String API_CODE = "ESB_001_12";
    private ISOMsg isoMsgReqNapas;

    public ThreadCallLPBASCIIChannel(ASCIIChannel channel, String ipSocket,
                                     ISOMsg isoMsgReqNapas) {
        this.channel = channel;
        this.ipSocket = ipSocket;
        this.isoMsgReqNapas = isoMsgReqNapas;
    }

    @Override
    public void run() {

        ISOMsg isoMsgReqNapasBase = (ISOMsg) isoMsgReqNapas.clone();
        ISOMsg isoResNapas = new ISOMsg();
        try {
            isoResNapas = caseMACFails(isoMsgReqNapas);
        } catch (Exception e) {
            //Timeout
            logger.info("logger");
        }
    }

    public ISOMsg caseMACFails(ISOMsg isoMsg) {
        ISOMsg isoResNapas = null;
        try {
            isoMsg.setMTI("0800");
            Calendar calendar = Calendar.getInstance();
            TimeZone time_zone = TimeZone.getTimeZone("GMT");
            java.util.Date date = calendar.getTime();
            SimpleDateFormat format1 = new SimpleDateFormat("MMddHHmmss");// F07_Transmission_Date_Time
            format1.setTimeZone(time_zone);
            String F07 = format1.format(date);
            SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
            String F11 = format2.format(date);
            isoResNapas.set(7, F07);
            isoResNapas.set(11, F11);
            isoResNapas.set(32, "970449");
            isoResNapas.set(70, "301");
            isoResNapas.setPackager(new Packager());
            channel.send(isoResNapas);

        } catch (Exception e) {
            logger.info("caseMACFails", isoMsg, e);
        }
        return isoResNapas;

    }
}
