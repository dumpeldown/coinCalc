# coinCalc

## Use Case
This command-line-tool can be used to monitor the value of crypto-currencies.    
In regular intervals - default is 3 minutes - the new prices will be fetch using the [crypto-compare API](https://min-api.cryptocompare.com/).   

We also support automatically subtracting the fees you payed, when using [coinbase](https://www.coinbase.com/). Maybe in the future, we will support other exchange-platforms' fee systems.

## How to use
Build the project using the [Apache-Maven build system](https://maven.apache.org/).
````
mvn clean package
````
After that, the output jar can be startet using
````
> java -jar target/coinCalc-${VERSION}.jar
````
### Input File
The program requires a `data.csv` file in the root directory of the project.
This repository contains a empty file, into which you need to input your values.  
For example, this is a valid input file:
````csv
COIN_SYMBOL;AMOUNT_BOUGHT;BOUGHT_AT
BTC;100;21543.99
ETH;50;295.45
DOGE;420;0.079099
````
### Output File
In addition to outputting the results to the console, the application also writes the current `TIMESTAMP` and `total return for all coins` to the `output.csv`.
This file is automatically created, when the first pair of values is written.    
The output format is: `yyyy-MM-ddThh:mm:ss.SSSSSSS;FLOAT_TWO_DECIMAL_PLACES`    
For example, this is a valid output file:
````csv
TIMESTAMP;TOTAL_RETURN
2021-02-08T20:52:11.6297904;15.06
2021-02-08T20:52:16.7473269;14.93
2021-02-08T20:52:21.8486597;14.93
2021-02-08T20:52:26.6585911;15.00
````
### Options
Using the flag `-freq <time in sec>` you can set the frequency of updating.
For example, when executing like this, the program will check for updates and display them on the console every 10 seconds.
````
> java -jar target/coinCalc-${VERSION}.jar -freq 10
````
Using the flag `-coinbase` you can enable the subtraction of fees you payed on coinbase. This will show the total payed fees and correctly subtracts the fees from you investment. This flag can be used in combination with the `-freq <time in sec>`-Option.   
See the examples below
````
> java -jar target/coinCalc-${VERSION}.jar -coinbase
> java -jar target/coinCalc-${VERSION}.jar -freq 10 -coinbase
> java -jar target/coinCalc-${VERSION}.jar -coinbase -freq 10
````
