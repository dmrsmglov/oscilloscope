package main;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

enum SignalType {
    sine,
    rectangle,
    triangle
}

class Signal {
    Signal(OscilloscopeScreen oscilloscopeScreen) {
        this.oscilloscope = oscilloscopeScreen;
    }

    private OscilloscopeScreen oscilloscope;
    private List<Point2D> points = new ArrayList<>();

    List<Point2D> getNewSignal(SignalType signalType) {
        points = new ArrayList<>();
        if (signalType == SignalType.sine) {
            buildSineSignal();
        }
        if (signalType == SignalType.rectangle) {
            buildRectSignal();
        }
        if (signalType == SignalType.triangle) {
            buildTriangleSignal();
        }

        return points;
    }

    private void buildSineSignal() {
        double baseOffsetX = 30, baseOffsetY = 250;
        int numberOfPoints = 440;
        for (int i = 0; i < numberOfPoints; ++i) {
            points.add(new Point2D(baseOffsetX + i + oscilloscope.getOffsetX(),
                    baseOffsetY + oscilloscope.getOffsetY() + oscilloscope.getAmplitude() * Math.sin(i * 6 /
                            (oscilloscope.getFrequency() - 6))));
        }
    }

    private void buildRectSignal() {
        double baseOffsetX = 30, baseOffsetY = 250;
        double x = baseOffsetX + oscilloscope.getOffsetX();
        double y = baseOffsetY + oscilloscope.getOffsetY();
        double curFrequency = Math.max(oscilloscope.getFrequency(), 1);
        double widthScreen = 500 + oscilloscope.getOffsetX() - baseOffsetX;
        int sign = 1;
        while (x < widthScreen) {
            if (sign == 1) {
                points.add(new Point2D(x, y - oscilloscope.getAmplitude()));
                points.add(new Point2D(Math.min(x + curFrequency /
                        oscilloscope.getDutyFactor(), widthScreen), y - oscilloscope.getAmplitude()));
                x += curFrequency / oscilloscope.getDutyFactor();
                if (x < widthScreen) {
                    points.add(new Point2D(x, y - oscilloscope.getAmplitude()));
                    points.add(new Point2D(x, y));
                }
                sign *= -1;
            } else if (sign == -1) {
                points.add(new Point2D(x, y));
                points.add(new Point2D(Math.min(x + curFrequency - curFrequency / oscilloscope.getDutyFactor(), widthScreen), y));
                x += curFrequency - curFrequency / oscilloscope.getDutyFactor();
                if (x < widthScreen) {
                    points.add(new Point2D(x, y - oscilloscope.getAmplitude()));
                    points.add(new Point2D(x, y));
                }
                sign *= -1;
            }
        }
    }

    private void buildTriangleSignal() {
        double baseOffsetX = 30, baseOffsetY = 250;
        double x = baseOffsetX + oscilloscope.getOffsetX();
        double y = baseOffsetY + oscilloscope.getOffsetY();
        double curFrequency = Math.max(oscilloscope.getFrequency(), 1);
        double widthScreen = 500 + oscilloscope.getOffsetX() - baseOffsetX;
        int sign = -1, it = 1;
        while (x < widthScreen) {
            points.add(new Point2D(x, y));
            points.add(new Point2D(x + curFrequency / 4, y + sign * oscilloscope.getAmplitude()));
            x += curFrequency / 4;
            y = y + sign * oscilloscope.getAmplitude();
            it++;
            if (it == 2) {
                it = 0;
                sign *= -1;
            }
        }
    }
}
