/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.main;

import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.DarkSkyComponentController;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.DarkSkyController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author serge
 * @date Feb 12, 2017
 */
public class Configuration {

    public static void init() {
        initPrivateData();

        String navisuHome = System.getProperty("user.home") + "/.navisu";
        String navisuDir = System.getProperty("user.dir");
        Path navisuHomePath = Paths.get(navisuHome);
        if (!Files.exists(navisuHomePath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(navisuHomePath);
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuHome + "/databases"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuHome + "/v/"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuHome + "/config"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuHome + "/config/"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuHome + "/caches"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuHome + "/caches/"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuHome + "/logs"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuHome + "/logs/"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuDir + "/cmd"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuDir + "/cmd"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuDir + "/privateData/ulhysses"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuDir + "/privateData/ulhysses"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuDir + "/privateData/asc"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuDir + "/privateData/asc"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuDir + "/privateData/elevation"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuDir + "/privateData/elevation"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuDir + "/privateData/tif"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuDir + "/privateData/tif"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (!Files.exists(Paths.get(navisuDir + "/privateData/shp"), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectory(Paths.get(navisuDir + "/privateData/shp"));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }
        String navisuCache = navisuHome + "/caches/caches.properties";
        if (!Files.exists(Paths.get(navisuCache), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createFile(Paths.get(navisuCache));
                writeDefaultCacheProperties(navisuCache);
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        } else {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(navisuCache));
                String dataDir = properties.getProperty("dataDir");
                if (dataDir == null) {
                    writeDefaultCacheProperties(navisuCache);
                }
            } catch (IOException ex) {
                Logger.getLogger(DarkSkyComponentController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }

        String configProperties = navisuHome + "/config/config.properties";
        if (!Files.exists(Paths.get(configProperties), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createFile(Paths.get(configProperties));
            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
            writeDefaultConfigProperties(configProperties);
        } else {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(configProperties));
                String s57ChartsDir = properties.getProperty("s57ChartsDir");
                String darkSkyApiKey = properties.getProperty("darkSkyApiKey");
                String allCountriesPath = properties.getProperty("allCountriesPath");
                String luceneAllCountriesIndexPath = properties.getProperty("luceneAllCountriesIndexPath");

                if (s57ChartsDir == null || darkSkyApiKey == null
                        || allCountriesPath == null || luceneAllCountriesIndexPath == null) {
                    writeDefaultConfigProperties(navisuCache);
                }
            } catch (IOException ex) {
                Logger.getLogger(DarkSkyComponentController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
        }

    }

    private static void writeDefaultConfigProperties(String configProperties) {
        //System.out.println("writeDefaultConfigProperties");
        try {
            List<String> keys = new ArrayList<>(Arrays.asList(
                    "s57ChartsDir", "darkSkyApiKey", "allCountriesPath", "luceneAllCountriesIndexPath",
                    "name", "mmsi", "country", "length", "width", "draught", "shipType", "navigationalStatus",
                    "callSign", "latitude", "longitude", "cog", "sog", "daeModelPath", "scale"
            ));
            Files.write(Paths.get(configProperties), keys, StandardOpenOption.WRITE);
        } catch (IOException ex) {
            Logger.getLogger(DarkSkyController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private static void writeDefaultCacheProperties(String cacheProperties) {

        /*System.out.println("writeDefaultCacheProperties");
        try {
            List<String> keys = new ArrayList<>(Arrays.asList(
                    "dataDir=","S57Stl="
            ));
            Files.write(Paths.get(cacheProperties), keys, StandardOpenOption.WRITE);
        } catch (IOException ex) {
            Logger.getLogger(DarkSkyController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
         */
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(cacheProperties));
        } catch (IOException ex) {
            Logger.getLogger(DarkSkyComponentController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        properties.setProperty("dataDir", "");
        properties.setProperty("S57Stl", "");
        properties.setProperty("KML", "");
        File f = new File(cacheProperties);
        OutputStream out;
        try {
            out = new FileOutputStream(f);
            properties.store(out, "Last directory choosed by user");
            out.close();
        } catch (IOException ex) {
        }
    }

    private static void initPrivateData() {

        String path = "privateData/";
        List<String> subDirs = new ArrayList<>(Arrays.asList("gpx", "kml", "nds", "obj", "x3d", "stl", "ulhysses", "nmea"));

        for (String s : subDirs) {
            File file = new File(path + s);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    /**
     * Adds the specified path to the java library path
     *
     * @param pathToAdd the path to add
     * @throws Exception
     */
    public static void addLibraryPath(String pathToAdd) throws Exception {
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);
        String[] paths = (String[]) usrPathsField.get(null);
        //check if the path to add is already present
        for (String path : paths) {
            if (path.equals(pathToAdd)) {
                return;
            }
        }
        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length - 1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }
}
