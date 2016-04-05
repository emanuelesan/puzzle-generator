package com.eds.netaclon.puzzlegraph.renderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * pane that shows a pic.
 */
public class ImageShow  {
        private JFrame frame;

        public static void main(String avg[]) throws IOException
        {
            BufferedImage img= ImageIO.read(new File("f://images.jpg"));
            ImageShow abc=new ImageShow(img);
            abc.show();

        }

        public ImageShow(BufferedImage img) throws IOException
        {
            ImageIcon icon=new ImageIcon(img);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(img.getWidth(), img.getHeight());
            JLabel lbl=new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            this.frame= frame;
        }

    public void show() {
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }

    public void repaint()
    {
        frame.repaint();
    }
}
