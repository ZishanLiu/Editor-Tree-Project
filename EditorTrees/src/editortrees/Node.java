package editortrees;

// A node in a height-balanced binary tree with rank.
// Except for the NULL_NODE (if you choose to use one), one node cannot
// belong to two different trees.

public class Node {

	enum Code {
		SAME, LEFT, RIGHT;
		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			}
		}
	}

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to
	// the
	// "publicly visible" effects

	char element;
	Node left, right; // subtrees
	int rank; // inorder position of this node within its own subtree.
	Code balance;
	Node parent;
	DisplayableNodeWrapper wrap;
	// Feel free to add other fields that you find useful

	// You will probably want to add several other methods

	// For the following methods, you should fill in the details so that they
	// work correctly
	public Node() {
		this.element = '\0';
		this.left = null;
		this.right = null;
		this.rank = getRank();
		this.balance = Code.SAME;
		this.wrap = new DisplayableNodeWrapper(this);
	}

	public Node(char ch) {
		this.element = ch;
		this.left = null;
		this.right = null;
		this.rank = getRank();
		this.balance = Code.SAME;
		this.wrap = new DisplayableNodeWrapper(this);
	}

	public int height() {
		if (left == null && right == null) {
			return 0;
		} else if (left == null) {
			return 1 + right.height();
		} else if (right == null) {
			return 1 + left.height();
		} else {
			return 1 + Math.max(left.height(), right.height());
		}
	}

	public int size() {
		// if (this.right != null) {
		// return this.right.rightSize() + 1 + this.rank;
		// }
		// return 1 + this.rank;
		// }
		//
		// public int rightSize() {
		// if (this == null) {
		// return 0;
		// }
		// if (left == null && right == null) {
		// return 1;
		// } else if (left == null) {
		// return right.rightSize() + 1;
		// } else if (right == null) {
		// return left.rightSize() + 1;
		// } else {
		// return left.rightSize() + right.rightSize() + 1;
		// }

		if (left == null && right == null) {
			return 1;
		} else if (left == null) {
			return right.size() + 1;
		} else if (right == null) {
			return left.size() + 1;
		} else {
			return left.size() + right.size() + 1;
		}
	}

	public int getRank() {
		Node left = this.left;
		if (left == null) {
			return 0;
		} else if (left.left == null && left.right == null) {
			return 1;
		} else if (left.left == null) {
			return left.right.getRank() + 1;
		} else if (left.right == null) {
			return left.left.getRank() + 1;
		} else {
			return left.left.getRank() + left.right.getRank() + 1;
		}
	}

	public DisplayableNodeWrapper getDisplayableNodePart() {

		return wrap;
	}

	public boolean hasLeft() {
		// TODO Auto-generated method stub.
		if (this.left != null) {
			return true;
		}
		return false;
	}

	public boolean hasRight() {
		if (this.right != null) {
			return true;
		}
		return false;
	}

	public boolean hasParent() {
		if (this.parent != null) {
			return true;
		}
		return false;
	}

	public Node getParent() {

		return this.parent;
	}

	public Node getLeft() {

		return this.left;
	}

	public Node getRight() {

		return this.right;
	}

	public char getElement() {

		return this.element;
	}

	public Code getBalance() {
		// TODO Auto-generated method stub.
		return this.balance;
	}
}