package pl.mc.cities.domain

/**
 * Compare by country name alphabetically
 * then by state name and by city name.
 * @param priorityCountry set the priority country
 * which always will be greater.
 */
class CountryPriorityCityComparator(
  private val priorityCountry: Country
) : Comparator<City> {
  override fun compare(first: City, second: City): Int {
    var compareResult = CountryPriorityComparator(priorityCountry).compare(
      first.state.country,
      second.state.country
    )
    if(compareResult == 0) compareResult = StateComparator().compare(first.state, second.state)
    if(compareResult == 0) compareResult = second.name.compareTo(first.name)
    return compareResult
  }
}

private class StateComparator : Comparator<State> {
  override fun compare(first: State, second: State): Int {
    return second.name.compareTo(first.name)
  }
}

private class CountryPriorityComparator(
  private val priorityCountry: Country
) : Comparator<Country> {
  override fun compare(first: Country, second: Country): Int {
    return if (first == second) {
      0
    } else if (first == priorityCountry && second != priorityCountry) {
      1
    } else if (first != priorityCountry && second == priorityCountry) {
      -1
    } else {
      //string compare is based on unicode of value
      //to compare alphabetically it needs reverse
      second.name.compareTo(first.name, ignoreCase = true)
    }
  }
}