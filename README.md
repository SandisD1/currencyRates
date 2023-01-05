# currencyRates

Enter the following command into you command-line to create a docker image for the mariaDB database that is used in this project.
-docker create -p 3306:3306 --name currency-database -e MARIADB_USER=example-user -e MARIADB_PASSWORD=my_cool_secret -e MARIADB_ROOT_PASSWORD=my-secret-pw -e MARIADB_DATABASE=currencydb mariadb:latest

Enter the following command into you command-line to start the mariaDB image in docker.
Once the command is executed please wait about 2 minutes for the database to initialize and become accessable before starting the app or update images.
-docker start currency-database

Enter the following command into you command-line to create a docker image that updates the currency rate values in your mariadb database.
-docker create --name database-update currency-javalin update

Enter the following command into you command-line to start the docker image that updates the currency rate values in your mariadb database.
-docker start database-update

Enter the following command into you command-line to create a docker image for the web-application with endponts.
-docker create -p 7070:7070 --name javalin-app currency-javalin runApp

Enter the following command into you command-line to start the docker image for the web-application with endponts.
-docker start javalin-app

To access the application Endpoints:
-Enter http://localhost:7070/latest into your browser to see the latest rates for all currencies.
-Enter http://localhost:7070/AUD into your browser to see the stored history of rates for the specific currency specified by the three letter currency code at the end of URL.

