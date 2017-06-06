package domomoufle.arduino;

public interface ArduinoReaderListener {    
    void onMesureReceived(Mesure a);
    void onNewAcquisition();
    void onEndAcquisition();
}
