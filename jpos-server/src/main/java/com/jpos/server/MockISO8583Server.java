package com.jpos.server;

import java.io.IOException;

import com.jpos.common.utils.Packager;
import org.jpos.iso.*;

import org.jpos.iso.channel.ASCIIChannel;

import org.jpos.iso.packager.GenericPackager;

public class MockISO8583Server implements ISORequestListener {

    public static void main(String[] args) throws ISOException {
        String hostname = "localhost";
        int portNumber = 5000;
        //127.0.0.1
//		ISOPackager packager = new GenericPackager("CustomConfig.xml");
        ServerChannel channel = new ASCIIChannel(hostname, portNumber, new Packager());

        ISOServer server = new ISOServer(portNumber, channel, null);

        server.addISORequestListener(new MockISO8583Server());

        System.out.println("ISO8583 server started...");
        new Thread(server).start();

        ISOUtil.sleep(10 * 1000);

    }

    public boolean process(ISOSource isoSrc, ISOMsg isoMsg) {
        try {
            System.out.println("ISO8583 incoming message on host ["
                    + ((BaseChannel) isoSrc).getSocket().getInetAddress()
                    .getHostAddress() + "]");
            receiveMessage(isoSrc, isoMsg);
            logISOMsg(isoMsg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private void receiveMessage(ISOSource isoSrc, ISOMsg isoMsg)
            throws ISOException, IOException {
        System.out.println("ISO8583 Message received...");
//		ISOMsg reply = (ISOMsg) isoMsg.clone();
        ISOMsg reply = new ISOMsg();
        reply.setMTI("0219");
        reply.set(39, "00");

        isoSrc.send(reply);
    }

    private static void logISOMsg(ISOMsg msg) {
        System.out.println("----ISO MESSAGE-----");
        try {
            System.out.println("  MTI : " + msg.getMTI());
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    System.out.println("    Field-" + i + " : "
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