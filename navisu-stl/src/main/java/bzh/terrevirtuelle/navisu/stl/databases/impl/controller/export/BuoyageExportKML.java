/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.stl.databases.impl.controller.export;

import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Buoyage;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Landmark;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.view.constants.BUOYAGE_INV;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author serge
 * @date 6/7/2018
 *
 */
public class BuoyageExportKML {

    String filename;
    protected RenderableLayer layer;
    protected List<Buoyage> buoyages;
    static int id = 0;
    protected String acronym;
    protected WorldWindow wwd = GeoWorldWindViewImpl.getWW();
    protected double lat;
    protected double lon;
    protected double elv;

    public BuoyageExportKML(String filename) {
        this.filename = filename;
    }

    public void export(List<Buoyage> buoyages) {
        String base = "";
        try {
            base = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException ex) {
            Logger.getLogger(BuoyageExportKML.class.getName()).log(Level.SEVERE, null, ex);
        }
        base = base.replace("</Document>", "");
        base = base.replace("</kml>", "");
        String buoys = "";
        for (Buoyage buoyage : buoyages) {
            acronym = BUOYAGE_INV.ATT.get(buoyage.getClass().getSimpleName());
            lat = buoyage.getLatitude();
            lon = buoyage.getLongitude();
            elv = Double.valueOf(buoyage.getElevation());
            String imageAddress = "";
            if (acronym.equals("LNDMRK")) {
                imageAddress = "img/landmarks_" + buoyage.getMarsys() + "/"
                        + acronym + "_"
                        + buoyage.getCategoryOfMark() + "_"
                        + buoyage.getConspicuousVisually() + "_"
                        + ((Landmark) buoyage).getFunction() + "_"
                        + buoyage.getColour() + "_"
                        + buoyage.getColourPattern() + "_"
                        + buoyage.getMarsys()
                        + ".png";
            } else {
                if (acronym.equals("DAYMAR")) {
                    imageAddress = "img/daymarks_"
                            + buoyage.getMarsys() + "/"
                            + acronym + "_"
                            + buoyage.getShape() + "_"
                            + buoyage.getCategoryOfMark() + "_"
                            + buoyage.getColour() + "_"
                            + buoyage.getColourPattern() + "_"
                            + buoyage.getNatureOfConstruction() + "_"
                            + buoyage.getMarsys()
                            + ".png";
                } else {
                    buoys = buoys.concat(insertedFile(lat, lon, 0.0, "lithops_0.dae"));
                    imageAddress = "img/buoyage_"
                            + buoyage.getMarsys() + "/"
                            + acronym + "_"
                            + buoyage.getShape() + "_"
                            + buoyage.getCategoryOfMark() + "_"
                            + buoyage.getColour() + "_"
                            + buoyage.getColourPattern() + "_"
                            + buoyage.getTopMark() + "_"
                            + buoyage.getMarsys() + ".png";
                }
            }
        }
        java.nio.file.Path path = Paths.get(filename);
        base = base.concat(buoys);
        base = base.concat("</Document>\n </kml>\n");
        try {
            Files.write(path, base.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(BuoyageExportKML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String insertedFile(double latitude, double longitude, double altitude, String model) {
        String result = "<Placemark>\n"
                + "<name>SketchUp Model of Macky Auditorium</name>\n"
                + "<description>University of Colorado, Boulder; model created by Noël Nemcik.</description>\n"
                + "<Model id=\"model_" + id + "\">\n"
                + "<altitudeMode>relativeToGround</altitudeMode>\n"
                + "<Location>\n"
                + "<longitude>" + longitude + "</longitude>\n"
                + "<latitude>" + latitude + "</latitude>\n"
                + "<altitude>" + altitude + "</altitude>\n"
                + "</Location>\n"
                + "<Orientation>\n"
                + "<heading>0</heading>\n"
                + "<tilt>0</tilt>\n"
                + "<roll>0</roll>\n"
                + "</Orientation>\n"
                + "<Scale>\n"
                + "<x>1</x>\n"
                + "<y>1</y>\n"
                + "<z>1</z>\n"
                + "</Scale>\n"
                + "<Link>\n"
                + "<href>/home/serge/Data/developement/ProjetNaVisu/navisu/navisu-launcher/data/collada/buoys/" + model + "</href>"
                + "</Link>\n"
                + "</Model>\n"
                + "</Placemark>\n";
        id++;
        return result;
    }
}