package bzh.terrevirtuelle.navisu.app.drivers.driver.impl;

import bzh.terrevirtuelle.navisu.app.drivers.driver.Driver;
import bzh.terrevirtuelle.navisu.app.drivers.driver.DriverManager;
import bzh.terrevirtuelle.navisu.app.drivers.driver.DriverManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.menu.MenuManagerServices;
import bzh.terrevirtuelle.navisu.app.guiagent.utilities.Translator;
import javafx.stage.FileChooser;
import org.capcaval.c3.component.ComponentState;
import org.capcaval.c3.component.annotation.UsedService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static bzh.terrevirtuelle.navisu.app.guiagent.utilities.Translator.tr;
import bzh.terrevirtuelle.navisu.core.util.Checker;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * NaVisu
 *
 * @author tibus
 * @date 11/11/2013 18:55
 */
public class DriverManagerImpl
        implements DriverManager, DriverManagerServices, ComponentState {

    protected static final Logger LOGGER = Logger.getLogger(DriverManagerImpl.class.getName());
    protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.navisu/caches/caches.properties";
    @UsedService
    MenuManagerServices menuBarServices;
    @UsedService
    GuiAgentServices guiAgentServices;

    protected FileChooser fileChooser;
    //  protected FileChooser fileChooserDock;
    Properties properties;
    protected List<Driver> availableDriverList = new ArrayList<>();

    @Override
    public void componentInitiated() {
    }

    @Override
    public void init() {
        this.fileChooser = new FileChooser();
        //   this.fileChooserDock = new FileChooser();
        this.fileChooser.setTitle(tr("popup.fileChooser.open"));

        properties = new Properties();
        try {
            properties.load(new FileInputStream(CACHE_FILE_NAME));
        } catch (IOException ex) {
            Logger.getLogger(DriverManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        String userInitialDirectory = properties.getProperty("dataDir");
        if (userInitialDirectory == null) {
            this.fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data"));
        } else {
            this.fileChooser.setInitialDirectory(new File(userInitialDirectory));

        }
    }

    @Override
    public void open(FileChooser.ExtensionFilter ext) {

        File selectedFile = this.fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            this.handleOpenFiles(fileChooser.getSelectedExtensionFilter().getDescription(), selectedFile);
            System.out.println("Load : " + selectedFile.getAbsolutePath());
        }
    }

    @Override
    public File open(String category, String[] ext) {
        File selectedFile=null;
        String userInitialDirectory = properties.getProperty(category);
        if (userInitialDirectory != null) {
            if (userInitialDirectory.equals("")) {
                userInitialDirectory = System.getProperty("user.dir") + "/data/";
                this.fileChooser.setInitialDirectory(new File(userInitialDirectory));
            } else {
                this.fileChooser.setInitialDirectory(new File(userInitialDirectory));
            }
        } else {
            userInitialDirectory = System.getProperty("user.dir") + "/data/";
            this.fileChooser.setInitialDirectory(new File(userInitialDirectory));
        }
        this.fileChooser.getExtensionFilters().clear();
        this.fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(category, Arrays.asList(ext)));
        try {
            selectedFile = this.fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                this.handleOpenFiles(fileChooser.getSelectedExtensionFilter().getDescription(), selectedFile);
                System.out.println("Load : " + selectedFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(Translator.tr("drivermanager.error"));
                alert.show();
            });
            System.out.println("");
        }
        return selectedFile;
    }

    protected void handleOpenFiles(String category, File file) {
        Driver driver = this.findDriverForFile(category, file.getAbsolutePath());
        if (driver != null) {
            guiAgentServices.getJobsManager().newJob(file.getName(), (progressHandle) -> {
                driver.open(progressHandle, file.getAbsolutePath());
            });
        } else {
            LOGGER.log(Level.WARNING, "Unable to find a driver for file \"{0}\"", file.getName());
        }
        properties.setProperty(category, file.getParent());
        File f = new File(CACHE_FILE_NAME);
        OutputStream out;
        try {
            out = new FileOutputStream(f);
            properties.store(out, "Last directory choosed by user");
            out.close();
        } catch (IOException ex) {
            System.out.println("Last directory choosed by user " + ex.toString());
        }
    }

    protected Driver findDriverForFile(String category, String file) {
        Driver compatibleDriver = null;

        for (Driver driver : this.availableDriverList) {
            if (driver.canOpen(category, file)) {
                compatibleDriver = driver;
                break;
            } else if (driver.canOpen(file)) {
                compatibleDriver = driver;
                break;
            }
        }
        return compatibleDriver;
    }

    @Override
    public void registerNewDriver(Driver driver) {

        Checker.notNull(driver, "Driver must not be null.");

        // Hold the driver
        this.availableDriverList.add(driver);

        // Add the supported extension to the file chooser
        this.fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(driver.getName(), driver.getExtensions()));
    }

    @Override
    public void componentStarted() {
    }

    @Override
    public void componentStopped() {
    }
}
