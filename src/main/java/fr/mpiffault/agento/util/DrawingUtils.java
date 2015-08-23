package fr.mpiffault.agento.util;

import java.awt.*;

public class DrawingUtils {

    /**
     * Return a color according to intensity given between 0(BLACK) et 1785(WHITE) and a max value
     * The color gradient sequence is Black=>Blue=>Cyan=>Green=>Yellow=>Red=>Pink=>White
     * @param intensityAsDouble intensity. a negative value will return BLACK
     * @param maxValue value beyond which it will be WHITE
     * @return A Color between BLACK (0) and WHITE (maxValue)
     */
    public static Color intensityToColor(double intensityAsDouble, double maxValue) {
        intensityAsDouble = (intensityAsDouble / maxValue) * 1785;
        int intensity = (int) intensityAsDouble;
        if (intensity >= 1785) return Color.WHITE;
        if (intensity <= 0) return Color.BLACK;
        if (intensity < 255)
            return new Color(0,0,intensity);
        else if (intensity < 510)
            return new Color(0, intensity%255, 255);
        else if (intensity < 765)
            return new Color(0, 255, 255 - intensity%255);
        else if (intensity < 1020)
            return new Color(intensity%255, 255, 0);
        else if (intensity < 1275)
            return new Color(255, 255 - intensity%255, 0);
        else if (intensity < 1530)
            return new Color(255, 0, intensity%255);
        else if (intensity < 1785)
            return new Color(255, intensity%255, 255);
        return Color.WHITE;

    }
}
