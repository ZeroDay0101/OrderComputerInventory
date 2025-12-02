package org.example.ecommerceorderandinventory.controller;

import jakarta.validation.Valid;
import org.example.ecommerceorderandinventory.dto.in.AddressInDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateUserDTO;
import org.example.ecommerceorderandinventory.dto.out.AddressOutDTO;
import org.example.ecommerceorderandinventory.dto.out.UserDTO;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.example.ecommerceorderandinventory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Only admin can delete all users or user can delete only his account
    @PreAuthorize("hasRole('ADMIN') or principal.userId == #id")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    //Only admin can look up logged-in users
    @PreAuthorize("hasRole('ADMIN') or principal.userId == #id")
    @GetMapping
    public ResponseEntity<UserDTO> getUser(@RequestParam long id) {
        UserDTO userDTO = userService.getUser(id);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getInfoOfUserMakingTheRequest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserDTO userDTO = userService.getInfoOfUserMakingTheRequest(userDetails);

        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();

        return ResponseEntity.ok(userDTOList);
    }

    /**
     * If logged user wants to change his username, he is allowed to do so.
     * If any other property is present in the request (like changing balance) and logged-in user is not an admin. Method will not run
     *
     * @return
     */
    @PreAuthorize("hasRole('ADMIN') or (#updateUserDTO.id == principal.userId and #updateUserDTO.role == null and #updateUserDTO.balance == null)")
    @PatchMapping
    public ResponseEntity<UserDTO> updateUser(
            @RequestBody @Valid UpdateUserDTO updateUserDTO
    ) {

        UserDTO userDTO = userService.updateUser(updateUserDTO);

        return ResponseEntity.ok(userDTO);
    }


    /**
     * @param addressInDTO
     * @return Because address is strict property used only by User and only accessed with User, it's set method will exceptionally be in User controller
     */
    @PatchMapping("/address")
    public ResponseEntity<AddressOutDTO> setUserAddress(@RequestBody @Valid AddressInDTO addressInDTO) {
        AddressOutDTO addressOutDTO = userService.changeUserAddress(addressInDTO);

        return ResponseEntity.ok(addressOutDTO);
    }


}
