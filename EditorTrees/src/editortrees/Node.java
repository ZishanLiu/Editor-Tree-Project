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
	// Feel free to add other fields that you find useful

	// You will probably want to add several other methods

	// For the following methods, you should fill in the details so that they
	// work correctly
	public Node() {
		this.element = '\0';
		this.left = null;
		this.right = null;
		this.rank = getRank();
		this.balance = getCode();
	}

	public Node(char ch) {
		this.element = ch;
		this.left = null;
		this.right = null;
		this.rank = getRank();
		this.balance = getCode();
	}

	public int height() {
		int left = this.left.height();
		int right = this.right.height();

		if (left > right) {
			return left + 1;
		}
		return right + 1;
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
		if (this == null) {
			return 0;
		}
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

	public Code getCode() {
		if (left == null && right == null) {
			return Code.SAME;
		} else if (left == null) {
			return Code.RIGHT;
		} else if (right == null) {
			return Code.LEFT;
		} else {
			if (left.size() > right.size()) {
				return Code.LEFT;
			} else if (left.size() < right.size()) {
				return Code.RIGHT;
			} else {
				return Code.SAME;
			}
		}
	}

}