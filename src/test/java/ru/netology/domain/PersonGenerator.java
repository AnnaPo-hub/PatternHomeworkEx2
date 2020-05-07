package ru.netology.domain;

import com.github.javafaker.Faker;

import java.util.Locale;

public class PersonGenerator {
    private PersonGenerator(){}


    public static  class Registration {
        private Registration(){}


        public static RegistrationData generate( String locale){
            Faker faker = new Faker(new Locale("en"));
            double random = Math.random();
            Status status;
            if (random>=0.5){
               status=Status.BLOCKED;
            }  else {
                status = Status.ACTIVE;
            }
            return new RegistrationData(
                    faker.name().firstName(),
                    faker. internet().password(),
                    status
            );
        }
    }
}
