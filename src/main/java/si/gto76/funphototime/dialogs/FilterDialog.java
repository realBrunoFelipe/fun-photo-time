package si.gto76.funphototime.dialogs;

import java.awt.image.BufferedImage;
import javax.swing.event.ChangeListener;

import si.gto76.funphototime.MyInternalFrame;
import si.gto76.funphototime.Utility;
import si.gto76.funphototime.filterthreads.FilterThread;

public abstract class FilterDialog extends MyDialog
								implements ChangeListener {
	
	protected BufferedImage imgIn, imgOut;
	protected MyInternalFrame selectedFrame;
	protected FilterThread filterThread;
	
	public FilterDialog( MyInternalFrame selectedFrame, String title ) {
		super(title);
		init(selectedFrame);
	}
	
	public FilterDialog( MyInternalFrame selectedFrame, String title, int orientation ) {
		super(title, orientation);
		init(selectedFrame);
	}
	// constructor with size
	public FilterDialog( MyInternalFrame selectedFrame, String title, int orientation, int x, int y) {
		super(title, orientation, x, y);
		init(selectedFrame);
	}
	
	private void init(MyInternalFrame selectedFrame) {
		this.selectedFrame = selectedFrame;
		imgIn = selectedFrame.getImg();
		imgOut = Utility.declareNewBufferedImageAndCopy(imgIn); //naredi kopijo
		selectedFrame.setImg(imgOut); //na kero usmeri kazalec od frejma
	}
    
    public void resetOriginalImage() {
    	//spremeni sliko iz izbranega okvirja v prvotno stanje
    	//ce sploh obstaja kaksna nit jo ustavi ali pocaka
    	if (filterThread != null) {
    		try {
	    		//ce je bil izbran cancel avtomatsko prekine zadnjo nit,
	    		//drugace (ce je bil OK) jo pocaka da konca svoje delo
	    		//to verjetno niti ni nujno
	    		if ( wasCanceled() ) 
					filterThread.t.interrupt();
				filterThread.t.join();
			} 
			catch ( InterruptedException e ) {}
		}
		selectedFrame.setImg(imgIn);
    }

	// When slider moves it first checks if any thread exists,
	// and if so, it terminates it. Then it starts a new thread.
	public void stopActiveThread() {
		if ( filterThread != null ) {
			filterThread.t.interrupt();
			try {
				filterThread.t.join();
			}
			catch ( InterruptedException f ) {}
		}
	}
	
	public BufferedImage getProcessedImage() {
		try {
			if (filterThread != null) {
				filterThread.t.join();
			}
		} 
		catch ( InterruptedException e ) {}
		return imgOut;
    }

}
