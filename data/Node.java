package team146.data;

public class Node {
	protected Node prevN;
	protected Node nextN;
	protected Object self;
	
	public Node(Object cur) {
		prevN = null; self = cur;
	}
	public Node(Node prev, Object cur) {
		self = cur; prevN = prev;
	}
	public Node(Node prev, Object cur, Node next) {
		self = cur; prevN = prev; nextN = next;
	}	
	public Node(Object prev, Object loc) {
		prevN = new Node(prev);
		self = loc;
	}

	public Object self() {
		return self;
	}
	public Node previous() {
		return prevN;
	}
	public Node next() {
		return nextN;
	}
	
	public Node add(Object next) {
		Node p = new Node(this, next);
		nextN = p;
		return p;
	}
	
	public boolean isRoot() {
		return prevN == null;
	}
	public boolean isEnd() {
		return nextN == null;
	}
	
	public Object nextTile() {
		return nextN.self;
	}
	public Object prevTile() {
		return prevN.self;
	}
	
	public Object goToStart() {
		Node p = this;
		while (!p.isRoot())
			p = prevN;
		return p.self;
	}
	public Object goToEnd() {
		Node p = this;
		while (!p.isEnd())
			p = nextN;
		return p.self;
	}
}