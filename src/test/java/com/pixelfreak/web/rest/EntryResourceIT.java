package com.pixelfreak.web.rest;

import com.pixelfreak.CovChartApp;
import com.pixelfreak.domain.Entry;
import com.pixelfreak.repository.EntryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EntryResource} REST controller.
 */
@SpringBootTest(classes = CovChartApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class EntryResourceIT {

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CONFIRMED = 1;
    private static final Integer UPDATED_CONFIRMED = 2;

    private static final Integer DEFAULT_DEATHS = 1;
    private static final Integer UPDATED_DEATHS = 2;

    private static final Integer DEFAULT_RECOVERED = 1;
    private static final Integer UPDATED_RECOVERED = 2;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private MockMvc restEntryMockMvc;

    private Entry entry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entry createEntity() {
        Entry entry = new Entry()
            .province(DEFAULT_PROVINCE)
            .country(DEFAULT_COUNTRY)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .confirmed(DEFAULT_CONFIRMED)
            .deaths(DEFAULT_DEATHS)
            .recovered(DEFAULT_RECOVERED)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON);
        return entry;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entry createUpdatedEntity() {
        Entry entry = new Entry()
            .province(UPDATED_PROVINCE)
            .country(UPDATED_COUNTRY)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .confirmed(UPDATED_CONFIRMED)
            .deaths(UPDATED_DEATHS)
            .recovered(UPDATED_RECOVERED)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON);
        return entry;
    }

    @BeforeEach
    public void initTest() {
        entryRepository.deleteAll();
        entry = createEntity();
    }

    @Test
    public void createEntry() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry
        restEntryMockMvc.perform(post("/api/entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeCreate + 1);
        Entry testEntry = entryList.get(entryList.size() - 1);
        assertThat(testEntry.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testEntry.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEntry.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testEntry.getConfirmed()).isEqualTo(DEFAULT_CONFIRMED);
        assertThat(testEntry.getDeaths()).isEqualTo(DEFAULT_DEATHS);
        assertThat(testEntry.getRecovered()).isEqualTo(DEFAULT_RECOVERED);
        assertThat(testEntry.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testEntry.getLon()).isEqualTo(DEFAULT_LON);
    }

    @Test
    public void createEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry with an existing ID
        entry.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryMockMvc.perform(post("/api/entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllEntries() throws Exception {
        // Initialize the database
        entryRepository.save(entry);

        // Get all the entryList
        restEntryMockMvc.perform(get("/api/entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].confirmed").value(hasItem(DEFAULT_CONFIRMED)))
            .andExpect(jsonPath("$.[*].deaths").value(hasItem(DEFAULT_DEATHS)))
            .andExpect(jsonPath("$.[*].recovered").value(hasItem(DEFAULT_RECOVERED)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));
    }
    
    @Test
    public void getEntry() throws Exception {
        // Initialize the database
        entryRepository.save(entry);

        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", entry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entry.getId()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.confirmed").value(DEFAULT_CONFIRMED))
            .andExpect(jsonPath("$.deaths").value(DEFAULT_DEATHS))
            .andExpect(jsonPath("$.recovered").value(DEFAULT_RECOVERED))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()));
    }

    @Test
    public void getNonExistingEntry() throws Exception {
        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEntry() throws Exception {
        // Initialize the database
        entryRepository.save(entry);

        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Update the entry
        Entry updatedEntry = entryRepository.findById(entry.getId()).get();
        updatedEntry
            .province(UPDATED_PROVINCE)
            .country(UPDATED_COUNTRY)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .confirmed(UPDATED_CONFIRMED)
            .deaths(UPDATED_DEATHS)
            .recovered(UPDATED_RECOVERED)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON);

        restEntryMockMvc.perform(put("/api/entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntry)))
            .andExpect(status().isOk());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeUpdate);
        Entry testEntry = entryList.get(entryList.size() - 1);
        assertThat(testEntry.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testEntry.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEntry.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testEntry.getConfirmed()).isEqualTo(UPDATED_CONFIRMED);
        assertThat(testEntry.getDeaths()).isEqualTo(UPDATED_DEATHS);
        assertThat(testEntry.getRecovered()).isEqualTo(UPDATED_RECOVERED);
        assertThat(testEntry.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testEntry.getLon()).isEqualTo(UPDATED_LON);
    }

    @Test
    public void updateNonExistingEntry() throws Exception {
        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Create the Entry

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntryMockMvc.perform(put("/api/entries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEntry() throws Exception {
        // Initialize the database
        entryRepository.save(entry);

        int databaseSizeBeforeDelete = entryRepository.findAll().size();

        // Delete the entry
        restEntryMockMvc.perform(delete("/api/entries/{id}", entry.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
