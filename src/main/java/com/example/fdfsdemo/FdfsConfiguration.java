package com.example.fdfsdemo;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import org.springframework.context.annotation.*;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * 功能描述：FastDFS配置类.
 *
 * @author liuguanghui
 * @since 2019-05-15
 */
@Configuration
// 注解，就可以拥有带有连接池的FastDFS Java客户端了
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FdfsConfiguration {

    /**
     * 单例形式获取fdfs客户端，解决并发下载报错[recv cmd: 0 is not correct, expect cmd: 100]
     */
    @Scope("prototype")
    @Bean
    public DefaultFastFileStorageClient defaultFastFileStorageClient() {
        DefaultFastFileStorageClient defaultFastFileStorageClient = new DefaultFastFileStorageClient();
        return defaultFastFileStorageClient;
    }
}
