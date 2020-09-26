package com.trw;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveOff {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Map<String, String> paths = readPaths();
        String sourceDir = "C:\\Users\\tweis\\AppData\\Roaming\\Apple Computer\\MobileSync\\Backup\\000e903852948d1b9729e79c039fc3f14941535b-20180801-210921";
        String destDir = "C:\\Users\\tweis\\OneDrive\\Documents\\TryingToSaveVoiceRecordings-Again\\files";
        Set<Path> files = getFiles(paths, sourceDir, 10);

        files.forEach(sourceFile -> {
            String friendlyName = paths.get(sourceFile.getFileName().toString());
            friendlyName = friendlyName.substring(friendlyName.lastIndexOf("/")+1);
            Path targetFile = Paths.get(destDir + "\\" + friendlyName + ".m4a");
            try {
                Files.copy(sourceFile, targetFile,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(files);
    }

    private static Map<String, String> readPaths() throws ClassNotFoundException, SQLException {
        var filePaths = new HashMap<String,String>();
        DriverManager.registerDriver(new org.sqlite.JDBC());
        var conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\tweis\\OneDrive\\Documents\\TryingToSaveVoiceRecordings-Again\\Manifest.db");
        var stmt = conn.createStatement();

        String sql = "select relativePath, fileID from Files where relativePath like '%Recordings%.m4a';";
        var rs = stmt.executeQuery(sql);
        while(rs.next()){
            String relativePath = rs.getString("relativePath");
            String fileId = rs.getString("fileID");
            System.out.println("File ID: " + fileId);
            filePaths.put(fileId, relativePath);
        }
        rs.close();
        conn.close();
        return filePaths;
    }

    private static Set<Path> getFiles(Map<String, String> filesToSave, String dir, int depth) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                    .filter(file -> {
                        boolean isFile = !Files.isDirectory(file);
                        boolean yes = (isFile && filesToSave.containsKey(file.getFileName().toString()));
                        return yes;
                    })
                    .collect(Collectors.toSet());
        }
    }
}
