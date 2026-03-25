package application;

import javax.swing.SwingUtilities;

import api.ClimaTempoAPI;
import ui.ClimaTempoUI;

public class ClimaTempoApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
		@Override
		public void run() {
			new ClimaTempoUI().setVisible(true);
		}
	});
}
}
