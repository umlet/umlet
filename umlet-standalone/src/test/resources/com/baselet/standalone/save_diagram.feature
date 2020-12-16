Feature: Test save diagram
  -

  Scenario: Saved diagram is the same diagram as a loaded diagram
    Given A new diagram with a element positioned at '100,100'
    When  the diagram has been saved
    Then load the saved diagram
    Then verify that the element is positioned at '100,100'