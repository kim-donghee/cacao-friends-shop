package cacao.friends.shop.modules.member.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.characterKind.repository.CharacterKindRepository;
import cacao.friends.shop.modules.member.CurrentMember;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.MemberService;
import cacao.friends.shop.modules.member.form.NotificationsForm;
import cacao.friends.shop.modules.member.form.PasswordForm;
import cacao.friends.shop.modules.member.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member/settings")
@RequiredArgsConstructor
public class SettingMemberController {
	
	private final MemberService memberService;
	
	private final PasswordFormValidator passwordFormValidator;
	
	private final CharacterKindRepository characterKindRepository;
	
	private final ModelMapper modelMapper;
	
	@InitBinder("passwordForm")
	public void passwordForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(passwordFormValidator);
	}
	
	@GetMapping("/profile")
	public String email(@CurrentMember Member member, Model model) {
		model.addAttribute(member);
		return "member/account/settings/profile";
	}
	
	@GetMapping("/address")
	public String updateAddressForm(@CurrentMember Member member, Model model) {
		AddressForm addressForm = new AddressForm();
		if(member.getAddress() != null)
			modelMapper.map(member.getAddress(), addressForm);
		model.addAttribute(addressForm);
		return "member/account/settings/address";
	}
	
	@PostMapping("/address")
	public String updateAddressSubmit(@CurrentMember Member member, @Valid AddressForm addressForm, 
			Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "member/account/settings/address";
		}
		memberService.updateAddress(member, addressForm);
		attributes.addFlashAttribute("message", "주소를 수정했습니다.");
		return "redirect:/member/settings/address";
	}
	
	@GetMapping("/password")
	public String updatePasswordform(@CurrentMember Member member, Model model) {
		model.addAttribute(new PasswordForm());
		return "member/account/settings/password";
	}
	
	@PostMapping("/password")
	public String updatePasswordSubmit(@CurrentMember Member member, @Valid PasswordForm passwordForm, 
			Errors errors, Model model, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "member/account/settings/password";
		}
		memberService.updatePassword(member, passwordForm.getNewPassword());
		attributes.addFlashAttribute("message", "패스워드를 수정했습니다.");
		return "redirect:/member/settings/password";
	}
	
	@GetMapping("/notifications")
	public String updateNotificationsForm(@CurrentMember Member member, Model model) {
		model.addAttribute(member);
		model.addAttribute(modelMapper.map(member, NotificationsForm.class));
		model.addAttribute("characterList", characterKindRepository.findAll());
		return "member/account/settings/notifications";
	}
	
	@PostMapping("/notifications")
	public String updateNotificationsSubmit(@CurrentMember Member member, 
			@Valid NotificationsForm notificationsForm, Errors errors, 
			Model model, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			model.addAttribute(member);
			model.addAttribute("characterList", characterKindRepository.findAll());
			return "member/account/settings/notifications";
		}
		CharacterKind character = characterKindRepository.findById(notificationsForm.getCharacterId())
				.orElseThrow(() -> new IllegalArgumentException("해당하는 캐릭터가 존재하지 않습니다."));
		memberService.updateNotifications(member, character, notificationsForm);
		attributes.addFlashAttribute("message", "알림을 수정했습니다.");
		return "redirect:/member/settings/notifications";
	}

}
