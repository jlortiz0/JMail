/*
 * Copyright (C) 2018 jlortiz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.awt.Color;
import java.awt.Dialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author jlortiz
 */
public class JMail {
    public static SocketInter sock = null;
    public static String ip;
    public static void main(String args[]) throws InterruptedException {
        Blake.hash("");
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        UIManager.put( "control", new Color( 128, 128, 128) );
        UIManager.put( "info", new Color(128,128,128) );
        UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
        UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
        UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
        UIManager.put( "nimbusFocus", new Color(115,164,209) );
        UIManager.put( "nimbusGreen", new Color(176,179,50) );
        UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
        UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
        UIManager.put( "nimbusOrange", new Color(191,98,4) );
        UIManager.put( "nimbusRed", new Color(169,46,34) );
        UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
        UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
        UIManager.put( "text", new Color( 230, 230, 230) );
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception e) {}
        //</editor-fold>
        new JMailConnector().setVisible(true);
    }
    
    public static void crash(String msg, String title) {
        error(msg, title);
        if (sock!=null) {
            try {sock.close();} catch (Exception e) {}
        }
        System.exit(1);
    }
    public static void error(String msg, String title) {
        JOptionPane.showMessageDialog((Dialog)null,
        msg,
        title,
        JOptionPane.ERROR_MESSAGE);
    }
    
    public static String getResponse(String msg) {
        long time = System.currentTimeMillis();
        if (msg!=null)
            sock.send(msg);
        try {
            while (sock.avail() == 0) {
                Thread.sleep(500);
                if (System.currentTimeMillis()-time > 3000) {
                    crash("We lost connection to the server at \""+ip+"\"\nPlease check your internet connection.", "Connection error");
                }
            }
        } catch (InterruptedException e) {
            crash("The program was interrupted.", "Operation Interrupted");
        }
        msg = sock.receive();
        if (msg.substring(0, Math.min(msg.length(), 4)).equals("QUIT"))
            crash("Server closed connection:\n"+msg.substring(5), "Kicked from server");
        return msg;
    }
}
