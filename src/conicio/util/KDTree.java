package conicio.util;

import java.util.*;

public class KDTree<E> {
	public static final Vector3[] basis = new Vector3[] {
		new Vector3(1, 0, 0),
		new Vector3(0, 1, 0),
		new Vector3(0, 0, 1)
	};

	private Node<E> root;
	private int     size;

	public KDTree() {
		this.root = null;
		this.size = 0;
	}
	
	public void put(Vector3 point, E data) {
		size++;
		
		if(root == null) {
			root = new Node<E>(point, data);
			return;
		}
	
		int     depth   = 0;
		Node<E> current = root;
		while(true) {
			Vector3 base = basis[depth % 3];
			if(point.dot(base) < current.point.dot(base)) {
				if(current.lNode == null) { current.lNode = new Node<E>(point, data); break; }
				else                      { current = current.lNode;                         }
			}
			else {
				if(current.rNode == null) { current.rNode = new Node<E>(point, data); break; }
				else                      { current = current.rNode;                         }
			}
			depth++;
		}
	}
	
	public Map<Vector3, E> get(Vector3 point, double radius) {
		Map<Vector3, E> found = new HashMap<Vector3, E>();
		get(point, radius, root, 0, found);
		return found;
	}
	
	private void get(Vector3 point, double radius, Node<E> current, int depth, Map<Vector3, E> found) {
		if(current == null) { return; }
		
		if(current.point.sub(point).normSq() < radius * radius) { found.put(current.point, current.data); }
	
		Vector3 base = basis[depth % 3];
		if(point.dot(base) - radius < current.point.dot(base)) {
			get(point, radius, current.lNode, depth + 1, found);
		}
		if(point.dot(base) + radius >= current.point.dot(base)) {
			get(point, radius, current.rNode, depth + 1, found);
		}
	}
	
	public Map<Vector3, E> nearest(Vector3 point, int amount) {
		SortedMap<Double, Node<E>> nearest = new TreeMap<Double, Node<E>>();
		nearest(point, amount, root, 0, nearest);
		
		Map<Vector3, E> found = new HashMap<Vector3, E>();
		for(Double d : nearest.keySet()) { found.put(nearest.get(d).point, nearest.get(d).data); }
		return found;
	}
	
	public void nearest(Vector3 point, int amount, Node<E> current, int depth, SortedMap<Double, Node<E>> nearest) {
		if(current == null) { return; }
	
		double distSq = nearest.size() < amount ? Double.POSITIVE_INFINITY : Math.pow(nearest.lastKey(), 2);
		
		if(current.point.sub(point).normSq() < distSq && current.point.sub(point).normSq() < 0.4) { 
			nearest.put(current.point.sub(point).normSq(), current);
			distSq = nearest.size() < amount ? Double.POSITIVE_INFINITY : Math.pow(nearest.lastKey(), 2);
		}
		if(nearest.size() > amount) { nearest.remove(nearest.lastKey()); }
		
		Vector3 base = basis[depth % 3];
		if(point.dot(base) - Math.sqrt(distSq) < current.point.dot(base) && point.dot(base) - 0.4 < current.point.dot(base)) {
			nearest(point, amount, current.lNode, depth + 1, nearest);
			distSq = nearest.size() < amount ? Double.POSITIVE_INFINITY : Math.pow(nearest.lastKey(), 2);
		}
		if(point.dot(base) + Math.sqrt(distSq) > current.point.dot(base) && point.dot(base) + 0.4 > current.point.dot(base)) {
			nearest(point, amount, current.rNode, depth + 1, nearest);
			distSq = nearest.size() < amount ? Double.POSITIVE_INFINITY : Math.pow(nearest.lastKey(), 2);
		}
	}
	
	public void balance() {
	
	}
	
	public int size() {
		return size;
	}

	private class Node<E> {
		public final Vector3 point;
		public final E       data;
		public       Node<E> lNode;
		public       Node<E> rNode;
		
		private Node(Vector3 point, E data) {
			this.point = point;
			this.data  = data;
			this.lNode = null;
			this.rNode = null;
		}
	}
	
	private class SizeLimitedMap { }
}