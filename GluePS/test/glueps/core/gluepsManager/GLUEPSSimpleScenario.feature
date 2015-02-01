Feature: Simple Example Test
	In order to provide a first example of tests 
	As the GLUEPS administrator
	I want to run the GLUEPS service
	
	Scenario: GLUEPS service is started
    	Given I run the GLUEPS Manager
    	When I probe the service
    	Then it should answer something valid