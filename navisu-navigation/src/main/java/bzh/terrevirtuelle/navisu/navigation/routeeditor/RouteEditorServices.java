/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.navigation.routeeditor;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import com.vividsolutions.jts.geom.Geometry;
import org.capcaval.c3.component.ComponentService;

/**
 * NaVisu
 *
 * @date 26 août 2015
 * @author Serge Morvan
 */
public interface RouteEditorServices extends ComponentService {

    void on(String... files);

    default void off() {
    }

    boolean canOpen(String category);

    InstrumentDriver getDriver();

    Geometry getBuffer();

    void showBuffer();
}
