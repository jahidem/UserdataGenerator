package io.github.jahidem.UserdataGenerator.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.jahidem.UserdataGenerator.models.UserDto;
import io.github.jahidem.UserdataGenerator.services.UserService;

@Controller
public class UserController {
  @Autowired
  UserService userService;

  @GetMapping("/home")
  public String homeController(
      @RequestParam(name = "country-region", required = false, defaultValue = "en-US") String countryRegion,
      @RequestParam(name = "seed", required = false, defaultValue = "0") long seed,
      @RequestParam(name = "error", required = false, defaultValue = "0") double error,

      Model model) {

    List<UserDto> userList = userService.getUser(countryRegion.substring(0, 2),
        countryRegion.substring(3, 5), seed, error, 0, 20);
    model.addAttribute("currentCountryRegion", countryRegion);
    model.addAttribute("seed", seed);
    model.addAttribute("error", error);
    model.addAttribute("userList", userList);

    return "home";
  }

  @GetMapping("/loadUser")
  public ResponseEntity<List<UserDto>> getUserController(
      @RequestParam(name = "country-region", required = false, defaultValue = "en-US") String countryRegion,
      @RequestParam(name = "page", required = false, defaultValue = "1") int page,
      @RequestParam(name = "seed", required = false, defaultValue = "0") long seed,
      @RequestParam(name = "error", required = false, defaultValue = "0") double error,
      Model model) {
    return ResponseEntity.ok(userService.getUser(countryRegion.substring(0, 2),
        countryRegion.substring(3, 5), seed, error, page, 10));
  }

}