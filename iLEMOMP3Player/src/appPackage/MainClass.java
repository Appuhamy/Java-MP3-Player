/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppPackage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.*;
import javazoom.jl.player.Player;
import javazoom.jl.player.AudioDeviceBase;
import javax.sound.sampled.*;

/**
 *
 * @author Chamindu_Appuhamy
 */
public class MainClass {

    FileInputStream fis;
    BufferedInputStream bis;
    public Player player;
    public long pauseLocation;
    public int available;
    public long songTotal;
    public String fileLocation;
    public float volume;
    public appPackage.MP3Player mP3Player;

    public void stop() {
        if (player != null) {
            player.close();
            pauseLocation = 0;
            mP3Player.sliderBar.setValue(0);
            songTotal = 0;
        }
    }

    public void pause() {
        if (player != null) {
            try {
                pauseLocation = fis.available();
                player.close();
            } catch (IOException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Resume() {
        try {
            fis = new FileInputStream(fileLocation);
            bis = new BufferedInputStream(fis);
            player = new Player(bis);
            fis.skip(songTotal - pauseLocation);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();
                    available=fis.available();
                } catch (JavaLayerException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    public void play(String path) {
        try {
            
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);
            player = new javazoom.jl.player.Player(bis);
            songTotal = fis.available();
            fileLocation = path + "";
            mP3Player.sliderBar.setMinimum(0);
            mP3Player.sliderBar.setMaximum((int) songTotal);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppPackage.MainClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(AppPackage.MainClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppPackage.MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException ex) {
                    Logger.getLogger(AppPackage.MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < songTotal; i++) {
                    int temp = 0;
                    if (temp % 10 == 0) {
                        temp++;
                        try {
                            long now = fis.available();
                            now = (songTotal - now);
                            mP3Player.sliderBar.setValue((int) now);
                            mP3Player.repaint();
                            mP3Player.revalidate();
                        } catch (IOException ex) {
                            Logger.getLogger(appPackage.MP3Player.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    temp++;
                }

            }
        }.start();
    }

    public void Sound(float f)  {
       
    }
}
