package uk.andrewgorton.digitalmarketplace.alerter.report;

/**
 * Created by koskinasm on 25/10/2016.
 */
public class PieChartData
{
    private String label;
    private Long value;
    private String color;

    public PieChartData(String label, Long value)
    {
        this.label = label;
        this.value = value;
        this.color = RandomColorGenerator.getRandomColor();
    }

    public String getLabel() {
        return label;
    }

    public Long getValue() {
        return this.value;
    }

    public String getColor() {
        return color;
    }
}
