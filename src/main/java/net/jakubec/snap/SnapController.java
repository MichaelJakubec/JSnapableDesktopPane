/*
 * Copyright 2020 Michael Jakubec
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package net.jakubec.snap;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.event.*;

final class SnapController {
	final JSnapableDesktopPane pane;

	//static final int NONE = 0;
	static final int LEFT = 1;
	static final int RIGHT = 2;

	public SnapController(JSnapableDesktopPane pane) {
		this.pane = pane;
		this.pane.addComponentListener(new ResizeHandler());
	}

	private JInternalFrame leftSnapFrame = null;
	private Dimension leftDimension;
	private JInternalFrame rightSnapFrame = null;
	private Dimension rightDimension;

	public void unsnap(JInternalFrame frame) {
		if (frame.equals(leftSnapFrame)) {
			leftSnapFrame = null;
			frame.setSize(leftDimension);
		} else if (frame.equals(rightSnapFrame)) {
			rightSnapFrame = null;
			frame.setSize(rightDimension);
		}
	}

	private class ResizeHandler extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			if (leftSnapFrame != null && rightSnapFrame != null) {
				int oldTotalWidth = leftSnapFrame.getWidth()+rightSnapFrame.getWidth();
				double leftFactor = ((double)leftSnapFrame.getWidth()) / ((double) oldTotalWidth);
				double rightFactor =  ((double)rightSnapFrame.getWidth()) / ((double) oldTotalWidth);
				int newWidth = pane.getWidth();
				int newLeftWidth = (int) (newWidth * leftFactor);
				int newRightWidth = (int) (newWidth * rightFactor);
				leftSnapFrame.setBounds(0,0,newLeftWidth, pane.getHeight());
				rightSnapFrame.setBounds(newLeftWidth,0,newRightWidth, pane.getHeight());

			}
		}
	}



	void resizeComponent(JInternalFrame frame) {
		if (frame.equals(leftSnapFrame)) {
			if (frame.getX() == 0 && compareLength(frame.getHeight() , pane.getHeight())) {
				if (rightSnapFrame != null) {
					rightSnapFrame.setBounds(frame.getWidth(), 0, pane.getWidth() - frame.getWidth(), pane.getHeight());
				}
			} else {
				System.out.println("LEFT UNSNAP");
				leftSnapFrame = null;
			}
		} else if (frame.equals(rightSnapFrame)) {
			if (compareLength(frame.getX() + frame.getWidth() ,pane.getWidth()) && compareLength(frame.getHeight() , pane.getHeight())) {
				if (leftSnapFrame != null) {
					leftSnapFrame.setBounds(0, 0, pane.getWidth() - frame.getWidth(), pane.getHeight());
				}
			} else {
				System.out.println("RIGHT UNSNAP if (" + (frame.getX() + frame.getWidth()) + " == " + pane.getWidth() +" && " + frame.getHeight() + " == "+ pane.getHeight()+") {");
				rightSnapFrame = null;
			}
		}
	}

	private boolean compareLength(int a, int b) {
		return Math.abs(a - b) < 2;
	}

	void snap(JInternalFrame frame, int direction) {
		if (direction == LEFT) {
			leftDimension = frame.getSize();
			frame.setBounds(0, 0, pane.getWidth() / 2, pane.getHeight());
			leftSnapFrame = frame;
		} else {
			rightDimension = frame.getSize();
			frame.setBounds(pane.getWidth() / 2, 0, pane.getWidth() / 2, pane.getHeight());
			rightSnapFrame = frame;
		}
	}

	public void deregisterInternalFrame(JInternalFrame frame) {
		JComponent component = ((BasicInternalFrameUI) frame.getUI())
				.getNorthPane();
		MouseListener[] mouseListeners = component.getMouseListeners();

		for (MouseListener l : mouseListeners) {
			if (l instanceof SnapAdapter) {
				((BasicInternalFrameUI) frame.getUI())
						.getNorthPane().removeMouseListener(l);
			}
		}
	}

	public void registerInternalFrame(final JInternalFrame frame) {
		JComponent component = ((BasicInternalFrameUI) frame.getUI())
				.getNorthPane();
		SnapAdapter snapAdapter = new SnapAdapter(this, frame, pane);
		component.addMouseListener(snapAdapter);
		frame.addMouseMotionListener(snapAdapter);
		frame.addComponentListener(snapAdapter);
		frame.getInputMap().put(KeyStroke.getKeyStroke("alt LEFT"), "snapLEFT");
		frame.getInputMap().put(KeyStroke.getKeyStroke("alt RIGHT"), "snapRIGHT");
		frame.getActionMap().put("snapLEFT", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				snap(frame,LEFT);
			}
		});
		frame.getActionMap().put("snapRIGHT", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				snap(frame,RIGHT);
			}
		});
	}

}
