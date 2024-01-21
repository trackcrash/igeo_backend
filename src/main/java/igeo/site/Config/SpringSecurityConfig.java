 package igeo.site.Config;

 import igeo.site.Game.MissionTracker;
 import igeo.site.Game.RoomTracker;
 import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
 import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
 import org.springframework.web.cors.CorsConfiguration;
 import org.springframework.web.cors.CorsConfigurationSource;

 import java.util.Arrays;
 import java.util.Collections;


 @Configuration
 @EnableWebSecurity
 public class SpringSecurityConfig {

     CorsConfigurationSource corsConfigurationSource() {
         return request -> {
             CorsConfiguration config = new CorsConfiguration();
             config.setAllowedHeaders(Collections.singletonList("*"));
             config.setAllowedMethods(Collections.singletonList("*"));
             config.setAllowedOriginPatterns(Collections.singletonList("*")); // ⭐️ 허용할 origin
             //config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000")); // ⭐️ 허용할 origin
             config.setAllowCredentials(true);
             return config;
         };
     }

     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                 .csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(authz -> authz
                         .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                         .anyRequest().authenticated()
                 )
                 .formLogin(form -> form
                         .loginPage("/user")
                         .permitAll()
                 )
                 .logout(logout -> logout
                         .logoutSuccessUrl("/")
                         .permitAll()
                 );

         return http.build();
     }

     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
         return authenticationConfiguration.getAuthenticationManager();
     }
     @Bean
     public RoomTracker roomTracker() {
         return new RoomTracker();
     }
     @Bean
     public MissionTracker missionTracker() {
         return new MissionTracker();
     }
     @Bean
     public WebSecurityCustomizer webSecurityCustomizer() {
         return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
     }
 }
