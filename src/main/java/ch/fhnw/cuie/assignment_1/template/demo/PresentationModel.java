package ch.fhnw.cuie.assignment_1.template.demo;

import javafx.beans.property.*;

/**
 * @author Dieter Holz
 */
public class PresentationModel {

    // all the properties waiting for being displayed
    private final BooleanProperty isMute = new SimpleBooleanProperty();
    private final IntegerProperty vol = new SimpleIntegerProperty();

    // all getters and setters (generated via "Code -> Generate -> Getter and Setter)


    public int getVol() {
        return vol.get();
    }

    public IntegerProperty volProperty() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol.set(vol);
    }

    public boolean isIsMute() {
        return isMute.get();
    }

    public BooleanProperty isMuteProperty() {
        return isMute;
    }

    public void setIsMute(boolean isMuted) {
        this.isMute.set(isMuted);
    }


}
