package cacao.friends.shop.modules.member;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserMember extends User {
	
	private final Member member;
	
	public UserMember(Member member) {
		super(member.getUsername(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
		this.member = member;
	}

}
