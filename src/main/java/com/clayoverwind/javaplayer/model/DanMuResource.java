package com.clayoverwind.javaplayer.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DanMuResource {

	private List<SingleDanMu> mOriginalList = new LinkedList<SingleDanMu>();

	private List<SingleDanMu> mPlayingList = new LinkedList<SingleDanMu>();

	private boolean mIsInitialized = false;

	private boolean mIsLoaded = false;

	private long mMaxTime = 0;

	public boolean isInitialized() {
		return mIsInitialized;
	}

	public boolean isLoaded() {
		return mIsLoaded;
	}

	public boolean initByFilePath(String danMuFilePath) {
		mIsInitialized = false;
		mIsLoaded = false;
		mOriginalList.clear();
		parseFromXMLFile(danMuFilePath);
		mIsInitialized = true;
		return true;
	}

	public void loadFromOriginalList() {
		mIsLoaded = false;
		mPlayingList.clear();
		mPlayingList.addAll(mOriginalList);
		mIsLoaded = true;
	}

	public long getMaxTime() {
		return mMaxTime;
	}

	public List<SingleDanMu> getDanMuListByCurTime(long time) {
		List<SingleDanMu> ret = new LinkedList<SingleDanMu>();

		Iterator<SingleDanMu> it = mPlayingList.iterator();
		while (it.hasNext()) {
			SingleDanMu danMu = it.next();
			if (danMu.getStartTime() > time) {
				break;
			}
			ret.add(danMu);
			it.remove();
		}
		return ret;
	}

	private void parseFromXMLFile(String fileName) {
		SAXBuilder builder = new SAXBuilder();
		try {
			System.out.println(fileName);
			File file = new File(fileName);
			Document document = builder.build(file);
			Element rootEl = document.getRootElement();
			List<Element> dList = rootEl.getChildren("d");
//			List<Element> dList = XPath.selectNodes(document, "/i/d");
			for (Iterator<Element> it = dList.iterator(); it.hasNext(); ) {
				Element singleDanMuEle = it.next();
				SingleDanMu danMu = new SingleDanMu(singleDanMuEle.getAttributeValue("p"), singleDanMuEle.getText());
				mOriginalList.add(danMu);
				if (danMu.getStartTime() > mMaxTime) {
					mMaxTime = danMu.getStartTime();
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//sort by start time
		Collections.sort(mOriginalList, new Comparator<SingleDanMu>() {
			public int compare(SingleDanMu arg0, SingleDanMu arg1) {
				return arg0.getStartTime() > arg1.getStartTime() ? 1 : -1;
			}
		});
	}

	public void needReload() {
		this.mIsLoaded = false;
	}

	public void jumpLoad(long time) {
		mIsLoaded = false;
		mPlayingList.clear();
		if(mOriginalList.size() > 0) {
			int fromIndex = 0;
			int toIndex = mOriginalList.size() - 1;
			for (SingleDanMu danMu : mOriginalList) {
				if(danMu.getStartTime() < time) {
					++fromIndex;
				} else {
					break;
				}
			}
			mPlayingList.addAll(mOriginalList.subList(fromIndex, toIndex));
		}
		mIsLoaded = true;
	}
}
