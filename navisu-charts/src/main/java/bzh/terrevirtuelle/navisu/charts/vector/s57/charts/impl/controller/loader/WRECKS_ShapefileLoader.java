/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.loader;

import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.view.constants.CATWRK;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.view.constants.EXPSOU;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.view.constants.QUASOU;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Wrecks;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.SurfaceText;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Serge Morvan
 * @date 4 juin 2014 NaVisu project
 */
public class WRECKS_ShapefileLoader
        extends LayerShapefileLoader {

    ShapefileRecord record;
    private final List<Wrecks> wrecks;
    private Set<Map.Entry<String, Object>> entries;
    private Wrecks wreck;

    public WRECKS_ShapefileLoader() {
        wrecks = new ArrayList<>();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    @Override
    protected Renderable createPoint(ShapefileRecord record, double latDegrees, double lonDegrees,
            PointPlacemarkAttributes attrs) {
        SurfaceText surfaceText = null;
        this.record = record;
        entries = record.getAttributes().getEntries();
        wreck = new Wrecks();
        wreck.setLatitude(latDegrees);
        wreck.setLongitude(lonDegrees);
        entries.stream().forEach((e) -> {
            if (e.getValue() != null) {
                if (e.getKey().equals("CATWRK")) {
                    wreck.setCategoryOfWreck(e.getValue().toString());
                }
                if (e.getKey().equals("EXPSOU")) {
                    wreck.setExpositionOfSounding(e.getValue().toString());
                }
                if (e.getKey().equals("QUASOU")) {
                    wreck.setQualityOfSoundingMeasurement(e.getValue().toString());
                }
                if (e.getKey().equals("TECSOU")) {
                    wreck.setTechniqueOfSoundingMeasurement(e.getValue().toString());
                }
                if (e.getKey().equals("VALSOU")) {
                    wreck.setValueOfSounding(e.getValue().toString());
                }
                if (e.getKey().equals("WATLEV")) {
                    wreck.setWaterLevelEffect(((Long) e.getValue()).toString());
                }
                if (e.getKey().equals("QUALTY")) {
                    wreck.setQualityOfSoundingMeasurement(((Long) e.getValue()).toString());
                }
            }
        }
        );
        if (wreck.getValueOfSounding() != null) {
            surfaceText = new SurfaceText(wreck.getValueOfSounding(), Position.fromDegrees(latDegrees, lonDegrees, 0));
        } else {
            surfaceText = new SurfaceText("     ", Position.fromDegrees(latDegrees, lonDegrees, 0));
        }
        surfaceText.setColor(Color.black);
        surfaceText.setTextSize(20.0);
        String tecsou = wreck.getTechniqueOfSoundingMeasurement();
        String label = null;
        // String s = String.format("%-12.5f%.20f", 12.23429837482, 9.10212023134);
        /*
         if (tecsou == null) {
         label = "WRECK \n"
         + "Lat : " + new Float(wreck.getLat()).toString() + "\n "
         + "Lon : " + new Float(wreck.getLon()).toString() + "\n"
         + "CATWRK : " + CATWRK.ATT.get(wreck.getCategoryOfWreck()) + "\n"
         + "EXPSOU : " + EXPSOU.ATT.get(wreck.getExpositionOfSounding()) + "\n"
         + "QUASOU : " + QUASOU.ATT.get(wreck.getQualityOfSoundingMeasurement()) + "\n"
         + "VALSOU : " + wreck.getValueOfSounding() + " m \n"
         + "WATLEV : " + WATLEV.ATT.get(wreck.getWaterLevelEffect()) + "\n";
         } else {
         label = "WRECK \n"
         + "Lat : " + new Float(wreck.getLat()).toString() + "\n "
         + "Lon : " + new Float(wreck.getLon()).toString() + "\n"
         + "CATWRK : " + CATWRK.ATT.get(wreck.getCategoryOfWreck()) + "\n"
         + "EXPSOU : " + EXPSOU.ATT.get(wreck.getExpositionOfSounding()) + "\n"
         + "QUASOU : " + QUASOU.ATT.get(wreck.getQualityOfSoundingMeasurement()) + "\n"
         + "TECSOU : " + TECSOU.ATT.get(wreck.getTechniqueOfSoundingMeasurement()) + "\n"
         + "VALSOU : " + wreck.getValueOfSounding() + " m \n"
         + "WATLEV : " + WATLEV.ATT.get(wreck.getWaterLevelEffect()) + "\n";
         }
         */

        if (tecsou == null) {
            tecsou = "";
            label = String.format("%-29s\n"
                    + "%-29s"
                    + "%-29s"
                    + "%-29s"
                    + "%-29s"
                    + "%-29s"
                    + "%-29s",
                    "WRECK",
                    "Lat : " + wreck.getLatitude() + "\n",
                    "Lon : " + wreck.getLongitude() + "\n",
                    "CATWRK : " + CATWRK.ATT.get(wreck.getCategoryOfWreck()) + "\n",
                    "EXPSOU : " + EXPSOU.ATT.get(wreck.getExpositionOfSounding()) + "\n",
                    "QUASOU : " + QUASOU.ATT.get(wreck.getQualityOfSoundingMeasurement()) + "\n",
                    "VALSOU : " + wreck.getValueOfSounding()
            );
           // System.out.println(label);
            //   "CATWRK : ", CATWRK.ATT.get(wreck.getCategoryOfWreck()),
            //   "EXPSOU : ", EXPSOU.ATT.get(wreck.getExpositionOfSounding()),
            //   "QUASOU : ", QUASOU.ATT.get(wreck.getQualityOfSoundingMeasurement()),
            //   "VALSOU : ", wreck.getValueOfSounding());
            /*
            
             + "CATWRK : " + CATWRK.ATT.get(wreck.getCategoryOfWreck()) + "\n"
             + "EXPSOU : " + EXPSOU.ATT.get(wreck.getExpositionOfSounding()) + "\n"
             + "QUASOU : " + QUASOU.ATT.get(wreck.getQualityOfSoundingMeasurement()) + "\n"
             + "VALSOU : " + wreck.getValueOfSounding() + " m \n"
             + "WATLEV : " + WATLEV.ATT.get(wreck.getWaterLevelEffect()) + "\n";
             } else {
             label = "WRECK \n"
             + "Lat : " + new Float(wreck.getLat()).toString() + "\n "
             + "Lon : " + new Float(wreck.getLon()).toString() + "\n"
             + "CATWRK : " + CATWRK.ATT.get(wreck.getCategoryOfWreck()) + "\n"
             + "EXPSOU : " + EXPSOU.ATT.get(wreck.getExpositionOfSounding()) + "\n"
             + "QUASOU : " + QUASOU.ATT.get(wreck.getQualityOfSoundingMeasurement()) + "\n"
             + "TECSOU : " + TECSOU.ATT.get(wreck.getTechniqueOfSoundingMeasurement()) + "\n"
             + "VALSOU : " + wreck.getValueOfSounding() + " m \n"
             + "WATLEV : " + WATLEV.ATT.get(wreck.getWaterLevelEffect()) + "\n";
             */
        }
        surfaceText.setValue(AVKey.DISPLAY_NAME, label);

        return surfaceText;
    }

    public List<Wrecks> getWrecks() {
        return wrecks;
    }

}
