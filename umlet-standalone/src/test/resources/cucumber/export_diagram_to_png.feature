Feature: Test exporting class diagram to a png file
  Scenario: Export an existing diagram to a png file
    Given An already created class diagram (class_diagram.uxf in the resource folder)
    When  The diagram has been exported to a png file
    Then  Verify that the exported png equals class_diagram.png in the resource folder