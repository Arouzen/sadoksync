/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sadoksync.sadoksync;

/**
 *
 * @author Arouz
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;

public class EmptyCanvas extends JComponent {

    private static Color m_tRed = new Color(255, 0, 0, 150);

    private static Color m_tGreen = new Color(0, 255, 0, 150);

    private static Color m_tBlue = new Color(0, 0, 255, 150);

    private static Font monoFont = new Font("Monospaced", Font.BOLD
            | Font.ITALIC, 36);

    private static Font sanSerifFont = new Font("SanSerif", Font.PLAIN, 12);

    private static Font serifFont = new Font("Serif", Font.BOLD, 24);

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w;
        int h;
        // draw entire component white
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // yellow circle
        g.setColor(Color.yellow);
        g.fillOval(0, 0, 240, 240);

        // magenta circle
        g.setColor(Color.magenta);
        g.fillOval(160, 160, 240, 240);

        // transparent red square
        g.setColor(m_tRed);
        g.fillRect(60, 220, 120, 120);

        // transparent green circle
        g.setColor(m_tGreen);
        g.fillOval(140, 140, 120, 120);

        // transparent blue square
        g.setColor(m_tBlue);
        g.fillRect(220, 60, 120, 120);

        g.setColor(Color.black);

        g.setFont(monoFont);
        FontMetrics fm = g.getFontMetrics();
        w = fm.stringWidth("No more");
        h = fm.getAscent();
        g.drawString("No more", 120 - (w / 2), 120 + (h / 4));

        g.setFont(sanSerifFont);
        fm = g.getFontMetrics();
        w = fm.stringWidth("media");
        h = fm.getAscent();
        g.drawString("media", 200 - (w / 2), 200 + (h / 4));

        g.setFont(serifFont);
        fm = g.getFontMetrics();
        w = fm.stringWidth("in list :--)");
        h = fm.getAscent();
        g.drawString("in list :--)", 280 - (w / 2), 280 + (h / 4));
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
