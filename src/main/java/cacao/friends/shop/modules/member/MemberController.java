package cacao.friends.shop.modules.member;

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

import cacao.friends.shop.modules.member.form.JoinForm;
import cacao.friends.shop.modules.member.validator.JoinFormValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	private final MemberRepository memberRepository;
	
	private final JoinFormValidator joinFormValidator;
	
	@InitBinder("joinForm")
	public void joinForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(joinFormValidator);
	}
	
	@GetMapping("/login")
	public String login() {
		return "member/account/login";
	}
	
	@GetMapping("/join")
	public String joinForm(Model model) {
		model.addAttribute(new JoinForm());
		return "member/account/join";
	}
	
	/**
	 * 회원 가입 -> 로그인
	 */
	@PostMapping("/join")
	public String joinSubmit(@Valid JoinForm joinForm, Errors errors) {
		if(errors.hasErrors()) {
			return "member/account/join";
		}
		Member member = memberService.saveNewAccount(joinForm);
		memberService.login(member);
		return "redirect:/";
	}
	
	/**
	 * 이메일, 토큰 정보를 확인 후 이메일 인증 계정으로 수정과 로그인
	 */
	@GetMapping("/check-email-token")
	public String checkEmailToken(String token, String email, Model model) {
		Member member = memberRepository.findByEmail(email);
		String view = "member/account/check-email-token";
		
		if(member == null || !member.isValidToken(token)) {
			model.addAttribute("error", "이메일 확인 링크가 정확하지 않습니다.");
			return view;
		}
		
		memberService.completeJoin(member);
		memberService.login(member);
		model.addAttribute("numberOfUser", memberRepository.countByEmailVerified(true));
		model.addAttribute("username", member.getUsername());
		return view;
	}
	
	@GetMapping("/email-login")
	public String emailLoginForm() {
		return "member/account/email-login";
	}
	
	/**
	 * 회원가입시에 입력한 이메일에 토큰 정보를 전송
	 */
	@PostMapping("/email-login")
	public String emailLoginSubmit(String email, Model model, RedirectAttributes attributes) {
		Member member = memberRepository.findByEmail(email);
		
		if(member == null) {
			model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
			return "member/account/email-login";
		}
		memberService.sendLoginLink(member);
		attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다.");
		return "redirect:/member/email-login";
	}
	
	
	/**
	 * 이메일과 토큰 정보를 확인 후에 로그인
	 */
	@GetMapping("/login-by-email")
	public String loginByEmail(String token, String email, Model model) {
		Member member = memberRepository.findByEmail(email);
		String view = "member/account/logged-in-by-email";
		
		if(member == null || !member.isValidToken(token)) {
			model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
			return view;
		}
		memberService.login(member);
		return view;
	}

}
