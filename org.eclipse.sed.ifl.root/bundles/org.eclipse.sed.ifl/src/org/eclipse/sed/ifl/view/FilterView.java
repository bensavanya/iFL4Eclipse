package org.eclipse.sed.ifl.view;

import org.eclipse.sed.ifl.general.IEmbeddable;
import org.eclipse.sed.ifl.general.IEmbedee;
import org.eclipse.sed.ifl.ide.gui.FilterPart;
import org.eclipse.sed.ifl.util.event.IListener;
import org.eclipse.sed.ifl.util.event.INonGenericListenerCollection;
import org.eclipse.sed.ifl.util.event.core.NonGenericListenerCollection;
import org.eclipse.sed.ifl.util.exception.EU;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class FilterView extends View implements IEmbeddable, IEmbedee {
	
	private FilterPart filterPart;
	
	public FilterView() {
		this.filterPart = (FilterPart) getPart();
	}

	private IViewPart getPart() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart view = page.findView(FilterPart.ID);
		
		if (view != null) {
			page.hideView(view);
		}
		EU.tryUnchecked(() -> page.showView(FilterPart.ID));
		view = page.findView(FilterPart.ID);
		if (view == null) {
			throw new RuntimeException();
		}
		else {
			return view;
		}
	}
	
	@Override
	public void init() {

		filterPart.eventlowerScoreLimitChanged().add(lowerScoreLimitChangedListener);
		filterPart.eventlowerScoreLimitEnabled().add(lowerScoreLimitEnabledListener);
		filterPart.eventContextSizeLimitEnabled().add(contextSizeLimitEnabledListener);
		filterPart.eventContextSizeLimitChanged().add(contextSizeLimitChangedListener);
		filterPart.eventContextSizeRelationChanged().add(contextSizeRelationChangedListener);
		filterPart.eventNameFilterChanged().add(nameFilterChangedListener);
		filterPart.eventLimitRelationChanged().add(limitFilterRelationChangedListener);
		
		super.init();
	}
	
	@Override
	public void teardown() {
		
		filterPart.eventlowerScoreLimitChanged().remove(lowerScoreLimitChangedListener);
		filterPart.eventlowerScoreLimitEnabled().remove(lowerScoreLimitEnabledListener);
		filterPart.eventContextSizeLimitEnabled().remove(contextSizeLimitEnabledListener);
		filterPart.eventContextSizeLimitChanged().remove(contextSizeLimitChangedListener);
		filterPart.eventContextSizeRelationChanged().remove(contextSizeRelationChangedListener);
		filterPart.eventNameFilterChanged().remove(nameFilterChangedListener);
		filterPart.eventLimitRelationChanged().remove(limitFilterRelationChangedListener);
		
		super.teardown();
	}
	
	@Override
	public void embed(IEmbeddable embedded) {
		filterPart.embed(embedded);

	}

	@Override
	public void setParent(Composite parent) {
		filterPart.setParent(parent);

	}

	public void showFilterPart() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(FilterPart.ID);
		} catch (PartInitException e) {
			System.out.println("Could not open filters view.");
		}
	}
	
	public void close() {
		filterPart.getSite().getPage().hideView(filterPart);
	}
	
	public void setScoreFilter(double min, double max, double current) {
		filterPart.setScoreFilter(min, max, current);
	}
	
	public void setScoreFilter(double min, double max) {
		filterPart.setScoreFilter(min, max);
	}
	
	private NonGenericListenerCollection<Double> lowerScoreLimitChanged = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<Double> eventlowerScoreLimitChanged() {
		return lowerScoreLimitChanged;
	}
	
	private IListener<Double> lowerScoreLimitChangedListener = lowerScoreLimitChanged::invoke;

	private NonGenericListenerCollection<Boolean> lowerScoreLimitEnabled = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<Boolean> eventlowerScoreLimitEnabled() {
		return lowerScoreLimitEnabled;
	}
	
	private IListener<Boolean> lowerScoreLimitEnabledListener = lowerScoreLimitEnabled::invoke;
	
	private NonGenericListenerCollection<Boolean> contextSizeLimitEnabled = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<Boolean> eventContextSizeLimitEnabled() {
		return contextSizeLimitEnabled;
	}
	
	private IListener<Boolean> contextSizeLimitEnabledListener = contextSizeLimitEnabled::invoke;
	
	private NonGenericListenerCollection<Integer> contextSizeLimitChanged = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<Integer> eventContextSizeLimitChanged() {
		return contextSizeLimitChanged;
	}
	
	private IListener<Integer> contextSizeLimitChangedListener = contextSizeLimitChanged::invoke;
	
	private NonGenericListenerCollection<String> contextSizeRelationChanged = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<String> eventContextSizeRelationChanged() {
		return contextSizeRelationChanged;
	}
	
	private IListener<String> contextSizeRelationChangedListener = contextSizeRelationChanged::invoke;
	
	private NonGenericListenerCollection<String> limitFilterRelationChanged = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<String> eventLimitFilterRelationChanged() {
		return limitFilterRelationChanged;
	}
	
	private IListener<String> limitFilterRelationChangedListener = limitFilterRelationChanged::invoke;
	
	private NonGenericListenerCollection<String> nameFilterChanged = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<String> eventNameFilterChanged() {
		return nameFilterChanged;
	}
	
	private IListener<String> nameFilterChangedListener = nameFilterChanged::invoke;
	
	
}
