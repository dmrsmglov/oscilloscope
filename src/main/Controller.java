package main;

import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;



import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;



public class Controller implements Initializable {
    public Canvas screenCanvas;
    public Button generatorSwitcher;
    public Slider horizontalOffsetSlider, verticalOffsetSlider, amplitudeSlider, frequencySlider;
    public ToggleButton sineSignalButton, rectSignalButton, triangleSignalButton;
    public Slider dutyFactorSlider;


    private OscilloscopeScreen oscilloscope = new OscilloscopeScreen();
    private SignalType signalType;
    private Signal signal = new Signal(oscilloscope);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        plotAxis();
        dutyFactorSlider.disableProperty().setValue(true);
    }

    private void plotAxis() {
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        double heightScreen = screenCanvas.getHeight(), weightScreen = screenCanvas.getWidth();
        drawCoordinateSystemCanvas(gc);
        String plotColor = "#c3c8cc";
        gc.setStroke(valueOf(plotColor));

        for (double cur = 10; cur < weightScreen; cur += 30) {
            gc.strokeLine(cur, 0, cur, heightScreen);
        }
        for (double cur = 10; cur < heightScreen; cur += 30) {
            gc.strokeLine(0, cur, weightScreen, cur);
        }
    }

    private void drawSignal() {
        List<Point2D> points = signal.getNewSignal(signalType);
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        String signalColor = "#f44242";
        gc.setStroke(valueOf(signalColor));
        for (int i = 1; i < points.size(); ++i) {
            gc.strokeLine(points.get(i).getX(), points.get(i).getY(), points.get(i - 1).getX(), points.get(i - 1).getY());
        }
    }

    private void drawCoordinateSystemCanvas(GraphicsContext gc) {
        String axisColor = "#000000";
        gc.setStroke(valueOf(axisColor));
        gc.strokeLine(250, 30, 250, 470);
        gc.strokeLine(30, 250, 470, 250);
    }

    private void clearCanvas() {
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());
        plotAxis();
    }

    public void clickGeneratorSwitcher(MouseEvent mouseEvent) {
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            generatorSwitcher.setText("Включить");
            oscilloscope.setEnabled(false);
        } else {
            drawSignal();
            oscilloscope.setEnabled(true);
            generatorSwitcher.setText("Выключить");
        }
    }


    public void setHorizontalOffset(MouseEvent mouseEvent) {
        oscilloscope.setOffsetX(horizontalOffsetSlider.getValue());
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }

    public void setVerticalOffset(MouseEvent mouseEvent) {
        oscilloscope.setOffsetY(-verticalOffsetSlider.getValue());
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }

    public void setAmplitude(MouseEvent mouseEvent) {
        oscilloscope.setAmplitude(amplitudeSlider.getValue());
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }

    public void setFrequency(MouseEvent mouseEvent) {
        oscilloscope.setFrequency(frequencySlider.getValue());
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }


    public void setRectSignal(MouseEvent mouseEvent) {
        if (signalType == SignalType.sine) {
            sineSignalButton.disableProperty().setValue(false);
        }
        if (signalType == SignalType.triangle) {
            triangleSignalButton.disableProperty().setValue(false);
        }
        signalType = SignalType.rectangle;
        rectSignalButton.disableProperty().setValue(true);
        dutyFactorSlider.disableProperty().setValue(false);
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }

    public void setTriangleSignal(MouseEvent mouseEvent) {
        if (signalType == SignalType.rectangle) {
            dutyFactorSlider.disableProperty().setValue(true);
            rectSignalButton.disableProperty().setValue(false);
        }
        if (signalType == SignalType.sine) {
            sineSignalButton.disableProperty().setValue(false);
        }
        signalType = SignalType.triangle;
        triangleSignalButton.disableProperty().setValue(true);
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }

    public void setSineSignal(MouseEvent mouseEvent) {
        if (signalType == SignalType.rectangle) {
            dutyFactorSlider.disableProperty().setValue(true);
            rectSignalButton.disableProperty().setValue(false);
        }
        if (signalType == SignalType.triangle) {
            triangleSignalButton.disableProperty().setValue(false);
        }
        signalType = SignalType.sine;
        sineSignalButton.disableProperty().setValue(true);
        if (oscilloscope.isEnabled()) {
            clearCanvas();
            drawSignal();
        }
    }

    public void setDutyFactor(MouseEvent mouseEvent) {
        oscilloscope.setDutyFactor(dutyFactorSlider.getValue());
        clearCanvas();
        drawSignal();
    }
}
