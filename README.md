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

Example:
```
%JAVA_HOME%\bin\java -jar target\backup-helper.jar -c plist -d "%APPDATA%\Apple Computer\MobileSync\Backup\BACKUP-ID" -f "Info.plist"
```

