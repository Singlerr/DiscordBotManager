package io.github.singlerr.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static List<File> getAllJarFiles(String path){
        File parent = new File(path);
        if(! parent.exists())
            return new ArrayList<>();
        return List.of(parent.listFiles()).stream().filter(f -> f.getName().endsWith(".jar")).collect(Collectors.toList());
    }
}
