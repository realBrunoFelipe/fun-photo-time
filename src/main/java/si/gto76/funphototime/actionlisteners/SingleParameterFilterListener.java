package si.gto76.funphototime.actionlisteners;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import si.gto76.funphototime.MyInternalFrame;
import si.gto76.funphototime.FunPhotoTimeFrame;
import si.gto76.funphototime.dialogs.FilterDialog;
import si.gto76.funphototime.dialogs.FilterDialogThatReturnsDouble;
import si.gto76.funphototime.enums.SingleParameterFilter;

public class SingleParameterFilterListener extends AbstractParameterFilterListener
											implements ActionListener {

	SingleParameterFilter filter;
	double value;
	
	public SingleParameterFilterListener(SingleParameterFilter filter, FunPhotoTimeFrame mainFrame) {
		super(mainFrame);
		this.filter = filter;
	}

	@Override
	protected FilterDialog getDialog(MyInternalFrame frameIn) {
		return (FilterDialog) filter.getDialog(frameIn);
	}

	@Override
	protected void storeValues(FilterDialog dialog) {
		value = ((FilterDialogThatReturnsDouble)dialog).getDouble();
	}

	@Override
	protected Thread createThread(MyInternalFrame frameIn, MyInternalFrame frameOut) {
		return new Thread(new WaitingThread(frameIn, frameOut));
	}
	
	@Override
	protected void createFilterThread(BufferedImage imgIn, MyInternalFrame frameOut) {
		filter.createThread(imgIn, value , frameOut);
	}
	
}
