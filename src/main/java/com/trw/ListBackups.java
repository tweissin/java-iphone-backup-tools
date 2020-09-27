package com.trw;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;

import java.io.File;

public class ListBackups {
    public static void listBackups(File rootDir) throws Exception {
        File[] dirs = rootDir.listFiles(File::isDirectory);
        for (File dir : dirs) {
            StringBuilder sb = new StringBuilder();
            sb.append("Backup ID " + dir.getName());

            var infoFile = new File(dir, "Info.plist");
            if (infoFile.exists()) {
                var rootDict = (NSDictionary) PropertyListParser.parse(infoFile);
                String displayName = rootDict.get("Display Name").toString();
                String lastBackupDate = rootDict.get("Last Backup Date").toString();
                sb.append(" [").append(displayName).append("] on ").append(lastBackupDate);
            }
            System.out.println(sb.toString());
        }
    }

    public static void listBackupDetails(File rootDir) throws Exception {
        File[] dirs = rootDir.listFiles(File::isDirectory);
        for (File dir : dirs) {
            System.out.println("Backup ID " + dir.getName());
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
