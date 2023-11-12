package com.publicrelay.nrtest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author roman
 * @since 11/10/23
 */
@Controller
public class TestController {

    /**
     * logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/test")
    public void test() {
        log.info("test");
    }
}
