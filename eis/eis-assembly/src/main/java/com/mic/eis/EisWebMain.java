package com.mic.eis;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author calisto
 * @date 2020-07-01 2:14 下午
 */

@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.mic.eis.mapper")
@SpringBootApplication
@EnableSwagger2
@Import(FdfsClientConfig.class)
public class EisWebMain {

    public static void main(String[] args) {
        SpringApplication.run(EisWebMain.class, args);
    }

}
