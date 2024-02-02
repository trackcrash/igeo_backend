 package igeo.site.Config;

 import igeo.site.Auth.JwtRequestFilter;
 import igeo.site.Game.MissionTracker;
 import igeo.site.Game.RoomTracker;
 import igeo.site.Provider.CustomAuthenticationProvider;
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
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


 @Configuration
 @EnableWebSecurity
 @EnableMethodSecurity(prePostEnabled = true)

 public class SpringSecurityConfig {
    //JWT 필터 추가
     @Autowired
     private JwtRequestFilter jwtRequestFilter;

     //캬루 수정
     //로그인 페이지 설정
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 .csrf(AbstractHttpConfigurer::disable)
                 .httpBasic(AbstractHttpConfigurer::disable)
                 .formLogin(AbstractHttpConfigurer::disable)
                 .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                 .authorizeHttpRequests((authorize) -> authorize
                         /*.requestMatchers("/user/register", "/", "user/login","oauth2/**").permitAll()*/
                         .requestMatchers("/**").permitAll()
                         .anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.defaultSuccessUrl("/home"))
                 .logout((logout) -> logout
                         .logoutSuccessUrl("/login")
                         .invalidateHttpSession(true)
                 );
         return http.build();
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
