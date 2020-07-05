package cacao.friends.shop.modules.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.category.CategoryRepository;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.characterKind.repository.CharacterKindRepository;
import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DataApplicationRunner implements ApplicationRunner {
	
	private final ResourceLoader resourceLoader;

	private final CharacterKindRepository characterKindRepository;
	
	private final CategoryRepository categoryRepository;

	@Override
	public void run(ApplicationArguments args) {
		createCharacter();
		createCategory();
	}
	
	private void createCategory() {
		if (categoryRepository.count() > 0)
			return;
		Category parentCategory = Category
				.builder().name("인형").build();
		Category childCategory1 = Category
				.builder().name("큰인형").parentCategory(parentCategory).build();
		Category childCategory2 = Category
				.builder().name("작은인형").parentCategory(parentCategory).build();
		categoryRepository.save(parentCategory);
		categoryRepository.save(childCategory1);
		categoryRepository.save(childCategory2);
	}

	private void createCharacter() {
		Long charCount = characterKindRepository.count();
		log.info("character kind count {}", charCount);



		try {
			File apeachFile = getFileByPath("/static/images/character/category_apeach_on.png");
			File conFile = getFileByPath("classpath:/static/images/character/category_con_on.png");
			File frodoFile = getFileByPath("classpath:/static/images/character/category_frodo_on.png");
			File jayzFile = getFileByPath("classpath:/static/images/character/category_jayz_on.png");
			File muziFile = getFileByPath("classpath:/static/images/character/category_muzi_on.png");
			File neoFile = getFileByPath("classpath:/static/images/character/category_neo_on.png");
			File ryanFile = getFileByPath("classpath:/static/images/character/category_ryan_on.png");
			File tubeFile = getFileByPath("classpath:/static/images/character/category_tube_on.png");
			if (charCount > 0)
				return;
			List<CharacterKind> list = new ArrayList<>();
			list.add(CharacterKind.builder().name("어피치").image(imageToBase64(apeachFile)).build());
			list.add(CharacterKind.builder().name("콘").image(imageToBase64(conFile)).build());
			list.add(CharacterKind.builder().name("프로토").image(imageToBase64(frodoFile)).build());
			list.add(CharacterKind.builder().name("재이지").image(imageToBase64(jayzFile)).build());
			list.add(CharacterKind.builder().name("무지").image(imageToBase64(muziFile)).build());
			list.add(CharacterKind.builder().name("네오").image(imageToBase64(neoFile)).build());
			list.add(CharacterKind.builder().name("라이언").image(imageToBase64(ryanFile)).build());
			list.add(CharacterKind.builder().name("튜브").image(imageToBase64(tubeFile)).build());
			characterKindRepository.saveAll(list);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private File getFileByPath(String path) throws Exception {
		ClassPathResource resource = new ClassPathResource(path);
		try {
			return resource.getFile();
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	private String imageToBase64(File file) {
		String base64Image = "";
		try(FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			base64Image = Base64.getEncoder().encodeToString(imageData);
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return "data:image/png;base64," + base64Image;
	}

}
