package uk.andrewgorton.digitalmarketplace.alerter.report;

public class KeyValueColorItem
{
    private String label;
    private Long value;
    private String color;

    public KeyValueColorItem(String label, Long value)
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
