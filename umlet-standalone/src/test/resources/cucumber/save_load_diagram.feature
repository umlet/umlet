Feature: Test saving and loading of a diagram
  Scenario: Saved diagram is the same diagram as a loaded diagram
    Given A new diagram with a class element positioned at 50, 75 with a width of 200 and a height of 100
    When  the diagram has been saved
    Then  load the saved diagram
    Then  verify that the loaded element is positioned at the same position with the same size