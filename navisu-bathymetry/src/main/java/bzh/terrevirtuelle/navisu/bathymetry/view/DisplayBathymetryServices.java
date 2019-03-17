/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.bathymetry.view;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import bzh.terrevirtuelle.navisu.domain.geometry.Point3DGeo;
import gov.nasa.worldwind.layers.RenderableLayer;
import java.util.List;
import org.capcaval.c3.component.ComponentService;

/**
 * @date 15 aout 2017
 * @author Serge Morvan
 */
public interface DisplayBathymetryServices
        extends ComponentService {

    InstrumentDriver getDriver();

    void displaySounding(double lat, double lon, double depth, RenderableLayer l);

    void displaySounding(List<Point3DGeo> points, RenderableLayer l);

    void displayDelaunaySounding(List<Point3DGeo> points, RenderableLayer l, double maxElevation);

}
