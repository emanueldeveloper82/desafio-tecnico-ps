package com.ciandt.mensagem_service.feign;

import feign.Feign;
import feign.Target;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuildDynamicFeignClient {

    private final ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;

    @Bean
    public DynamicFeignClient dynamicFeignClient() {
        return Feign.builder()
                .encoder(new SpringEncoder(messageConverters))
                .decoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)))
                .contract(new SpringMvcContract())
                .target(Target.EmptyTarget.create(DynamicFeignClient.class));
    }
}
