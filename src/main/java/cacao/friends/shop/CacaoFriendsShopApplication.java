package cacao.friends.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;

@SpringBootApplication
public class CacaoFriendsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacaoFriendsShopApplication.class, args);
	}
	
//	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			@Autowired
			ItemRepository itemRepository;
			
			@Override
			@Transactional
			public void run(ApplicationArguments args) throws Exception {
				for(int i=0; i<100; i++) {
					itemRepository.save(Item
							.builder()
							.name("example")
							.price((int) Math.random())
							.stockQuantity(100)
							.shortDescript("example")
							.detail("example")
							.build());
				}
			}
		};
	}

}
