package finance.progoti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import finance.progoti.repository.UserRepository;
import finance.progoti.user.model.User;

@Configuration
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	@Autowired
	UserRepository userRepository;
	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			return;
		}
		
		String username = authentication.getName();
		User user = userRepository.findByEmail(username);
		request.getSession().setAttribute("user", user);
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	protected String determineTargetUrl(Authentication authentication) {
		String url = "/login?error=true";
		// Fetch the roles from Authentication object
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}

		// check user role and decide the redirect URL
		if (roles.contains("ADMIN_USER")) {
			url = "/admin_home";
		} 
		else if (roles.contains("SITE_USER")) {
			url = "/home";
		}
		else if(roles.contains("SUPER_USER"))
		{
			url = "/admin";
		}
		else if(roles.contains("SUPER_ADMIN"))
		{
			url = "/super_admin";
		}
		
		return url;
	}
	
}
