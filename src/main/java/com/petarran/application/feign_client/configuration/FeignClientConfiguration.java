package com.petarran.application.feign_client.configuration;

import com.petarran.application.feign_client.PostFeignClient;
import com.petarran.application.feign_client.UserFeignClient;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientsConfiguration.class)
public class FeignClientConfiguration implements ApplicationContextAware {
    @Value("${base.path}")
    private String basePath;

    private ApplicationContext applicationContext;

    @Bean
    public ListToStringCommaRequestInterceptor listToStringCommaRequestInterceptor() { return new ListToStringCommaRequestInterceptor(); }


    @Bean
    public PostFeignClient firmaFeignClient(){
        return createClient(PostFeignClient.class,"post/");
    }

    @Bean
    public UserFeignClient userFeignClient(){ return createClient(UserFeignClient.class, "user/"); }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private <T> T createClient(Class<T> type, String uri) {
        return Feign.builder()
                .encoder(applicationContext.getBean(Encoder.class))
                .decoder(applicationContext.getBean(Decoder.class))
                .requestInterceptor(applicationContext.getBean(ListToStringCommaRequestInterceptor.class))
                .client(new Client.Default(null, null))
                .target(type, basePath + uri);
    }
}
