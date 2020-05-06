package ru.netology.domain;

import com.github.javafaker.Faker;

import java.util.Locale;

public class PersonGenerator {
    private PersonGenerator(){}

    public static class Registration {
        private Registration(){}

        public static RegistrationData generate( String locale){
            Faker faker = new Faker(new Locale("en"));
            return new RegistrationData(
                    faker.name().firstName(),
                    faker. internet().password()
            );


        }
    }
}
