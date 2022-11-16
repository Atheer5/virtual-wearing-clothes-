# Jira Ticket:

https://harridev.atlassian.net/browse/HARRI-148808

# Team_Schedule Project:

The aim of this project to be able to 
1. create a large schedules with large number of shifts, Employees and positions 
2. test PUT forecast API wiith large number of revenue centers
3. add large number of labels and revenue centers

to test  Performance (Non-Functional Testing).

This project consists of 6 folders:

1. AddRevenueCenter.
2. addToSchedule.
3. new_emp.
4. AddShifts.
5. PUT_Forecast_API.
6. Add_Labels

**DB Configration User and password can be changed in AWS_RDS/config.js** <br />


## 1. AddRevenueCenter:
​
This folder contains one script and one config file .<br />
​
**addRevenueCenters.js** : <br />
this script use to add number of revenue center to the brand. <br />
​
Let assume we have theses configuration: <br />
​
```
const config = {
    brand_id: '10218015',
    Revenue_cenetrs_count: 1  
}
```
Revenue_cenetrs_count represent the count of Revenue cenetrs will be added to the brand with brand_id 
Example: <br />
​
From above configuration: <br />
​
One revenue center will be added to the brand with id = 10218015. <br />

## 2. addToSchedule:
1. This script used to add employee not included in schedule to schedule . <br />
2. ​Brand id can be edit in **Config.js** <br />

## 3. new_emp:
​
This folder contain three script , one config file and two input file  <br />
### 1.AddNewEmpWithPos.js :
This script used to add new schedule employee with one position . 
1. To run this script position code  must be added to **positioncode** file manually .
2. Number of new employee equal to the number of position code in **positioncode** file .
3. ​Brand id can be edit in **Config.js** <br />
### 2.PositionsForEmp.js :
This script use to add 25 position for 10 employee "25 position for each employee".
1. To run this script you need to add 10 employee id in **employeeID** file and 25 position code in **positioncode** file .
2. This script used to increase the number of position in brand not the number of employee .
3. Brand id can be edit in **Config.js** <br />
### 3.positionTOemployee.js : 
This script used to add 100 position for 100 employee "one position for one employee"
1. To run this script you need to add 100 employee id in **employeeID** file and 100 position code in **positioncode** file .
2. This script used to add position with one employee .
3. Brand id can be edit in **Config.js** <br />
### 4.employeeID :
Input file contains numbmer of employee id for employee in the brand
### 5.positioncode :
Input file contains position codes in the brand .
### 6.config :
This config file contain brand id for brand used in test .

## 4. AddShifts:
​
This folder contain one input file ,one output folder ,one config file and two scripts.
​
### 1.AddRolesToschedule.js :
This script used to add roles to the schedule.
1. To run this script you need to add position code to be add to the schedule in **positioncode** file
2. This position must be in brand used in test .
3. ​Brand id can be edit in **Config.js** <br />
​
### 2.AddShifts :
This script used to add 1000 shifts randomly.
1. To use this script you need to add date for schedule containe number of assignee id more than 1000
2. ​Brand id and date can be edit in **Config.js** <br />
### 3.positioncode :
Input file contains position codes in the brand .
​
### 4.config :
```
const config = {
    brand_id: '10218015',
    date:  "Dec 26, 2038"
}
```
​
### 5.outputs :
 This folder contain three output file
  1. added_shifts : contain information about shifts **added** to schedule 
  2. all_shifts_info :contain information about all shifts **can be added** to the schedule 
  3. schedule_response :contain GET schedule mode API response .

## 5. PUT_Forecast_API.
### a. put_forecast_API_All_Day

This script working on preparing large payload contains large revenue centers and randome total_guests, total_sales values for each revenue  <br />
  1. date can be randomly chooesd between 2 dates by specifing start and end date using this function **randomDate** <br />
  2. Brand id can be edit in **forecast_Config.js** <br />
  3. Connection to DB required to run the script. 

### b. put_forecast_API_hour

This script working on preparing large payload contains large revenue centers and randome total_guests, total_sales values for all hours in each revenue <br />
  1. date can be randomly chooesd between 2 dates by specifing start and end date using this function **randomDate** <br />
  2. Brand id can be edit in **forecast_Config.js** <br />
  3. Connection to DB required to run the script. 


## 6. Add Label.

This script working on creating number of labels to the brand <br />
```
const config = {
    brand_id:10218015,
    number_labels:1
}
```
number_labels represent the count of labels will be added to the brand with brand_id  <br />
Example: <br />
​
From above configuration: <br />
​
One label will be added to the brand with id = 10218015.

