package com.iimmersao.userapplication;

import com.iimmersao.springmimic.SpringMimicApplicationRunner;
import com.iimmersao.springmimic.annotations.ComponentScan;
import com.iimmersao.springmimic.annotations.SpringMimicApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringMimicApplication
@ComponentScan("com.iimmersao.userapplication")public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringMimicApplicationRunner.run(Main.class);
    }
}
