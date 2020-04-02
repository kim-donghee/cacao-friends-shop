package cacao.friends.shop.modules.account;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.account.form.JoinForm;
import cacao.friends.shop.modules.account.validator.JoinFormValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
	
	private final AccountService accountService;
	
	private final AccountRepository accountRepository;
	
	private final JoinFormValidator joinFormValidator;
	
	@InitBinder("joinForm")
	public void joinForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(joinFormValidator);
	}
	
	@GetMapping("/login")
	public String login() {
		return "account/login";
	}
	
	@GetMapping("/join")
	public String joinForm(Model model) {
		model.addAttribute(new JoinForm());
		return "account/join";
	}
	
	/**
	 * 회원 가입 -> 로그인
	 */
	@PostMapping("/join")
	public String joinSubmit(@Valid JoinForm joinForm, Errors errors) {
		if(errors.hasErrors()) {
			return "account/join";
		}
		
		Account account = accountService.saveNewAccount(joinForm);
		accountService.login(account);
		
		return "redirect:/";
	}
	
	/**
	 * 이메일, 토큰 정보를 확인 후 이메일 인증 계정으로 수정과 로그인
	 */
	@GetMapping("/check-email-token")
	public String checkEmailToken(String token, String email, Model model) {
		Account account = accountRepository.findByEmail(email);
		String view = "account/check-email-token";
		
		if(account == null || !account.isValidToken(token)) {
			model.addAttribute("error", "이메일 확인 링크가 정확하지 않습니다.");
			return view;
		}
		
		accountService.complateJoin(account);
		accountService.login(account);
		
		model.addAttribute("numberOfUser", accountRepository.countByEmailVerified(true));
		model.addAttribute("username", account.getUsername());
		return view;
	}
	
	@GetMapping("/email-login")
	public String emailLoginForm() {
		return "account/email-login";
	}
	
	/**
	 * 회원가입시에 입력한 이메일에 토큰 정보를 전송
	 */
	@PostMapping("/email-login")
	public String emailLoginSubmit(String email, Model model, RedirectAttributes attributes) {
		Account account = accountRepository.findByEmail(email);
		
		if(account == null) {
			model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
			return "account/email-login";
		}
		
		accountService.sendLoginLink(account);
		attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다.");
		
		return "redirect:/account/email-login";
	}
	
	
	/**
	 * 이메일과 토큰 정보를 확인 후에 로그인
	 */
	@GetMapping("/login-by-email")
	public String loginByEmail(String token, String email, Model model) {
		Account account = accountRepository.findByEmail(email);
		String view = "account/logged-in-by-email";
		
		if(account == null || !account.isValidToken(token)) {
			model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
			return view;
		}
		
		accountService.login(account);
		return view;
	}

}
