/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.databases.impl.controller.loader;

import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.Geo;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.Coastline;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author serge
 */
public class CoastlineDBLoader
        extends ResultSetDBLoader {

    public CoastlineDBLoader(Connection connection) {
        super(connection, "COALNE");
    }

    @Override
    public List<? extends Geo> retrieveObjectsIn(double latMin, double lonMin, double latMax, double lonMax) {
        objects = new ArrayList<>();
        String geom = "";
        resultSet = retrieveResultSetIn(latMin, lonMin, latMax, lonMax);
        Coastline object;
        try {
            while (resultSet.next()) {
                object = new Coastline();
                geom = resultSet.getString("geom");
                if (geom != null) {
                    object.setGeom(geom);
                    object.getLabels().put("COALNE","Coastline");
                    objects.add(object);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoastlineDBLoader.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        return objects;
    }
}
