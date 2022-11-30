package com.jpos.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.jpos.common.utils.Packager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;

public class SampleRequest {

    public static void main(String[] args) throws IOException, ISOException {
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

        ASCIIChannel c = new ASCIIChannel("localhost", 5000, new Packager());
        logISOMsg(isoMsg);
        System.out.println(ISOUtil.hexdump(isoMsg.pack()));
        c.connect();
        c.send(isoMsg);
        ISOMsg response = c.receive();
        System.out.println("****************Response *********************");
        logISOMsg(response);
// Get and print the output result

    }

    private static void logISOMsg(ISOMsg msg) {
        System.out.println("----ISO MESSAGE-----");
        try {
            System.out.println(" MTI : " + msg.getMTI());
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    System.out.println(" Field-" + i + " : "
                            + msg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--------------------");
        }
    }
}