Feature: Tracker feature

#  Scenario: There are three buttons on InPostTracker
#    Then I wait for 4 seconds
#    Then I wait for the "InPostTrackerActivity" screen to appear
#   Then I wait for the view with id "imageButtonSearch" to appear
#    Then I wait for the view with id "imageButtonScan" to appear
#    Then I wait for the view with id "imageButtonClear" to appear


#  Scenario: There is a default track number entered 
#    Then I wait for 4 seconds
#    Then I wait for the "InPostTrackerActivity" screen to appear
#    Then I should see "900000295100000916048229"

#  Scenario: Clear button works
#    Then I wait for 4 seconds
#    Then I wait for the "InPostTrackerActivity" screen to appear
#    Then I should see "900000295100000916048229"
#    When I press view with id "imageButtonClear"
#    Then I should not see "900000295100000916048229"

#  Scenario: I am not able to enter text into tracking number
#    Then I wait for 4 seconds
#    Then I wait for the "InPostTrackerActivity" screen to appear
#    Then I should see "900000295100000916048229"
#    When I press view with id "imageButtonClear"
#    Then I should not see "900000295100000916048229"
#    Then I enter "rubbish" into input field number 1
#    Then I should not see "rubbish"

#  Scenario: I am not able to query for empty number
#    Then I wait for 4 seconds
#    Then I wait for the "InPostTrackerActivity" screen to appear
#    Then I should see "900000295100000916048229"
#    Then I clear input field number 1 
#    Then I press view with id "imageButtonSearch"
#    Then I should not see "Nieprawidłowy numer przesyłki"

  Scenario: I get a result for incorrect number
    Then I wait for 4 seconds
    Then I wait for the "InPostTrackerActivity" screen to appear
    Then I should see "900000295100000916048229"
    Then I clear "900000295100000916048229"
    Then I enter "000000000000000000000000" into input field number 1
    Then I press view with id "imageButtonSearch"
    Then I wait for the view with id "webViewResult" to appear
    Then I wait for progress
    Then I should see "Nieprawidłowy numer przesyłki"


