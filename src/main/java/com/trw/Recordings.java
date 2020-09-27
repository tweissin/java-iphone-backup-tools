package com.trw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Recordings {
    public static void downloadRecordings(File file, File outputDir) throws Exception {
        var recordings = getRecordings(file);
        Map<String,String> fileIdToName = new HashMap<>();
        recordings.forEach(recording-> {
            fileIdToName.put(recording.get("fileID"), recording.get("name"));
        });
        downloadFiles(fileIdToName, file.getParent(), outputDir);
    }

    private static void downloadFiles(Map<String, String> filesToSave, String dir, File destDir) throws IOException {
        System.out.println("Finding files in " + dir);
        try (Stream<Path> stream = Files.walk(Paths.get(dir), 4)) {
            stream.filter(file -> {
                boolean isFile = !Files.isDirectory(file);
                boolean yes = (isFile && filesToSave.containsKey(file.getFileName().toString()));
                return yes;
            }).forEach(sourceFile -> {
                String friendlyName = filesToSave.get(sourceFile.getFileName().toString());
                Path targetFile = Paths.get(destDir.getAbsolutePath() + "\\" + friendlyName);
                System.out.println(" - writing file " + targetFile);
                try {
                    Files.copy(sourceFile, targetFile,
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    public static void listRecordings(File file) throws Exception {
        var recordings = getRecordings(file);
        recordings.forEach(r->{
            System.out.println(r.get("name"));
        });
    }

    public static ArrayList<Map<String, String>> getRecordings(File file) throws Exception {
        DriverManager.registerDriver(new org.sqlite.JDBC());
        var conn = DriverManager.getConnection("jdbc:sqlite:" + file.getCanonicalPath());
        var stmt = conn.createStatement();
        var sql = "select fileID, relativePath from Files where relativePath like 'Media/Recordings/%.m4a' order by relativePath;";
        var rs = stmt.executeQuery(sql);
        var names = new ArrayList<Map<String,String>>();
        var pattern  = Pattern.compile("Media/Recordings/(.*).m4a");
        while(rs.next()) {
            var fileID = rs.getString("fileID");
            var relativePath = rs.getString("relativePath");
            var matcher = pattern.matcher(relativePath);
            if (matcher.find()) {
                var name = matcher.group(1);
                var info = new HashMap<String,String>();
                info.put("name", name + ".m4a");
                info.put("fileID", fileID);
                names.add(info);
            }
        }
        rs.close();
        conn.close();
        return names;
    }
}
