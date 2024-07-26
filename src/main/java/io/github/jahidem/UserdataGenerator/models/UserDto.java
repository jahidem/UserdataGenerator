package io.github.jahidem.UserdataGenerator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
  private Integer index;
  private String peselNumber;
  private String fullName;
  private String fullAddress;
  private String phoneNumber;
}
