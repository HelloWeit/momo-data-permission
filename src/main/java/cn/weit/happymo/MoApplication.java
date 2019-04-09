package cn.weit.happymo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author weitong
 */
@SpringBootApplication
@EnableTransactionManagement
public class MoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoApplication.class, args);

    }
}
