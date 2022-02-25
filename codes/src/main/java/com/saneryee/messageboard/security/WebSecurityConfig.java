/**
 * 
 */

package com.saneryee.messageboard.security;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

import com.saneryee.messageboard.security.jwt.AuthEntryPointJwt;
import com.saneryee.messageboard.security.jwt.AuthTokenFilter;
import com.saneryee.messageboard.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${saneryee.app.jwtSecretKey}")
  private String jwtSecretKey;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  DataSource datasource;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/api/auth/**").permitAll()
      .antMatchers("/api/**").permitAll()
      .antMatchers("/h2-console/**").permitAll() //for h2-console Delete this line if you want to deploy on production evironment
      .anyRequest().authenticated();
    
    // For remmember me  
    http.rememberMe()
      .rememberMeParameter("rememberme")
      .tokenRepository(tokenRepository())
      .tokenValiditySeconds(60 * 60 * 24 * 30)
      .userDetailsService(userDetailsService);

      
    
    http.headers().frameOptions().disable(); // for h2-console Delete this line if you want to deploy on production evironment

    //Set Auth Token Filter Before UsernamePasswordAuthenticationFilter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * Remember Me services
   * @return
   */
  @Bean
  public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
    PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
        "rememberme", userDetailsService, tokenRepository());
    return tokenBasedservice;
  }
  /**
   * Token repository
   * @return  TokenRepository
   */
  @Bean
  public PersistentTokenRepository tokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(datasource);
    // Create table when application starts
    tokenRepository.setCreateTableOnStartup(true);
    return tokenRepository;
  }

  @Bean
  public TokenBasedRememberMeServices rememberMeServices() {
    TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
        jwtSecretKey, userDetailsService);
        rememberMeServices.setCookieName("compassCookie");
        rememberMeServices.setTokenValiditySeconds(60*60*24*30); // 30 days
    return rememberMeServices;
  }


}
