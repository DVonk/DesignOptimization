import inf.minife.fe.Beam2D;
import inf.minife.fe.Constraint;
import inf.minife.fe.DOF;
import inf.minife.fe.Force;
import inf.minife.fe.HollowCircleS;
import inf.minife.fe.Material;
import inf.minife.fe.Model;

/**
 * @author kl Create a three story structure
 */
public class ThreeStory {

	/**
	 * The entire structural system.
	 */
	protected Model model_;

	HollowCircleS pipe_;

	public static void main(String[] args) {
		Model model = new ThreeStory().model_;

		model.printStructure();
		model.solve();
		model.printResults();
	}

	public ThreeStory() {

		// model
		model_ = new Model();

		Material mat = model_.createMaterial(1, 210000.0, // N/mm^2
				7.88E-6); // kg/mm^3
		// nodes
		double width = 4000; // mm
		double height = 2000; // mm

		for (int i = 0; i < 4; i++) {
			model_.createNode(2 * i + 1, 0, i * height, 0);
			model_.createNode(2 * i + 2, width, i * height, 0);
		}

		// forces
		Force fLeft = new Force();
		fLeft.setValue(DOF.T_X, 1000.0); // N
		fLeft.setValue(DOF.T_Y, -10000.0); // N
		model_.getNode(7).setForce(fLeft);

		Force fRight = new Force();
		fRight.setValue(DOF.T_Y, -10000); // N
		model_.getNode(8).setForce(fRight);

		// constraints
		Constraint con = new Constraint();
		con.setFree(DOF.T_X, false);
		con.setFree(DOF.T_Y, false);
		con.setFree(DOF.T_Z, false);
		con.setFree(DOF.R_X, false);
		con.setFree(DOF.R_Y, false);
		con.setFree(DOF.R_Z, false);

		model_.getNode(1).setConstraint(con);
		model_.getNode(2).setConstraint(con);

		// elements
		pipe_ = model_.createSection(1, HollowCircleS.TYPE, Beam2D.TYPE);
		pipe_.setDiameter(300); // mm
		pipe_.setWTK(10); // mm

		int idx = 1;
		for (int i = 0; i < 3; i++) {
			model_.createElement(idx++, Beam2D.TYPE, mat, pipe_, model_
					.getNode(2 * i + 1), model_.getNode(2 * i + 3));
			model_.createElement(idx++, Beam2D.TYPE, mat, pipe_, model_
					.getNode(2 * i + 3), model_.getNode(2 * i + 4));
			model_.createElement(idx++, Beam2D.TYPE, mat, pipe_, model_
					.getNode(2 * i + 4), model_.getNode(2 * i + 2));

			// optional
			//model_.createElement(idx++, Beam2D.TYPE, mat, pipe_, model_
			//		.getNode(2 * i + 3), model_.getNode(2 * i + 2));

		}
	}

	static public Model create() {
		ThreeStory ts = new ThreeStory();
		return ts.model_;
	}

	HollowCircleS getPipe() {
		return pipe_;
	}
}