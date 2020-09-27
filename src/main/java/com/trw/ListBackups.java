package com.trw;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;

import java.io.File;
import java.io.FileFilter;

public class ListBackups {
    public static void doIt(File rootDir) throws Exception {
        File[] dirs = rootDir.listFiles(File::isDirectory);
        for (File dir : dirs) {
            System.out.println("Backup " + dir.getName());
            dumpBackupInfo(dir);
        }
    }

    private static void dumpBackupInfo(File dir) throws Exception {
        var infoFile = new File(dir, "Info.plist");
        if (!infoFile.exists()) {
            return;
        }
        var rootDict = (NSDictionary) PropertyListParser.parse(infoFile);
        dumpInfo("Display Name", rootDict);
        dumpInfo("Last Backup Date", rootDict);
        dumpInfo("iTunes Version", rootDict);
        dumpInfo("Device Name", rootDict);
        dumpInfo("Phone Number", rootDict);
        dumpInfo("Product Name", rootDict);
        dumpInfo("Product Version", rootDict);
    }

    private static void dumpInfo(String field, NSDictionary rootDict) {
        System.out.println(" -> " + field + ": " + rootDict.get(field));
    }
}
