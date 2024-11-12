Feature: Company feature

  Scenario Outline: Company scenario
    Given I want to check company information
    When I run the query
    Then I should see the company information "<CEO>" "<Name>" "<Founder>" <Valuation> <Founded> <Employees>

    Examples:
        | CEO | Name | Founder | Valuation | Founded | Employees |
        | Elon Musk | SpaceX | Elon Musk | 74000000000.0 | 2002 | 9500 |

