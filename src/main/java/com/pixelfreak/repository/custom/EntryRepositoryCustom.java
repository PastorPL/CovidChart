package com.pixelfreak.repository.custom;

import java.util.List;

public interface EntryRepositoryCustom {

    List<String> findAllCountryNames();

    List<String> findAllProvinceForCountry(String country);

}
