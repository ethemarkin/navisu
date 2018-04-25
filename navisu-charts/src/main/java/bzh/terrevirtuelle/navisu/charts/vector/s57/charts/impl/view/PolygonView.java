/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.charts.impl.view;

import bzh.terrevirtuelle.navisu.core.view.geoview.worldwind.impl.GeoWorldWindViewImpl;
import bzh.terrevirtuelle.navisu.topology.TopologyServices;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import java.util.Map;

/**
 *
 * @author serge
 */
public class PolygonView
        implements PolyGeomView {

    protected TopologyServices topologyServices;
    protected RenderableLayer layer;
    protected Polygon polygon;
    protected WorldWindow wwd = GeoWorldWindViewImpl.getWW();
    protected String label;
    String tmp = "";

    public PolygonView(TopologyServices topologyServices, RenderableLayer layer) {
        this.topologyServices = topologyServices;
        this.layer = layer;
    }

    @SuppressWarnings("unchecked")
    //   @Override
    public void display(String geometries, ShapeAttributes attrs, ShapeAttributes hattrs,
            Map<String, String> labels) {
        // System.out.println("geometries : " + geometries);
        polygon = topologyServices.wktMultiPolygonToWwjPolygon(geometries);
        polygon.setAttributes(attrs);
        polygon.setHighlightAttributes(hattrs);
        polygon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        if (labels != null) {
            labels.keySet().forEach((key) -> {
                tmp = labels.get(key);
                if (tmp != null) {
                    label += " " + tmp + "\n";
                }
            });
            polygon.setValue(AVKey.DISPLAY_NAME, label);
        }
        layer.addRenderable(polygon);
    }
}
