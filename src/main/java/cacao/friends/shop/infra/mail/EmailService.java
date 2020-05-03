package cacao.friends.shop.infra.mail;

public interface EmailService {
	
	void sendEmail(EmailMessage emailMessage);
	
	String createText(String username, String link, String linkName, String message);

}
