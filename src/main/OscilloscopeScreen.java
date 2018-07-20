package main;

class OscilloscopeScreen {
    private boolean enabled = false;
    private double offsetX = 0, offsetY = 0, amplitude = 50, frequency = 50, pulse = 2;

    boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    double getOffsetX() {
        return offsetX;
    }

    void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    double getOffsetY() {
        return offsetY;
    }

    void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    double getAmplitude() {
        return amplitude;
    }

    void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    double getFrequency() {
        return frequency;
    }

    void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    double getPulse() {
        return pulse;
    }

    void setPulse(double pulse) {
        this.pulse = pulse;
    }
}
