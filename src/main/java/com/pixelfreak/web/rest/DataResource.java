package com.pixelfreak.web.rest;

import com.pixelfreak.domain.Entry;
import com.pixelfreak.repository.EntryRepository;
import com.pixelfreak.service.GitHubService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        final List<Entry> entryList = this.entryRepository.findByCountryOrderByLastUpdateAsc(country);
        final List<Entry> entryListDuplicate = new ArrayList<>(entryList);
        final Set<LocalDate> orderDateList = entryList.get(0).getLastUpdate().datesUntil(entryList.get(entryList.size()-1).getLastUpdate()).collect(Collectors.toSet());

        final Map<LocalDate,Entry> existingDates = new HashMap<>();
        for(int i=0;i<entryList.size();i++) {
            Entry entry = entryList.get(i);
            orderDateList.remove(entry.getLastUpdate());
            existingDates.put(entry.getLastUpdate(),entry);
        }
        List<LocalDate> list = new ArrayList<>(orderDateList);
        Collections.sort(list);

        for(LocalDate missingDate: list) {
            int i=0;
            while(!existingDates.keySet().contains(missingDate.minusDays(i))) {
                i++;
            }
            Entry toBeCopied = existingDates.get(missingDate.minusDays(i));
            Entry newEntry = new Entry();
            newEntry.setLastUpdate(missingDate);
            newEntry.setId("Non"+i);
            newEntry.setConfirmed(toBeCopied.getConfirmed());
            newEntry.setCountry(toBeCopied.getCountry());
            newEntry.setDeaths(toBeCopied.getDeaths());
            newEntry.setLat(toBeCopied.getLat());
            newEntry.setLon(toBeCopied.getLon());
            newEntry.setProvince(toBeCopied.getProvince());
            newEntry.setRecovered(toBeCopied.getRecovered());
            entryList.add(newEntry);
        }

        Collections.sort(entryList, Comparator.comparing(Entry::getLastUpdate));

        return ResponseUtil.wrapOrNotFound(Optional.of(entryList));
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
