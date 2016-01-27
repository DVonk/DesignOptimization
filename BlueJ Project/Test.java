import inf.minife.fe.Constraint;
import inf.minife.fe.DOF;
import inf.minife.fe.Force;
import inf.minife.fe.Model;
import inf.minife.fe.RectangleS;
import inf.minife.fe.Truss2D;
import inf.minife.view2.Viewer2;

public class Test 
{
    private Model model;
    
    private int[][] letter = {{0,2},{2,5},{5,3},{3,0}}; //O
    
    public static void main(String[] args) 
    {
        Model m = new Test().getModel();
        Viewer2 viewer = new Viewer2(m);
        viewer.setVisible(true);
    }

    public Test() 
    {
        // model
        model = new Model();

        double E = 210000; // N/mm^2 (modulus of elasticity)
        double rho = 7.88E-6; // kg/mm^2 (density of steel)
        model.createMaterial(1, E, rho);

        for (int i = 1; i <= 4; i++) 
        {
            RectangleS r;
            r = model.createSection(i, RectangleS.TYPE, Truss2D.TYPE);
            r.setTKY(10); // mm
            r.setTKZ(10); // mm

            // Realtable r = model.createRealtable(i, Truss2D.TYPE);
            // r.setValue(TrussElement.RT_A, A);
        }
        

        //Compute active nodes
        boolean[] nodeActive = new boolean[9];
        for (int i = 0; i < nodeActive.length; i++)
            nodeActive[i] = false;
        
        for (int i = 0; i < letter.length; i++)
            for (int j = 0; j < letter[0].length; j++)
                nodeActive[letter[i][j]] = true;

        //Create nodes
        double l = 1000.0; //length between vertical nodes
        double k = 500.0; //length between horizontal nodes
        if (nodeActive[0]) model.createNode(0, 0, 0, 0);
        if (nodeActive[1]) model.createNode(1, 0, l, 0);
        if (nodeActive[2]) model.createNode(2, 0, 2*l, 0);
        if (nodeActive[3]) model.createNode(3, 2*k, 0, 0);
        if (nodeActive[4]) model.createNode(4, 2*k, l, 0);
        if (nodeActive[5]) model.createNode(5, 2*k, 2*l, 0);
        if (nodeActive[6]) model.createNode(6, k, 0, 0);
        if (nodeActive[7]) model.createNode(7, k, l, 0);
        if (nodeActive[8]) model.createNode(8, k, 2*l, 0);
        
        
        // forces
        //Force f = new Force();
        //f.setValue(DletterF.T_X, 30000.0); // N
        //f.setValue(DletterF.T_Y, -20000.0); // N
        //model.getNode(4).setForce(f);
        // constraints
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        model.getNode(0).setConstraint(c);
		model.getNode(2).setConstraint(c);
		model.getNode(3).setConstraint(c);
		
        // elements
         for (int i = 0; i < letter.length; i++)
         {
             model.createElement(i+1, Truss2D.TYPE, model.getMaterial(1), model.getRealtable(i+1), model.getNode(letter[i][0]), model.getNode(letter[i][1]));
         }
    }

    public Model getModel() 
    {
        return model;
    }
}