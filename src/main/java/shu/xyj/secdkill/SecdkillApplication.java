package shu.xyj.secdkill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("shu.xyj.secdkill.mapper")
public class SecdkillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecdkillApplication.class, args);
    }

}
