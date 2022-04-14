package qiwen.com.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "qiwen.com.library.mapper")
public class LibManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibManageApplication.class, args);
    }

}
