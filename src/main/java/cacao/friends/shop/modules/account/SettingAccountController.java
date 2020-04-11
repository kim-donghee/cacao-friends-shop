package cacao.friends.shop.modules.account;

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

import cacao.friends.shop.modules.account.form.NotificationsForm;
import cacao.friends.shop.modules.account.form.PasswordForm;
import cacao.friends.shop.modules.account.validator.PasswordFormValidator;
import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.characterKind.CharacterKindRepository;
import cacao.friends.shop.modules.main.CurrentAccount;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/account/settings")
@RequiredArgsConstructor
public class SettingAccountController {
	
	private final AccountRepository accountRepository;
	
	private final AccountService accountService;
	
	private final PasswordFormValidator passwordFormValidator;
	
	private final CharacterKindRepository characterKindRepository;
	
	private final ModelMapper modelMapper;
	
	@InitBinder("passwordForm")
	public void passwordForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(passwordFormValidator);
	}
	
	@GetMapping("/address")
	public String updateAddressForm(@CurrentAccount Account account, Model model) {
		AddressForm addressForm = new AddressForm();
		if(account.getAddress() != null)
			modelMapper.map(account.getAddress(), addressForm);
		model.addAttribute(addressForm);
		return "account/settings/address";
	}
	
	@PostMapping("/address")
	public String updateAddressSubmit(@CurrentAccount Account account, @Valid AddressForm addressForm, 
			Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "account/settings/address";
		}
		
		accountService.updateAddress(account, addressForm);
		
		attributes.addFlashAttribute("message", "주소를 수정했습니다.");
		
		return "redirect:/account/settings/address";
	}
	
	@GetMapping("/password")
	public String updatePasswordform(@CurrentAccount Account account, Model model) {
		model.addAttribute(new PasswordForm());
		return "account/settings/password";
	}
	
	@PostMapping("/password")
	public String updatePasswordSubmit(@CurrentAccount Account account, @Valid PasswordForm passwordForm, 
			Errors errors, Model model, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "account/settings/password";
		}
		
		accountService.updatePassword(account, passwordForm.getNewPassword());
		
		attributes.addFlashAttribute("message", "패스워드를 수정했습니다.");
		
		return "redirect:/account/settings/password";
	}
	
	@GetMapping("/notifications")
	public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
		model.addAttribute(modelMapper.map(account, NotificationsForm.class));
		model.addAttribute("tagList", characterKindRepository.findAll());
		return "account/settings/notifications";
	}
	
	// TODO 캐릭터 저장기능 구현 후에 마무리...
	@PostMapping("/notifications")
	public String updateNotificationsSubmit(@CurrentAccount Account account, 
			@Valid NotificationsForm notificationsForm, Errors errors, 
			Model model, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "account/settings/notifications";
		}
		
		CharacterKind tag = characterKindRepository.findByName(notificationsForm.getTagName());
		
		accountService.updateNotifications(account, tag, notificationsForm);
		
		attributes.addFlashAttribute("message", "알림을 수정했습니다.");
		
		return "account/settings/notifications";
	}
	

}
