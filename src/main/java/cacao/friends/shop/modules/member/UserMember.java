package cacao.friends.shop.modules.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserMember extends User {
	
	private final Member member;
	
	public UserMember(Member member) {
		super(member.getUsername(), member.getPassword(), memberAuthorities());
		this.member = member;
	}

	public static List<SimpleGrantedAuthority> memberAuthorities() {
		List<SimpleGrantedAuthority>  authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}
