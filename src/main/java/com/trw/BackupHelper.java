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
    @Option(names = {"-c"}, required = true, description = "Commands: plist")
    String command;

    @Option(names = {"-d"}, required = true, description = "Directory")
    File dir;

    @Option(names = {"-f"}, required = true, description = "Filename")
    String filename;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new BackupHelper()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        if (command.equals("plist")) {
            var file = new File(dir, filename);
            var rootDict = (NSDictionary) PropertyListParser.parse(file);
            DumpPlist.dump(rootDict);
        }
        return 0;
    }
}
