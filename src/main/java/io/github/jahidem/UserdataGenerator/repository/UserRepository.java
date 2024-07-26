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

  private final List<User> users = new ArrayList<>();

  public UserRepository() {
    this.initialize("en", "US", 0, 0);
  }

  @SuppressWarnings("deprecation")
  public void initialize(String language, String country, long seed, double currError) {
    this.locale = new Locale(language, country);
    this.seed = seed;
    this.removeAll();
    this.initializeUser(20, 0);

    this.error = (long) Math.floor(currError);
    // the fraction part of error act asprobability of occuring one more error for
    // the current enrty
    this.error += this.getRandomDouble(1) < currError - Math.floor(currError) ? 1 : 0;
  }

  private void initializeUser(int number, int page) {
    this.random = new Random(this.seed + page);
    this.faker = new Faker(locale, random);
    for (int u = 0; u < number; u++)
      users.addLast(
          this.fakeUser());
  }

  private void removeAll() {
    while (!users.isEmpty())
      users.removeLast();
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
    return users;
  }

  public List<User> getUserPage(int page) {
    initializeUser(10, page);
    return users.subList(20 + (page-1) * 10, 20 + (page-1) * 10 + 10); // 10 users
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
