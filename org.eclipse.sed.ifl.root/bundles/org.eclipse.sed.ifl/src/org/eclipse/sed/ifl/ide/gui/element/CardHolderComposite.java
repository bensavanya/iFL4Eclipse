package org.eclipse.sed.ifl.ide.gui.element;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.sed.ifl.control.score.Score;
import org.eclipse.sed.ifl.model.source.IMethodDescription;
import org.eclipse.sed.ifl.util.event.INonGenericListenerCollection;
import org.eclipse.sed.ifl.util.event.core.NonGenericListenerCollection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class CardHolderComposite extends Composite {

	private HashMap<Integer, List<Map.Entry<IMethodDescription, Score>>> contents = new HashMap<>();
	
	private Composite cardArea;
	private Composite buttonArea;
	private int pageCount = 1;
	private Label pageCountLabel;
	
	public CardHolderComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		cardArea = new Composite(this, SWT.NONE);
		GridLayout gl_cardArea = new GridLayout(4, false);
		gl_cardArea.marginWidth = 0;
		gl_cardArea.marginHeight = 0;
		cardArea.setLayout(gl_cardArea);
		GridData gd_cardArea = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_cardArea.widthHint = 1300;
		gd_cardArea.heightHint = 440;
		cardArea.setLayoutData(gd_cardArea);

		buttonArea = new Composite(this, SWT.NONE);
		buttonArea.setLayout(new GridLayout(5, false));
		GridData gd_buttonArea = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_buttonArea.heightHint = 52;
		gd_buttonArea.widthHint = 438;
		buttonArea.setLayoutData(gd_buttonArea);
		
		setSize(Math.max(cardArea.getSize().x, buttonArea.getSize().x), Math.addExact(cardArea.getSize().y, buttonArea.getSize().y));
		
		Button home = new Button(buttonArea, SWT.NONE);
		home.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		home.setText("<<");
		home.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageCount(1, 0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
		
		Button pageDown = new Button(buttonArea, SWT.NONE);
		pageDown.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		pageDown.setText("<");
		pageDown.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageCount(pageCount, -1);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
			
		});
		
		pageCountLabel = new Label(buttonArea, SWT.NONE);
		pageCountLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Button pageUp = new Button(buttonArea, SWT.NONE);
		pageUp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		pageUp.setText(">");
		pageUp.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageCount(pageCount, +1);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
			
		});
		
		Button end = new Button(buttonArea, SWT.NONE);
		end.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		end.setText(">>");
		end.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageCount(contents.size(), 0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});
	}
	
	public void setContent(Map<IMethodDescription, Score> scores) {
		contents.clear();
		ArrayList<Map.Entry<IMethodDescription, Score>> elements = new ArrayList<Map.Entry<IMethodDescription, Score>>();
		int numberOfElementsPerPage = 8;
		int counter = 1;
		int pageNumber = 1;
		for(Map.Entry<IMethodDescription, Score> pageContent : scores.entrySet()) {
			if(counter<numberOfElementsPerPage) {
				elements.add(pageContent);
				counter++;
			} else {
				elements.add(pageContent);
				contents.put(pageNumber, elements);
				pageNumber++;
				elements = new ArrayList<Map.Entry<IMethodDescription, Score>>();
				counter = 1;
			}
		}
		if(counter>1) {
			contents.put(pageNumber, elements);

		}
		setPageCount(1, 0);
	}
	
	private void setPageContent(int pageNumber) {
		clearMethodScores();
		for (Entry<IMethodDescription, Score> entry : contents.get(pageNumber)) {
			CodeElementUI element = new CodeElementUI(cardArea, SWT.NONE,
					entry.getValue(),
					entry.getKey().getId().getName(),
					entry.getKey().getId().getSignature(),
					entry.getKey().getId().getParentType(),
					entry.getKey().getLocation().getAbsolutePath(),
					entry.getKey().getLocation().getBegining().getOffset().toString(),
					entry.getKey().getContext().size()+1,
					entry.getKey().isInteractive(),
					entry.getValue().getLastAction());
			element.setData(entry.getKey());
			element.setData("score", entry.getValue());
			element.setData("entry", entry);
			
			element.setChildrenBackgroundColor();
		}
		cardArea.requestLayout();
		displayedPageChanged.invoke(getDisplayedCards());
	}
	
	public List<CodeElementUI> getDisplayedCards() {
		ArrayList<CodeElementUI> rvList = new ArrayList<CodeElementUI>();
		for (Control control: cardArea.getChildren()) {
			rvList.add(((CodeElementUI)control));
		}
		return rvList;
	}
	
	private void setPageCount(int currentPage, int change) {
		if(currentPage+change >= 1 && currentPage+change <= contents.size()) {
			setPageContent(currentPage+change);
			pageCount = currentPage+change;
			pageCountLabel.setText(pageCount + "/" + contents.size());
			pageCountLabel.requestLayout();
			pageCountLabel.setVisible(true);
		}
	}

	public void clearMethodScores() {
		for(Control control : cardArea.getChildren()) {
			control.setMenu(null);
			control.dispose();
		}
	}
	
	private NonGenericListenerCollection<List<CodeElementUI>> displayedPageChanged = new NonGenericListenerCollection<>();
	
	public INonGenericListenerCollection<List<CodeElementUI>> eventDisplayedPageChanged() {
		return displayedPageChanged;
	}
}