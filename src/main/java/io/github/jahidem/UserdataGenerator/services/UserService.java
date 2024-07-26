package io.github.jahidem.UserdataGenerator.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jahidem.UserdataGenerator.models.User;
import io.github.jahidem.UserdataGenerator.models.UserDto;
import io.github.jahidem.UserdataGenerator.repository.UserRepository;

@Service
public class UserService {
  public String en = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  public String es = en + "ñÑ";
  public String th = "กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮะิอิึุูเแโใไวย็ำๆฯ";

  @Autowired
  UserRepository userRepository;

  public void initialize(String language, String country, long seed, double error) {
    userRepository.initialize(language, country, seed, error);
  }

  public List<UserDto> getInitialUserList() {
    List<UserDto> rawData = dtoFromUser(userRepository.getUserList(), 0);
    return applyUserError(rawData);
  }

  public List<UserDto> getUserPage(int page) {
    List<UserDto> rawData = dtoFromUser(userRepository.getUserPage(page), page);
    return applyUserError(rawData);

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

  public List<UserDto> applyUserError(List<UserDto> rawData) {
    long currentError = userRepository.getError();
    List<UserDto> corruptData = rawData.stream()
        .map(userDto -> userDto.toBuilder()
            .fullName(applyEntryError(userDto.getFullName(), currentError, false))
            .fullAddress(applyEntryError(userDto.getFullAddress(), currentError, false))
            .phoneNumber(applyEntryError(userDto.getPhoneNumber(), currentError, true))
            .build())
        .collect(Collectors.toList());
    return corruptData;

  }

  public String applyEntryError(String entry, long error, boolean isNumber) {
    int randomErrorType = userRepository.getRandom(3);
    for (int u = 0; u < error; u++) {
      switch (randomErrorType) {
        case 0:
          entry = deleteTypeError(entry);
          break;
        case 1:
          entry = swapTypeError(entry);
          break;
        case 2:
          entry = addTypeError(entry, isNumber);
          break;
        default:
          break;
      }
    }
    return entry;
  }

  private String deleteTypeError(String object) {
    if (object.length() == 0)
      return object;

    int index = userRepository.getRandom(object.length());

    if (index == object.length() - 1) {
      return object.substring(0, index);
    }
    return object.substring(0, index) + object.substring(index + 1);
  }

  private String swapTypeError(String object) {
    if (object.length() == 0)
      return object;

    int fromIndex = userRepository.getRandom(object.length());
    int toIndex = userRepository.getRandom(object.length());
    char fromChar = object.charAt(fromIndex);
    char toChar = object.charAt(toIndex);

    object = object.substring(0, fromIndex) + toChar + object.substring(fromIndex + 1);
    object = object.substring(0, toIndex) + fromChar + object.substring(toIndex + 1);
    return object;
  }

  private String addTypeError(String object, boolean isNumber) {
    String lang = userRepository.getLocale().getLanguage();
    String letters = this.en;
    if (lang == "es")
      letters = this.es;
    if (lang == "th")
      letters = this.th;
    int index = 0;
    if (object.length() != 0)
      index = userRepository.getRandom(object.length());

    if (index == object.length() - 1)
      return isNumber ? object.concat(String.valueOf(userRepository.getRandom(10)))
          : object.concat(String.valueOf(letters.charAt(userRepository.getRandom(letters.length()))));
    return isNumber
        ? object.substring(0, index)
            .concat(String.valueOf(userRepository.getRandom(10)).concat(object.substring(index + 1)))
        : object.substring(0, index)
            .concat(String.valueOf(letters.charAt(userRepository.getRandom(letters.length()))))
            +
            object.substring(index + 1);
  }

}
