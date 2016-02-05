 

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
import java.util.*;

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
    
    double sigmaMax = 2; // N/mm^2

    TestLettersRect letters;    
    Model model;

    public static void main(String[] args) 
    {
        new OptViewer(new TestLettersOpt()).setVisible(true);
    }

    public TestLettersOpt() {

        letters = new TestLettersRect();
        model = letters.getModel();

        addDesignVariable("support bar width a [mm]", 10, 100, 200);
        addDesignVariable("support bar width b [mm]", 10, 100, 200);

        addFunctionName(0, "Total Mass");
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
        System.out.println("Elements: " + elements.length);
        int n = elements.length;
        int off = countObjectives();
        double sigma;

        for (int i = 1; i < n; i=i+2) 
        {
            System.out.println((i*2) + "/" + f.length);
            sigma = elements[i].getResult(Beam3D.RS_SMAX_I);
            f[i] = Math.abs(sigma) / sigmaMax - 1.0;
            sigma = elements[i].getResult(Beam3D.RS_SMAX_J);
            f[i+1] = Math.abs(sigma) / sigmaMax - 1.0;
        }
    }

    @Override
    public double[] evaluate(double[] x) {

        f = new double[1 + countConstraints()];
        int elemID = model.getElements().length;
        // the support width
        double a = x[0];
        double b = x[1];
        ArrayList<Integer> nodes = letters.getOptimizeThis();
        int j;
        for(int i=0; i<nodes.size(); i++)
        {
            j = nodes.get(i);
            RectangleS cS = (RectangleS) model.getRealtable(j);
            cS.setTKY(a);
            cS.setTKZ(b);
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
