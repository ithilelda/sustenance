package top.ithilelda.sustenance;

public class Configuration {
    private int fastHealInterval = 5;
    private float fastHealExhaustionLevel = 4.0F; // how much exhaustion is added for each fast heal operation. 4 exhaustion decreases 1 saturation/food level.
    private float fastHealExhaustionRatio = 4.0F; // exhaustion level / exhaustion ratio = heart healed.
    private int slowHealInterval = 40;
    private int slowHealThreshold = 15;
    private float saturationLimit = 40.0F;

    public int getFastHealInterval() {
        return fastHealInterval;
    }

    public void setFastHealInterval(int fastHealInterval) {
        this.fastHealInterval = fastHealInterval;
    }

    public float getFastHealExhaustionLevel() {
        return fastHealExhaustionLevel;
    }

    public void setFastHealExhaustionLevel(float fastHealExhaustionLevel) {
        this.fastHealExhaustionLevel = fastHealExhaustionLevel;
    }

    public float getFastHealExhaustionRatio() {
        return fastHealExhaustionRatio;
    }

    public void setFastHealExhaustionRatio(float fastHealExhaustionRatio) {
        this.fastHealExhaustionRatio = fastHealExhaustionRatio;
    }

    public int getSlowHealInterval() {
        return slowHealInterval;
    }

    public void setSlowHealInterval(int slowHealInterval) {
        this.slowHealInterval = slowHealInterval;
    }

    public int getSlowHealThreshold() {
        return slowHealThreshold;
    }

    public void setSlowHealThreshold(int slowHealThreshold) {
        this.slowHealThreshold = slowHealThreshold;
    }

    public float getSaturationLimit() {
        return saturationLimit;
    }

    public void setSaturationLimit(float saturationLimit) {
        this.saturationLimit = saturationLimit;
    }
}
