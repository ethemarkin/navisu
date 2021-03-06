package bzh.terrevirtuelle.navisu.widgets.radialmenu.menu;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;

/**
 * NaVisu
 *
 * @author Jordan Mens
 */
public class RadialMenuContainer
        extends RadialMenuItem {

    private ObservableList<RadialMenuItem> items = FXCollections.observableArrayList();
    private BooleanProperty isChildrenVisible;

    public RadialMenuContainer() {

        setChildrenVisible(false);
        getPath().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            setChildrenVisible(!isChildrenVisible());
          //  System.out.println("MOUSE_CLICKED");
            event.consume();
        });

        items.addListener((InvalidationListener) obs -> update());
    }

    protected void updateChildren() {
        if (items.size() == 0) {
            return;
        }
        //TODO Radial separator
        double radialItemLenght = 360 / items.size();
        double ratio = 1.0 / items.size() / 1.0;//2.0 ou 1.7 ou 1.2
        for (int i = 0; i < items.size(); i++) {
            RadialMenuItem radialMenuItem = items.get(i);

            double parentInnerRadius = radialMenuItem.getParentItem().getInnerRadius();
            double parentOuterRadius = radialMenuItem.getParentItem().getOuterRadius();
            double parentArcLenght = radialMenuItem.getParentItem().getLength();

            double newInnerRadius = parentOuterRadius;
            double newOuterRadius = parentOuterRadius + (parentOuterRadius - parentInnerRadius);

            // double ratio = parentOuterRadius / newOuterRadius;
            // double ratio = 0.5;
            double newArcLenght = parentArcLenght * ratio;
            //double newArcLenght = radialItemLenght;
            double startAngle = radialMenuItem.getParentItem().getStartAngle(); //+ ((newArcLenght - parentArcLenght) / 2);

            radialMenuItem.setInnerRadius(newInnerRadius);
            radialMenuItem.setOuterRadius(newOuterRadius);
            radialMenuItem.setStartAngle(i * newArcLenght + startAngle);
            radialMenuItem.setLength(newArcLenght);
            radialMenuItem.setGap(radialMenuItem.getParentItem().getGap());
            radialMenuItem.setVisible(isChildrenVisible());

            if (radialMenuItem instanceof RadialMenuContainer) {
                ((RadialMenuContainer) radialMenuItem).updateChildren();
            }
        }
    }

    public void addItem(RadialMenuItem item) {
        item.setParentItem(this);
        items.add(item);
        getChildren().add(item);
    }

    public void removeItem(RadialMenuItem item) {
        item.setParentItem(null);
        items.remove(item);
        getChildren().remove(item);
    }

    public ObservableList<RadialMenuItem> getItems() {
        return items;
    }

    /**
     * *********************************************************
     */
    public final BooleanProperty isChildrenVisibleProperty() {
        if (isChildrenVisible == null) {
            isChildrenVisible = new SimpleBooleanProperty(this, "isChildrenVisible") {
                @Override
                protected void invalidated() {
                    for (RadialMenuItem item : items) {
                        item.setVisible(get());
                    }
                }
            };
        }
        return isChildrenVisible;
    }

    public final boolean isChildrenVisible() {
        return isChildrenVisibleProperty().get();
    }

    public final void setChildrenVisible(boolean isChildrenVisible) {
        this.isChildrenVisibleProperty().set(isChildrenVisible);
    }

//	private int computeLevel(int level) {
//
//		if(this instanceof RadialMenuItem) {
//			return level;
//		}
//
//		if(getParentItem() == null) {
//			return level;
//		}
//
//		return ((RadialMenuContainer)getParentItem()).computeLevel(level + 1);
//	}
}
