package sample;

import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;

public class Controller implements Initializable {
    public Canvas screenCanvas;
    public Button generatorSwitcher;
    public Slider horizontalOffsetSlider, verticalOffsetSlider, amplitudeSlider, frequencySlider;
    public ToggleButton sineSignalButton, rectSignalButton, triangleSignalButton;
    public Slider dutyFactorSlider;

    private boolean enabled = false, isSine = false, isRect = false, isTriangle = false;
    private double offsetX = 0, offsetY = 0, amplitude = 50, frequency = 50, dutyFactor = 2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        plotAxis();
        dutyFactorSlider.disableProperty().setValue(true);
    }

    private void plotAxis() {
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        double heightScreen = screenCanvas.getHeight(), weightScreen = screenCanvas.getWidth();
        drawCoordinateSystemCanvas(gc);
        gc.setStroke(valueOf("#c3c8cc"));

        for (double cur = 10; cur < weightScreen; cur += 30) {
            gc.strokeLine(cur, 0, cur, heightScreen);
        }
        for (double cur = 10; cur < heightScreen; cur += 30) {
            gc.strokeLine(0, cur, weightScreen, cur);
        }
    }

    private void drawSignal() {
        if (!isSine && !isRect && !isTriangle){
            return;
        }
        if (isSine){
            drawSineSignal();
        }
        if (isRect){
            drawRectSignal();
        }
        if (isTriangle){
            drawTriangleSignal();
        }
    }

    private void drawCoordinateSystemCanvas(GraphicsContext gc) {
        gc.setStroke(BLACK);
        gc.strokeLine(250, 30, 250, 470);
        gc.strokeLine(30, 250, 470, 250);
    }
    private void clearCanvas(){
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());
        plotAxis();
    }

    public void clickGeneratorSwitcher(MouseEvent mouseEvent) {
        if (enabled) {
            clearCanvas();
            generatorSwitcher.setText("Включить");
            enabled = false;
        } else {
            drawSignal();
            enabled = true;
            generatorSwitcher.setText("Выключить");
        }
    }

    private void drawRectSignal(){
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        gc.setStroke(valueOf("#f44242"));
        double baseOffsetX = 30, baseOffsetY = 250;
        double x = baseOffsetX + offsetX;
        double y = baseOffsetY + offsetY;
        double curFrequency = Math.max(frequency, 1);
        double widthScreen = 500 + offsetX - baseOffsetX;
        int sign = 1;
        while (x < widthScreen){
            if (sign == 1){
                gc.strokeLine(x, y - amplitude, Math.min(x + curFrequency / dutyFactor, widthScreen), y - amplitude);
                x += curFrequency / dutyFactor;
                if (x < widthScreen){
                    gc.strokeLine(x, y - amplitude, x, y);
                }
                sign *= -1;
            } else if (sign == -1){
                gc.strokeLine(x, y, Math.min(x + curFrequency - curFrequency / dutyFactor, widthScreen), y);
                x += curFrequency - curFrequency / dutyFactor;
                if (x < widthScreen){
                    gc.strokeLine(x, y - amplitude, x, y);
                }
                sign *= -1;
            }
        }
    }
    private void drawTriangleSignal(){
        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        gc.setStroke(valueOf("#f44242"));
        double baseOffsetX = 30, baseOffsetY = 250;
        double x = baseOffsetX + offsetX;
        double y = baseOffsetY + offsetY;
        double curFrequency = Math.max(frequency, 1);
        double widthScreen = 500 + offsetX - baseOffsetX;
        int sign = -1, it = 1;
        while (x < widthScreen){
            gc.strokeLine(x,  y, x + curFrequency / 4, y + sign * amplitude);
            x += curFrequency / 4;
            y = y + sign * amplitude;
            it++;
            if (it == 2){
                it = 0;
                sign *= -1;
            }
        }
    }



    private void drawSineSignal() {
        Point2D[] points = new Point2D[440];
        double baseOffsetX = 30, baseOffsetY = 250;
        for (int i = 0; i < points.length; ++i) {
            points[i] = new Point2D(baseOffsetX + i + offsetX, baseOffsetY + offsetY + amplitude * Math.sin(i * 6 / (frequency - 6)));
        }

        GraphicsContext gc = screenCanvas.getGraphicsContext2D();
        gc.setStroke(valueOf("#f44242"));
        for (int i = 1; i < points.length; ++i){
            gc.strokeLine(points[i].getX(), points[i].getY(), points[i-1].getX(), points[i-1].getY());
        }
    }

    public void setHorizontalOffset(MouseEvent mouseEvent) {
        offsetX = horizontalOffsetSlider.getValue();
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }

    public void setVerticalOffset(MouseEvent mouseEvent) {
        offsetY = -verticalOffsetSlider.getValue();
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }

    public void setAmplitude(MouseEvent mouseEvent) {
        amplitude = amplitudeSlider.getValue();
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }

    public void setFrequency(MouseEvent mouseEvent) {
        frequency = frequencySlider.getValue();
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }


    public void setRectSignal(MouseEvent mouseEvent) {
        if (isSine){
            sineSignalButton.disableProperty().setValue(false);
        }
        if (isTriangle){
            triangleSignalButton.disableProperty().setValue(false);
        }
        rectSignalButton.disableProperty().setValue(true);
        dutyFactorSlider.disableProperty().setValue(false);
        isRect = true;
        isSine = false;
        isTriangle = false;
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }

    public void setTriangleSignal(MouseEvent mouseEvent) {
        if (isRect){
            dutyFactorSlider.disableProperty().setValue(true);
            rectSignalButton.disableProperty().setValue(false);
        }
        if (isSine){
            sineSignalButton.disableProperty().setValue(false);
        }
        isRect = false;
        isSine = false;
        isTriangle = true;
        triangleSignalButton.disableProperty().setValue(true);
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }

    public void setSineSignal(MouseEvent mouseEvent) {
        if (isRect){
            dutyFactorSlider.disableProperty().setValue(true);
            rectSignalButton.disableProperty().setValue(false);
        }
        if (isTriangle){
            triangleSignalButton.disableProperty().setValue(false);
        }
        isRect = false;
        isSine = true;
        isTriangle = false;
        sineSignalButton.disableProperty().setValue(true);
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }

    public void setDutyFactor(MouseEvent mouseEvent) {
        dutyFactor = dutyFactorSlider.getValue();
        if (enabled){
            clearCanvas();
            drawSignal();
        }
    }
}
