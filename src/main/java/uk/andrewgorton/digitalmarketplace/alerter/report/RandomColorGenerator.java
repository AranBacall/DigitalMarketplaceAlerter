package uk.andrewgorton.digitalmarketplace.alerter.report;

import java.awt.*;
import java.util.Random;

/**
 * Created by koskinasm on 25/10/2016.
 */
public class RandomColorGenerator {

    private static Random rand = new Random();

    public static String getRandomColor()
    {
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        Color color = new Color(r,g,b);
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        return hex;
    }
}
