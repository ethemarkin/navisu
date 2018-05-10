package bzh.terrevirtuelle.navisu.stl.charts;

import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriver;
import org.capcaval.c3.component.ComponentService;

/**
 * NaVisu
 *
 * @author Serge Morvan
 * @date 25/2/2017
 */
public interface StlChartComponentServices
        extends ComponentService {

    void openChart(String file);

    InstrumentDriver getDriver();

}
