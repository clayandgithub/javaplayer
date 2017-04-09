package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.FrameTickEvent;
import com.clayoverwind.javaplayer.event.TickEvent;
import com.clayoverwind.javaplayer.iview.IDanMuPanel;
import com.clayoverwind.javaplayer.model.SingleDanMu;
import com.clayoverwind.javaplayer.presenter.DanMuPlayer;
import com.clayoverwind.javaplayer.util.StringUtil;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DanMuPanel extends JPanel implements IDanMuPanel {

	private static final long serialVersionUID = -2630361033538823440L;

	private static final int RANDOM_Y_DIFF = 32;

	List<SingleDanMuLabel> mDanMuLabelList = new LinkedList<>();

	private DanMuPlayer danMuPlayer;

	private Random mRandom = new Random();
	
	public DanMuPanel(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(null);
		this.setBackground(Color.RED);
		this.setOpaque(false);

		danMuPlayer = new DanMuPlayer(this);
		mRandom.setSeed(System.currentTimeMillis());
		Application.INSTANCE.subscribe(this);
	}

	@Subscribe
	public void addRandomDanMu(TickEvent e) {
		if (mRandom.nextInt(100) > 50) {
			System.out.println("---------addRandomDanMu-------------------");
			addDanMu(StringUtil.genRandomString(10), "0xFFFFFF", mRandom.nextInt(SingleDanMu.MAX_LINE_COUNT));
		}

	}

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

	@Subscribe
	public void danMuTick(FrameTickEvent event) {
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
}
