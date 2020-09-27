package com.trw;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "backup-helper", mixinStandardHelpOptions = true, version = "0.1",
        description = "iPhone backup helper.")
public class BackupHelper implements Callable<Integer> {
    @Option(names = {"-c"}, required = true, description = "Commands: plist list-backups")
    String command;

    @Option(names = {"-d"}, description = "Directory")
    File dir;

    @Option(names = {"-f"}, description = "Filename")
    String filename;

    @Option(names = {"-o"}, description = "Output directory")
    File outputDir;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new BackupHelper()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        File file;
        switch (command) {
            case "plist" -> {
                if (dir == null || filename == null) {
                    System.out.println("Both directory and filename are required");
                    return 1;
                }
                file = new File(dir, filename);
                var rootDict = (NSDictionary) PropertyListParser.parse(file);
                DumpPlist.dump(rootDict);
            }
            case "list-backups" -> {
                if (dir == null) {
                    dir = new File(System.getenv("APPDATA") + "\\Apple Computer\\MobileSync\\Backup");
                }
                ListBackups.doIt(dir);
            }
            case "list-recordings" -> {
                if (dir == null) {
                    System.out.println("Directory is required");
                    return 1;
                }
                file = new File(dir, "Manifest.db");
                Recordings.listRecordings(file);
            }
            case "download-recordings" -> {
                if (dir == null || outputDir == null) {
                    System.out.println("Both input directory and output directory are required");
                    return 1;
                }
                file = new File(dir, "Manifest.db");
                Recordings.downloadRecordings(file, outputDir);
            }
        }
        return 0;
    }
}
