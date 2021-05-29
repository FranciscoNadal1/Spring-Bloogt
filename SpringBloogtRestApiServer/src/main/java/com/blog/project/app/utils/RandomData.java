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
	
	public String getRandomImage() {
		Faker faker = new Faker();
		Random rand = new Random();
		int int_random = rand.nextInt(100000); 
		
		String loremImages = "https://picsum.photos/seed/"+ int_random +"/500/300";
		return loremImages;

	}
	
	public String randomUsername() {
		Faker faker = new Faker();
		Random rand = new Random();
		int int_random = rand.nextInt(14); 

		if(int_random == 1)
			return faker.ancient().god().replace(" ", "_");
		
		else if(int_random == 2)
			return faker.ancient().primordial().replace(" ", "_");
		
		else if(int_random == 3)
			return faker.ancient().titan().replace(" ", "_");
		
		else if(int_random == 4)
			return faker.rickAndMorty().character().replace(" ", "_");
		
		else if(int_random == 5)
			return faker.pokemon().name().replace(" ", "_");
		
		else if(int_random == 6)
			return faker.lordOfTheRings().character().replace(" ", "_");
		
		else if(int_random == 7)
			return faker.harryPotter().character().replace(" ", "_");
		
		else if(int_random == 8)
			return faker.cat().name().replace(" ", "_");
		
		else if(int_random == 9)
			return faker.witcher().character().replace(" ", "_");
		
		else if(int_random == 10)
			return faker.book().author().replace(" ", "_");
		
		else if(int_random == 11)
			return faker.beer().name().replace(" ", "_");
		
		else if(int_random == 12)
			return faker.artist().name().replace(" ", "_");
		
		else if(int_random == 13)
			return faker.gameOfThrones().character().replace(" ", "_");
		
		else if(int_random == 14)
			return faker.gameOfThrones().city().replace(" ", "_");
		
		else
			return faker.ancient().hero().replace(" ", "_");
	}	
	
	public String randomFirstName() {
		Faker faker = new Faker();
		return faker.name().firstName();		
	}
	
	public String randomLastName() {
		Faker faker = new Faker();
		return faker.name().lastName();
	}	
	
	public String randomMessage(boolean includeHashtag) {
		Faker faker = new Faker();
		Random rand = new Random();
		int int_random = rand.nextInt(9); 
		String message = "";
		
		if(int_random == 1)
			message = faker.rickAndMorty().quote();

		else if(int_random == 2)
			message =  faker.yoda().quote();
		
		else if(int_random == 3)
			message =  faker.chuckNorris().fact();	
		
		else if(int_random == 4)
			message =  faker.shakespeare().romeoAndJulietQuote();

		else if(int_random == 5)
			message =  faker.shakespeare().asYouLikeItQuote();

		else if(int_random == 6)
			message =  faker.shakespeare().kingRichardIIIQuote();

		else if(int_random == 7)
			message =  faker.harryPotter().quote();		

		else if(int_random == 8)
			message =  faker.witcher().quote();		
		
		else if(int_random == 9)
			message =  faker.lorem().paragraph();
		else
			message =  faker.gameOfThrones().quote();
			
			if(includeHashtag)
				return message + " " + this.getRandomHashtag();
			
		return message;
		
	}	
	

	public String getRandomHashtag() {
		Faker faker = new Faker();
		
		return "#" + faker.ancient().god();
	}
}
