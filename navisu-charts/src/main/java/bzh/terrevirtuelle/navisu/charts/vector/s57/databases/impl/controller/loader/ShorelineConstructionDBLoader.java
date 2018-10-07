/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.charts.vector.s57.databases.impl.controller.loader;

import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.Geo;
import bzh.terrevirtuelle.navisu.domain.charts.vector.s57.model.geo.ShorelineConstruction;
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
public class ShorelineConstructionDBLoader
        extends ResultSetDBLoader {

    public ShorelineConstructionDBLoader(Connection connection) {
        super(connection, "SLCONS");
    }

    @Override
    public List<? extends Geo> retrieveObjectsIn(double latMin, double lonMin, double latMax, double lonMax) {
        objects = new ArrayList<>();
        String geom = "";
        double height;
        resultSet = retrieveResultSetIn(latMin, lonMin, latMax, lonMax);
        ShorelineConstruction object;
        //height tjrs 0
        try {
            while (resultSet.next()) {
                object = new ShorelineConstruction();
                geom = resultSet.getString("geom");
                object.setGeom(geom);
                object.getLabels().put("SLCONS", "ShorelineConstruction");
                objects.add(object);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PontoonDBLoader.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        
        return objects;
    }
}
