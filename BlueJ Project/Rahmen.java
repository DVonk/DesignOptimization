 

import inf.minife.fe.Constraint;
import inf.minife.fe.DOF;
import inf.minife.fe.Element;
import inf.minife.fe.Model;
import inf.minife.fe.Node;

/**
 * The frame of the RUB Motorsport racing car
 */
public class Rahmen extends BaseStructure{
	/**
	 * Inner width of the frame (space needed for main components)
	 */
	public static double INNER_WIDTH = 550.0;
	
	/**
	 * Outer width of the frame 
	 */
	public static double OUTER_WIDTH = 800.0;
	
	/**
	 * Half of the inner width
	 */
	private static double INNER_DISTANCE = INNER_WIDTH / 2.0;
	
	/**
	 * Half of the outher width
	 */
	private static double OUTER_DISTANCE = OUTER_WIDTH / 2.0;
	
	/**
	 * Maximum height of the frame
	 */
	public static double MAX_HEIGHT = 1137.0;
	
	/**
	 * How much the roll hoop bracing should be skewed
	 */
	public static double UEBERROLLBUEGEL_DELTA_X = 100.0;
	
	/**
	 * Height of the engine
	 */
	public static double MOTOR_HEIGHT = 623.0;
	
	/**
	 * Height of the front
	 */
	public static double FRONTHAUBE_HEIGHT = 702.0;
	
	/**
	 * Width of the front
	 */
	public static double FRONTHAUBE_WIDTH = 300.0;
	
	/**
	 * Half of the front's width
	 */
	private static double FRONTHAUBE_DISTANCE = FRONTHAUBE_WIDTH / 2.0;
	
	/**
	 * Height of the steering wheel
	 */
	public static double LENKRAD_HEIGHT = 133.3;
	
	/**
	 * Weight of the engine
	 */
	public static double MOTOR_GEWICHT = 70.0; //kg
	
	/**
	 * Weight of the driver + seat.
	 */
	public static double FAHRER_GEWICHT = 85.0; //kg
	
	public static double GRAVITATIONAL_ACCELERATION = 9.81; // m/s^2
	
	/**
	 * Creates a frame
	 */
	public Rahmen() {
		super(new Model());
		
		// helping variables to hold temporary values
		double x,y,z;
		
		//Bodenplatte
		createNode(0, -INNER_DISTANCE, 0, "boden_vorne_links");
		createNode(0, INNER_DISTANCE, 0, "boden_vorne_rechts");
		createNode(670, INNER_DISTANCE, 0, "boden_segment1_rechts");
		createNode(970, INNER_DISTANCE, 0, "boden_segment2_rechts");
		createNode(1748, INNER_DISTANCE, 0, "boden_segment3_rechts");
		createNode(2490, INNER_DISTANCE, 0, "boden_segment4_rechts");
		createNode(2790, INNER_DISTANCE, 0, "boden_hinten_rechts");		
		createNode(670, -INNER_DISTANCE, 0, "boden_segment1_links");
		createNode(970, -INNER_DISTANCE, 0, "boden_segment2_links");
		createNode(1748, -INNER_DISTANCE, 0, "boden_segment3_links");
		createNode(2490, -INNER_DISTANCE, 0, "boden_segment4_links");
		createNode(2790, -INNER_DISTANCE, 0, "boden_hinten_links"); 

		//Bodenplatte		
		connectNodesSymmetric("boden_vorne", "boden_segment1");
		connectNodesSymmetric("boden_segment1", "boden_segment2");
		connectNodesSymmetric("boden_segment2", "boden_segment3");
		connectNodesSymmetric("boden_segment3", "boden_segment4");
		connectNodesSymmetric("boden_segment4", "boden_hinten");
		//Querverbindungen
		connectNodes("boden_vorne_links", "boden_vorne_rechts");
		connectNodes("boden_segment1_links", "boden_segment1_rechts");
		connectNodes("boden_segment2_links", "boden_segment2_rechts");
		connectNodes("boden_segment3_links", "boden_segment3_rechts");
		connectNodes("boden_segment4_links", "boden_segment4_rechts");
		connectNodes("boden_hinten_links", "boden_hinten_rechts");
		
		//Schnauze
		createNode(0, -INNER_DISTANCE, 350, "front_oben_links"); 
		createNode(0, INNER_DISTANCE, 350, "front_oben_rechts");
		createNode(0, -INNER_DISTANCE, 175, "front_mitte_links"); //zwischen knoten, vorne
		createNode(0, INNER_DISTANCE, 175, "front_mitte_rechts");
		
		connectNodesSymmetric("boden_vorne", "front_mitte");
		connectNodesSymmetric("front_mitte", "front_oben");
		connectNodes("front_oben_links", "front_oben_rechts"); 
		
		// Radaufhängung
		createNode(670, -INNER_DISTANCE, 108, "vorderrad_unten_links"); // unterer node beim vorderrad
		createNode(670, INNER_DISTANCE, 108, "vorderrad_unten_rechts");
		
		createNode(670, -INNER_DISTANCE, 419, "vorderrad_oben_links"); 
		createNode(670, INNER_DISTANCE, 419, "vorderrad_oben_rechts"); 
		
		// Radaufh�ngung vorne
		connectNodesSymmetric("front_mitte", "vorderrad_unten");
		connectNodesSymmetric("boden_vorne", "vorderrad_unten");
		connectNodesSymmetric("boden_segment1", "vorderrad_unten");
		connectNodesSymmetric("vorderrad_unten", "vorderrad_oben");
		connectNodesSymmetric("front_oben", "vorderrad_unten");
		connectNodesSymmetric("front_oben", "vorderrad_oben");
		
		//Vorderseite kabine
		createNode(970, -INNER_DISTANCE, 300, "kabine_vorne_mitte_links"); 
		createNode(970, INNER_DISTANCE, 300, "kabine_vorne_mitte_rechts"); 
		createNode(970, -INNER_DISTANCE, 450, "kabine_vorne_oben_links");
		createNode(970, INNER_DISTANCE, 450, "kabine_vorne_oben_rechts");						
		
		// Radaufh�ngung hinten
		connectNodesSymmetric("vorderrad_oben","kabine_vorne_oben");
		connectNodesSymmetric("vorderrad_oben","kabine_vorne_mitte");
		connectNodesSymmetric("vorderrad_unten","kabine_vorne_mitte");
		connectNodesSymmetric("boden_segment2","vorderrad_unten");
		connectNodesSymmetric("kabine_vorne_mitte", "kabine_vorne_oben");
		connectNodes("kabine_vorne_oben_links","kabine_vorne_oben_rechts");
		
		// verbreiterung
		createNode(1748, -OUTER_DISTANCE, 0, "verbreiterung_links");
		createNode(1748, OUTER_DISTANCE, 0, "verbreiterung_rechts");
		
		connectNodesSymmetric("boden_segment2", "verbreiterung");		
		connectNodesSymmetric("boden_segment4", "verbreiterung");
		connectNodesSymmetric("boden_segment3", "verbreiterung");
		
		// seitenw�nde kabine
		createNode(ueberrollbuegel_x(300), -OUTER_DISTANCE, 300, "verbreiterung_oben_links");
		createNode(ueberrollbuegel_x(300), OUTER_DISTANCE, 300, "verbreiterung_oben_rechts"); 
		
		connectNodesSymmetric("verbreiterung", "verbreiterung_oben");
		connectNodesSymmetric("kabine_vorne_mitte","verbreiterung_oben");
		connectNodesSymmetric("kabine_vorne_mitte","verbreiterung");
		connectNodesSymmetric("verbreiterung","verbreiterung_oben");
		
		//Hinterrad
		createNode(2117,-INNER_DISTANCE, 205, "motor_links");
		createNode(2117,INNER_DISTANCE, 205, "motor_rechts");		
		createNode(2490,-INNER_DISTANCE, 420, "hinterrad_oben_links");
		createNode(2490,INNER_DISTANCE, 420, "hinterrad_oben_rechts");
				
		connectNodesSymmetric("verbreiterung_oben", "hinterrad_oben");
		connectNodesSymmetric("verbreiterung_oben", "motor");
		connectNodesSymmetric("boden_segment3", "motor");
		connectNodesSymmetric("boden_segment4", "motor");
		connectNodesSymmetric("hinterrad_oben", "motor");		
		
		//Heck
		createNode(2790, -INNER_DISTANCE, 275, "heck_oben_links");
		createNode(2790, INNER_DISTANCE, 275, "heck_oben_rechts");		
		
		//Motorb�gel
		createNode(motorbuegel_x(MOTOR_HEIGHT), -INNER_DISTANCE, MOTOR_HEIGHT, "motor_oben_links");
		createNode(motorbuegel_x(MOTOR_HEIGHT), INNER_DISTANCE, MOTOR_HEIGHT, "motor_oben_rechts");
		connectNodesSymmetric("motor_oben", "heck_oben");
		connectNodesSymmetric("verbreiterung_oben", "motor_oben");
		connectNodes("motor_oben_links", "motor_oben_rechts");
		
		//�berrollb�gel
		createNode(ueberrollbuegel_x(496), -OUTER_DISTANCE, 495, "buegel_unten_links");
		createNode(ueberrollbuegel_x(495), OUTER_DISTANCE, 495, "buegel_unten_rechts");
		createNode(ueberrollbuegel_x(1067), -243, 1067, "buegel_mitte_links");
		createNode(ueberrollbuegel_x(1067), 243, 1067, "buegel_mitte_rechts");
		createNode(ueberrollbuegel_x(1137), -148.5, 1137, "buegel_oben_links");
		createNode(ueberrollbuegel_x(1137), 148.5, 1137, "buegel_oben_rechts");
		connectNodesSymmetric("verbreiterung_oben", "buegel_unten");
		connectNodesSymmetric("buegel_unten", "buegel_mitte");
		connectNodesSymmetric("buegel_mitte", "buegel_oben");
		connectNodes("buegel_oben_links", "buegel_oben_rechts");
		connectNodesSymmetric("buegel_mitte", "hinterrad_oben");
		
		//Abschluss
		createNode(2790, 0, 0, "heck_mitte_unten");
		createNode(2790, 0, 420, "heck_mitte_oben");
		
		connectNodes("heck_mitte_unten", "heck_mitte_oben");
		connectNodes("hinterrad_oben_links", "heck_mitte_oben");
		connectNodes("hinterrad_oben_rechts", "heck_mitte_oben");
		connectNodes("heck_oben_links", "heck_mitte_unten");
		connectNodes("heck_oben_rechts", "heck_mitte_unten");
		connectNodes("heck_oben_links", "heck_mitte_oben");
		connectNodes("heck_oben_rechts", "heck_mitte_oben");
		
		//Fronthaube
		x = getNode("boden_segment2_links").getCoordinate(Node.X);
		y = getNode("kabine_vorne_oben_links").getCoordinate(Node.Y);
		createNode(x, -FRONTHAUBE_DISTANCE, FRONTHAUBE_HEIGHT, "cockpit_oben_links");
		createNode(x, FRONTHAUBE_DISTANCE, FRONTHAUBE_HEIGHT, "cockpit_oben_rechts");
		createNode(x, -INNER_DISTANCE, FRONTHAUBE_HEIGHT-LENKRAD_HEIGHT, "cockpit_unten_links");
		createNode(x, INNER_DISTANCE, FRONTHAUBE_HEIGHT-LENKRAD_HEIGHT, "cockpit_unten_rechts");
		
		connectNodesSymmetric("kabine_vorne_oben", "cockpit_unten");
		connectNodesSymmetric("cockpit_unten", "cockpit_oben");
		connectNodesSymmetric("front_oben", "cockpit_oben");
		connectNodes("cockpit_oben_links", "cockpit_oben_rechts");
		
		//Vorderradaufh�ngung
		x = getNode("vorderrad_unten_links").getCoordinate(Node.X);
		z = getNode("kabine_vorne_mitte_links").getCoordinate(Node.Z);
		createNode(x, -INNER_DISTANCE, z, "vorderrad_mitte_links");
		createNode(x, INNER_DISTANCE, z, "vorderrad_mitte_rechts");
		z = getNode("vorderrad_unten_links").getCoordinate(Node.Z);
		x = getNode("kabine_vorne_mitte_links").getCoordinate(Node.X);
		createNode(x, -INNER_DISTANCE, z, "vorderrad2_mitte_links");
		createNode(x, INNER_DISTANCE, z, "vorderrad2_mitte_rechts");
		
		connectNodesSymmetric("boden_segment2", "vorderrad2_mitte");
		connectNodesSymmetric("vorderrad2_mitte", "kabine_vorne_mitte");
				
		//Hinterradaufh�ngung
		z = getNode("vorderrad_unten_links").getCoordinate(Node.Z);
		x = getNode("boden_segment4_links").getCoordinate(Node.X);
		createNode(x, -INNER_DISTANCE, z, "hinterrad_unten_links");
		createNode(x, INNER_DISTANCE, z, "hinterrad_unten_rechts");
		z = getNode("heck_oben_links").getCoordinate(Node.Z);
		createNode(x, -INNER_DISTANCE, z, "hinterrad_mitte_links");
		createNode(x, INNER_DISTANCE, z, "hinterrad_mitte_rechts");
		x = getNode("boden_hinten_links").getCoordinate(Node.X);
		z = getNode("vorderrad_unten_links").getCoordinate(Node.Z);
		createNode(x, -INNER_DISTANCE, z, "hinterrad2_unten_links");
		createNode(x, INNER_DISTANCE, z, "hinterrad2_unten_rechts");
		
		//Hinterradaufh�ngung
		connectNodesSymmetric("boden_segment4", "hinterrad_unten");
		connectNodesSymmetric("hinterrad_unten", "hinterrad_mitte");
		connectNodesSymmetric("hinterrad_mitte", "hinterrad_oben");
		
		connectNodesSymmetric("boden_hinten", "hinterrad2_unten");
		connectNodesSymmetric("boden_hinten", "heck_oben");
		connectNodesSymmetric("hinterrad_oben", "heck_oben");		
		
		//Fixierung an Radaufh�ngung
		Constraint cstr = new Constraint();
		cstr.setFree(DOF.T_X, false);
		cstr.setFree(DOF.T_Y, false);
		cstr.setFree(DOF.T_Z, false);
		cstr.setFree(DOF.R_X, false);
		cstr.setFree(DOF.R_Y, false);
		cstr.setFree(DOF.R_Z, false);
		
		model.getNode(getNodeIndex("vorderrad_unten_links")).setConstraint(cstr);
		model.getNode(getNodeIndex("vorderrad_unten_rechts")).setConstraint(cstr);
		model.getNode(getNodeIndex("vorderrad2_mitte_links")).setConstraint(cstr);
		model.getNode(getNodeIndex("vorderrad2_mitte_rechts")).setConstraint(cstr);
		
		model.getNode(getNodeIndex("hinterrad_unten_links")).setConstraint(cstr);
		model.getNode(getNodeIndex("hinterrad_unten_rechts")).setConstraint(cstr);
		model.getNode(getNodeIndex("hinterrad2_unten_links")).setConstraint(cstr);
		model.getNode(getNodeIndex("hinterrad2_unten_rechts")).setConstraint(cstr);
		
		
		//Motor aufh�ngen. Gewicht gleichverteilt
		addForce(getNode("motor_links"), DOF.T_Z, -(MOTOR_GEWICHT * GRAVITATIONAL_ACCELERATION / 4.0)); //N
		addForce(getNode("motor_rechts"), DOF.T_Z, -(MOTOR_GEWICHT * GRAVITATIONAL_ACCELERATION / 4.0)); //N
		addForce(getNode("motor_oben_links"), DOF.T_Z, -(MOTOR_GEWICHT * GRAVITATIONAL_ACCELERATION / 4.0)); //N
		addForce(getNode("motor_oben_rechts"), DOF.T_Z, -(MOTOR_GEWICHT * GRAVITATIONAL_ACCELERATION / 4.0)); //N
		
		//Fahrersitz. Gewicht 40/60 verteilt
		addForce(getNode("boden_segment2_links"), DOF.T_Z, -(FAHRER_GEWICHT * GRAVITATIONAL_ACCELERATION * 0.2)); //N
		addForce(getNode("boden_segment2_rechts"), DOF.T_Z, -(FAHRER_GEWICHT * GRAVITATIONAL_ACCELERATION * 0.2)); //N
		addForce(getNode("boden_segment3_links"), DOF.T_Z, -(FAHRER_GEWICHT * GRAVITATIONAL_ACCELERATION * 0.3)); //N
		addForce(getNode("boden_segment3_rechts"), DOF.T_Z, -(FAHRER_GEWICHT * GRAVITATIONAL_ACCELERATION * 0.3)); //N
		
		//Eigengewicht der einzelnen Element jeweils hinzuf�gen
		for(int i=1; i < model.getElements().length; i++){
			Element e = model.getElement(i);
			double mass = e.getMass();
			double f = mass / 2.0 * GRAVITATIONAL_ACCELERATION; //N
			
			addForce(e.getNode(0), DOF.T_Z, -f); //N
			addForce(e.getNode(1), DOF.T_Z, -f); //N
		}
		
		listNodes();
	}
		
	/**
	 * Calc the x-coordinate for the roll hoop bracing for a z-coordinate
	 */
	private double ueberrollbuegel_x(double z){
		double s = UEBERROLLBUEGEL_DELTA_X / MAX_HEIGHT;
		Node base = getNode("verbreiterung_links");
		return s * z + base.getCoordinate(Node.X);
	}
	
	/**
	 * Calc the x-coordinate for the engine hoop for a z-coordinate
	 */
	private double motorbuegel_x(double z){
		Node heck = getNode("heck_oben_links");
		Node rad =  getNode("hinterrad_oben_links");
		double s = (heck.getCoordinate(Node.Z) - rad.getCoordinate(Node.Z)) / 
				(heck.getCoordinate(Node.X) - rad.getCoordinate(Node.X));		
		double b = -(s * heck.getCoordinate(Node.X) - heck.getCoordinate(Node.Z));
		return ((z - b) / s);
	}
}