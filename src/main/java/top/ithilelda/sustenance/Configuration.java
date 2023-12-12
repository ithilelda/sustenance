package top.ithilelda.sustenance;

public class Configuration {
    public int fastHealInterval = 5;
    public float fastHealExhaustionLevel = 4.0F; // how much exhaustion is added for each fast heal operation. 4 exhaustion decreases 1 saturation/food level.
    public float fastHealExhaustionRatio = 4.0F; // exhaustion level / exhaustion ratio = heart healed.
    public int slowHealInterval = 40;
    public int slowHealThreshold = 15;
}
