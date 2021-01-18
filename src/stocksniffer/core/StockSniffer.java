/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stocksniffer.core;

import java.io.File;
import java.io.IOException;
import stocksniffer.view.URLFileReader;
import stocksniffer.view.DefaultURLAnalyzer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StockSniffer {

    private static String ANSI_GREEN = "\u001B[32m";
    private static String ANSI_RED = "\u001B[31m";
    private static String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            run();
            System.out.println("\nA dormir.........ZZZZZZZ.....ZZZZZZ");
            Thread.sleep(60000);
        }
    }

    private static void run() throws InterruptedException {
        File sound = new File("sound/alerta.wav");
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            System.out.println("ERROR LineUnavailable" + ex.getMessage());
        }
        try {
            clip.open(AudioSystem.getAudioInputStream(sound));
        } catch (LineUnavailableException ex) {
            Logger.getLogger(StockSniffer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(StockSniffer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StockSniffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String documentoFuente = "urls.txt";
        URLFileReader reader = new URLFileReader();
        Set<String> urls = reader.read(documentoFuente);
        DefaultURLAnalyzer checker;
        for (String url : urls) {
            checker = new DefaultURLAnalyzer(url);
            if (checker.checkAvailability(url)) {
                System.out.println(checker.nameExtractor() + /*ANSI_GREEN +*/ " [DISPONIBLE]" + /*ANSI_RESET +*/ " (" + checker.priceExtractor() + ")");
                clip.start();
            } else {
                System.out.println(checker.nameExtractor() + /*ANSI_RED + */ " [AGOTADO] " + /*ANSI_RESET +*/ " (" + checker.priceExtractor() + ")");
            }
            Thread.sleep(1000);;
        }
    }
}
