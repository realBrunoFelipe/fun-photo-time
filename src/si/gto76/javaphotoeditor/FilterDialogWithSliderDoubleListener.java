package si.gto76.javaphotoeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.gto76.javaphotoeditor.dialogs.FilterDialogWithSliderDouble;
import si.gto76.javaphotoeditor.dialogs.GammaDialog;
import si.gto76.javaphotoeditor.filterthreads2.GammaThread2;

public class FilterDialogWithSliderDoubleListener implements ActionListener{
	JavaPhotoEditorFrame mainFrame;
	Filter fi;

	public FilterDialogWithSliderDoubleListener(Filter fi, JavaPhotoEditorFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.fi = fi;
    }
	
	public void actionPerformed(ActionEvent e) {
    	MyInternalFrame frameIn = (MyInternalFrame) mainFrame.desktop.getSelectedFrame();
    	FilterDialogWithSliderDouble dialog = fi.getDialog(frameIn); //right CAST
    	
    	if (!dialog.wasCanceled()) {
    		double values = dialog.getValues(); //right VALUE TYPE
    		MyInternalFrame frameOut = mainFrame.createZoomedFrame(dialog.getProcessedImage(), frameIn);
    		fi.createThread(frameIn.getOriginalImg(), values , frameOut); //right THREAD
    	}
    	dialog.resetOriginalImage();
    }
	
}