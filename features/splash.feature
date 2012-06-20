Feature: Splash screen feature

  Scenario: When I start application it starts with splash screen 
    Then I wait for the "SplashScreenActivity" screen to appear

  Scenario: When I wait I see splas screen transition
    Then I wait for 3 seconds
    Then I wait for the "InPostTrackerActivity" screen to appear
