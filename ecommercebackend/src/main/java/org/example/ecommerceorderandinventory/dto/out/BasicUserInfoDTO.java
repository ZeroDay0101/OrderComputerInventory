package org.example.ecommerceorderandinventory.dto.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Class used to return basic user info stored in securityContextHolder. For all user details, UserDTO class should used.
 */
@Getter
@Setter
@Builder
public class BasicUserInfoDTO {
    private long id;
    private String username;
}
