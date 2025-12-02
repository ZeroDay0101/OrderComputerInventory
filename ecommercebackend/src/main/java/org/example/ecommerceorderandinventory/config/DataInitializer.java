package org.example.ecommerceorderandinventory.config;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerceorderandinventory.entity.Item;
import org.example.ecommerceorderandinventory.entity.ItemStatus;
import org.example.ecommerceorderandinventory.entity.ItemType;
import org.example.ecommerceorderandinventory.entity.user.Role;
import org.example.ecommerceorderandinventory.entity.user.User;
import org.example.ecommerceorderandinventory.repository.ItemRepository;
import org.example.ecommerceorderandinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;

    @Value("${application.default-user-balance}")
    private int defaultUserBalance;

    @Value("${application.enable-test-configuration}")
    private boolean enableTestConfiguration;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) {
        if (enableTestConfiguration) {
            if(userRepository.count() == 0) {
                log.warn("Default admin and user accounts are being created, this method should run only for testing purposes! For production, set application.enable-default-users in application.properties to false.");
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                admin.setBalance(9999999);

                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user"));
                user.setRole(Role.USER);
                user.setBalance(defaultUserBalance);

                userRepository.save(admin);
                userRepository.save(user);
            }
            if(itemRepository.count() == 0) {
                Item defaultItem = new Item();
                defaultItem.setItemType(ItemType.MOUSE);
                defaultItem.setModel("Test item");
                defaultItem.setQuantity(999);
                defaultItem.setPrice(10);
                defaultItem.setItemStatus(ItemStatus.ACTIVE);

                itemRepository.save(defaultItem);
            }
        }
    }
}
