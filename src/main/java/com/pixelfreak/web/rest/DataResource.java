package com.pixelfreak.web.rest;

import com.pixelfreak.domain.Entry;
import com.pixelfreak.repository.EntryRepository;
import com.pixelfreak.service.GitHubService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/files")
public class DataResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(DataResource.class);

    private final GitHubService gitHubService;

    private final EntryRepository entryRepository;

    public DataResource(GitHubService gitHubService, EntryRepository entryRepository) {
        this.gitHubService = gitHubService;
        this.entryRepository = entryRepository;
    }

    @PostMapping("/update")
    public void githubFiles() {
        this.gitHubService.updateEntriesFromGithub();
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Entry>> getEntry(@PathVariable String country) {
        log.debug("REST request to get Country : {}", country);
        Optional<List<Entry>> entry = Optional.of(this.entryRepository.findByCountry(country));
        return ResponseUtil.wrapOrNotFound(entry);
    }

    @GetMapping("/countryName")
    public ResponseEntity<List<String>> getCountries() {
        log.debug("REST request to get Countries");
        Optional<List<String>> entry = Optional.of(this.entryRepository.findAllCountryNames());
        return ResponseUtil.wrapOrNotFound(entry);
    }

    @GetMapping("/lastUpdate")
    public ResponseEntity<Entry> getLastUpdateDate() {
        log.debug("REST request to get last update date");
        Optional<Entry> entry = Optional.of(this.entryRepository.findFirstByOrderByLastUpdateDesc());
        return ResponseUtil.wrapOrNotFound(entry);
    }


}
