/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.gpx;

import bzh.terrevirtuelle.navisu.app.drivers.driver.Driver;
import org.capcaval.c3.component.ComponentService;

/**
 *
 * @author Serge
 */
public interface GpxObjectServices extends ComponentService {

    Driver getDriver();

    void openFile(String file);

}
