package com.example.myapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // @createdDate, @lastModifiedDate などを有効にする
public class JpaConfig {
    
}
