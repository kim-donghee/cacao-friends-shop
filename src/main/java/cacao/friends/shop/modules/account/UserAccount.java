package cacao.friends.shop.modules.account;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserAccount extends User {
	
	private final Account account;
	
	public UserAccount(Account account) {
		super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
		this.account = account;
	}

}
