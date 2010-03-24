package com.nixsolutions.calltracer.ui;

import com.nixsolutions.calltracer.ui.data.CallVisitorsHolder;
import com.nixsolutions.calltracer.ui.executors.CallQueryExecutor;
import com.nixsolutions.calltracer.ui.forms.CallTracerUI;
import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author denis_k
 *         Date: 17.03.2010
 *         Time: 12:09:59
 */
public class UI {
	public static void main(String[] args) {
		CallVisitorsHolder callVisitorsHolder = new CallVisitorsHolder();

		CallDaoHandler callDaoHandler = new CallDaoHandler();
		CallQueryExecutor callQueryExecutor = new CallQueryExecutor(callDaoHandler, callVisitorsHolder);

		CallTracerUI callTracerUI = new CallTracerUI(callQueryExecutor);
		callTracerUI.setCallQueryExecutor(callQueryExecutor);
		//========================================================
		UI ui = new UI();
		ui.createUI(callTracerUI);
	}

	private void createUI(CallTracerUI callTracerUI) {
		JFrame frame = new JFrame("CallTracerUI");
		frame.setContentPane(callTracerUI.$$$getRootComponent$$$());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		frame.setSize(screenWidth / 2, screenHeight / 4);
		frame.setLocation(screenWidth / 4, screenHeight / 4);
		//frame.pack();
		frame.setVisible(true);

	}
}
