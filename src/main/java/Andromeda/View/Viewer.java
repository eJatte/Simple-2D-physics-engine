/*
 * Classname: Viewer.java
 * Author: Elias Jätte
 * Date: 2017-12-06
 */

package Andromeda.View;

import Andromeda.Collision.Transform;
import Andromeda.Render.Camera;
import Andromeda.Render.Drawable;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 * A class for displaying information. Can display a menu, a dashboard and
 * render drawables.
 * @author Elias Jätte, c16eje
 */
public class Viewer {
    private JFrame frame;
    private RenderPanel renderPanel;
    private JPanel dashboard;
    private JDialog dialogBox;

    /**
     * Creates a new viewer. Creates a new window with a render panel.
     */
    public Viewer(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(640,480));
        frame.setVisible(true);

        renderPanel = new RenderPanel();

        frame.add(renderPanel, BorderLayout.CENTER);

        renderPanel.setBackground(new Color(60, 60, 60));

        renderPanel.grabFocus();

        finalizeFrame();
    }

    /**
     * Set a menu bar for the viewer. Will be set a the top of the window.
     *
     * @param bar           The menu bar
     */
    public void setMenuBar(JMenuBar bar){
        frame.add(bar,BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Render the drawables in the array.
     *
     * @param drawables         The drawables which are to be rendered.
     */
    public void renderDrawables(ArrayList<Drawable> drawables){
        renderPanel.draw(drawables);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Set the dashboard of the viewer. Will be set at the bottom of the window.
     * @param dashboard
     */
    public void setDashboard(JPanel dashboard){
        if(this.dashboard != null)
            frame.remove(this.dashboard);
        frame.add(dashboard, BorderLayout.WEST);
        this.dashboard = dashboard;
        frame.revalidate();
        frame.repaint();
    }

    public Point getMousePosition(){
        return renderPanel.getMousePosition();
    }

    /**
     * Adds a mouse listener to the renderPanel
     * @param listener          The listener
     */
    public void addRenderPanelMouseListener(MouseListener listener){
        renderPanel.addMouseListener(listener);
    }

    public void addRenderPanelMouseWheelListener(MouseWheelListener listener){
        renderPanel.addMouseWheelListener(listener);
    }

    public void addRenderPanelKeyListener(KeyListener listener){
        renderPanel.addKeyListener(listener);
    }

    public Transform getScreenTransform(){
        return renderPanel.getScreenTransform();
    }

    public void setCamera(Camera camera){
        renderPanel.setCamera(camera);
    }

    /**
     * Display a dialog box
     *
     * @param panel     Panel to display
     * @param title     Title of the dialog box
     */
    public void displayDialogBox(JPanel panel, String title, int minX, int minY){
        closeDialogBox();
        dialogBox = new JDialog();
        dialogBox.setLocationRelativeTo(frame);
        dialogBox.setMinimumSize(new Dimension(minX,minY));
        dialogBox.setTitle(title);
        dialogBox.setAlwaysOnTop(true);
        dialogBox.add(panel);
        dialogBox.pack();
        dialogBox.setVisible(true);
    }

    /**
     * Closes the current dialog box
     */
    public void closeDialogBox(){
        if(dialogBox != null)
            dialogBox.dispose();
    }

    /**
     * Display an error prompt
     *
     * @param prompt        The message
     */
    public void prompt(String prompt) {
        JOptionPane.showMessageDialog(frame, prompt,"Error"
                ,JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Finalizes the frame and sets its position and size according to the
     * screen size.
     */
    private void finalizeFrame(){
        frame.pack();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        frame.setSize((int)(width*0.8),(int)(height*0.8));
        frame.setLocation(width/2-frame.getSize().width/2,
                height/2-frame.getSize().height/2);
    }
}
