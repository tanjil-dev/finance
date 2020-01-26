package finance.progoti;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppConfig {
	@Bean
    public JavaMailSender mailSender() {
		Properties prop = new Properties();
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.smtp.starttls.required", true);
		prop.put("mail.smtp.auth", true);
		
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("tanzil.ovi578@gmail.com");
        javaMailSender.setPassword("hunhlqwhagepictu");
        javaMailSender.setJavaMailProperties(prop);
   
        return javaMailSender;
    }
}
