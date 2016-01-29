 

import inf.minife.fe.Beam3D;
import inf.minife.fe.Element;
import inf.minife.fe.HollowCircleS;
import inf.minife.fe.Model;

import org.mopack.sopti.problems.ProblemType1;
import org.mopack.sopti.ui.swing.minife.ModelProvider;
import org.mopack.sopti.ui.swing.minife.OptViewer;

/**
 * Solves the optimization problem for the RUB Motorsport racing car frame.
 * Optimizes the diameter and thickness for each beam.
 */
public class RahmenOpt extends ProblemType1 implements ModelProvider {

	/**
	 * Max. allowable stress
	 */
	static public final double SIGMA_MAX = 250.0; // N/mm^2
	
	/**
	 * Object to be optimized
	 */
	private Rahmen rahmen;
	
	/**
	 * Model of the object 
	 */
	private Model model;
	
	/**
	 * Overall count of model's elements
	 */
	private int elementCount;

	public static void main(String[] args) {
		new OptViewer(new RahmenOpt()).setVisible(true);
	}
	
	/**
	 * Creates an instance of the this optimizer and the view.
	 */
	public RahmenOpt() {
		//Get model and information
		rahmen = new Rahmen();
		model = rahmen.getModel();
		elementCount = model.getElements().length;
		
		//Add diameter and thickness as design variables for _each_ element
		for (int i = 1; i <= elementCount; i++) {
			addDesignVariable("diameter of beam "+i+" [mm]", 25, 35, 50);
			addDesignVariable("thickness of beam "+i+" [mm]", 2.5, 5, 9);
		}
		
		// Objective function is the total mass
		addFunctionName(0, "total mass [kg]");
		
		// Constraints are: Stress must be below allowable stress
		for (int i = 1; i <= elementCount; i++) {
			addFunctionName("stress member " + i + " at I");
		}
		for (int i = 1; i <= elementCount; i++) {
			addFunctionName("stress member " + i + " at J");
		}
		
		// First run
		evaluate(getInitial());
	}

	/**
	 * Compute for each element both stress contraints (each element has to nodes at it's ends)
	 */
	private void computeStressConstraints(double[] f) {
		int offset = countObjectives();
		Element[] elements = model.getElements();
		double sigma;	
		
		for (int i = 0; i < elementCount; i++) {
			sigma = elements[i].getResult(Beam3D.RS_SMAX_I);
			f[i + offset] = Math.abs(sigma) / SIGMA_MAX - 1.0;
			sigma = elements[i].getResult(Beam3D.RS_SMAX_J);
			f[i + offset + elementCount] = Math.abs(sigma) / SIGMA_MAX - 1.0;
		}
	}

	/**
	 * Evaluate the given "solution"
	 */
	@Override
	public double[] evaluate(double[] x) {
		double[] f = new double[countObjectives() + countConstraints()];
		
		for (int i = 1; i <= elementCount; i++) {
			// get diameter and thickness from the given solution
			double diameter = x[(i-1)*2];
			double thickness = x[(i-1)*2 + 1];
			// update the cross section
			HollowCircleS c = (HollowCircleS) model.getRealtable(i);
			c.setDiameter(diameter);
			c.setWTK(thickness);
		}

		// run analysis
		model.solve();
		
		// update objective function
		f[0] = model.getTotalMass();
		
		//update constrain functions
		computeStressConstraints(f);
		
		System.out.println("Total mass: " + model.getTotalMass());
		
		return f;
	}
	
	/**
	 * Returns the number of stress contraints. Two contraints for each element
	 */
	@Override
	public int countConstraints() {
		int stressContraints = elementCount * 2;
		return stressContraints; 
	}
	
	/**
	 * Return the model of the optimization problem
	 */
	@Override
	public Model getModel() {
		return model;
	}
}
