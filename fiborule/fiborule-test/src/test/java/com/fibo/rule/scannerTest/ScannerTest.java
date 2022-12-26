package com.fibo.rule.scannerTest;

import com.fibo.rule.core.client.FiboNodeScanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScannerTest.class)
@EnableAutoConfiguration
public class ScannerTest {

    @Test
    public void test() throws IOException {
        Set<Class<?>> classes = FiboNodeScanner.scanPackage("com.fibo.rule.alltest.node");
        System.out.println(classes);
    }

}
