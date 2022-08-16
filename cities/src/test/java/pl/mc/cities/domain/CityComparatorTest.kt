package pl.mc.cities.domain

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.lessThan
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class CityComparatorTest{

  @Test
  fun `priority country cities should be grater even if not greater alphabetically`() {
    //GIVEN
    val priorityCountry = Country("United States")
    val greaterCity = City.create(
      "B Greater City", "B State name", "United States")
    val lessCity = City.create(
      "A Less City", "A State name", "Algeria")
    //WHEN
    val actual = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, lessCity)
    val actualReverse = CountryPriorityCityComparator(priorityCountry).compare(lessCity, greaterCity)
    val equal = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, greaterCity)
    //THEN
    assertThat(actual, greaterThan(0))
    assertThat(actualReverse, lessThan(0))
    assertThat(equal, equalTo(0))
  }

  @Test
  fun `country should be compared alphabetically if no one is priority country`() {
    val priorityCountry = Country("United States")
    val greaterCity = City.create(
      "B Greater City", "B State name", "Algeria")
    val lessCity = City.create(
      "A ALess City", "A State name", "Poland")
    //WHEN
    val actual = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, lessCity)
    val actualReverse = CountryPriorityCityComparator(priorityCountry).compare(lessCity, greaterCity)
    val equal = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, greaterCity)
    //THEN
    assertThat(actual, greaterThan(0))
    assertThat(actualReverse, lessThan(0))
    assertThat(equal, equalTo(0))
  }

  @Test
  fun `when country is the same then compare states`() {
    val priorityCountry = Country("United States")
    val greaterCity = City.create(
      "B Greater City", "AState", "Poland")
    val lessCity = City.create(
      "A ALess City", "BState", "Poland")
    //WHEN
    val actual = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, lessCity)
    val actualReverse = CountryPriorityCityComparator(priorityCountry).compare(lessCity, greaterCity)
    val equal = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, greaterCity)
    //THEN
    assertThat(actual, greaterThan(0))
    assertThat(actualReverse, lessThan(0))
    assertThat(equal, equalTo(0))
  }

  @Test
  fun `when country is the same and state is the same then compare city`() {
    val priorityCountry = Country("United States")
    val greaterCity = City.create(
      "ACity", "AState", "Poland")
    val lessCity = City.create(
      "BCity", "AState", "Poland")
    //WHEN
    val actual = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, lessCity)
    val actualReverse = CountryPriorityCityComparator(priorityCountry).compare(lessCity, greaterCity)
    val equal = CountryPriorityCityComparator(priorityCountry).compare(greaterCity, greaterCity)
    //THEN
    assertThat(actual, greaterThan(0))
    assertThat(actualReverse, lessThan(0))
    assertThat(equal, equalTo(0))
  }
}

