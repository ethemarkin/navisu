/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.geometry.geodesy;

import bzh.terrevirtuelle.navisu.util.Pair;
import gov.nasa.worldwind.geom.Position;
import java.util.List;
import org.capcaval.c3.component.ComponentService;

/**
 * @date 13 sept 2017
 * @author Serge Morvan
 */
public interface GeodesyServices
        extends ComponentService {

    double getDistanceM(Position posA, Position posB);

    double getDistanceM(double latA, double lonA, double latB, double lonB);

    Position getPosition(Position posA, double bearing, double distance);

    Position getPosition(double latA, double lonA, double bearing, double distance);

    List<Pair<Position, Position>> split(int count, Position a, Position b);

    double getAzimuth(Position posA, Position posB);

    double getAzimuth(double latA, double lonA, double latB, double lonB);
}
