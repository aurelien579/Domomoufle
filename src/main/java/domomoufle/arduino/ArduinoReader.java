package domomoufle.arduino;

import fr.insalyon.p2i2.javaarduino.usb.ArduinoUsbChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

public class ArduinoReader {

    private final ArrayList<ArduinoReaderListener> listeners;
    private ReadingThread readingThread;
    private String port;

    public ArduinoReader(String port) throws SerialPortException, IOException {
        listeners = new ArrayList();
        this.port = port;
        readingThread = new ReadingThread(this, port);
        readingThread.start();
    }

    public void addListener(ArduinoReaderListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ArduinoReaderListener listener) {
        listeners.remove(listener);
    }

    private void emitNewAcquisition() {
        for (ArduinoReaderListener listener : listeners) {
            listener.onNewAcquisition();
        }
    }

    private void emitAcquisitionReceived(Mesure mesure) {
        for (ArduinoReaderListener listener : listeners) {
            listener.onMesureReceived(mesure);
        }
    }

    private void emitEndAcquisition() {
        for (ArduinoReaderListener listener : listeners) {
            listener.onEndAcquisition();
        }
    }

    public void startReading() {
        stopReading();
        readingThread.startReading();
    }

    public void stopReading() {
        if (readingThread != null) {
            readingThread.stopReading();
        }
    }
    
    public void close() {
        readingThread.close();
    }

    public void changePort(String port) throws SerialPortException, IOException {
        readingThread.close();
        this.port = port;
        readingThread = new ReadingThread(this, port);
        readingThread.start();   
    }

    private class ReadingThread extends Thread {

        private final ArduinoReader reader;
        private final BufferedReader input;
        private final ArduinoUsbChannel vcpChannel;

        private boolean running;

        public ReadingThread(ArduinoReader reader, String port) throws SerialPortException, IOException {
            vcpChannel = new ArduinoUsbChannel(port);
            vcpChannel.open();
            this.reader = reader;
            this.input = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));
        }

        @Override
        public void run() {
            running = true;
            String line;
            try {
                do {
                    line = input.readLine();

                    if (line != null && running) {
                        if (line.equals("NEW")) {
                            reader.emitNewAcquisition();
                        } else if (line.equals("END")) {
                            reader.emitEndAcquisition();
                        } else if (line.split(",").length == 5) {
                            Mesure m = Mesure.parse(line);
                            if (m != null) {
                                reader.emitAcquisitionReceived(m);
                            }
                        }
                    }
                } while (line != null);
            } catch (IOException ex) {
                Logger.getLogger(ReadingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void stopReading() {
            System.out.println("closing");
            running = false;
        }

        public boolean isRunning() {
            return running;
        }

        private void startReading() {
            System.out.println("startReading");
            running = true;
        }

        private void close() {
            try {
                stopReading();
                vcpChannel.close();
            } catch (IOException ex) {
                Logger.getLogger(ArduinoReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
