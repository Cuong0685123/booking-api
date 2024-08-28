/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tc.booking.api;

import com.tc.booking.api.exception.ApiException;
import com.tc.booking.api.service.UserApiSvc;
import com.tc.booking.model.entity.User;
import com.tc.booking.repo.UserRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author binh
 */
@Component
@Slf4j
public class AppListener {
  
  @EventListener(ContextRefreshedEvent.class)
  public void onContextRefresh(ContextRefreshedEvent event) {
    log.info("Application context refreshed");
    ApplicationContext ctx = event.getApplicationContext();
    initApp(ctx);
  }
  
  private void initApp(ApplicationContext ctx) {
    try {
      JwtHelper jwtHelper = ctx.getBean(JwtHelper.class);
      jwtHelper.loadKeyStore();
      
      UserRepository userRepo = ctx.getBean(UserRepository.class);
      UserApiSvc userApiSvc = ctx.getBean(UserApiSvc.class);
      
      User user = userRepo.findByLogin("admin")
          .orElse(null);
      if (user != null) {
        return;
      }
      
      user = new User();
      user.setLogin("admin");
      user.setPassword("secret");
      user.setActive(true);
      user.setCreatedAt(new Date());
      user.setCreatedBy("__system__");
      userApiSvc.addUser(user);
      
      log.info("Added admin user with default password");
    } catch (ApiException ex) {
      log.error("Failed to initialize application", ex);
    }
  }
}
