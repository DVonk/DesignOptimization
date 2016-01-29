 

import inf.minife.fe.Constraint;
import inf.minife.fe.DOF;
import inf.minife.fe.Force;
import inf.minife.fe.Model;
import inf.minife.fe.RectangleS;
import inf.minife.fe.Realtable;
import inf.minife.fe.Truss2D;
import inf.minife.fe.Beam3D;
import inf.minife.fe.Truss3D;
import inf.minife.fe.HollowCircleS;
import inf.minife.view2.Viewer2;

public class TestLetters {

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
    public HollowCircleS rTable;


    public static void main(String[] args) {
        Model m = new TestLetters().getModel();

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

    public TestLetters() 
    {
        String line1 = "HELLEEWORLD";
        // model
        model = new Model();
        
        //Rohre
        double E = 210000; // N/mm^2 (modulus of elasticity)
        double rho = 7.88E-6; // kg/mm^2 (density of steel)
        model.createMaterial(1, E, rho);
        //Seil
        E = 110000;
        model.createMaterial(2, E, rho);
        
        Force f = new Force();
        f.setValue(DOF.T_Y, -20.0); // N
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        c.setFree(DOF.T_Z, false);
        
        int id = 10000;
        for (int i = 0; i < id; i++) {
            //RectangleS r;
            //r = model.createSection(i, RectangleS.TYPE, Beam2D.TYPE);
            //r = model.createSection(i, RectangleS.TYPE, Beam2D.TYPE);
            //r.setTKY(100); // mm
            //r.setTKZ(100);
            rTable = new HollowCircleS();
            rTable.setDiameter(55); //mm
            rTable.setWTK(2); //mm
            // Realtable r = model.createRealtable(i, Truss2D.TYPE);
            // r.setValue(TrussElement.RT_A, A);
        }
        
        //Questange oben
        /*int sumNodes = line1.length();
        int elemID = 0;
        for(int i=0; i<line1.length()+1; i++)
        {
            model.createNode(i, i*1000+i*200, 2500, 50);
            if(i>0)
            {
                HollowCircleS cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
                cS.setDiameter(rTable.getDiameter());
                cS.setWTK(rTable.getWTK());               
                model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(i-1), model.getNode(i));
            }
        }
        */
        int elemID = 0;
        int sumNodes = 0;
        //*Buchstaben in String duchgehen
        for(int i=0; i<line1.length(); i++)
        {
            //Nodes in Buchstaben durchgehen
            int[][] nodes = getLetterNodes(line1.charAt(i));//nodePositions;
            for(int j=0; j<nodes.length; j++)
            {
                int[] thisNode = nodes[j];
                // Ich glaub das rechnet in mm, darum alles x 100
                //createNode(int id, double x, double y, double z)
                int fstNode = thisNode[0];
                int sndNode = thisNode[1];
                System.out.println((sumNodes+fstNode) + " : " + sndNode);               
                
                // Beide Node-Nr werden überprüft ob sie in diesem Buchstaben schon existieren
                if(model.getNode(fstNode+i*18) == null)
                {
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[fstNode][1]*100;
                    model.createNode(sumNodes+fstNode+i*18, nodeX, nodeY, 0);
                    model.createNode(sumNodes+fstNode+i*18+9, nodeX, nodeY, 100);
                    if(fstNode == 0 || fstNode == 3 || fstNode == 6)
                        {
                            model.getNode(sumNodes+fstNode+i*18).setConstraint(c);
                            model.getNode(sumNodes+fstNode+i*18+9).setConstraint(c);
                        }
                    else
                        {
                            model.getNode(sumNodes+fstNode+i*18).setForce(f);
                            model.getNode(sumNodes+fstNode+i*18+9).setForce(f);
                        }
                    if(fstNode == 2 || fstNode == 8 || fstNode == 5)
                        {
                            
                        }
                }
                if(model.getNode(sndNode+i*18) == null)
                {
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[sndNode][1]*100;
                    model.createNode(sumNodes+sumNodes+sndNode+i*18, nodeX, nodeY, 0);
                    model.createNode(sumNodes+sumNodes+sndNode+i*18+9, nodeX, nodeY, 100);
                    if(sndNode == 0 || sndNode == 3 || sndNode == 6)
                        {
                            model.getNode(sumNodes+sndNode+i*18).setConstraint(c);
                            model.getNode(sumNodes+sndNode+i*18+9).setConstraint(c);
                        }
                    else
                        {
                            model.getNode(sumNodes+sndNode+i*18).setForce(f);
                            model.getNode(sumNodes+sndNode+i*18+9).setForce(f);
                        }
                    if(sndNode == 2 || sndNode == 8 || sndNode == 5)
                        {
                            
                        }
                }
                
               HollowCircleS cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setDiameter(rTable.getDiameter());
               cS.setWTK(rTable.getWTK());               
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+fstNode+i*18), model.getNode(sumNodes+sndNode+i*18));
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setDiameter(rTable.getDiameter());
               cS.setWTK(rTable.getWTK());    
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+fstNode+i*18+9), model.getNode(sumNodes+sndNode+i*18+9));
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setDiameter(rTable.getDiameter());
               cS.setWTK(rTable.getWTK());    
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+fstNode+i*18), model.getNode(sumNodes+fstNode+i*18+9)); 
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setDiameter(rTable.getDiameter());
               cS.setWTK(rTable.getWTK());    
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+sndNode+i*18), model.getNode(sumNodes+sndNode+i*18+9));
            }
        }//*/
        
        
    }
       
        // forces
        
        
        //model.getNode(2).setForce(f);

        // constraints
        
        //model.getNode(0).setConstraint(c);
        //model.getNode(3).setConstraint(c);
        //model.getNode(0).setConstraint(c);
        //model.getNode(3).setConstraint(c);   

    public Model getModel() {
        return model;
    }
}