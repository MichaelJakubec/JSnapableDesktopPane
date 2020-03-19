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
import java.awt.*;
import java.awt.event.*;

class SnapAdapter extends MouseAdapter implements ComponentListener {
	private final SnapController snapController;
	private final JInternalFrame frame;

	private boolean dragged = false;

	SnapAdapter(SnapController snapController, JInternalFrame frame, JSnapableDesktopPane pane) {
		this.snapController = snapController;
		this.frame = frame;
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (!dragged) {
			return;
		}
		dragged = false;
		Point releasePoint = e.getPoint();
		Point point = SwingUtilities.convertPoint((Component) e.getSource(), releasePoint, snapController.pane);

		if (point.getX() < 50) {
			snapController.snap(frame, SnapController.LEFT);
		} else if (point.getX() > snapController.pane.getWidth() - 50) {
			snapController.snap(frame, SnapController.RIGHT);
		} else {
			snapController.unsnap(frame);
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		snapController.resizeComponent(frame);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		dragged = true;
	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}
}
