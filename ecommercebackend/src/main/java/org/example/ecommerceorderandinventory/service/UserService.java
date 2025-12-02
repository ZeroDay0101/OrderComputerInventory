package org.example.ecommerceorderandinventory.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerceorderandinventory.dto.in.AddressInDTO;
import org.example.ecommerceorderandinventory.dto.in.CreateUserDTO;
import org.example.ecommerceorderandinventory.dto.in.UpdateUserDTO;
import org.example.ecommerceorderandinventory.dto.out.AddressOutDTO;
import org.example.ecommerceorderandinventory.dto.out.UserDTO;
import org.example.ecommerceorderandinventory.entity.user.Address;
import org.example.ecommerceorderandinventory.entity.user.Role;
import org.example.ecommerceorderandinventory.entity.user.User;
import org.example.ecommerceorderandinventory.mappers.AddressMapper;
import org.example.ecommerceorderandinventory.mappers.UserMapper;
import org.example.ecommerceorderandinventory.repository.AddressRepository;
import org.example.ecommerceorderandinventory.repository.UserRepository;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AddressRepository addressRepository;


    private final AddressMapper addressMapper;


    private final PasswordEncoder bCryptPasswordEncoder;


    @Value("${application.default-user-balance}")
    private int defaultUserBalance;

    public UserService(UserRepository userRepository, UserMapper userMapper, AddressRepository addressRepository, AddressMapper addressMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserDTO createUserDTO) {
        createUserDTO.setPassword(bCryptPasswordEncoder.encode(createUserDTO.getPassword()));
        User user = userMapper.fromDTOToUser(createUserDTO);

        if (userRepository.findByUsername(createUserDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with username " + user.getUsername() + " already exists");
        }


        //Default user role is USER
        user.setRole(Role.USER);

        //Some default amount of money for the start
        user.setBalance(defaultUserBalance);

        userRepository.save(user);
    }

    public void deleteUser(long id) {
        User user = userRepository.getReferenceById(id);

        userRepository.delete(user);
    }

    public UserDTO getUser(long id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return userMapper.fromUserToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.fromUserToDTOList(users);
    }

    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        long id = updateUserDTO.getId();
        String username = updateUserDTO.getUsername();
        Role role = updateUserDTO.getRole();
        Double balance = updateUserDTO.getBalance();
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        if (username != null) {
            user.setUsername(username);
        }
        if (role != null) {
            user.setRole(role);
        }
        if (balance != null) {
            user.setBalance(balance);
        }

        return userMapper.fromUserToDTO(user);
    }

    public AddressOutDTO changeUserAddress(AddressInDTO addressInDTO) {
        User user = userRepository.findById(addressInDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address;
        if (user.getAddress() == null) {
            address = new Address();        // or map from DTO
        } else {
            address = addressRepository.findById(user.getAddress().getId()).orElseThrow(
                    () -> new RuntimeException("Address not found")
            );
        }

        address.setCountry(addressInDTO.getCountry());
        address.setCity(addressInDTO.getCity());
        address.setStreet(addressInDTO.getStreet());
        address.setPostalCode(addressInDTO.getPostalCode());
        address.setHouseNumber(addressInDTO.getHouseNumber());

        user.setAddress(address);

        return addressMapper.fromAddressToDTO(address);
    }


    public UserDTO getInfoOfUserMakingTheRequest(UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        return userMapper.fromUserToDTO(user);
    }
}
