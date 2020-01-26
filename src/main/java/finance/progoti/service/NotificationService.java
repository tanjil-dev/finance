package finance.progoti.service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import finance.progoti.storage.StorageService;
import finance.progoti.user.model.Product;
import finance.progoti.user.model.User;

@Service
public class NotificationService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
    private StorageService storageService;
	
//	@Autowired
//	public NotificationService(JavaMailSender javaMailSender) {
//		this.javaMailSender = javaMailSender;
//	}
	
	
	
	public void sendNotification(User user, Product product) throws MailException, Exception {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		helper.addAttachment(product.getImagePath() + ".jpg", storageService.loadAsResource(product.getImagePath()));
		
/*		helper.setTo(user.getEmail());
		helper.setText("<div th:if=\"${product.imagePath != null}\">\r\n" + 
				"				<img style=\"height: 200px; width: 200px;\" alt=\"\"  th:src=\"@{|/image/${product.imagePath}|}\">\r\n" + 
				"				</div>");
		helper.setSubject("Convayence Bill"); */
		
	//	String processedHTMLTemplate = this.constructHTMLTemplate();
		
//	    SimpleMailMessage mail = new SimpleMailMessage();
		final StringBuffer messageBody = new StringBuffer("Bill has been paid.\n");
		messageBody.append("Date: ").append(product.getDate()).append("\n");
		messageBody.append("Purpose: ").append(product.getPurpose()).append("\n");
		messageBody.append("From: ").append(product.getBrand()).append("\n");
		messageBody.append("To: ").append(product.getMadein()).append("\n");
		messageBody.append("Mode: ").append(product.getMode()).append("\n");
		messageBody.append("Amount: ").append(product.getPrice()).append("\n");
		messageBody.append("Note: ").append(product.getNote()).append("\n");
		messageBody.append("Submitted By: ").append(product.getSubmittedby()).append("\n");
		messageBody.append("ApprovedBy: ").append(product.getApprovedby()).append("\n");
		messageBody.append("Paid By: ").append(product.getPaidby()).append("\n");
		messageBody.append("Image: ");
		helper.setTo(product.getSubmittedby());
		helper.setCc(user.getEmail());
		helper.setSubject("Convayence Bill");
		helper.setText(messageBody.toString());
		javaMailSender.send(message);
		
		/* MimeMessagePreparator preparator = message -> {
             MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
             //helper.setFrom("Sample <sample@example.com>");
             helper.setTo(product.getSubmittedby());
             helper.setSubject("Convayence Bill");
             helper.setText(processedHTMLTemplate, true);
        };

        javaMailSender.send(preparator);
		*/
		//mailSender.setHost(this.environment.getProperty(HOST));
        //mailSender.setPort(Integer.parseInt(this.environment.getProperty(PORT)));
        //mailSender.setProtocol(this.environment.getProperty(PROTOCOL));
        //mail.setUsername(user.setEmail("tanzil.ovi578@gmail.com"));
        //mail.setPassword(user.setPassword(""));
		
	}
	
	/*private String constructHTMLTemplate() {
        Context context = new Context();
        context.getVariable("product.price");
        return templateEngine.process("admin", context);
    } */
	
}
