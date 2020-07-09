package cacao.friends.shop.modules.member.controller;

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

import cacao.friends.shop.modules.member.CurrentMember;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.MemberRepository;
import cacao.friends.shop.modules.member.MemberService;
import cacao.friends.shop.modules.member.form.JoinForm;
import cacao.friends.shop.modules.member.validator.JoinFormValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberRepository memberRepository;
	
	private final MemberService memberService;
	
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
	
	// 회원 가입 처리 -> 로그인
	@PostMapping("/join")
	public String joinSubmit(@Valid JoinForm joinForm, Errors errors) {
		if(errors.hasErrors()) {
			return "member/account/join";
		}
		Member newMember = memberService.joinProcess(joinForm);
		memberService.login(newMember);
		return "redirect:/";
	}
	
	// 이메일 가입 토큰 재전송
	@GetMapping("/resend-join-confirm-email")
	public String resendJoinConfirmEmail(@CurrentMember Member member) {
		memberService.sendJoinConfirmEmail(member);
		return "redirect:/member/settings/profile";
	}
	
	// 이메일 인증 처리 -> 로그인
	@GetMapping("/check-email-token")
	public String checkEmailToken(String email, String token, Model model) {
		Member member = memberRepository.findByEmail(email);
		if(!memberService.validToken(member, token)) {
			model.addAttribute("error", "이메일 확인 링크가 정확하지 않습니다.");
			return "member/account/check-email-token";
		}
		memberService.completeJoin(member);
		memberService.login(member);
		model.addAttribute("numberOfUser", memberRepository.countByEmailVerified(true));
		model.addAttribute("username", member.getUsername());
		return "member/account/check-email-token";
	}
	
	@GetMapping("/email-login")
	public String emailLoginForm() {
		return "member/account/email-login";
	}
	
	// 이메일에 로그인 토큰 전송
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
	
	// 토큰 정보 확인후 로그인
	@GetMapping("/login-by-email")
	public String loginByEmail(String email, String token, Model model) {
		Member member = memberRepository.findByEmail(email);
		if(!memberService.validToken(member, token)) {
			model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
			return "member/account/logged-in-by-email";
		}
		memberService.login(member);
		return "member/account/logged-in-by-email";
	}

}
