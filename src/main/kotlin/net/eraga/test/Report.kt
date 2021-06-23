package net.eraga.test

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.eraga.test.models.Person
import net.eraga.test.repositories.PersonRepository
import java.util.function.Consumer

class Report(personRepository: PersonRepository) {
    private var regions: MutableList<Region?> = ArrayList()
    private fun getRegionByCode(regionCode: Int): Region? {
        return regions.stream()
            .filter { region: Region? -> region!!.code == regionCode }
            .findFirst()
            .orElse(null)
    }

    @JvmName("getRegions1")
    fun getRegions(): List<Region?> {
        return regions
    }

    @JsonPropertyOrder("code", "cities", "persons")
    inner class Region(@field:JsonProperty("regionCode") var code: Int) {

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var cities: MutableList<City?> = ArrayList()

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var persons: MutableList<Person> = ArrayList()

        fun getCityByName(cityName: String?): City? {
            return cities.stream()
                .filter { city: City? ->
                    city!!.name == cityName
                }
                .findFirst()
                .orElse(null)
        }

    }

    inner class City(var name: String) {
        var persons: MutableList<Person> = ArrayList()

    }

    init {
        personRepository.findAll().forEach(Consumer { person: Person? ->
            val regionCode = person?.region
            val cityName = person?.city
            var region = regionCode?.let { getRegionByCode(it) }
            if (region == null) {
                region = regionCode?.let { Region(it) }
                regions.add(region)
            }
            if (cityName == null) {
                if (person != null) {
                    region?.persons?.add(person)
                }
            } else {
                var city = region?.getCityByName(person.city)
                if (city == null) {
                    city = City(cityName)
                    region?.cities?.add(city)
                }
                city.persons.add(person)
            }
        })
    }
}