/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author binh
 */
@EnableScheduling
@EnableJpaRepositories(basePackages = {"com.tc.booking.repo"})
@EntityScan(basePackages = {"com.tc.booking.model"})
@Configuration
public class BookingBootConfig {
  
  private static final String dateFormat = "yyyy-MM-dd";
  private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

  /**
   *
   * @return
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      builder.simpleDateFormat(dateTimeFormat);
      builder.serializers(new LocalDateSerializer(
          DateTimeFormatter.ofPattern(dateFormat)));
      builder.serializers(new LocalDateTimeSerializer(
          DateTimeFormatter.ofPattern(dateTimeFormat)));
    };
  }

  /**
   *
   * @return
   */
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource
        = new ReloadableResourceBundleMessageSource();
    // messageSource.setBasename("classpath:messages");
    messageSource.setBasenames(
        "classpath:com/tc/booking/api/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  /**
   *
   * @return
   */
  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
    exec.setCorePoolSize(100);
    exec.setThreadNamePrefix("task-");
    return exec;
  }

  /**
   *
   * @return
   */
  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(100);
    taskScheduler.setThreadNamePrefix("sch-");
    return taskScheduler;
  }
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
