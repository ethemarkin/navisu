/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.topology.impl;

import bzh.terrevirtuelle.navisu.domain.util.Pair;
import bzh.terrevirtuelle.navisu.topology.Topology;
import bzh.terrevirtuelle.navisu.topology.TopologyServices;
import com.vividsolutions.jts.algorithm.Centroid;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.io.WKTReader;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.SurfacePolylines;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.capcaval.c3.component.ComponentState;

/**
 *
 * @author Serge Morvan
 * @date Dec 8, 2017
 */
public class TopologyImpl
        implements Topology, TopologyServices, ComponentState {

    @Override
    public void componentStarted() {
        /* Nothing to do here */ }

    @Override
    public void componentStopped() {
        /* Nothing to do here */ }

    @Override
    public void componentInitiated() {
    }

    @Override
    public String wwjPositionsToPolygonWkt(List<? extends Position> positions) {
        String geometry = "POLYGON((";
        int l = positions.size();
        for (int i = 0; i < l - 1; i++) {
            geometry += positions.get(i).getLongitude().getDegrees() + " " + positions.get(i).getLatitude().getDegrees() + ",";
        }
        geometry += positions.get(l - 1).getLongitude().getDegrees() + " " + positions.get(l - 1).getLatitude().getDegrees() + "))";
        return geometry;
    }

    @Override
    public String wwjLatLonsToPolygonWkt(List<LatLon> positions) {
        String geometry = "POLYGON((";
        int l = positions.size();
        for (int i = 0; i < l - 1; i++) {
            geometry += positions.get(i).getLongitude().getDegrees() + " " + positions.get(i).getLatitude().getDegrees() + ",";
        }
        geometry += positions.get(l - 1).getLongitude().getDegrees() + " " + positions.get(l - 1).getLatitude().getDegrees() + "))";
        return geometry;
    }

    @Override
    public String wwjPositionsToLineWkt(List<? extends Position> positions) {
        if (positions != null && positions.size() > 2) {
            String geometry = "LINESTRING(";
            int l = positions.size();
            for (int i = 0; i < l - 1; i++) {
                geometry += positions.get(i).getLongitude().getDegrees() + " " + positions.get(i).getLatitude().getDegrees() + ",";
            }
            geometry += positions.get(l - 1).getLongitude().getDegrees() + " " + positions.get(l - 1).getLatitude().getDegrees() + ")";
            return geometry;
        } else {
            return "";
        }
    }

    @Override
    public MultiPoint wwjPositionsToJtsMultiPoint(Set<Pair<Double, Double>> positions) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Pair<Double, Double> c : positions) {
            coordinates.add(new Coordinate(c.getX(), c.getY()));
        }
        Coordinate coordinates1[] = coordinates.toArray(new Coordinate[coordinates.size()]);
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createMultiPoint(coordinates.toArray(coordinates1));
    }

    @Override
    public String wwjSurfacePolylinesToWkt(List<SurfacePolylines> polylines) {
        if (polylines != null) {
            List<List<LatLon>> listListLatLon = new ArrayList<>();
            polylines.stream().map((s) -> s.getLocations()).map((ll) -> {
                List<LatLon> listLatLon = new ArrayList<>();
                for (LatLon lll : ll) {
                    listLatLon.add(lll);
                }
                return listLatLon;
            }).forEach((listLatLon) -> {
                listListLatLon.add(listLatLon);
            });
            String geometry = "MULTILINESTRING(";
            int size0 = listListLatLon.size();
            for (int i = 0; i < size0 - 1; i++) {
                List<LatLon> l = listListLatLon.get(i);
                int size1 = l.size();
                geometry += "(";
                for (int j = 0; j < size1 - 1; j++) {
                    geometry += l.get(j).longitude.degrees + " " + l.get(j).latitude.degrees + ",";
                }
                geometry += l.get(size1 - 1).longitude.degrees + " " + l.get(size1 - 1).latitude.degrees;
                geometry += "),";
            }
            List<LatLon> l = listListLatLon.get(size0 - 1);
            int size1 = l.size();
            geometry += "(";
            for (int j = 0; j < size1 - 1; j++) {
                geometry += l.get(j).longitude.degrees + " " + l.get(j).latitude.degrees + ",";
            }
            geometry += l.get(size1 - 1).longitude.degrees + " " + l.get(size1 - 1).latitude.degrees;
            geometry += "))";
            return geometry;
        } else {
            return null;
        }
    }

    @Override
    public String wwjSurfacePolylinesToWktWithCoalescence(List<SurfacePolylines> polylines) {
        List<LatLon> tmp = new ArrayList<>();
        // Iterable<? extends LatLon> tmp;

        List<LatLon> listLatLon = new ArrayList<>();
        for (SurfacePolylines s : polylines) {
            tmp.clear();

            for (LatLon i : s.getLocations()) {
                tmp.add(i);
            }

            int size = tmp.size();
            if (tmp.get(0) != tmp.get(size - 1)) {
                for (LatLon ll : tmp) {
                    listLatLon.add(ll);
                }
            }
        }
        String geometry = "LINESTRING(";
        int size = listLatLon.size();
        for (int j = 0; j < size - 1; j++) {
            geometry += listLatLon.get(j).longitude.degrees + " " + listLatLon.get(j).latitude.degrees + ",";
        }
        geometry += listLatLon.get(size - 1).longitude.degrees + " " + listLatLon.get(size - 1).latitude.degrees;
        geometry += ")";
        return geometry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<Double, Double> wtkGetCentroid(String wkt) {
        WKTReader wktReader = new WKTReader();
        Geometry geometry = null;
        Pair<Double, Double> location = null;
        if (wkt != null) {
            try {
                geometry = wktReader.read(wkt);
            } catch (com.vividsolutions.jts.io.ParseException ex) {
                Logger.getLogger(TopologyImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            Centroid centroid;
            if (geometry != null) {
                centroid = new Centroid(geometry);
                Coordinate coord = centroid.getCentroid();
                location = new Pair(coord.y, coord.x);
            }
        }
        return location;
    }

    @Override
    public Polygon wktPolygonToWwjPolygon(String geometry) {
        List<Position> positions;
        Polygon polygon = null;
        positions = wktPolygonToPositions(geometry);
        if (positions != null) {
            polygon = new Polygon(positions);
        }
        return polygon;
    }

    //MULTILINESTRING((-4.49067 48.37985,-4.49005 48.37938,-4.48888 48.3785))
    @Override
    public Polygon wktMultiLineToWwjPolygon(String geometry) {
        String tmp = geometry.replace("MULTILINESTRING((", "");
        tmp = tmp.replace("))", "");
        String[] posTab0 = tmp.split(",");
        if (posTab0.length > 2) {
            List<Position> positions = new ArrayList<>();
            for (String s : posTab0) {
                String[] posTab1 = s.split("\\s+");
                positions.add(new Position(Angle.fromDegrees(Double.valueOf(posTab1[1])),
                        Angle.fromDegrees(Double.valueOf(posTab1[0])), 0));
            }
            return new Polygon(positions);
        } else {
            return null;
        }
    }

    @Override
    public Path wktMultiLineToWwjPath(String geometry,double height) {
        String tmp = geometry.replace("MULTILINESTRING((", "");
        tmp = tmp.replace("))", "");
        String[] posTab0 = tmp.split(",");
        List<Position> positions = new ArrayList<>();
        for (String s : posTab0) {
            String[] posTab1 = s.split("\\s+");
            positions.add(new Position(Angle.fromDegrees(Double.valueOf(posTab1[1])),
                    Angle.fromDegrees(Double.valueOf(posTab1[0])), height));
        }
        return new Path(positions);
    }

    @Override
    public Geometry wwjPolygonToJtsGeometry(Polygon polygon) {
        Iterable<? extends LatLon> latLon = polygon.getOuterBoundary();
        List<Position> positionList = new ArrayList<>();
        for (LatLon l : latLon) {
            positionList.add(new Position(l, 10.0));
        }
        Coordinate[] coordinates = new Coordinate[positionList.size()];
        for (int i = 0; i < positionList.size(); i++) {
            coordinates[i] = new Coordinate(positionList.get(i).getLongitude().getDegrees(),
                    positionList.get(i).getLatitude().getDegrees());
        }
        return new GeometryFactory().createLineString(coordinates);
    }

    @Override
    public List<Position> wktPolygonToPositions(String geometry) {
        List<Position> positions = null;
        String[] tab0;
        String[] tab1;
        String[] tab2;
        if (geometry != null && geometry.toUpperCase().contains("POLYGON")) {
            tab0 = geometry.split("\\(\\(");
            if (tab0.length > 1) {
                positions = new ArrayList<>();
                tab1 = tab0[1].split("\\)\\)");
                tab2 = tab1[0].split(",");
                int l = tab2.length;
                //   for(String s : tab2){
                //       System.out.println("s : " + s);
                //  }
                for (int i = 0; i < l; i++) {
                    String[] latLon = tab2[i].trim().split(" ");
                    String lat = latLon[1];
                    String lon = latLon[0];

                    positions.add(new Position(Angle.fromDegrees(Double.parseDouble(lat)),
                            Angle.fromDegrees(Double.parseDouble(lon)), 5));
                }
            }
        }
        return positions;
    }

    @Override
    public Polygon wktPolygonToWwjPolygon(Geometry geometry) {
        Coordinate[] coordinates = geometry.getCoordinates();
        List<Position> positions = new ArrayList<>();
        for (Coordinate c : coordinates) {
            positions.add(new Position(Angle.fromDegrees(c.y), Angle.fromDegrees(c.x), 100.0));
        }
        Polygon polygon = new Polygon(positions);
        return polygon;
    }

    @Override
    public String wwjLocationsToWKT(Iterable<? extends LatLon> locations) {
        String[] tab;
        String result = "POLYGON((";
        List<String> locList = new ArrayList<>();
        for (LatLon l : locations) {
            tab = l.toString().split(",");
            if (tab.length == 3) {
                tab[0] = tab[0].replace("(", "");
                tab[0] = tab[0].replace("°", "");
                tab[1] = tab[1].replace("°", "");
                locList.add(tab[1].trim() + " " + tab[0].trim());
            }
        }
        for (int i = 0; i < locList.size() - 1; i++) {
            result += locList.get(i) + ",";
        }
        result += locList.get(locList.size() - 1) + "))";
        return result;
    }

    @Override
    public Geometry filterWwjLatLonsWithGeometryJts(Geometry geometry, List<LatLon> pts) {
        CoordinateList list = new CoordinateList(geometry.getCoordinates());
        list.closeRing();
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing ring = geometryFactory.createLinearRing(list.toCoordinateArray());
        com.vividsolutions.jts.geom.Polygon polygonEnv = geometryFactory.createPolygon(ring, null);

        String wkt = wwjLatLonsToPolygonWkt(pts);
        WKTReader wktReader = new WKTReader();
        Geometry geometryFiltered = null;
        Geometry polygon = null;
        if (wkt != null) {
            try {
                polygon = wktReader.read(wkt);
            } catch (com.vividsolutions.jts.io.ParseException ex) {
                Logger.getLogger(TopologyImpl.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
            if (polygonEnv.contains(polygon)) {
                geometryFiltered = polygon;
            } else {
                try {
                    geometryFiltered = polygonEnv.intersection(polygon);
                } catch (Exception e) {

                }
            }
        } else {
            geometryFiltered = null;
        }
        return geometryFiltered;
    }
}