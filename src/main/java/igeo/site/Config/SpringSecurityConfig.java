 package igeo.site.Config;

 import igeo.site.Game.MissionTracker;
 import igeo.site.Game.RoomTracker;
 import igeo.site.Provider.CustomAuthenticationProvider;
 import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.http.HttpMethod;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.AuthenticationProvider;
 import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
 import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
 import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
 import org.springframework.web.cors.CorsConfiguration;
 import org.springframework.web.cors.CorsConfigurationSource;
 import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

 import java.util.Arrays;
 import java.util.Collections;
 import java.util.List;


 @Configuration
 @EnableWebSecurity
 @EnableMethodSecurity(prePostEnabled = true)

 public class SpringSecurityConfig {

     CorsConfigurationSource corsConfigurationSource() {
         CorsConfiguration configuration = new CorsConfiguration();

         configuration.addAllowedOrigin("*"); // 모든 origin 허용 (실제 운영 환경에서는 적절한 값을 설정하세요)
         configuration.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
         configuration.addAllowedHeader("*"); // 모든 헤더 허용
         configuration.setAllowCredentials(true); // 인증 정보 허용

         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**", configuration);

         return source;

     }

     @Bean
     public AuthenticationProvider authenticationProvider() {
         return new CustomAuthenticationProvider();
     }
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                 .csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(authz -> authz
                         .requestMatchers(new AntPathRequestMatcher("/user/check_authentication")).authenticated()
                         .anyRequest().permitAll()
                 )
                 // ... 다른 설정 ...
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
