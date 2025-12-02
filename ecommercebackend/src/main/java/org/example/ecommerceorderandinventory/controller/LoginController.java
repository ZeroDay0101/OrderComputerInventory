package org.example.ecommerceorderandinventory.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerceorderandinventory.dto.in.LoginDTO;
import org.example.ecommerceorderandinventory.dto.out.BasicUserInfoDTO;
import org.example.ecommerceorderandinventory.mappers.UserMapper;
import org.example.ecommerceorderandinventory.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextHolderRepository;

    public LoginController(AuthenticationManager authenticationManager, SecurityContextRepository securityContextHolderRepository, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.securityContextHolderRepository = securityContextHolderRepository;
    }


    @PostMapping
    public ResponseEntity<BasicUserInfoDTO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest httpRequest,
                                                  HttpServletResponse httpResponse) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        // 2. Create and populate a new SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // 4. Also set it in the holder for current thread
        SecurityContextHolder.setContext(context);
        // 3. Persist it via the repository
        securityContextHolderRepository.saveContext(context, httpRequest, httpResponse);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok().body(
                BasicUserInfoDTO.builder()
                        .id(userDetails.getUserId())
                        .username(userDetails.getUsername())
                        .build()
        );
    }


    /**
     * Checks if is authenticated and returns most basic details from securityContextHolder about the user.
     *
     * @return BasicUserInfoDTO
     */
    @GetMapping
    public ResponseEntity<BasicUserInfoDTO> checkIfUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok().body(
                BasicUserInfoDTO.builder()
                        .id(userDetails.getUserId())
                        .username(userDetails.getUsername())
                        .build()
        );
    }
}
