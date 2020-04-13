package com.pixelfreak.web.rest;

import com.pixelfreak.domain.Entry;
import com.pixelfreak.repository.EntryRepository;
import com.pixelfreak.service.GitHubService;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.collections4.map.MultiKeyMap;
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

    @GetMapping("/provinces/{country}")
    public ResponseEntity<List<String>> getProvinceForCountry(@PathVariable String country) {
        log.debug("REST request to get province for {}", country);
        List<String> provinces = this.entryRepository.findAllProvinceForCountry(country);

        if (provinces.contains("")) {
            int index = provinces.indexOf("");
            provinces.set(index, country);
        }
        if(provinces.size()>1){
            provinces.add("(Total)");
        }
        Optional<List<String>> entry = Optional.of(provinces);
        return ResponseUtil.wrapOrNotFound(entry);
    }

    @GetMapping("/countryAndProvince/{country}/{province}")
    public ResponseEntity<List<Entry>> getCountryAndProvinceByArgs(@PathVariable String country, @PathVariable String province) {
        log.debug("REST request to get data  {} in {}", province, country);

        if (province.equalsIgnoreCase(country)) {
            province = "";
        }

        if (province.contains("Total")) {
            final List<String> provinces = this.getProvinceForCountry(country).getBody();
            provinces.remove("(Total)");
            int index = provinces.indexOf(country);
            if(index != -1) {
                provinces.set(index, "");
            }


            final MultiKeyMap provinceDateMap = new MultiKeyMap();
            final Set<LocalDate> datesSet = new HashSet<>();
            for (String provinceName : provinces) {
                List<Entry> entryList = this.getCountryAndProvinceByArgs(country, provinceName).getBody();
                for (Entry entry : entryList) {
                    provinceDateMap.put(provinceName, entry.getLastUpdate(), entry);
                    datesSet.add(entry.getLastUpdate());
                }
            }

            List<Entry> returnList = new ArrayList<>();
            for (LocalDate date : datesSet) {
                int sum = 0;
                Entry exampleEntry = null;
                for (String provinceName : provinces) {
                    exampleEntry = (Entry) provinceDateMap.get(provinceName, date);
                    sum += exampleEntry.getConfirmed();
                }
                Entry finalEntry = new Entry();
                finalEntry.setId(country + "_" + province + "_" + date.toString());
                finalEntry.setCountry(country);
                finalEntry.setProvince("Total");
                finalEntry.setLastUpdate(date);
                finalEntry.setLat(exampleEntry.getLat());
                finalEntry.setLon(exampleEntry.getLon());
                finalEntry.setConfirmed(sum);
                returnList.add(finalEntry);
            }
            Collections.sort(returnList, Comparator.comparing(Entry::getLastUpdate));
            return ResponseUtil.wrapOrNotFound(Optional.of(returnList));

        } else {
            return ResponseUtil.wrapOrNotFound(Optional.of(this.entryRepository.findByCountryAndProvinceOrderByLastUpdateAsc(country, province)));
        }

    }

}
