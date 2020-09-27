# iPhone backup tools for Java

Building off the great work done here: https://github.com/richinfante/iphonebackuptools

This provides a subset of utilities written in Java.

See also
- https://www.richinfante.com/2017/3/16/reverse-engineering-the-ios-backup

## Prerequisites

- Java 14

## Building and running

```
mvn clean install
%JAVA_HOME%\bin\java -jar target\backup-helper.jar
```

Examples:

Listing contents of a plist file
```
%JAVA_HOME%\bin\java -jar target\backup-helper.jar -c plist -d "%APPDATA%\Apple Computer\MobileSync\Backup\BACKUP-ID" -f "Info.plist"
```

Listing all backups
```
%JAVA_HOME%\bin\java -jar target\backup-helper.jar -c list-backups
```

List recordings
```
%JAVA_HOME%\bin\java -jar target\backup-helper.jar -c list-recordings -b 000e903852948d1b9729e79c039fc3f14941535b-20180801-210921
```

Download all recordings
```
%JAVA_HOME%\bin\java -jar target\backup-helper.jar -c download-recordings -o c:\temp -b 000e903852948d1b9729e79c039fc3f14941535b-20180801-210921
```