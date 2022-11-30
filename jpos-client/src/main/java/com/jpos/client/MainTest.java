package com.jpos.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class MainTest {
    private static final Logger logger = LogManager.getLogger(MainTest.class);

    public static void main(String[] args) {
        try {
            ThreadIBFT2LPB thread = new ThreadIBFT2LPB("localhost", "5000");
            thread.start();
            Thread.sleep(3000);
            logger.info("test");
        } catch (Exception e) {
            logger.info("Exception: " + e.toString());
        }
    }
}