# Digital Marketplace Alerter

[![Build Status](https://travis-ci.org/AndrewGorton/DigitalMarketplaceAlerter.svg?branch=master)](https://travis-ci.org/AndrewGorton/DigitalMarketplaceAlerter)

## So what is it?

The [UK Government](https://www.gov.uk) advertises packages of work on the [Digital Marketplace](https://www.digitalmarketplace.service.gov.uk/digital-outcomes-and-specialists/opportunities) website.

This application monitors that website, and can notify people of any new opportunities which appear.


## Building

Maven3 and JDK8 is required. A fat jar is produced via Maven Package in the target subdirectory.
```
$ mvn package
<snip>
Process finished with exit code 0
$ ls -sh target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar
23M target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar
$
```

## Running

Copy the ```config/local.yml.sample``` file to ```config/local.yml``` and edit the contents (specifically the port at the top, and the email details at the bottom of the file.)

Test the database migrations first.

```
$ java -jar target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar db migrate config/local.yml --dry-run
<snip>
-- Release Database Lock
UPDATE PUBLIC.DATABASECHANGELOGLOCK SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

INFO  [2016-09-26 12:11:38,504] liquibase: Successfully released change log lock
$
```

If no errors, then run the migration scripts.

```
$ java -jar target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar db migrate config/local.yml
<snip>
INFO  [2016-09-26 12:15:06,218] liquibase: Successfully released change log lock
$ 
```

If this is the first time running, set the admin password on the database

```
$ java -jar target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar setuserpassword -u admin -p new_password_here config/local.yml
INFO  [2016-09-26 12:16:02,643] org.eclipse.jetty.util.log: Logging initialized @1224ms
INFO  [2016-09-26 12:16:02,730] io.dropwizard.assets.AssetsBundle: Registering AssetBundle with name: assets for path /static/*
Salted password: Y9v3wAX58S0mnew_password_here
Hash of salted password: 8dda6faf9e69379a7cc12f05ee0db9c0ee24798fd26f677d70747bc83caf9947
Updated
$
```

You can also create new users and assign passwords to them by running the following command:

```
$ java -jar target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar createnewuser -u username -p password config/local.yml
INFO  [2016-10-24 10:48:49,857] org.eclipse.jetty.util.log: Logging initialized @1248ms
INFO  [2016-10-24 10:48:49,935] io.dropwizard.server.SimpleServerFactory: Registering jersey handler with root path prefix: /dma
INFO  [2016-10-24 10:48:49,935] io.dropwizard.server.SimpleServerFactory: Registering admin handler with root path prefix: /dma/admin
INFO  [2016-10-24 10:48:49,935] io.dropwizard.assets.AssetsBundle: Registering AssetBundle with name: assets for path /static/*
Salted password: IvY2bTG39jdEpassword
Hash of salted password: c7837936972d8c1e312fe91a837abc79d65f88571cfc82a517d09d4d439594e6
A new user with username 'username' and password 'password' has been created!

```

Now you can start it.

```
$ java -jar target/DigitalMarketplaceAlerter-1.0.6-SNAPSHOT.jar server config/local.yml
<snip>
INFO  [2016-09-26 12:16:57,023] org.eclipse.jetty.server.Server: Started @3241ms
```

The software won't return to the command line - you can use ```CTRL-C``` to terminate it. Browse to the host and port, and 
follow the link to log in to view the list of opportunities this software has seen, and the list of people to alert when
new opportunities are detected.

The regex from the alert is matched against the opportunity's customer, and if it matches an email is sent. To 
match all alerts, use a regex of ```.*```. You can temporarily disable alerts, or delete them completely from the
list of alerts.

In the Opportunities view, you can "unalert" an opportunity. It will then be picked up for notifications on the
next sweep (10 seconds) and emails alerts generated. This works well if you had an email outage.

### Diagnostics

This app uses an embedded [H2 database](http://www.h2database.com/). You can view the contents
of the database with the following:-

```
$ java -jar ~/.m2/repository/com/h2database/h2/1.4.192/h2-1.4.192.jar
```

The Digital Markeplace website is processed every 30 minutes.

Alerts monitor the database every 10 seconds for any new opportunities which have not been 
marked as alerted, and generates emails based on these.


The application is a [Dropwizard](http://www.dropwizard.io) application.

### Licensing

Please read [LICENSE.txt](LICENSE.txt) for licensing information.

### Troubleshooting

*Help! I'm behind a proxy!*

```
/usr/bin/java -Dhttps.proxyHost=my.proxy.com -Dhttps.proxyPort=3128 -jar <jarFile> server <configFile>
```
