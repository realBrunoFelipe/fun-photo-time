package si.gto76.funphototime.dialogs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import si.gto76.funphototime.MyInternalFrame;
import si.gto76.funphototime.Utility;
import si.gto76.funphototime.filterthreads.HistogramStretchingThread;

public class HistogramStretchingDialog extends FilterDialog
								implements ChangeListener, FilterDialogThatReturnsInts  {

    private BufferedImage histogramImg1, histogramImg2;
    private JPanel p;
    private ImageIcon icon;
    private JLabel label;
    private JSlider sld1, sld2;
    private JOptionPane op;
    private JDialog dlg;
    
    //TODO 2 Ce ga zaprem z x-om pride do napake
    public HistogramStretchingDialog( MyInternalFrame selectedFrame, BufferedImage histogramImg ) {
    	super(selectedFrame, "Histogram"); 
    	
     	histogramImg1 = histogramImg;
        histogramImg2 = Utility.declareNewBufferedImageAndCopy(histogramImg1);
    	
    	p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	
        icon = new ImageIcon((Image)histogramImg2, "description");
        
        label = new JLabel(icon);
        label.setHorizontalAlignment(JLabel.CENTER);
		p.add(label);
         
        sld1 = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
        sld1.addChangeListener(this);
        p.add(sld1);
        
        sld2 = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
        sld2.addChangeListener(this);
        p.add(sld2);
        
        op = new JOptionPane(p,
    		JOptionPane.PLAIN_MESSAGE,
    		JOptionPane.OK_CANCEL_OPTION);
    	
    	dlg = op.createDialog("Histogram");  //(this, "Histogram");
    	dlg.setSize(300, 264);
    	dlg.setLocation(MyDialog.location);
    	dlg.setAlwaysOnTop(true);
    	dlg.setVisible(true);
    	dlg.dispose();
    }
    
    public int[] getInts() {
    	//Vrne vrednosti slajderjev
    	int[] value = new int[2];
    	value[0] = sld1.getValue();
    	value[1] = sld2.getValue();
    	return value;
    }
    
    public boolean wasCanceled() {
    	//pogleda ce je uporabnik zaprl dialog z cancel gumbom
    	MyDialog.location = dlg.getLocation();
    	int option = ((Integer)op.getValue()).intValue();
		if (option == JOptionPane.CANCEL_OPTION) return true;
		else return false;
    }
    
    public void stateChanged(ChangeEvent e) {
    	processPicture(); 	
    	// Poslusa slajderja in sproti izrisuje pomozne crte na histogram
    	//Kako zdej od tle spreminjat ikono v labelu v dialogu?
        histogramImg2 = Utility.declareNewBufferedImageAndCopy(histogramImg1);
        
        int val1 = sld1.getValue();
        int val2 = sld2.getValue();
        
        //Da se ne morta prekrizat
        if ( val1 >= val2 ) {
        	sld1.setValue(val2);
        }
        if ( val2 <= val1 ) {
        	sld2.setValue(val1);
        }
        
		Graphics2D g2 = histogramImg2.createGraphics();
    	final Color black = Color.black;
    	final int height = 400;
    	int height2 = 0;
    	
    	g2.setPaint(black);
    	
    	//da rise crtkani crti
    	final float dash1[] = {10.0f};
	    final BasicStroke dashed = new BasicStroke(1.0f, 
	                                          BasicStroke.CAP_BUTT, 
	                                          BasicStroke.JOIN_MITER, 
	                                          10.0f, dash1, 0.0f);
		g2.setStroke(dashed);
		
    	g2.draw(new Line2D.Double(val1, height, val1, height2));
    	g2.draw(new Line2D.Double(val2, height, val2, height2));
		
		icon.setImage((Image) histogramImg2);
        
        dlg.repaint(100, 0, 0, 1000, 1000);
    
    } 
    
	protected void processPicture() {
		stopActiveThread();
		//nato naredi novo nit
		filterThread = new HistogramStretchingThread(imgIn, imgOut, getInts(), selectedFrame);
	}

}
                                   		
