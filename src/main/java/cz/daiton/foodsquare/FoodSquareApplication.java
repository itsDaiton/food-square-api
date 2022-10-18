package cz.daiton.foodsquare;

import cz.daiton.foodsquare.IO.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class FoodSquareApplication implements CommandLineRunner {

    @Resource
    FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(FoodSquareApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileStorageService.init();
    }
}
