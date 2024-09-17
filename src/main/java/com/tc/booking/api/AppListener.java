package com.tc.booking.api;

import com.tc.booking.api.exception.ApiException;
import com.tc.booking.api.service.AccountApiSvc;
import com.tc.booking.model.entity.Account;
import com.tc.booking.repo.AccountDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for the application context refresh event to perform initialization tasks.
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

      AccountDAO accountDAO = ctx.getBean(AccountDAO.class); // Use AccountDAO
      AccountApiSvc accountApiSvc = ctx.getBean(AccountApiSvc.class); // Use AccountApiSvc

      // Check if the admin account already exists
      Account account = accountDAO.findByUsername("admin")
          .orElse(null);

      if (account != null) {
        log.info("Admin account already exists.");
        return;
      }

      // Create a new admin account
      account = new Account();
      account.setUsername("admin");
      // Use a default password, it will be encoded by the service
      String rawPassword = "secret";
      
      // Encode the password
      String encodedPassword = accountApiSvc.encodePassword(rawPassword);
      account.setPassword(encodedPassword);
      
      // Save the new admin account
      accountApiSvc.saveAccount(account);

      log.info("Added admin account with default password");
    } catch (ApiException ex) {
      log.error("Failed to initialize application", ex);
    }
  }
}
