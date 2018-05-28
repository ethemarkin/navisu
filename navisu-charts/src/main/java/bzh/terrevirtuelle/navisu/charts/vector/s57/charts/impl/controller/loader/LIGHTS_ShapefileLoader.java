/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.loader;

import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.controller.S57ChartComponentController;
import bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.view.S57LightView;
import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.view.constants.COLOR;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.view.constants.COLOR_NAME;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Light;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Renderable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Serge Morvan
 * @date 4 juin 2014 NaVisu project
 */
public class LIGHTS_ShapefileLoader
        extends LayerShapefileLoader {

    private final List<Light> lightList;
    private Set<Map.Entry<String, Object>> entries;
    private Light data;
    private final RenderableLayer airspaceLayer;
    protected WorldWindow wwd;
    protected Globe globe;
    private double elevation;
    private final S57ChartComponentController chartS57Controller;

    public LIGHTS_ShapefileLoader() {
        lightList = new ArrayList<>();
        chartS57Controller = S57ChartComponentController.getInstance();
        layer = chartS57Controller.getLayersManagerServices().getLayer("S57 charts", "LIGHTS");
        layer.setPickEnabled(true);
        airspaceLayer = chartS57Controller.getLayersManagerServices().getLayer("S57 charts", "LIGHTS_SECTORS");
        airspaceLayer.setPickEnabled(true);
        globe = GeoWorldWindViewImpl.getWW().getModel().getGlobe();
        if (!chartS57Controller.getLayers().contains(layer)) {
            chartS57Controller.getLayers().add(layer);
        }
    }

    @Override
    protected PointPlacemarkAttributes createPointAttributes(ShapefileRecord record) {
        PointPlacemarkAttributes normalAttributes = new PointPlacemarkAttributes();
        return normalAttributes;
    }

    @Override
    protected Renderable createPoint(ShapefileRecord record, double latDegrees, double lonDegrees,
            PointPlacemarkAttributes attrs) {

        entries = record.getAttributes().getEntries();
        elevation = globe.getElevation(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees));
        data = new Light();
        //System.out.println("entries " + entries);
        lightList.add(data);
        data.setLatitude(latDegrees);
        data.setLongitude(lonDegrees);
        entries.stream().forEach((e) -> {
            if (e.getKey().equals("RCID")) {
                data.setId((Long) e.getValue());
            } else if (e.getKey().equals("COLOUR")) {
                data.setColour((String) e.getValue());
            } else if (e.getKey().equals("SIGPER")) {
                if (e.getValue() != null) {
                    data.setSignalPeriod(e.getValue().toString());
                }
            } else if (e.getKey().equals("VALNMR")) {
                if (e.getValue() != null) {
                    data.setValueOfNominalRange(e.getValue().toString());
                }
            } else if (e.getKey().equals("HEIGHT")) {
                if (e.getValue() != null) {
                    data.setHeight(e.getValue().toString());
                }
            } else if (e.getKey().equals("SECTR1")) {
                if (e.getValue() != null) {
                    data.setSectorLimitOne(e.getValue().toString());
                }
            } else if (e.getKey().equals("SECTR2")) {
                if (e.getValue() != null) {
                    data.setSectorLimitTwo(e.getValue().toString());
                }
            }
        });

        S57LightView lightView = new S57LightView(data);

        float range = 1;
        if (data.getValueOfNominalRange() != null) {
            range = new Float(data.getValueOfNominalRange());
            range *= 0.3;
        }
        if (data.getSectorLimitOne() != null && data.getSectorLimitTwo() != null) {
            lightView.setCenter(new LatLon(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees)));
            lightView.setRadii(140.0 * range, 150.0 * range);
            if (data.getHeight() != null) {
                lightView.setAltitude(elevation + new Double(data.getHeight()));
                // creation du volume de la lumiere
                //  lightView.setAltitudes(100, 300);
            } else {
                lightView.setAltitude(elevation + 35.0);
            }

            ArrayList<Position> pathPositions = new ArrayList<>();
            pathPositions.add(Position.fromDegrees(latDegrees, lonDegrees, elevation));//35.0
            LatLon latLon = Position.greatCircleEndPosition(new LatLon(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees)),
                    Angle.fromDegrees(new Double(data.getSectorLimitOne()) - 180),
                    Angle.fromDegrees(.0015 * range));
            pathPositions.add(new Position(latLon.getLatitude(), latLon.getLongitude(), elevation ));//+35.0
            Path line = new Path(pathPositions);
            layer.addRenderable(line);

            pathPositions.clear();
            pathPositions.add(Position.fromDegrees(latDegrees, lonDegrees, elevation ));//+ 35.0
            latLon = Position.greatCircleEndPosition(new LatLon(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees)),
                    Angle.fromDegrees(new Double(data.getSectorLimitTwo()) - 180),
                    Angle.fromDegrees(.0015 * range));
            pathPositions.add(new Position(latLon.getLatitude(), latLon.getLongitude(), elevation));// + 35.0
            line = new Path(pathPositions);
            layer.addRenderable(line);

            lightView.setAzimuths(Angle.fromDegrees(new Float(data.getSectorLimitOne()) + 180),
                    Angle.fromDegrees(new Float(data.getSectorLimitTwo()) + 180));
            String label = "Light \n"
                    + "Lat : " + Double.toString(latDegrees) + "\n"
                    + "Lon : " + Double.toString(lonDegrees) + "\n"
                    + "Color : " + COLOR_NAME.getColor(data.getColour()) + "\n"
                    + (data.getSignalPeriod() != null ? "Period : " + data.getSignalPeriod() + " s" + "\n" : "")
                    + (data.getHeight() != null ? "Height : " + data.getHeight() + " m" + "\n" : "")
                    + (data.getValueOfNominalRange() != null ? "Nominal range : " + data.getValueOfNominalRange() + " Nm" + "\n" : "")
                    + "Sect1 : " + data.getSectorLimitOne() + "\n"
                    + "Sect2 : " + data.getSectorLimitTwo() + "\n";
            lightView.setValue(AVKey.DISPLAY_NAME, label);
            lightView.getAttributes().setDrawOutline(true);
            // Si la couleur est blanche, la vue est jaune
            if (data.getColour().contains("1")) {
                lightView.getAttributes().setInteriorMaterial(new Material(COLOR.ATT.get("6")));
                lightView.getAttributes().setOutlineMaterial(new Material(COLOR.ATT.get("6")));
            } else {
                lightView.getAttributes().setInteriorMaterial(new Material(COLOR.ATT.get(data.getColour())));
                lightView.getAttributes().setOutlineMaterial(new Material(COLOR.ATT.get(data.getColour())));
            }

            airspaceLayer.addRenderable(lightView);
        } else {

            lightView.setCenter(new LatLon(Angle.fromDegrees(latDegrees), Angle.fromDegrees(lonDegrees)));
            if (data.getColour() != null) {
                if (!data.getColour().contains("1")) {
                    lightView.setRadii(100.0 * range, 110.0 * range);
                } else {
                    lightView.setRadii(140.0 * range, 150.0 * range);
                }
            }
            if (data.getHeight() != null) {
                lightView.setAltitude(elevation + new Double(data.getHeight()));
            } else {
                lightView.setAltitude(elevation );//+ 35.0
            }
            lightView.setAzimuths(Angle.fromDegrees(0), Angle.fromDegrees(360));
            String label = "Light \n"
                    + "Lat : " + Double.toString(latDegrees) + "\n"
                    + "Lon : " + Double.toString(lonDegrees) + "\n"
                    + "Color : " + COLOR_NAME.getColor(data.getColour()) + "\n"
                    + (data.getSignalPeriod() != null ? "Period : " + data.getSignalPeriod() + " s" + "\n" : "")
                    + (data.getHeight() != null ? "Height : " + data.getHeight() + " m" + "\n" : "")
                    + (data.getValueOfNominalRange() != null ? "Nominal range : " + data.getValueOfNominalRange() + " Nm" + "\n" : "");
            lightView.setValue(AVKey.DISPLAY_NAME, label);
            lightView.getAttributes().setDrawOutline(true);

            // Si la couleur est blanche, la vue est jaune
            if (data.getColour() != null) {
                if (data.getColour().contains("1")) {
                    lightView.getAttributes().setInteriorMaterial(new Material(COLOR.ATT.get("6")));
                    lightView.getAttributes().setOutlineMaterial(new Material(COLOR.ATT.get("6")));
                } else {
                    lightView.getAttributes().setInteriorMaterial(new Material(COLOR.ATT.get(data.getColour())));
                    lightView.getAttributes().setOutlineMaterial(new Material(COLOR.ATT.get(data.getColour())));
                }
            }
        }
        airspaceLayer.addRenderable(lightView);

        return lightView;
    }

    public List<Light> getLights() {
        return lightList;
    }

    public RenderableLayer getAirspaceLayer() {
        return airspaceLayer;
    }

}
