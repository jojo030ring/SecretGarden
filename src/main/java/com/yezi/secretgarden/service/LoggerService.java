package com.yezi.secretgarden.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class LoggerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public void traceLoggerTest(String msg) {
        logger.trace(msg);
    }
    public void infoLoggerTest(String msg) {
        logger.info(msg);
    }
    public void debugLoggerTest(String msg) {
        logger.debug(msg);
    }
    public void warnLoggerTest(String msg) {
        logger.warn(msg);
    }
    public void errorLoggerTest(String msg) {
        logger.error(msg);
    }

}
