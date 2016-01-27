 

import inf.minife.fe.Constraint;
import inf.minife.fe.DOF;
import inf.minife.fe.Force;
import inf.minife.fe.Model;
import inf.minife.fe.RectangleS;
import inf.minife.fe.Truss2D;
import inf.minife.view2.Viewer2;

/**
 * @author kl Create a three bar structure similar to the ANSYS problem...
 */

public class HangingLetters {

    private Model model;
    // H E L O W R D   Bei gelegenheit vllt mal alphabetisch sortieren
    private int[][][] letternodes = {{{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}}, {{0,0},{0,10},{0,20},{10,20}}, {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}}, 
                                    {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}}, {{0,0},{2,20},{5,10},{8,20},{10,0}}, {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}},
                                    {{0,0},{0,10},{0,20},{10,10}}};
    
    private int[][][] letterElements = { {{0,1}, {1,2}, {1,4}, {3,4},{4,5}}, // H
                                        {},  // E
                                        };
                                    

    public static void main(String[] args) {
        Model m = new HangingLetters().getModel();

        m.printStructure();
        //m.solve();
        m.printResults();

        Viewer2 viewer = new Viewer2(m);
        viewer.setVisible(true);
    }

    public int[][] getLetterNodes(char c)
    {
       switch(c)
       {
           case 'H': return letternodes[0];
           case 'E': return letternodes[1];
           case 'L': return letternodes[2];
           case 'O': return letternodes[3];
           case 'W': return letternodes[4];
           case 'R': return letternodes[5];
           case 'D': return letternodes[6];
           default: return letternodes[0];
        }
    }
    
    public int[][] getLetterElements(char c)
    {
       switch(c)
       {
           case 'H': return letterElements[0];
           case 'E': return letterElements[1];
           case 'L': return letterElements[2];
           case 'O': return letterElements[3];
           case 'W': return letterElements[4];
           case 'R': return letterElements[5];
           case 'D': return letterElements[6];
           default: return letterElements[0];
        }
    }
    
    public HangingLetters() {
        String line1 = "H";
        // model
        model = new Model();

        double E = 210000; // N/mm^2 (modulus of elasticity)
        double rho = 7.88E-6; // kg/mm^2 (density of steel)
        model.createMaterial(1, E, rho);

        //Buchstaben in String duchgehen
        int id = 0;
        for(int i=0; i<line1.length(); i++)
        {
            //Nodes in Buchstaben durchgehen
            int[][] nodes = getLetterNodes(line1.charAt(i));
            for(int j=0; j<nodes.length; j++)
            {
                int[] thisNode = nodes[j];
                // Ich glaub das rechnet in mm, darum alles x 100
                 //createNode(int id, double x, double y, double z)
                 System.out.println(id + "- " + thisNode[0] + ":" + thisNode[1]);
                model.createNode(id, thisNode[0]*100, thisNode[1]*100, 0);
                id++;
            }
        }
        
        
        
        
        for (int i = 0; i < id; i++) {
            RectangleS r;
            r = model.createSection(i, RectangleS.TYPE, Truss2D.TYPE);
            r.setTKY(10); // mm
            r.setTKZ(10); // mm

            // Realtable r = model.createRealtable(i, Truss2D.TYPE);
            // r.setValue(TrussElement.RT_A, A);
        }

        // nodes
        //double b = 1000.0; // mm
        //double h = 1000.0; // mm
        //model.createNode(100, -b, 0, 0);
        //model.createNode(200, 0, 0, 0);
        //model.createNode(300, b, 0, 0);
        //model.createNode(400, 0, -h, 0);

        // forces
        Force f = new Force();
        f.setValue(DOF.T_X, 30000.0); // N
        f.setValue(DOF.T_Y, -20000.0); // N
        model.getNode(id-1).setForce(f);

        // constraints
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        model.getNode(1).setConstraint(c);
        model.getNode(2).setConstraint(c);
        
        int elemID = 0;
        // elements
        for(int i=0; i<line1.length(); i++)
        {
            int [][] elems  = getLetterElements(line1.charAt(i));
            for(int j=0; j<elems.length;j++)
            {
                int startNode = elems[j][0];
                int endNode = elems[j][1];
                System.out.println(elemID + "- " + startNode + ":" + endNode);
                model.createElement(elemID, Truss2D.TYPE, model.getMaterial(1), model.getRealtable(elemID), model.getNode(startNode), model.getNode(endNode));
                elemID++;
            }
        }
        
        //model.createElement(1, Truss2D.TYPE, model.getMaterial(1),
        //        model.getRealtable(1), model.getNode(1), model.getNode(4));
        //model.createElement(2, Truss2D.TYPE, model.getMaterial(1),
        //        model.getRealtable(2), model.getNode(2), model.getNode(4));
    }

    public Model getModel() {
        return model;
    }
}