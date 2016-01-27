 

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
    
    // Experimental: neue node generierung
    private int[][] nodePositions = {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20},{5,0},{5,10},{5,20}};
    private int[][] A = {{0,1},{1,2},{2,5},{5,4},{4,3},{1,4}};
    private int[][] H = {{0,1},{1,2},{5,4},{4,3},{1,4}};
    private int[][] E = {{0,1},{1,2},{2,5},{1,4},{0,3}};
    private int[][] L = {{0,1},{1,2},{0,3}};
    private int[][] O = {{0,1},{1,2},{2,5},{5,4},{4,3},{0,3}};
    private int[][] W = {{0,1},{1,2},{0,7},{3,7},{4,3},{4,5}};
    private int[][] R = {{0,1},{1,2},{2,5},{5,1},{1,3}};
    private int[][] D = {{0,1},{1,2},{2,8},{8,4},{4,6},{0,6}};
    
    // H E L O W R D   Bei gelegenheit vllt mal alphabetisch sortieren
    // Letternodes sind die positionen der Nodes {x,y}. erster index ist der Buchstabe, siehe eine zeile dürber für reihenfolge
    private int[][][] letternodes = {{{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}}, {{0,0},{0,10},{0,20},{10,20}}, {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}}, 
                                    {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}}, {{0,0},{2,20},{5,10},{8,20},{10,0}}, {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20}},
                                    {{0,0},{0,10},{0,20},{10,10}}};
    
    // LetterElements : z.B. {0,1} heißt (für diesen Buchstaben. NodeNummern beachten bei anderen!) Node 0 und Node 1 verbinden
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
           case 'H': return H;
           case 'E': return E;
           case 'L': return L;
           case 'O': return O;
           case 'W': return W;
           case 'R': return R;
           case 'D': return D;
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
        String line1 = "HELLO";
        // model
        model = new Model();

        double E = 210000; // N/mm^2 (modulus of elasticity)
        double rho = 7.88E-6; // kg/mm^2 (density of steel)
        model.createMaterial(1, E, rho);

        
        int id = 100;
        for (int i = 0; i < id; i++) {
            RectangleS r;
            r = model.createSection(i, RectangleS.TYPE, Truss2D.TYPE);
            r.setTKY(10); // mm
            r.setTKZ(10); // mm

            // Realtable r = model.createRealtable(i, Truss2D.TYPE);
            // r.setValue(TrussElement.RT_A, A);
        }
        
        //Buchstaben in String duchgehen
        int elemID = 0;
        for(int i=0; i<line1.length(); i++)
        {
            //Nodes in Buchstaben durchgehen
            int[][] nodes = getLetterNodes(line1.charAt(i));
            for(int j=0; j<nodes.length; j++)
            {
                int[] thisNode = nodes[j];
                // Ich glaub das rechnet in mm, darum alles x 100
                //createNode(int id, double x, double y, double z)
                int fstNode = thisNode[0];
                int sndNode = thisNode[1];
                System.out.println(fstNode + " : " + sndNode);
                
                if(model.getNode(fstNode+i*9) == null)
                {
                    System.out.println("erstelle Node " + (fstNode+i*9));
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*100;
                    int nodeY = nodePositions[fstNode][1]*100;
                    model.createNode(fstNode+i*9, nodeX, nodeY, 0);
                }
                if(model.getNode(sndNode+i*9) == null)
                {
                    System.out.println("erstelle Node " + (sndNode+i*9));
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*100;
                    int nodeY = nodePositions[sndNode][1]*100;
                    model.createNode(sndNode+i*9, nodeX, nodeY, 0);
                }
                model.createElement(elemID, Truss2D.TYPE, model.getMaterial(1), model.getRealtable(elemID), model.getNode(fstNode+i*9), model.getNode(sndNode+i*9));
                elemID++;
            }
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
        //model.getNode(id-1).setForce(f);

        // constraints
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        //model.getNode(1).setConstraint(c);
        //model.getNode(2).setConstraint(c);
        
        //int elemID = 0;
        // elements
        //for(int i=0; i<line1.length(); i++)
        //{
        //    int [][] elems  = getLetterElements(line1.charAt(i));
        //    for(int j=0; j<elems.length;j++)
         //   {
         //       int startNode = elems[j][0];
         //       int endNode = elems[j][1];
         //       System.out.println(elemID + "- " + startNode + ":" + endNode);
          //      model.createElement(elemID, Truss2D.TYPE, model.getMaterial(1), model.getRealtable(elemID), model.getNode(startNode), model.getNode(endNode));
           //     elemID++;
           // }
        // }
        
        //model.createElement(1, Truss2D.TYPE, model.getMaterial(1),
        //        model.getRealtable(1), model.getNode(1), model.getNode(4));
        //model.createElement(2, Truss2D.TYPE, model.getMaterial(1),
        //        model.getRealtable(2), model.getNode(2), model.getNode(4));
    }

    public Model getModel() {
        return model;
    }
}