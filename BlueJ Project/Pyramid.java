 

import inf.minife.fe.Constraint;
import inf.minife.fe.DOF;
import inf.minife.fe.Force;
import inf.minife.fe.Model;
import inf.minife.fe.RectangleS;
import inf.minife.fe.Realtable;
import inf.minife.fe.Truss2D;
import inf.minife.fe.Beam2D;
import inf.minife.fe.Truss3D;
import inf.minife.view2.Viewer2;

public class Pyramid {

    private Model model;
    
    // neue node generierung



    public static void main(String[] args) {
        Model m = new Pyramid(6).getModel();
        
        m.printStructure();
        //m.solve();
        m.printResults();

        Viewer2 viewer = new Viewer2(m);
        viewer.setVisible(true);
    }

    public int getNodeSum(int n)
    {
        int ret = ((n+1)*(n+2))/2;
        return ret;
    }

    public Pyramid(int n) {
        // model
        model = new Model();

        double E = 210000; // N/mm^2 (modulus of elasticity)
        double rho = 7.88E-6; // kg/mm^2 (density of steel)
        model.createMaterial(1, E, rho);
        //Realtable realtable = model. createRealtable (1, Truss3D.TYPE);
        
        int id = 50;
        for (int i = 0; i < id; i++) {
            RectangleS r;
            //r = model.createSection(i, RectangleS.TYPE, Beam2D.TYPE);
            r = model.createSection(i, RectangleS.TYPE, Beam2D.TYPE);
            r.setTKY(100); // mm
            r.setTKZ(100);
            // Realtable r = model.createRealtable(i, Truss2D.TYPE);
            // r.setValue(TrussElement.RT_A, A);
        }
        
        int totNodes = getNodeSum(n);
        int nodeID = 0;
        for(int i=1; i<=totNodes; i++)
        {
            for(int j=0; j<i; j++)
            {
                //nt nodeX = 
                //model.createNode(nodeID, nodeX, nodeY, 0);
                //nodeID++;
            }
             //model.createElement(elemID, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(elemID), model.getNode(fstNode+i*18), model.getNode(fstNode+i*18+9));
        }
        
        // Nodes
        model.createNode(0, 0, 0, 0);
        model.createNode(1, 20000, 0, 0);
        model.createNode(2, 40000, 0, 0);
        model.createNode(3, 10000, 10000, 0);
        model.createNode(4, 30000, 10000, 0);
        model.createNode(5, 20000, 20000, 0);
        model.createElement(0, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(0), model.getNode(0), model.getNode(1));
        model.createElement(1, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(1), model.getNode(1), model.getNode(2));
        model.createElement(2, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(2), model.getNode(0), model.getNode(3));
        model.createElement(3, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(3), model.getNode(1), model.getNode(3));
        model.createElement(4, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(4), model.getNode(1), model.getNode(4));
        model.createElement(5, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(5), model.getNode(2), model.getNode(4));
        model.createElement(6, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(6), model.getNode(3), model.getNode(4));
        model.createElement(7, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(7), model.getNode(3), model.getNode(5));
        model.createElement(8, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(8), model.getNode(4), model.getNode(5));
        
        
        
        
        // forces
        Force f = new Force();
        //f.setValue(DOF.T_X, 20.0); // N
        f.setValue(DOF.T_Y, -200.0); // N
        //model.getNode(4).setForce(f);

        // constraints
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        c.setFree(DOF.T_Z, false);
        model.getNode(2).setConstraint(c);
        model.getNode(1).setConstraint(c);
        model.getNode(0).setConstraint(c);
        //model.getNode(3).setConstraint(c);
    }

    public Model getModel() {
        return model;
    }
}