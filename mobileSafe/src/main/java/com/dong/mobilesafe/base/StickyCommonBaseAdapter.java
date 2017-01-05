package com.dong.mobilesafe.base;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.SectionIndexer;

import com.dong.mobilesafe.utils.MyLogger;

public abstract class StickyCommonBaseAdapter<E,T> extends CommonBaseAdapter<E> implements StickyListHeadersAdapter, SectionIndexer {
	protected LayoutInflater mInflater;
	protected List<T> sections;
	protected List<Integer> sectionIndices;

	public StickyCommonBaseAdapter(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		sections = new ArrayList<T>();
		sectionIndices = new ArrayList<Integer>();
	}
	
	@Override
	public Object[] getSections() {
		
		return sections.toArray();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
	     if (sectionIndices.size() == 0) {
	            return 0;
	        }
	     
	        if (sectionIndex >= sectionIndices.size()) {
	        	sectionIndex = sectionIndices.size() - 1;
	        } else if (sectionIndex < 0) {
	        	sectionIndex = 0;
	        }
	        return sectionIndices.get(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		 for (int i = 0; i < sectionIndices.size(); i++) {
	            if (position < sectionIndices.get(i)) {
	                return i - 1;
	            }
	        }
	        return sectionIndices.size() - 1;

	}
	
	
	@Override
	public long getHeaderId(int position) {
		MyLogger.jLog().d(" 1 = "+sectionIndices.get(1));
		return sectionIndices.get(getSectionForPosition(position));

	}
	
	
	public void setSections(List<T> sections) {
		if(sections != null) {
			this.sections.addAll(sections);
		}
	}
	
	
	protected abstract void updateSectionIndices(List<E> list);
	
	

}
