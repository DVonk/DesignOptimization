 

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

public class TestLetters 
{
    private Model model;
    
    // neue node generierung
    private int[][] nodePositions = {{0,0},{0,10},{0,20},{10,0},{10,10},{10,20},{5,0},{5,10},{5,20}};
    private int[][] supportNodes = {{0,1},{0,6},{1,2},{1,7},{2,8},{3,4},{3,6},{4,5},{4,7},{5,8},{6,7},{7,8}};
    private int[][] space = {};
    private int[][] A = {{0,1},{1,2},{2,5},{5,4},{4,3},{1,4}};
    private int[][] B = {{0,1},{1,2},{2,5},{5,7},{7,3},{0,3},{1,7}};
    private int[][] C = {{0,1},{1,2},{2,5},{0,3}};
    private int[][] D = {{0,1},{1,2},{2,8},{8,4},{4,6},{0,6}};
    private int[][] E = {{0,1},{1,2},{2,5},{1,4},{0,3}};
    private int[][] F = {{0,1},{1,2},{2,5},{1,4}};
    private int[][] G = {{0,1},{1,2},{2,5},{0,3},{4,7},{4,3}};
    private int[][] H = {{0,1},{1,2},{5,4},{4,3},{1,4}};
    private int[][] I = {{6,7},{7,8}};
    private int[][] J = {{0,1},{0,3},{3,4},{4,5}};
    private int[][] K = {{0,1},{1,2},{1,5},{1,3}};
    private int[][] L = {{0,1},{1,2},{0,3}};
    private int[][] M = {{0,1},{1,2},{2,7},{7,5},{5,4},{4,3}};
    private int[][] N = {{0,1},{1,2},{2,7},{7,3},{3,4},{4,5}};
    private int[][] O = {{0,1},{1,2},{2,5},{5,4},{4,3},{0,3}};
    private int[][] P = {{0,1},{1,2},{2,5},{5,4},{4,1}};
    private int[][] Q = {{0,1},{1,2},{2,5},{5,4},{4,3},{0,3},{3,7}};
    private int[][] R = {{0,1},{1,2},{2,5},{5,1},{1,3}};
    private int[][] S = {{1,2},{4,3},{1,4},{2,5},{3,0}};
    private int[][] T = {{6,7},{7,8},{2,8},{8,5}};
    private int[][] U = {{0,1},{1,2},{0,3},{3,4},{4,5}};
    private int[][] V = {{2,6},{6,5}};
    private int[][] W = {{0,1},{1,2},{0,7},{3,7},{4,3},{4,5}};
    private int[][] X = {{2,7},{7,3},{0,7},{7,5}};
    private int[][] Y = {{2,7},{7,5},{6,7}};
    private int[][] Z = {{2,5},{5,7},{7,0},{0,3}};
    private int[][] n0 = {{0,1},{1,2},{2,5},{5,4},{4,3},{0,3},{0,7},{7,5}};
    private int[][] n1 = {{3,4},{4,5}};
    private int[][] n2 = {{2,5},{5,4},{4,1},{1,0},{0,3}};
    private int[][] n3 = {{2,5},{5,4},{4,1},{3,0},{4,3}};
    private int[][] n4 = {{2,1},{1,4},{5,4},{4,3}};
    private int[][] n5 = {{5,2},{2,1},{1,4},{4,3},{3,0}};
    private int[][] n6 = {{0,1},{1,2},{2,5},{0,3},{3,4},{4,1}};
    private int[][] n7 = {{2,5},{5,4},{4,3}};
    private int[][] n8 = {{0,1},{1,2},{2,5},{5,4},{4,3},{3,0},{1,4}};
    private int[][] n9 = {{1,2},{2,5},{5,4},{4,3},{3,0},{1,4}};
    
    public HollowCircleS rTable, rTable2;


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
           case ' ': return space;
           case 'A': return A;
           case 'B': return B;
           case 'C': return C;
           case 'D': return D;
           case 'E': return E;
           case 'F': return F;
           case 'G': return G;
           case 'H': return H;
           case 'I': return I;
           case 'J': return J;
           case 'K': return K;
           case 'L': return L;
           case 'M': return M;
           case 'N': return N;
           case 'O': return O;
           case 'P': return P;
           case 'Q': return Q;
           case 'R': return R;
           case 'S': return S;
           case 'T': return T;
           case 'U': return U;
           case 'V': return V;
           case 'W': return W;
           case 'X': return X;
           case 'Y': return Y;
           case 'Z': return Z;
           case '0': return n0;
           case '1': return n1;
           case '2': return n2;
           case '3': return n3;
           case '4': return n4;
           case '5': return n5;
           case '6': return n6;
           case '7': return n7;
           case '8': return n8;
           case '9': return n9;
           default: return H;
        }
    }

    public TestLetters() 
    {
        String line1 = "HELLO 123";
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
        
        rTable = new HollowCircleS();
        rTable.setDiameter(180); //mm
        rTable.setWTK(20);
        
        rTable2 = new HollowCircleS();
        rTable2.setDiameter(20); //mm
        rTable2.setWTK(2);
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
            int upperConnect, lowerConnect;
            //Nodes in Buchstaben durchgehen
            int[][] nodes = getLetterNodes(line1.charAt(i));//nodePositions;
            // Hinteres Gerüst
            for(int j=0; j<supportNodes.length; j++)
            {
                HollowCircleS cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
                int[] thisNode = supportNodes[j];
                int fstNode = thisNode[0];
                int sndNode = thisNode[1];
                System.out.println("j: " +j);    
                
                if(model.getNode(fstNode+i*18) == null)
                {
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[fstNode][1]*100;
                    model.createNode(sumNodes+fstNode+i*18, nodeX, nodeY, -300);
                    System.out.println("node: " +(fstNode+i*18) + " created");    
                }
                if(model.getNode(sndNode+i*18) == null)
                {
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[sndNode][1]*100;
                    model.createNode(sumNodes+sndNode+i*18, nodeX, nodeY, -300);
                    System.out.println("node: " +(sndNode+i*18) + " created");    
                }
                cS = model.createSection(++elemID, rTable2.TYPE, Beam3D.TYPE);
                cS.setDiameter(rTable2.getDiameter());
                cS.setWTK(rTable2.getWTK());    
                model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+fstNode+i*18), model.getNode(sumNodes+sndNode+i*18));
                
                if(i>0 && j==supportNodes.length-1)
                {
                    cS = model.createSection(elemID, rTable2.TYPE, Beam3D.TYPE);
                    cS.setDiameter(rTable2.getDiameter());
                    cS.setWTK(rTable2.getWTK());    
                    model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+5+(i-1)*18), model.getNode(sumNodes+2+i*18));
                    cS = model.createSection(elemID, rTable2.TYPE, Beam3D.TYPE);
                    cS.setDiameter(rTable2.getDiameter());
                    cS.setWTK(rTable2.getWTK());    
                    model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+3+(i-1)*18), model.getNode(sumNodes+0+i*18));
                }
            }
            
            
            
            for(int j=0; j<nodes.length; j++)
            {
                int[] thisNode = nodes[j];
                // Ich glaub das rechnet in mm, darum alles x 100
                //createNode(int id, double x, double y, double z)
                int fstNode = thisNode[0];
                int sndNode = thisNode[1];       
                
                // Beide Node-Nr werden überprüft ob sie in diesem Buchstaben schon existieren
                if(model.getNode(fstNode+i*18+9) == null)
                {
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[fstNode][1]*100;
                    
                    model.createNode(sumNodes+fstNode+i*18+9, nodeX, nodeY, 0);
                    //model.createNode(sumNodes+fstNode+i*18+9, nodeX, nodeY, 100);
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
                if(model.getNode(sndNode+i*18+9) == null)
                {
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[sndNode][1]*100;
                    
                    model.createNode(sumNodes+sndNode+i*18+9, nodeX, nodeY, 0);
                    //model.createNode(sumNodes+sumNodes+sndNode+i*18+9, nodeX, nodeY, 100);
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
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+fstNode+i*18+9), model.getNode(sumNodes+sndNode+i*18+9));
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setDiameter(rTable.getDiameter());
               cS.setWTK(rTable.getWTK());    
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+fstNode+i*18+9), model.getNode(sumNodes+fstNode+i*18)); 
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setDiameter(rTable.getDiameter());
               cS.setWTK(rTable.getWTK());    
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sumNodes+sndNode+i*18+9), model.getNode(sumNodes+sndNode+i*18));
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