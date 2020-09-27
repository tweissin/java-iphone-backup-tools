package com.trw;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;

import java.io.File;
import java.util.*;

public class DumpPlist {
    public static void dump(NSDictionary dictionary) {
        System.out.println("Dumping plist info");
        dictionary.forEach((k,v) -> {
            if (v instanceof NSString) {
                System.out.println(" " + k + " -> " + v.toString());
            }
            if (v instanceof NSArray) {
                dumpArray(k, (NSArray) v, " ");
            }
            if (v instanceof NSDictionary) {
                System.out.println(" " + k + " dictionary");
                NSDictionary dict = (NSDictionary)v;
                TreeMap<String,NSObject> tmap = new TreeMap<>();
                dict.forEach(tmap::put);
                tmap.forEach((sk,sv) -> {
                    if (sv instanceof NSArray) {
                        dumpArray(sk, (NSArray) sv, "  -> ");
                    } else {
                        System.out.println("  -> " + sk + " -> " + sv.toString());
                    }
                });
            }
        });
    }

    private static void dumpArray(String sk, NSArray sv, String s) {
        System.out.println(s + sk + " array");
        NSArray array = sv;
        List<String> items = new ArrayList<>();
        for (NSObject nsObject : array.getArray()) {
            items.add(nsObject.toString());
        }
        Collections.sort(items);
        items.forEach(item-> {
            System.out.println("  -> " + item);
        });
    }
}
