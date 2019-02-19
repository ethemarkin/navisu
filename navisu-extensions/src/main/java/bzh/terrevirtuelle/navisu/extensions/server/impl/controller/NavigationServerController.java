/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.extensions.server.impl.controller;

import bzh.terrevirtuelle.navisu.app.drivers.databasedriver.DatabaseDriverManagerServices;
import bzh.terrevirtuelle.navisu.app.drivers.driver.DriverManagerServices;
import bzh.terrevirtuelle.navisu.app.drivers.instrumentdriver.InstrumentDriverManagerServices;
import bzh.terrevirtuelle.navisu.app.drivers.webdriver.WebDriverManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.domain.navigation.model.NavigationDataSet;
import bzh.terrevirtuelle.navisu.extensions.commands.Command;
import bzh.terrevirtuelle.navisu.extensions.commands.NavigationCmdComponentServices;
import bzh.terrevirtuelle.navisu.util.xml.ImportExportXML;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;

//import org.vertx.java.core.Vertx;
/**
 *
 * @author serge
 * @date Jan 14, 2016
 *
 */
public class NavigationServerController {

    protected static final Logger LOGGER = Logger.getLogger(NavigationServerController.class.getName());
    private static NavigationServerController INSTANCE;

    private final GuiAgentServices guiAgentServices;
    private final DriverManagerServices driverManagerServices;
    private final WebDriverManagerServices webDriverManagerServices;
    private final InstrumentDriverManagerServices instrumentDriverManagerServices;
    private final DatabaseDriverManagerServices databaseDriverManagerServices;
    private NavigationCmdComponentServices navigationCmdComponentServices;
    private NavigationDataSet navigationDataSet;

    private Properties properties;
    private final String PROPERTIES_NAME = "properties/navigation.properties";
    private final String START_CMD = "/navigation";
    private final String HTML_RESPONSE = "data/html/response.html";
    private final String HTML_RESPONSE_CMD = "/";

    private Vertx dataVertx;
    private Vertx cmdVertx;

    private int port;
    private Command command;

    private NavigationServerController(GuiAgentServices guiAgentServices,
            DriverManagerServices driverManagerServices,
            WebDriverManagerServices webDriverManagerServices,
            InstrumentDriverManagerServices instrumentDriverManagerServices,
            DatabaseDriverManagerServices databaseDriverManagerServices,
            NavigationCmdComponentServices navigationCmdComponentServices) {
        this.guiAgentServices = guiAgentServices;
        this.driverManagerServices = driverManagerServices;
        this.webDriverManagerServices = webDriverManagerServices;
        this.instrumentDriverManagerServices = instrumentDriverManagerServices;
        this.databaseDriverManagerServices = databaseDriverManagerServices;
        this.navigationCmdComponentServices = navigationCmdComponentServices;
        initProperties();
    }

    public static NavigationServerController getInstance(GuiAgentServices guiAgentServices,
            DriverManagerServices driverManagerServices,
            WebDriverManagerServices webDriverManagerServices,
            InstrumentDriverManagerServices instrumentDriverManagerServices,
            DatabaseDriverManagerServices databaseDriverManagerServices,
            NavigationCmdComponentServices navigationCmdComponentServices) {
        if (INSTANCE == null) {
            INSTANCE = new NavigationServerController(guiAgentServices,
                    driverManagerServices,
                    webDriverManagerServices,
                    instrumentDriverManagerServices,
                    databaseDriverManagerServices,
                    navigationCmdComponentServices);
        }
        return INSTANCE;
    }

    public void init() {
        this.port = new Integer(properties.getProperty("port").trim());
        initServer();
    }

    public void init(int port) {
        this.port = port;
        initServer();
    }

    private void initProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_NAME));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void initServer() {
        System.out.println("initServer");
        cmdVertx = VertxFactory.newVertx();
        try {
            cmdVertx.createHttpServer().websocketHandler((final ServerWebSocket ws) -> {
              //  System.out.println("(ws.path() : " + ws.path());
                if (ws.path().equals(START_CMD)) {
                    ws.dataHandler((Buffer data) -> {
                     //  System.out.println("data.toString() : " + data.toString());
                        command = command(data.toString());
                        if (command != null) {
                            if (command.getNavigationData() != null && command.getArg() == null) {
                                navigationDataSet = navigationCmdComponentServices.doIt(command.getCmd(), command.getNavigationData());
                                if (navigationDataSet != null) {
                                    if (navigationDataSet.size() > 0) {
                                        String r = response(navigationDataSet);
                                        ws.writeTextFrame(r);
                                    } else {
                                        ws.writeTextFrame("");
                                    }
                                }
                            }
                            if (command.getNavigationData() != null && command.getArg() != null) {
                                navigationDataSet = navigationCmdComponentServices.doIt(command.getCmd(), command.getNavigationData(), command.getArg());
                                if (navigationDataSet != null) {
                                    if (navigationDataSet.size() > 0) {
                                        String r = response(navigationDataSet);
                                        ws.writeTextFrame(r);
                                    } else {
                                        ws.writeTextFrame("");
                                    }
                                }
                            }
                            if (command.getArg() != null) {
                                navigationDataSet = navigationCmdComponentServices.doIt(command.getCmd(), command.getArg());
                                if (navigationDataSet != null) {
                                    if (navigationDataSet.size() > 0) {
                                        String r = response(navigationDataSet);
                                        ws.writeTextFrame(r);
                                    } else {
                                        ws.writeTextFrame("");
                                    }
                                }
                            }
                        } else {
                            ws.writeTextFrame("NACK");
                        }
                    });
                } else {
                    ws.reject();
                }
            }).requestHandler((HttpServerRequest req) -> {
                if (req.path().equals(HTML_RESPONSE_CMD)) {
                    req.response().sendFile(HTML_RESPONSE);
                }
            }).listen(port);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private Command command(String data) {
        Command navCmd = null;
        try {
            navCmd = new Command();
            navCmd = ImportExportXML.imports(navCmd, new StringReader(data));
        } catch (JAXBException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
        //LOGGER.log(Level.INFO, data);
       // System.out.println("data : " + data);
        //System.out.println("command : " + navCmd);
        return navCmd;
    }

    private String response(NavigationDataSet response) {
        StringWriter xmlString = new StringWriter();
        try {
            ImportExportXML.exports(response, xmlString);
        } catch (JAXBException ex) {
            System.out.println("ex " + ex);
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }

        //TODO
        return xmlString.toString();
    }

    public void setNavigationCmdComponentServices(NavigationCmdComponentServices navigationCmdComponentServices) {
        this.navigationCmdComponentServices = navigationCmdComponentServices;
    }

}
