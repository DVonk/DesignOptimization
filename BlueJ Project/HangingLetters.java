 

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

public class HangingLetters {

    private Model model;
    
    // neue node generierung
    private int[][] nodePositions = {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20},{5,0},{5,10},{5,20}};
    private int[][] A = {{0,1},{1,2},{2,5},{5,4},{4,3},{1,4}};
    private int[][] H = {{0,1},{1,2},{5,4},{4,3},{1,4}};
    //private int[][] H = {{1,2},{5,4},{4,3},{1,4},{2,8}};
    private int[][] E = {{0,1},{1,2},{2,5},{1,4},{0,3}};
    private int[][] L = {{0,1},{1,2},{0,3}};
    private int[][] O = {{0,1},{1,2},{2,5},{5,4},{4,3},{0,3}};
    private int[][] W = {{0,1},{1,2},{0,7},{3,7},{4,3},{4,5}};
    private int[][] R = {{0,1},{1,2},{2,5},{5,1},{1,3}};
    private int[][] D = {{0,1},{1,2},{2,8},{8,4},{4,6},{0,6}};
    private int[][] I = {{6,7},{7,8}};
    private int[][] T = {{6,7},{7,8},{2,8},{8,5}};
    private int[][] V = {{2,6},{6,5}};
    private int[][] Y = {{2,7},{7,5},{6,7}};
    private int[][] S = {{1,2},{4,3},{1,4},{2,5},{3,0}};
    private int[][] n8 ={{0,1},{1,2},{2,5},{5,4},{4,3},{3,0},{1,4}};


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
           case 'I': return I;
           case 'T': return T;
           case 'V': return V;
           case 'S': return S;
           case '8': return n8;
           default: return H;
        }
    }

    public HangingLetters() {
        String line1 = "HEVT";
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
                
                
                // Beide Node-Nr werden überprüft ob sie in diesem Buchstaben schon existieren
                if(model.getNode(fstNode+i*9) == null)
                {
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[fstNode][1]*100;
                    model.createNode(fstNode+i*9, nodeX, nodeY, 0);
                }
                if(model.getNode(sndNode+i*9) == null)
                {
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[sndNode][1]*100;
                    model.createNode(sndNode+i*9, nodeX, nodeY, 0);
                }
                model.createElement(elemID, Beam2D.TYPE, model.getMaterial(1), model.getRealtable(elemID), model.getNode(fstNode+i*9), model.getNode(sndNode+i*9));
                elemID++;
            }
        }
       
        // forces
        Force f = new Force();
        //f.setValue(DOF.T_X, 20.0); // N
        f.setValue(DOF.T_X, 20.0); // N
        //model.getNode(4).setForce(f);

        // constraints
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        c.setFree(DOF.T_Z, false);
        //model.getNode(2).setConstraint(c);
        //model.getNode(5).setConstraint(c);
        //model.getNode(0).setConstraint(c);
        //model.getNode(3).setConstraint(c);
    }

    public Model getModel() {
        return model;
    }
}