package cacao.friends.shop.modules.account;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.MemberRepository;
import cacao.friends.shop.modules.member.MemberService;
import cacao.friends.shop.modules.member.form.JoinForm;

@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class AccountServiceTest {
	
	@Autowired MemberService memberService;
	
	@Autowired MemberRepository memberRepository;
	
	@PersistenceContext EntityManager em;
	
	@Test
	void 저장() {
		JoinForm joinForm = new JoinForm();
		joinForm.setUsername("dong");
		joinForm.setPassword("12345678");
		joinForm.setEmail("dong@dong.com");
		
		Member newAccount = memberService.saveNewAccount(joinForm);
		
		memberService.completeJoin(newAccount);
		
		assertNotNull(newAccount.getEmailCheckToken());
		assertTrue(newAccount.isEmailVerified());
	}
	
	@Test
	void 저장_주소수정() {
		JoinForm joinForm = new JoinForm();
		joinForm.setUsername("dong");
		joinForm.setPassword("12345678");
		joinForm.setEmail("dong@dong.com");
		
		Member newAccount = memberService.saveNewAccount(joinForm);
		
		em.flush();
		em.clear();
		
		AddressForm addressForm = new AddressForm();
		addressForm.setCity("city");
		addressForm.setStreet("street");
		addressForm.setZipcode("zipcode");
		addressForm.setEtc("etc");
		
		memberService.updateAddress(newAccount, addressForm);
		
		em.flush();
		em.clear();
		
		Member findAccount = memberRepository.findById(1L).get();
		
		assertNotNull(findAccount.getAddress());
	}

}
