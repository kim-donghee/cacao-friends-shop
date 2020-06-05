package cacao.friends.shop.modules.main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cacao.friends.shop.modules.characterKind.CharacterKindRepository;
import cacao.friends.shop.modules.characterKind.CharacterOrderSaleDto;
import cacao.friends.shop.modules.item.ItemRepository;
import cacao.friends.shop.modules.item.search.ItemCondition;
import cacao.friends.shop.modules.item.search.ItemStatus;
import cacao.friends.shop.modules.order.OrdersCondition;
import cacao.friends.shop.modules.order.OrdersRepository;
import cacao.friends.shop.modules.order.form.OrdersSearchForm;
import cacao.friends.shop.modules.question.QuestionRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final ItemRepository itemRepository;
	
	private final CharacterKindRepository characterKindRepository;
	
	private final OrdersRepository ordersRepository;
	
	private final QuestionRepository questionRepository;
	
	private final ObjectMapper objectMapper;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("itemNew6", 
				itemRepository.findTop6ByPublishedAndPausedAndClosedOrderByPublishedDateTimeDesc(true, false, false));
		model.addAttribute("itemPopular6", itemRepository.findPopular());
		return "member/index";
	}
	
	@GetMapping("/manager")
	public String managerHome(Model model) throws JsonProcessingException {
		LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
		LocalDateTime lastWeekMonday = now.minusWeeks(1).with(DayOfWeek.MONDAY);
		LocalDateTime lastWeekSunday = now.minusWeeks(1).with(DayOfWeek.SUNDAY);
		
		OrdersCondition orderCondition = new OrdersCondition(new OrdersSearchForm(null, "READY"));
		ItemCondition itemCondition = new ItemCondition();
		itemCondition.settingItemStatus(ItemStatus.ALL);
		
		List<CharacterOrderSaleDto> characterLastWeekOrderSaleList = 
				characterKindRepository.findLastWeekOrderSale(lastWeekMonday, lastWeekSunday);
		String lastWeekSaleList = objectMapper.writeValueAsString(characterLastWeekOrderSaleList);
		
		List<CharacterOrderSaleDto> characterOrderSaleList = characterKindRepository.findOrderSale();
		String saleList = objectMapper.writeValueAsString(characterOrderSaleList);
		
		model.addAttribute("now", now);
		model.addAttribute("lastWeekMonday", lastWeekMonday);
		model.addAttribute("lastWeekSunday", lastWeekSunday);
		model.addAttribute("lastWeekSaleList", lastWeekSaleList);
		model.addAttribute("saleList", saleList);
		model.addAttribute("newOrderCount", ordersRepository.countByCondition(orderCondition));
		model.addAttribute("draftItemCount", itemRepository.countByCondition(itemCondition));
		model.addAttribute("newQuestionCount", questionRepository.countByAnswered(false));
		return "manager/index";
	}
	
	@GetMapping("/manager/login")
	public String login() {
		return "manager/login";
	}

}
