package si.gto76.funphototime.dialogs;

import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import si.gto76.funphototime.MyInternalFrame;
import si.gto76.funphototime.filterthreads.BitPlaneThread;


public class BitPlaneDialog extends FilterDialog  {
	
	private static final long serialVersionUID = 5942837537107901624L;
	private JSpinner spinner;
	
	public BitPlaneDialog( MyInternalFrame selectedFrame ) {
		super( selectedFrame, "Bit Plane", BoxLayout.X_AXIS, 265, 120);
		
		SpinnerModel model =
        new SpinnerNumberModel(1, 1, 8, 1);
		spinner = new JSpinner(model);
		
		spinner.addChangeListener(this);
		addComponent(spinner);
		
	}
	
	public void stateChanged(ChangeEvent e)  {
		/* OLD:
		if ( filterThread != null ) {
			filterThread.t.interrupt();
		}*/
		stopActiveThread();
		filterThread = new BitPlaneThread(imgIn, imgOut, getValues(), selectedFrame);
	}
    
    public int getValues() {
    	return (Integer) spinner.getValue();
    }

}

