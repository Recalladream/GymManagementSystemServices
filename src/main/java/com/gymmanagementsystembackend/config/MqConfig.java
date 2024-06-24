package com.gymmanagementsystembackend.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor(){
            MybatisPlusInterceptor mpInterceptor=new MybatisPlusInterceptor();
            mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
            return mpInterceptor;
        }
}
