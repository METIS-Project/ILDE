Feature: Create VLE
	In order to deploy my designs to a target VLE 
	As the GLUEPS user
	I want to create a VLE with my credentials
	
	Scenario: Create VLE (valid data)
    	Given I am on the GLUEPS initial page
    	When I provide valid VLE data
    	Then I should see my VLE in the list of VLEs