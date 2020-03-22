package com.pixelfreak.web.rest;

import com.pixelfreak.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     */
    @GetMapping("/files")
    public void githubFiles() {
        this.gitHubService.updateEntriesFromGithub();
    }


}
