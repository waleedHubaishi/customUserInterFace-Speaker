package ch.fhnw.cuie.assignment_1.template;

import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

public class SimpleControl extends StackPane {
    private static final double ARTBOARD_WIDTH  = 100;
    private static final double ARTBOARD_HEIGHT = 100;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 25;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 800;

    // Todo: replace these elements with all parts of your Custom Control
    private Region speakerVolOne;
    private Region speakerVolTwo;
    private Region speakerVolThree;
    private Region speakerMute;


    // Todo: replace this Property with all Properties needed to keep the status of your Custom Control
    private final BooleanProperty isMute = new SimpleBooleanProperty();
    private final IntegerProperty vol = new SimpleIntegerProperty();

    // needed for resizing
    private Pane drawingPane;

    public SimpleControl() {
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        setupValueChangeListeners();
        setupBinding();
    }

    private void initializeSelf() {
        // load stylesheets
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);

        getStyleClass().add("simpleControl");
    }

    private void initializeParts() {
        //todo initialize all parts
        speakerVolOne = new Region();
        speakerVolOne.getStyleClass().addAll("volOne");

        speakerVolTwo = new Region();
        speakerVolTwo.getStyleClass().addAll("volTwo");

        speakerVolThree = new Region();
        speakerVolThree.getStyleClass().addAll("volThree");

        speakerMute = new Region();
        speakerMute.getStyleClass().addAll("mute");

        speakerVolTwo.setVisible(false);
        speakerVolThree.setVisible(false);
        speakerMute.setVisible(false);
        speakerVolOne.setVisible(true);

        // always needed
        drawingPane = new StackPane();
        drawingPane.getStyleClass().addAll("drawingPane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        // todo add all parts to drawingPane
        drawingPane.getChildren().addAll(speakerVolOne,speakerVolTwo,speakerVolThree,speakerMute);
        getChildren().addAll(drawingPane);
    }

    private void setupEventHandlers() {
        speakerVolThree.setOnMouseClicked(event -> setIsMute(!isIsMute()));
        speakerVolTwo.setOnMouseClicked(event -> setIsMute(!isIsMute()));
        speakerVolOne.setOnMouseClicked(event -> setIsMute(!isIsMute()));
        speakerMute.setOnMouseClicked(event -> setIsMute(!isIsMute()));
    }

    // to restote the previous volume before mute
    private void returnAfterMute()
    {
        if(getVol()==0)
        {
            speakerVolOne.setVisible(true);
        }
        else if(getVol() == 1)
        {
            speakerVolTwo.setVisible(true);
        }
        else if(getVol() == 2)
        {
            speakerVolThree.setVisible(true);
        }
    }

    //showing and disabling the correct region as the volumem change
    private void setupValueChangeListeners() {
        volProperty().addListener((observable, oldValue, newValue) -> {
            speakerVolOne.setVisible(false);
            speakerVolTwo.setVisible(false);
            speakerVolThree.setVisible(false);
            speakerMute.setVisible(false);
            setIsMute(false);

            if((int)newValue == 0) {
               speakerVolOne.setVisible(true);
            }

            else if((int)newValue == 1) {

                  speakerVolTwo.setVisible(true);

            }
              else if((int)newValue == 2) {
                  speakerVolThree.setVisible(true);
            }
        });

        isMuteProperty().addListener((observable, oldValue, newValue) -> {

            speakerVolOne.setVisible(false);
            speakerVolTwo.setVisible(false);
            speakerVolThree.setVisible(false);
            speakerMute.setVisible(false);

            if(isIsMute())
            {
                speakerMute.setVisible(newValue);
            }
            else
            {
                returnAfterMute();
            }
        });
    }

    private void setupBinding() {
        //todo
        //nothing to be done in here
    }


    //resize by scaling
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void resize() {
        Insets padding         = getPadding();
        double availableWidth  = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    // some handy functions

    private double percentageToValue(double percentage, double minValue, double maxValue){
        return ((maxValue - minValue) * percentage) + minValue;
    }

    private double valueToPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue);
    }

    private double valueToAngle(double value, double minValue, double maxValue) {
        return percentageToAngle(valueToPercentage(value, minValue, maxValue));
    }

    private double mousePositionToValue(double mouseX, double mouseY, double cx, double cy, double minValue, double maxValue){
        double percentage = angleToPercentage(angle(cx, cy, mouseX, mouseY));

        return percentageToValue(percentage, minValue, maxValue);
    }

    private double angleToPercentage(double angle){
        return angle / 360.0;
    }

    private double percentageToAngle(double percentage){
        return 360.0 * percentage;
    }

    private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx     = deltaX / radius;
        double ny     = deltaY / radius;
        double theta  = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }

    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.cos(Math.toRadians(angle - 90))),
                           cY + (radius * Math.sin(Math.toRadians(angle - 90))));
    }

    private Text createCenteredText(String styleClass) {
        return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
    }

    private Text createCenteredText(double cx, double cy, String styleClass) {
        Text text = new Text();
        text.getStyleClass().add(styleClass);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
        text.setWrappingWidth(width);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setY(cy);
        text.setX(cx - (width / 2.0));

        return text;
    }

    private Group createTicks(double cx, double cy, int numberOfTicks, double overallAngle, double tickLength, double indent, double startingAngle, String styleClass) {
        Group group = new Group();

        double degreesBetweenTicks = overallAngle == 360 ?
                                     overallAngle /numberOfTicks :
                                     overallAngle /(numberOfTicks - 1);
        double outerRadius         = Math.min(cx, cy) - indent;
        double innerRadius         = Math.min(cx, cy) - indent - tickLength;

        for (int i = 0; i < numberOfTicks; i++) {
            double angle = 180 + startingAngle + i * degreesBetweenTicks;

            Point2D startPoint = pointOnCircle(cx, cy, outerRadius, angle);
            Point2D endPoint   = pointOnCircle(cx, cy, innerRadius, angle);

            Line tick = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
            tick.getStyleClass().add(styleClass);
            group.getChildren().add(tick);
        }

        return group;
    }

    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // all getter and setter


    public boolean isIsMute() {
        return isMute.get();
    }

    public BooleanProperty isMuteProperty() {
        return isMute;
    }

    public void setIsMute(boolean isMute) {
        this.isMute.set(isMute);
    }

    public int getVol() {
        return vol.get();
    }

    public IntegerProperty volProperty() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol.set(vol);
    }


}
