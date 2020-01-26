package finance.progoti;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomLoginSuccessHandler successHandler;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				// URLs matching for access rights
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				
				.antMatchers("/home/**").hasAnyAuthority("SITE_USER")
				.antMatchers("/history").hasAnyAuthority("SITE_USER")
				.antMatchers("/new").hasAnyAuthority("SITE_USER")
				.antMatchers("/edit/**").hasAnyAuthority("SITE_USER")
				.antMatchers("/save").hasAnyAuthority("SITE_USER")
				.antMatchers("/delete/**").hasAnyAuthority("SITE_USER")
				
				.antMatchers("/admin/**").hasAnyAuthority("SUPER_USER")
				.antMatchers("/adminbillhome").hasAnyAuthority("SUPER_USER")
				.antMatchers("/adminBill_history").hasAnyAuthority("SUPER_USER")
				.antMatchers("/createExcel").hasAnyAuthority("SUPER_USER")
				.antMatchers("/add_note/**").hasAnyAuthority("SUPER_USER")
				.antMatchers("/save_note").hasAnyAuthority("SUPER_USER")
				.antMatchers("/historyAdminPay").hasAnyAuthority("SUPER_USER")
				.antMatchers("/new_admin_bill").hasAnyAuthority("SUPER_USER")
				.antMatchers("/saveAdminBill").hasAnyAuthority("SUPER_USER")
				.antMatchers("/pay/**").hasAnyAuthority("SUPER_USER")
				.antMatchers("/cancelpay/**").hasAnyAuthority("SUPER_USER")
				.antMatchers("/editAdminBill/**").hasAnyAuthority("SUPER_USER")
				
				.antMatchers("/admin_home/**").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/createHeadExcel").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/add_headNote/**").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/save_headNote").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_homeBill").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_homeBill_history").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/historyAdmin").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/new_admin_home_bill").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/saveAdmin_homeBill").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/approve/**").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/cancelapprove/**").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/editAdmin_homeBill/**").hasAnyAuthority("ADMIN_USER")
				
				.antMatchers("/super_admin/**").hasAnyAuthority("SUPER_ADMIN")
				.antMatchers("/register").hasAnyAuthority("SUPER_ADMIN")
				
				.anyRequest().authenticated()
				.and()
				// form login
				.csrf().disable().formLogin()
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.successHandler(successHandler)
				.usernameParameter("email")
				.passwordParameter("password")
				.and()
				// logout
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login").and()
				.exceptionHandling()
				.accessDeniedPage("/access-denied");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}

