 

import inf.minife.view2.Viewer2;
import inf.minife.fe.Model;

/**
 * Small viewer class for the RUB Motorsport racing car frame
 */
public class VehicleViewer {
  public static void main(String[] args){
	  Model m = (new Rahmen()).getModel();
	  (new Viewer2(m)).setVisible(true);
	  
	  System.out.println("Gesamtmasse: " + m.getTotalMass());
	  System.out.println("Gesamtvolumen: " + m.getTotalVolume());
  }
}
