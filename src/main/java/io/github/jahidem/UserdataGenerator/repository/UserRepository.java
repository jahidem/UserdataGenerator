package io.github.jahidem.UserdataGenerator.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import io.github.jahidem.UserdataGenerator.models.User;
import io.github.jahidem.UserdataGenerator.models.UserDto;
import net.datafaker.Faker;

@Component
public class UserRepository {
  public String en = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  public String es = en + "ñÑ";
  public String th = "กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮะิอิึุูเแโใไวย็ำๆฯ";

  @SuppressWarnings("deprecation")
  public List<UserDto> getUser(String language, String country, long seed, double currError, int page, int number) {
    Locale locale = new Locale(language, country);
    Random random = new Random(seed);
    long error = (long) Math.floor(currError);
    // the fraction part of error act asprobability of occuring one more error for
    // the current enrty
    error += random.nextDouble(1) < currError - Math.floor(currError) ? 1 : 0;
    Faker faker = new Faker(locale, random);

    List<User> users = new ArrayList<>();
    for (int u = 0; u < number; u++)
      users.addLast(
          this.fakeUser(faker));
    List<UserDto> rawDtos = dtoFromUser(users, page);

    return applyUserError(
        rawDtos, error, locale, random);
  }

  private User fakeUser(Faker faker) {
    User newUser = User.builder()
        .idNumber(faker.idNumber())
        .name(faker.name())
        .address(faker.address())
        .phoneNumber(faker.phoneNumber()).build();
    return newUser;

  }

  public List<UserDto> dtoFromUser(List<User> userList, int page) {

    List<UserDto> dtos = new ArrayList<>();
    int startIndex = 21 + (page - 1) * 10;

    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      dtos.add(UserDto.builder()
          .index(startIndex + i)
          .peselNumber(user.getIdNumber().peselNumber())
          .fullName(user.getName().fullName())
          .fullAddress(user.getAddress().fullAddress())
          .phoneNumber(user.getPhoneNumber().phoneNumber())
          .build());
    }

    return dtos;

  }

  public List<UserDto> applyUserError(List<UserDto> rawData, long currentError, Locale locale, Random random) {
    List<UserDto> corruptData = rawData.stream()
        .map(userDto -> userDto.toBuilder()
            .fullName(applyEntryError(userDto.getFullName(), currentError, false, locale, random))
            .fullAddress(applyEntryError(userDto.getFullAddress(), currentError, false, locale, random))
            .phoneNumber(applyEntryError(userDto.getPhoneNumber(), currentError, true, locale, random))
            .build())
        .collect(Collectors.toList());
    return corruptData;

  }

  public String applyEntryError(String entry, long error, boolean isNumber, Locale locale, Random random) {
    int randomErrorType = random.nextInt(3);
    for (int u = 0; u < error; u++) {
      switch (randomErrorType) {
        case 0:
          entry = deleteTypeError(entry, random);
          break;
        case 1:
          entry = swapTypeError(entry, random);
          break;
        case 2:
          entry = addTypeError(entry, isNumber, locale, random);
          break;
        default:
          break;
      }
    }
    return entry;
  }

  private String deleteTypeError(String object, Random random) {
    if (object.length() == 0)
      return object;

    int index = random.nextInt(object.length());

    if (index == object.length() - 1) {
      return object.substring(0, index);
    }
    return object.substring(0, index) + object.substring(index + 1);
  }

  private String swapTypeError(String object, Random random) {
    if (object.length() == 0)
      return object;

    int fromIndex = random.nextInt(object.length());
    int toIndex = random.nextInt(object.length());
    char fromChar = object.charAt(fromIndex);
    char toChar = object.charAt(toIndex);

    object = object.substring(0, fromIndex) + toChar + object.substring(fromIndex + 1);
    object = object.substring(0, toIndex) + fromChar + object.substring(toIndex + 1);
    return object;
  }

  private String addTypeError(String object, boolean isNumber, Locale locale, Random random) {
    String lang = locale.getLanguage();
    String letters = this.en;
    if (lang == "es")
      letters = this.es;
    if (lang == "th")
      letters = this.th;
    int index = 0;
    if (object.length() != 0)
      index = random.nextInt(object.length());

    if (index == object.length() - 1)
      return isNumber ? object.concat(String.valueOf(random.nextInt(10)))
          : object.concat(String.valueOf(letters.charAt(random.nextInt(letters.length()))));
    return isNumber
        ? object.substring(0, index)
            .concat(String.valueOf(random.nextInt(10)).concat(object.substring(index + 1)))
        : object.substring(0, index)
            .concat(String.valueOf(letters.charAt(random.nextInt(letters.length()))))
            +
            object.substring(index + 1);
  }
}
