package cn.guet.minichatadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.guet.minichatadmin.mapper")
public class MiniChatAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniChatAdminApplication.class, args);
    }

}
