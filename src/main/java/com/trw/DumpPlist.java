package com.trw;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;

import java.io.File;

public class DumpPlist {
    public static void dump(NSDictionary dictionary) {
        System.out.println("Dumping plist info");
        dictionary.forEach((k,v) -> {
            if (v instanceof NSString) {
                System.out.println(" " + k + " -> " + v.toString());
            }
        });
    }
}
