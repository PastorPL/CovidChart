package com.pixelfreak.web.rest;

import com.pixelfreak.domain.User;
import com.pixelfreak.repository.UserRepository;
import com.pixelfreak.security.SecurityUtils;
import com.pixelfreak.service.GitHubService;
import com.pixelfreak.service.MailService;
import com.pixelfreak.service.UserService;
import com.pixelfreak.service.dto.PasswordChangeDTO;
import com.pixelfreak.service.dto.UserDTO;
import com.pixelfreak.web.rest.errors.EmailAlreadyUsedException;
import com.pixelfreak.web.rest.errors.InvalidPasswordException;
import com.pixelfreak.web.rest.errors.LoginAlreadyUsedException;
import com.pixelfreak.web.rest.vm.KeyAndPasswordVM;
import com.pixelfreak.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class DataResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(DataResource.class);

    private final GitHubService gitHubService;

    public DataResource(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }


    /**
     * {@code GET  /files} : activate the registered user.
     *
     */
    @GetMapping("/files")
    public List<String> githubFiles() {
        try {
            return this.gitHubService.getFiles();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
