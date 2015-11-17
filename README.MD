### Weathering Heights

An app that uses openweather api to show you the daily forecast for your selected location.

## Requirements

- Android Studio 1.4+.
- Java 1.6+.
- Target Android SDK 23 (gradle will warn you if you don't have it).

## Features

1. Search by city name or zip code/ postal code
2. Search by GPS (type lat,lon in the search box e.g. "-33.867, 151.207" and you'll see the an auto suggest searching these GPS coordinates).
3. Most recent location loads automatically
4. Save locations automatically. Locations selected are saved as "recent locations".
5. My locations. Clicking on a recent location will set it as your current location.
6. Delete location.
7. Multi-market. Austral, US, Canada support. Includes different color schemes, app names, icons & units (imperial or metric).

## Testing

### Unit Tests

These cover:

- Open Weather API Urls are correct and being escaped.
- Google AutoComplete API Urls are correct and being escaped.
- DateUtils convert from Epoc to Date correctly.
- DateUtils return correct short Day format
- Check whether input is GPS coordinates (used in search to determin whether we need to search by name, zip/post code or GPS coords).

### Instrumentation Tests

These have only been testing on 5.1 and 6.0 and as far as I know, the newer espresso style tests will only work on these platforms.
In my testing they seem to randomly work on older platforms but aren't reliable.

Instrumentation tests cover:

- Launching MainActivity and verifying whether empty message is displayed and whether the FAB is on screen.
- Clicking FAB and validating transition to search screen (Uses espresso)
- Launching SearchActivity and validating that the AutoCompleteTextView exists and that it's hint is correct.
- Writing text to AutoCompleteTextView and then testing the clear button the validate it's working.
- Database write (Database holds recent locations)
- Database read
- Database delete 
- Check DateUtils epoc to date conversion
- Check DateUtils date to day conversion




