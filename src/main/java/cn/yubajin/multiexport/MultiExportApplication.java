package cn.yubajin.multiexport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiExportApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiExportApplication.class, args);
        System.out.println("http://localhost:10088/swagger-ui.html#/");
    }

}
