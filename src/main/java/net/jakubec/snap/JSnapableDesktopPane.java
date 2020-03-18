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

/**
 * This class provides the possibility to add snapping behaviour like know from Windows 7, 10 or Gnome 3 to a JDesktopPane.
 * Moving a JInternalPane to the left or right side of the JDesktop pane automatically snaps it the left or right half of the
 * JDesktopPane.
 *
 * Do enable snapping
 */
public class JSnapableDesktopPane extends JDesktopPane {

	private SnapController controller;

	public JSnapableDesktopPane(){
		controller = new SnapController(this);
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);
		if (comp instanceof JInternalFrame) {
			final JInternalFrame frame = (JInternalFrame) comp;
			controller.deregisterInternalFrame(frame);
		}
	}

	@Override
	public void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);
		if (comp instanceof JInternalFrame) {
			final JInternalFrame frame = (JInternalFrame) comp;
			controller.registerInternalFrame(frame);

		}
	}
}
