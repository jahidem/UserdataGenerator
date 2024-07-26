package io.github.jahidem.UserdataGenerator.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.IdNumber;
import net.datafaker.providers.base.Name;
import net.datafaker.providers.base.PhoneNumber;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
  private IdNumber idNumber;
  private Name name;
  private Address address;
  private PhoneNumber phoneNumber;

}
