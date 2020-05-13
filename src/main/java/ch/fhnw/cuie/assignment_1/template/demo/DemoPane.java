package ch.fhnw.cuie.assignment_1.template.demo;

import ch.fhnw.cuie.assignment_1.template.SimpleControl;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {

    private final PresentationModel pm;

    // declare the custom control
    private SimpleControl cc;

    // all controls
    private Button volUp;
    private Button volDown;
    private Button muteBtn;



    public DemoPane(PresentationModel pm) {
        this.pm = pm;
        initializeSelf();
        initializeControls();
        layoutControls();
        setupBindings();
        setEventsHandler();
        valueChangedListner();
    }

    //initilize the simple contrl object and the buttons
    private void initializeControls() {
        setPadding(new Insets(10));
        cc = new SimpleControl();
        pm.setVol(0);
        pm.setIsMute(false);
        volDown = new Button("-");
        volUp = new Button("+");
        muteBtn = new Button("Mute");
    }

    //setup actions for the buttons
    private void setEventsHandler()
    {
        volUp.setOnAction(event -> {
            pm.setIsMute(false);
            changeButtonText(pm.isIsMute());
            if(pm.getVol()< 2) {
                pm.setVol(pm.getVol()+1);
                volUp.setDisable(false);
                volDown.setDisable(false);
            }
             if(pm.getVol() == 2)
            {
                volUp.setDisable(true);
                volDown.setDisable(false);
            }
        });

        volDown.setOnAction(event -> {
            pm.setIsMute(false);
            changeButtonText(pm.isIsMute());
            if(pm.getVol()> 0) {
                pm.setVol(pm.getVol()-1);
                volUp.setDisable(false);
                volDown.setDisable(false);
            }
            if(pm.getVol() == 0)
            {
                volUp.setDisable(false);
                volDown.setDisable(true);
            }
        });

        muteBtn.setOnAction(event -> {
            if(pm.isIsMute() == false) {
                changeButtonText(pm.isIsMute());
                pm.setIsMute(true);
            }
            else
            {
                changeButtonText(pm.isIsMute());
                pm.setIsMute(false);

            }
        });
    }

    private void initializeSelf() {
        // load stylesheets
        String fonts = getClass().getResource("../fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("../style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }

    //setting up the UI elemnts on its positions
    private void layoutControls() {
        VBox buttons = new VBox(5);
        buttons.getChildren().addAll(volUp,volDown,muteBtn);
        VBox controlPane = new VBox(new Label("press one of the following or\nclick on the image to mute:"), buttons);

        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(cc);
        setRight(controlPane);
    }


    //bind the mute as well as the volume properties from the simple control to the presentation model
    private void setupBindings() {
        cc.volProperty().bindBidirectional(pm.volProperty());
        cc.isMuteProperty().bindBidirectional(pm.isMuteProperty());
    }

    private void valueChangedListner()
    {
        pm.isMuteProperty().addListener((observable, oldValue, newValue) -> {
            changeButtonText(newValue);
        });
    }

    //the responsible of changing the text on the mute button
    public void changeButtonText(boolean isMute)
    {
        if(isMute)
        {
            muteBtn.setText("Unmute");
        }
        else
        {
            muteBtn.setText("Mute");
        }
    }

}
