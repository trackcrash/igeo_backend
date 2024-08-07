 package igeo.site.Config;

 import igeo.site.Auth.CustomOAuth2UserService;
 import igeo.site.Auth.JwtRequestFilter;
 import igeo.site.Game.MissionTracker;
 import igeo.site.Game.RoomTracker;
 import igeo.site.Handler.CustomLogoutSuccessHandler;
 import igeo.site.Handler.CustomOauth2SuccessHandler;
 import igeo.site.Provider.CustomAuthenticationProvider;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.AuthenticationProvider;
 import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
 import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
 import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
 import org.springframework.security.config.http.SessionCreationPolicy;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

 import java.util.Collections;


 @Configuration
 @EnableWebSecurity
 @EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
 public class SpringSecurityConfig {
    //JWT 필터 추가
     private final JwtRequestFilter jwtRequestFilter;
     private final CustomOAuth2UserService customOAuth2UserService;
     private final CustomOauth2SuccessHandler oAuth2AuthenticationSuccessHandler;
     //캬루 수정
     //로그인 페이지 설정
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 .csrf(AbstractHttpConfigurer::disable)
                 .httpBasic(AbstractHttpConfigurer::disable)
                 .formLogin(AbstractHttpConfigurer::disable)
                 .sessionManagement((sessionManagement) -> sessionManagement
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests((authorize) -> authorize
                         /*.requestMatchers("/user/register", "/", "user/login","oauth2/**").permitAll()*/
                         .requestMatchers("/**").permitAll()
//                         .requestMatchers("/admin").hasRole("ROLE_ADMIN")
                         .anyRequest().permitAll())
//				.formLogin(formLogin -> formLogin
//						.loginPage("/login")
//						.defaultSuccessUrl("/home"))
                 .oauth2Login(oauth2Login->
                         oauth2Login
                                 .loginPage("/login/google")
                                 .defaultSuccessUrl("/login-success")
                                 .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                         .userService(customOAuth2UserService)
                                 )
                                 .successHandler(oAuth2AuthenticationSuccessHandler)
                 )
                 .logout((logout) -> logout
                                 .logoutUrl("/user/logout")
                                 .logoutSuccessHandler(customLogoutSuccessHandler())
                                 .invalidateHttpSession(true)
                 );
         http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
         return http.build();
     }


     @Bean
     public LogoutSuccessHandler customLogoutSuccessHandler() {
      return new CustomLogoutSuccessHandler();
     }

     @Bean
     public AuthenticationProvider authenticationProvider() {
         return new CustomAuthenticationProvider();
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
