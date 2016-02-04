 

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
import java.util.*;


public class TestLettersRect 
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
    
    public RectangleS rTable, rTable2;
    ArrayList<Integer> optimizeThis = new ArrayList<Integer>();



    public static void main(String[] args) {
        Model m = new TestLettersRect().getModel();

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

    public TestLettersRect() 
    {
        String line1 = "HELLO0234";
        // model
        model = new Model();
        int fixNode;
        if(gerade(line1.length()))
        {
            fixNode = line1.length()/2;
        }
        else
        {
            fixNode = (line1.length()-1)/2;
        }
        //Rohre
        double E = 210000; // N/mm^2 (modulus of elasticity)
        double rho = 7.88E-6; // kg/mm^2 (density of steel)
        model.createMaterial(1, E, rho);
        int bar_diam = 60;
        int bar_diam2 = 180;
        //Seil
        E = 110000;
        model.createMaterial(2, E, rho);
        
        Force f = new Force();
        f.setValue(DOF.T_Y, -20.0); // N
        Constraint c = new Constraint();
        c.setFree(DOF.T_X, false);
        c.setFree(DOF.T_Y, false);
        c.setFree(DOF.T_Z, false);
        
        rTable = new RectangleS();
        rTable.setTKY(200); //mm
        rTable.setTKZ(200);
        
        rTable2 = new RectangleS();
        rTable.setTKY(200); //mm
        rTable.setTKZ(200);

        int elemID = 0;
        //*Buchstaben in String duchgehen
        for(int i=0; i<line1.length(); i++)
        {
            //Nodes in Buchstaben durchgehen
            int[][] nodes = getLetterNodes(line1.charAt(i));//nodePositions;
            
            // Hinteres Gerüst
            for(int j=0; j<supportNodes.length; j++)
            {
                RectangleS cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
                int[] thisNode = supportNodes[j];
                int fstNode = thisNode[0];
                int sndNode = thisNode[1];
                System.out.println("j: " +j);    
                optimizeThis.add(elemID);
                // Beide if's checken ob Node X und/oder Y in thisNode = {X,Y} schon vorhanden sind, sonst werden sie erstellt
                // es gibt insgesamt 18 nodes pro Buchstabe 9 Buchstabe und 9 für Gerüst dahinter. darum kommt man mit NodeNr + BuchstabenNr*18 zum gewünschten Node. 
                // Hinweis: Dadurch Nodes nicht durchgängig nummeriert. Bsp: In einem O gibt es keinen Node 7:  7 + "Position_vom_O" *18 = NULL
                if(model.getNode(fstNode+i*18) == null)
                {
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[fstNode][1]*100;
                    model.createNode(fstNode+i*18, nodeX, nodeY, -300);
                }
                if(model.getNode(sndNode+i*18) == null)
                {
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[sndNode][1]*100;
                    model.createNode(sndNode+i*18, nodeX, nodeY, -300); 
                }
                
                cS = model.createSection(++elemID, rTable2.TYPE, Beam3D.TYPE);
                cS.setTKY(bar_diam);
                cS.setTKZ(bar_diam);  

                // 1,4 und 7 sind die mittleren Nodes und bekommen dickere Verbindungen
                if((fstNode == 1 && sndNode == 7) || (fstNode == 4 && sndNode==7))
                    { 
                        cS.setTKY(bar_diam);
                        cS.setTKZ(bar_diam);  
                    }
                model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(fstNode+i*18), model.getNode(sndNode+i*18));
                optimizeThis.add(elemID-1);                
                // Verbindungen zwischen Buchstaben (5-2, 4-1, 3-0) 4-1 dicker weil Anschluss an Tragarm
                // Buchstabe wird nach seiner fertigstellung mit vorigem Buchstaben verbunden
                // Ja ich weiß dass es aus der inneren for-schleife raus könnte
                if(i>0 && j==supportNodes.length-1)
                {
                    cS = model.createSection(elemID, rTable2.TYPE, Beam3D.TYPE);
                    cS.setTKY(bar_diam);
                    cS.setTKZ(bar_diam);    
                    model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(5+(i-1)*18), model.getNode(2+i*18));
                    optimizeThis.add(elemID-1);     
                    cS = model.createSection(elemID, rTable2.TYPE, Beam3D.TYPE);
                    cS.setTKY(bar_diam);
                    cS.setTKZ(bar_diam);    
                    model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(3+(i-1)*18), model.getNode(0+i*18));
                    optimizeThis.add(elemID-1);     
                    //dickerer Durchmesser. Dieser könnte optimiert werden
                    cS = model.createSection(elemID, rTable2.TYPE, Beam3D.TYPE);
                    cS.setTKY(bar_diam);
                    cS.setTKZ(bar_diam);   
                    model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(4+(i-1)*18), model.getNode(1+i*18));
                    optimizeThis.add(elemID-1);     
                    Integer[] oT = {4+(i-1)*18, 1+i*18};
                }
            }
            
            
            
            for(int j=0; j<nodes.length; j++)
            {
                int[] thisNode = nodes[j];
                // Ich glaub das rechnet in mm, darum alles x 100
                int fstNode = thisNode[0];
                int sndNode = thisNode[1];       
                
                // Beide if's checken ob Node X und/oder Y in thisNode = {X,Y} schon vorhanden sind, sonst werden sie erstellt
                if(model.getNode(fstNode+i*18+9) == null)
                {
                    int nodeX = nodePositions[fstNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[fstNode][1]*100;
                    model.createNode(fstNode+i*18+9, nodeX, nodeY, 0);
                    if(i== fixNode && (fstNode == 1 || fstNode == 7 || fstNode == 4))
                        {          
                            //model.getNode(fstNode+i*18).setConstraint(c);
                            //model.getNode(fstNode+i*18+9).setConstraint(c);
                                model.createNode(1000+fstNode, nodeX, nodeY, -1500);
                                model.createNode(1001+fstNode, nodeX, nodeY+500, -1500);
                                model.getNode(1000+fstNode).setConstraint(c);
                                model.getNode(1001+fstNode).setConstraint(c);
                                RectangleS cS = model.createSection(1000+fstNode, rTable.TYPE, Beam3D.TYPE);
                                cS.setTKY(500);
                                cS.setTKZ(500);               
                                model.createElement(1000+fstNode, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(1000+fstNode), model.getNode(1000+fstNode), model.getNode(fstNode+i*18));
                                cS = model.createSection(1001+fstNode, rTable.TYPE, Beam3D.TYPE);
                                cS.setTKY(500);
                                cS.setTKZ(500);             
                                model.createElement(1001+fstNode, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(1001+fstNode), model.getNode(1001+fstNode), model.getNode(fstNode+i*18));
                        }
                    else
                        {
                            model.getNode(fstNode+i*18).setForce(f);
                            model.getNode(fstNode+i*18+9).setForce(f);
                        }
                }
                if(model.getNode(sndNode+i*18+9) == null)
                {
                    int nodeX = nodePositions[sndNode][0]*100+i*1000+i*200;
                    int nodeY = nodePositions[sndNode][1]*100;
                    
                    model.createNode(sndNode+i*18+9, nodeX, nodeY, 0);
                    if(i==fixNode && (sndNode == 1 || sndNode == 4 || sndNode == 7))
                        {
                           //model.getNode(sndNode+i*18).setConstraint(c);
                           //model.getNode(sndNode+i*18+9).setConstraint(c);
   

                                model.createNode(1000+sndNode, nodeX, nodeY, -1500);
                                model.createNode(1001+sndNode, nodeX, nodeY+500, -1500);
                                model.getNode(1000+sndNode).setConstraint(c);
                                model.getNode(1001+sndNode).setConstraint(c);
                                RectangleS cS = model.createSection(1000+sndNode, rTable.TYPE, Beam3D.TYPE);
                                cS.setTKY(500);
                                cS.setTKZ(500);               
                                model.createElement(1000+sndNode, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(1000+sndNode), model.getNode(1000+sndNode), model.getNode(sndNode+i*18));
                                cS = model.createSection(1001+sndNode, rTable.TYPE, Beam3D.TYPE);
                                cS.setTKY(500);
                                cS.setTKZ(500);            
                                model.createElement(1001+sndNode, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(1001+sndNode), model.getNode(1001+sndNode), model.getNode(sndNode+i*18));
                        }
                    else
                        {
                            model.getNode(sndNode+i*18).setForce(f);
                            model.getNode(sndNode+i*18+9).setForce(f);
                        }
                }
                
               RectangleS cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setTKY(bar_diam2);
               cS.setTKZ(bar_diam2);              
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(fstNode+i*18+9), model.getNode(sndNode+i*18+9));
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
              cS.setTKY(bar_diam2);
               cS.setTKZ(bar_diam2);    
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(fstNode+i*18+9), model.getNode(fstNode+i*18)); 
               cS = model.createSection(elemID, rTable.TYPE, Beam3D.TYPE);
               cS.setTKY(bar_diam2);
               cS.setTKZ(bar_diam2);     
               model.createElement(elemID, Beam3D.TYPE, model.getMaterial(1),model.getRealtable(elemID++), model.getNode(sndNode+i*18+9), model.getNode(sndNode+i*18));
            }
        }//*/
    }
    public ArrayList<Integer> getOptimizeThis()
    {
        return optimizeThis;
    }

    public Model getModel() {
        return model;
    }
    public Boolean gerade(int l)
    {
        if (l%2 == 0) {return true;}
        else {return false;}
    }
}