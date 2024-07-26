package io.github.jahidem.UserdataGenerator.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.stereotype.Component;
import io.github.jahidem.UserdataGenerator.models.User;
import net.datafaker.Faker;

@Component
public class UserRepository {
  private Random random;
  private Locale locale;
  private long seed;
  private Faker faker;
  private long error;

  public UserRepository() {
    this.initialize("en", "US", 0, 0);
  }

  @SuppressWarnings("deprecation")
  public void initialize(String language, String country, long seed, double currError) {
    this.locale = new Locale(language, country);
    this.seed = seed;
    this.random = new Random(this.seed);
    this.error = (long) Math.floor(currError);
    // the fraction part of error act asprobability of occuring one more error for
    // the current enrty
    this.error += this.getRandomDouble(1) < currError - Math.floor(currError) ? 1 : 0;
  }

  private List<User> initializeUser(int number, int page) {
    this.random = new Random(this.seed + page);
    this.faker = new Faker(locale, random);

    List<User> users = new ArrayList<>();
    for (int u = 0; u < page; u++)
      users.addLast(
          this.fakeUser());
    return users;
  }

  private User fakeUser() {
    User newUser = User.builder()
        .idNumber(faker.idNumber())
        .name(faker.name())
        .address(faker.address())
        .phoneNumber(faker.phoneNumber()).build();
    return newUser;

  }

  public List<User> getUserList() {
    return initializeUser(20, 0);
  }

  public List<User> getUserPage(int page) {
    return initializeUser(10, page);
  }

  public int getRandom(int upperBound) {
    return this.random.nextInt(upperBound);
  }

  public double getRandomDouble(int upperBound) {
    return this.random.nextDouble(upperBound);
  }

  public Locale getLocale() {
    return this.locale;
  }

  public long getError() {
    return this.error;
  }
}
