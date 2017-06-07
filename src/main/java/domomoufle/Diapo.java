package domomoufle;

import domomoufle.arduino.ArduinoReaderListener;
import domomoufle.arduino.Mesure;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Diapo implements ArduinoReaderListener {

    @Override
    public void onMesureReceived(Mesure a) {
    }

    @Override
    public void onNewAcquisition() {            
    }

    @Override
    public void onEndAcquisition() {
        try {
            Thread.sleep(200);
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_RIGHT);
        } catch (AWTException | InterruptedException ex) {
            Logger.getLogger(Diapo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
