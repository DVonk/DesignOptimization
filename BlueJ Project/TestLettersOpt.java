 

import inf.minife.fe.Element;
import inf.minife.fe.Model;
import inf.minife.fe.Node;
import inf.minife.fe.RectangleS;
import inf.minife.fe.Truss2D;
import inf.minife.fe.Realtable;
import inf.minife.fe.Truss2D;
import inf.minife.fe.Beam3D;
import inf.minife.fe.Truss3D;
import inf.minife.fe.HollowCircleS;
import org.mopack.sopti.problems.ProblemType1;
import org.mopack.sopti.ui.swing.minife.ModelProvider;
import org.mopack.sopti.ui.swing.minife.OptViewer;

/**
 * @author kl
 * 
 *         Optimize a ThreeBar object.
 * 
 *         - optimization variables are - the support distance b and - the side
 *         lengths of bars 1, 2 and 3 (assume square shape)
 * 
 *         - the objective function is the total mass
 * 
 *         - constraints are stresses in each element
 * 
 */
public class TestLettersOpt extends ProblemType1 implements ModelProvider {

    // the array f containing
    // - the object function f[0] and
    // - the constraint values f[1], f[2], ...
    double[] f;
    
    double sigmaMax = 250; // N/mm^2

    TestLetters letters;    
    Model model;

    public static void main(String[] args) 
    {
        new OptViewer(new TestLettersOpt()).setVisible(true);
    }

    public TestLettersOpt() {

        letters = new TestLetters();
        model = letters.getModel();

        addDesignVariable("middle bar diameter [mm]", 50, 100, 200);
        addDesignVariable("middle bar wall thickness [mm]", 1, 2, 4);

        addFunctionName(0, "diameter [mm]");
        for (int i = 0; i < model.getElements().length; i++) 
        {
            addFunctionName("stress member " + i + "at Node I");
            addFunctionName("stress member " + i + "at Node J");
        }
        
        // update
        evaluate(getInitial());
    }

    void computeStressConstraints() {

        Element[] elements = model.getElements();
        int n = elements.length;
        int off = countObjectives();
        double sigma;

        for (int i = 0; i < n; i++) 
        {
            sigma = elements[i].getResult(Beam3D.RS_SMAX_I);
            f[i] = Math.abs(sigma) / sigmaMax - 1.0;
            sigma = elements[i].getResult(Beam3D.RS_SMAX_J);
            f[i+off] = Math.abs(sigma) / sigmaMax - 1.0;
        }
    }

    @Override
    public double[] evaluate(double[] x) {

        f = new double[1 + countConstraints()];

        // the suppor   t width
        double b = x[0];

        model.getNode(1).setCoordinate(Node.X, -b);
        model.getNode(3).setCoordinate(Node.X, b);

        for (int i = 1; i <= 3; i++) 
        {
            HollowCircleS r  = (HollowCircleS) model.getRealtable(i);
            r.setDiameter(20); //mm
            r.setWTK(2);
        }

        // run analysis
        model.solve();

        f[0] = model.getTotalMass();
        computeStressConstraints();

        return f;
    }

    @Override
    public int countConstraints() 
    {
        return model.getElements().length;
    }

    @Override
    public Model getModel() 
{
        return model;
    }
}
