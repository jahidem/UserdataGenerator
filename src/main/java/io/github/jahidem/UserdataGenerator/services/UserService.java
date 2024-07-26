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

  @Autowired
  UserRepository userRepository;

  public List<UserDto> getUser(String language, String country, long seed, double error, int page, int number) {
    return userRepository.getUser(language, country, seed, error, page, number);
  }

}
