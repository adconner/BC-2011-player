package hex.data;

import java.util.ArrayList;

public class NodeStream {
	protected ArrayList<Node> stream;
	
	public NodeStream(Node start) {
		stream = new ArrayList<Node>();
		stream.add(start);
	}
	
	public Node firstNode() {
		return stream.get(0);
	}
	public Node lastNode() {
		return stream.get(stream.size()-1);
	}

	public Object start() {
		return stream.get(0).self();
	}
	public Object end() {
		return stream.get(stream.size()-1).self();
	}
	
	public boolean contains(Object loc) {
		Node p = stream.get(0);
		while (!p.isEnd()) {
			if (p.self().equals(loc))
				return true;
			p = p.next();
		}
		//Hasn't check the last node yet
		return p.self().equals(loc);
	}

	public static boolean containsInALOfNS(Object loc, ArrayList<NodeStream> nodes) {
		for (NodeStream node : nodes)
			if (node.contains(loc))
				return true;
		return false;
	}
}