package com.blog.project.app.utils;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.blog.project.app.entities.User;
import com.github.javafaker.Faker;

@Service
public class RandomData {

	public String randomBotUsernameNotAlreadyRegistered(List<User> users) {
		String randomName = "BOT-" + this.randomUsername();

		for(User user : users) {
			if(user.getUsername().equals(randomName)) {
				return this.randomBotUsernameNotAlreadyRegistered(users);
			}
		}
		return randomName;
	}	
	
	public String randomUsername() {
		Faker faker = new Faker();
		Random rand = new Random();
		int int_random = rand.nextInt(14); 

		if(int_random == 1)
			return faker.ancient().god();
		
		else if(int_random == 2)
			return faker.ancient().primordial();
		
		else if(int_random == 3)
			return faker.ancient().titan();
		
		else if(int_random == 4)
			return faker.rickAndMorty().character();
		
		else if(int_random == 5)
			return faker.pokemon().name();
		
		else if(int_random == 6)
			return faker.lordOfTheRings().character();
		
		else if(int_random == 7)
			return faker.harryPotter().character();
		
		else if(int_random == 8)
			return faker.cat().name();
		
		else if(int_random == 9)
			return faker.witcher().character();
		
		else if(int_random == 10)
			return faker.book().author();
		
		else if(int_random == 11)
			return faker.beer().name();
		
		else if(int_random == 12)
			return faker.artist().name();
		
		else if(int_random == 13)
			return faker.gameOfThrones().character();
		
		else if(int_random == 14)
			return faker.gameOfThrones().city();
		
		else
			return faker.ancient().hero();
	}	
	
	public String randomFirstName() {
		Faker faker = new Faker();
		return faker.name().firstName();		
	}
	
	public String randomLastName() {
		Faker faker = new Faker();
		return faker.name().lastName();
	}	
	
	public String randomMessage() {
		Faker faker = new Faker();
		Random rand = new Random();
		int int_random = rand.nextInt(9); 
		
		if(int_random == 1)
			return faker.rickAndMorty().quote();

		else if(int_random == 2)
			return faker.yoda().quote();
		
		else if(int_random == 3)
			return faker.chuckNorris().fact();	
		
		else if(int_random == 4)
			return faker.shakespeare().romeoAndJulietQuote();

		else if(int_random == 5)
			return faker.shakespeare().asYouLikeItQuote();

		else if(int_random == 6)
			return faker.shakespeare().kingRichardIIIQuote();

		else if(int_random == 7)
			return faker.harryPotter().quote();		

		else if(int_random == 8)
			return faker.witcher().quote();		
		
		else if(int_random == 9)
			return faker.lorem().paragraph();

		else
			return faker.gameOfThrones().quote();
		
	}	
		
}
