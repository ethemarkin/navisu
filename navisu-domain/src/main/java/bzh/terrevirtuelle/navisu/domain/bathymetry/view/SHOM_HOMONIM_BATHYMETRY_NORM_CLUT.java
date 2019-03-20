package bzh.terrevirtuelle.navisu.domain.bathymetry.view;

import bzh.terrevirtuelle.navisu.domain.util.Pair;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.paint.Color;

public class SHOM_HOMONIM_BATHYMETRY_NORM_CLUT {

    static final private List<Range> RANGES;
    static final public double MAX = 1.0;

    static {
        RANGES = new ArrayList<>();
        //  RANGES.add(Range.closedOpen(-0.15, 0.0));
        RANGES.add(Range.closedOpen(0.0, 0.01));
        RANGES.add(Range.closedOpen(0.01, 0.03));
        RANGES.add(Range.closedOpen(0.03, 0.05));
        RANGES.add(Range.closedOpen(.05, 0.07));
        RANGES.add(Range.closedOpen(.07, 0.09));
        RANGES.add(Range.closedOpen(.09, 0.11));
        RANGES.add(Range.closedOpen(0.11, 0.13));
        RANGES.add(Range.closedOpen(0.13, 0.15));
        RANGES.add(Range.closedOpen(0.15, 0.17));
        RANGES.add(Range.closedOpen(0.17, 0.19));
        RANGES.add(Range.closedOpen(0.19, 0.21));
        RANGES.add(Range.closedOpen(0.21, 0.23));
        RANGES.add(Range.closedOpen(0.23, 0.25));
        RANGES.add(Range.closedOpen(0.25, 0.27));
        RANGES.add(Range.closedOpen(0.27, 0.30));
        RANGES.add(Range.closedOpen(0.30, 0.40));
        RANGES.add(Range.closedOpen(0.40, 0.50));
        RANGES.add(Range.closedOpen(0.50, 0.75));
        RANGES.add(Range.closedOpen(0.75, 1.00));
        RANGES.add(Range.closed(1.0, 1.0));
    }
    private static final Map<Integer, Color> ATT = Collections.unmodifiableMap(new HashMap<Integer, Color>() {
        {
            put(0, Color.web("#d0321b"));
            put(1, Color.web("#dc4d1f"));
            put(2, Color.web("#e15d22"));
            put(3, Color.web("#e37226"));
            put(4, Color.web("#e6882a"));
            put(5, Color.web("#eeb534"));
            put(6, Color.web("#f3cd3a"));
            put(7, Color.web("#e7fd4f"));
            put(8, Color.web("#d8fc4b"));
            put(9, Color.web("#bafc46"));
            put(10, Color.web("#98f741"));
            put(11, Color.web("#7cdf42"));
            put(12, Color.web("#8befca"));
            put(13, Color.web("#7cd9fc"));
            put(14, Color.web("#3561f9"));
            put(15, Color.web("#1930ff"));
            put(16, Color.web("#2132cc"));
            put(17, Color.web("#1d2cb2"));
            put(18, Color.web("#1a2799"));
            put(19, Color.web("#162080"));
            put(20, Color.web("#162081"));
        }
    });

    @SuppressWarnings("unchecked")
    public static Color getColor(double data) {
        Color result = null;
        for (int i = 0; i < RANGES.size(); i++) {
            if (RANGES.get(i).contains(data)) {
                result = SHOM_HOMONIM_BATHYMETRY_NORM_CLUT.ATT.get(i);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Pair<Double, Double> getBounds(double data) {
        Pair<Double, Double> result = null;
        for (int i = 0; i < RANGES.size(); i++) {
            if (RANGES.get(i).contains(data)) {
                result = new Pair(RANGES.get(i).lowerEndpoint(), RANGES.get(i).upperEndpoint());
            }
        }
        System.out.println("result : " + result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Pair<Color, Color> getColors(double data) {
        Pair<Double, Double> bounds = getBounds(data);
        Pair<Color, Color> result = null;
        if (bounds != null) {
            result = new Pair(getColor(bounds.getX()), getColor(bounds.getY()));
        }
        System.out.println("result : "+result);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Set<Map.Entry<Integer, Color>> entries = SHOM_HOMONIM_BATHYMETRY_NORM_CLUT.ATT.entrySet();
        buffer.append("[");
        entries.stream().forEach((e) -> {
            buffer.append("[").append(e.getKey()).append(", ").append(e.getValue()).append("]");
        });
        buffer.append("]");
        return buffer.toString();
    }

}
