Feature: Mail an existing diagram to someone else
  Scenario: Mail an existing diagram to someone else
    Given An already created class diagram
    Then send the email with subject: 'New diagram'
    Then  Verify if the email has been sent with the correct data