 

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import inf.minife.fe.Beam3D;
import inf.minife.fe.Force;
import inf.minife.fe.HollowCircleS;
import inf.minife.fe.Model;
import inf.minife.fe.Node;

/**
 * Base class for a RUB Motorsport racing car frame.
 */
public class BaseStructure {
	protected Model model;
	
	/**
	 * Absolute count of all nodes 
	 */
	protected static int NODE_COUNT = 0;
	
	/**
	 * Absolute count of all elements 
	 */
	protected static int ELEMENT_COUNT = 0;	
	
	/**
	 * Saves node indexes by name
	 */
	protected static HashMap<String,Integer> NAMEND_NODES = new HashMap<String,Integer>();
	
	/**
	 * The base realtable for all elements
	 */
	protected HollowCircleS baseRealtable;
	
	/**
	 * Creates an instance of the structure for a model
	 * @param model
	 */
	public BaseStructure(Model model){
		this.model = model;
		
		//Steel: 25CrMo4/25CrMoS4
		double E = 210000; // N/mm^2 (modulus of elasticity)
		double rho = 7.75E-6; // kg/mm^2 (density of steel)
		model.createMaterial(1, E, rho);
		
		//Beam parameter: 25mm diameter / 2mm thickness
		baseRealtable = new HollowCircleS();
		baseRealtable.setDiameter(25); //mm
		baseRealtable.setWTK(2); //mm
	}
	
	/**
	 * Connects two nodes with a standard element by index
	 * @param index1 Index of node 1
	 * @param index2 Index of Node 2
	 * @return Index of the new element
	 */
	protected int connectNodes(int index1, int index2){
		ELEMENT_COUNT++;
		//Section
		HollowCircleS c = model.createSection(ELEMENT_COUNT, baseRealtable.TYPE, Beam3D.TYPE);
		c.setDiameter(baseRealtable.getDiameter());
		c.setWTK(baseRealtable.getWTK());
		//Create a new element
		model.createElement(ELEMENT_COUNT, Beam3D.TYPE, model.getMaterial(1),
				model.getRealtable(ELEMENT_COUNT), model.getNode(index1), model.getNode(index2));
		return ELEMENT_COUNT;
	}
	
	/**
	 * Connects two nodes with a standard element by name 
	 * @param name1 Name of node 1
	 * @param name2 Name of node 2
	 */
	protected void connectNodes(String name1, String name2){
		connectNodes(getNodeIndex(name1), getNodeIndex(name2));
	}
	
	/**
	 * Connects two nodes with a standard element by name and index
	 * @param name Name of node 1
	 * @param index Index of node 2
	 */
	protected void connectNodes(String name, int index){
		connectNodes(getNodeIndex(name), index);
	}
	
	/**
	 * Connects two nodes with a standard element by index and name 
	 * @param index index of node 1
	 * @param name Name of node 2
	 */
	protected void connectNodes(int index, String name){
		connectNodes(index, getNodeIndex(name));
	}
	
	/**
	 * Connects nodes in a symmetric way. Uses naming conventions to determine the symmetric nodes.
	 * Nodes must be names "..._links" and "..._rechts".
	 * @param name1 Basename of node 1 (e.g. bodenplatte will be expanded to bodenplatte_links and bodenplatte_rechts)
	 * @param name2 Basename of node 2
	 */
	protected void connectNodesSymmetric(String name1, String name2){
		connectNodes(getNodeIndex(name1+"_links"), getNodeIndex(name2+"_links"));
		connectNodes(getNodeIndex(name1+"_rechts"), getNodeIndex(name2+"_rechts"));
	}
	
	/**
	 * Creates a node at given coordinates
	 * @param x Coordinate at X
	 * @param y Coordinate at Y
	 * @param z Coordinate at Z
	 * @return Node index
	 */
	protected int createNode(double x, double y, double z){
		return createNode(x, y, z, "");
	}
	
	/**
	 * Creates a names node at given coordinates
	 * @param x Coordinate at X
	 * @param y Coordinate at Y
	 * @param z Coordinate at Z
	 * @param name Name of the new node
	 * @return Node index
	 */
	protected int createNode(double x, double y, double z, String name){
		NODE_COUNT++;
		model.createNode(NODE_COUNT, x, y ,z);
		name = name.isEmpty() ? "unnamed" + NODE_COUNT : name;
		NAMEND_NODES.put(name, NODE_COUNT);
		return NODE_COUNT;
	}
	
	/**
	 * Returns the model
	 * @return The model
	 */
	public Model getModel(){
		return this.model;
	}
	
	/**
	 * Returns the node index for a give name
	 * @param name Name of the node
	 * @return Index of node
	 */
	public static int getNodeIndex(String name){
		try{
			return NAMEND_NODES.get(name);
		}
		catch(NullPointerException e){
			System.out.println("Node with name not found: " + name);
			return 0;
		}
	}
	
	/**
	 * Returns the node by name
	 * @param name Node's name
	 * @return The corresponding node
	 */
	public Node getNode(String name){
		return getModel().getNode(getNodeIndex(name));
	}
	
	/**
	 * Adds or creates force to an node
	 * @param n The node the force should be added
	 * @param dof DOF of the force
	 * @param value Value of the force
	 */
	public void addForce(Node n, int dof, double value){
		Force f = n.getForce();
		if(f == null){
			f = new Force();
			f.setValue(dof, 0);
			n.setForce(f);
		}
		double old = f.getValue(dof);
		f.setValue(dof, old + value);
	}
	
	/**
	 * List all nodes with name
	 */	
	static public void listNodes(){
		Collection<Integer> indexes = NAMEND_NODES.values();
		List<Integer> sorted_indexes = asSortedList(indexes);
		for(Integer index : sorted_indexes){
			String name = getKeyByValue(NAMEND_NODES, index);
			System.out.println("Node "+index+": "+name);
		}
	}
	
	private static<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
	
	private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
