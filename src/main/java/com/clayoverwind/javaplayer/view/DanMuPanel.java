package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.*;
import com.clayoverwind.javaplayer.iview.IDanMuPanel;
import com.clayoverwind.javaplayer.model.DanMuResource;
import com.clayoverwind.javaplayer.model.SingleDanMu;
import com.clayoverwind.javaplayer.presenter.DanMuPlayer;
import com.clayoverwind.javaplayer.util.StringUtil;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DanMuPanel extends JPanel implements IDanMuPanel {

	public static final int MSPF = 25;

	private static final long serialVersionUID = -2630361033538823440L;

	private static final int RANDOM_Y_DIFF = 32;

	public static final long FINAL_DELAY_TIME = 5000;

	List<SingleDanMuLabel> mDanMuLabelList = new LinkedList<>();

	private DanMuResource danMuResource;

	private Timer tickTimer;//used to do logic and ui update

	private DanMuPlayer danMuPlayer;

	private Random mRandom = new Random();

	private long time = 0;

	private long duration = 0;

	private boolean isPlaying = false;

	public DanMuPanel(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(null);
//		this.setBackground(Color.blue);
		this.setOpaque(false);

		danMuPlayer = new DanMuPlayer(this);
		mRandom.setSeed(System.currentTimeMillis());
		Application.INSTANCE.subscribe(this);
		danMuResource = new DanMuResource();

		tickTimer = new Timer(MSPF, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
	}

//	public void addRandomDanMu(TickEvent e) {
//		if (mRandom.nextInt(100) > 50) {
//			System.out.println("---------addRandomDanMu-------------------");
//			addDanMu(StringUtil.genRandomString(10), "0xFFFFFF", mRandom.nextInt(SingleDanMu.MAX_LINE_COUNT));
//		}
//	}

	public void addDanMu(String content, String colorHexStr, int lineNo) {
		SingleDanMuLabel newDanMuLabel = new SingleDanMuLabel(content, colorHexStr, lineNo);
		mDanMuLabelList.add(newDanMuLabel);
		int y = this.getHeight() / SingleDanMu.MAX_LINE_COUNT * lineNo + mRandom.nextInt(RANDOM_Y_DIFF);
		newDanMuLabel.setLocation(this.getWidth(), y);
		this.add(newDanMuLabel);
	}
	
	public void clearAllDanMu() {
		for (JLabel l : mDanMuLabelList) {
			this.remove(l);
		}
		mDanMuLabelList.clear();
		repaint();
	}

	public void tick() {
		List<SingleDanMu> danmuList = danMuResource.getDanMuListByCurTime(time);
		for(SingleDanMu danmu : danmuList) {
			addDanMu(danmu.getContent(), danmu.getColorHexStr(), danmu.getLineNo());
		}
		danMuTick();
	}

	private void danMuTick() {
		for (Iterator<SingleDanMuLabel> it = mDanMuLabelList.iterator(); it.hasNext();) {
			SingleDanMuLabel l = it.next();
			l.setLocation(l.getX() - l.getSpeed(), l.getY());
//			l.setBounds(l.getX() - 2, l.getY(), l.getWidth(), l.getHeight());
			if (l.getX() + l.getWidth() < 0) {
				System.out.println("remove-------------------");
				this.remove(l);
				it.remove();
			}
		}
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public DanMuPlayer getDanMuPlayer() {
		return danMuPlayer;
	}

	@Override
	public void paintCallback(Graphics g) {
		this.paint(g);
	}

	@Override
	public void loadDanMuResourceFromFile(String filePath, boolean startDirectly) {
		danMuResource.initByFilePath(filePath);
		danMuResource.loadFromOriginalList();
		time = 0;
		duration = danMuResource.getMaxTime() + FINAL_DELAY_TIME;
		if (startDirectly) {
			if (!tickTimer.isRunning()) {
				tickTimer.start();
			}
		}
	}

	@Override
	@Subscribe
	public void onPlaying(PlayingEvent event) {
		if (!isPlaying) {
			if (!danMuResource.isLoaded()) {
				if (danMuResource.isInitialized()) {
					danMuResource.loadFromOriginalList();
					duration = danMuResource.getMaxTime() + FINAL_DELAY_TIME;
				}
			}

			isPlaying = true;
			tickTimer.start();
		}
	}

	@Override
	@Subscribe
	public void onPaused(PausedEvent event) {
		if (isPlaying) {
			tickTimer.stop();
			isPlaying = false;
		}
	}

	@Override
	@Subscribe
	public void onStopped(StoppedEvent event) {
		//logic
		tickTimer.stop();
		danMuResource.needReload();
		isPlaying = false;

		//ui
		clearAllDanMu();
	}

	public boolean isPlaying() {
		return isPlaying();
	}

	@Override
	public void setPosition(float v) {

	}

	@Override
	public void setTime(long time) {
		this.time = Math.min(time, duration);
	}

	@Override
	public void setDuration(long duration) {

	}

//	@Subscribe
//	public void onTick(TickEvent tickEvent) {
//		if (overlayComponent != null) {
//			Point srcPosition = overlayComponent.getLocationOnScreen();
//			Dimension srcSize = overlayComponent.getSize();
//			this.setLocation(srcPosition.x, srcPosition.y);
//			this.setSize(srcSize);
//		}
//	}
}
