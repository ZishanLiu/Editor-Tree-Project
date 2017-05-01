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
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
		this.wrap = new DisplayableNodeWrapper(this);
	}

	public Node(char ch) {
		this.element = ch;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
		this.wrap = new DisplayableNodeWrapper(this);
	}

	public int height() {
		if (this == EditTree.NULL_NODE) {
			return -1;
		}
		if (this.balance == Code.RIGHT) {
			return 1 + right.height();
		} else {
			return 1 + left.height();
		}
	}

	/*
	 * Use rank to implement size make it more efficient.
	 */
	public int size() {
		if (this == EditTree.NULL_NODE) {
			return 0;
		}
		return this.rank + right.size() + 1;
	}

	/**
	 * This is our second add method. We get help from Yiyu Ma and Yuan Zhou. I
	 * use NULL_NODE as the base case to avoid lots of situations of nullpointer
	 * errors. I recurse into the node according to the rank, and refresh the
	 * node's balance and rank. I do the needed rotations during refreshing. I
	 * tried to use the parent field, but I actually did not use it to add. But
	 * I think it might be helpful while doing delete method. So I keep it. I
	 * use a Wrapper class to return both the node and the boolean to see if I
	 * need to keep changing or not.
	 */
	public Wrapper add(char ch, int pos, Node parent) {
		if (this == EditTree.NULL_NODE) {
			Node add = new Node(ch);
			add.parent = parent;
			return new Wrapper(add, true, 0);
		}
		if (pos <= this.rank) {
			this.rank++;
			Wrapper temp = this.left.add(ch, pos, this);
			this.left = temp.getNode();
			Boolean bool = temp.keepChanging;
			if (!bool) {
				return new Wrapper(this, false, temp.numberOfRotation);
			}
			if (this.balance == Code.SAME) {
				this.balance = Code.LEFT;
				return new Wrapper(this, true, 0);
			} else if (this.balance == Code.RIGHT) {
				this.balance = Code.SAME;
				return new Wrapper(this, false, 0);
			} else if (this.balance == Code.LEFT) {
				if (this.left.balance == Code.LEFT) {
					Node output = this.singleRight(this);
					output.parent = parent;
					temp.numberOfRotation++;
					return new Wrapper(output, false, temp.numberOfRotation);
				} else if (this.left.balance == Code.RIGHT) {
					Node output = this.doubleRight(this);
					output.parent = parent;
					temp.numberOfRotation += 2;
					return new Wrapper(output, false, temp.numberOfRotation);
				} else {
					return new Wrapper(this, false, temp.numberOfRotation);
				}
			}
		} else {
			Wrapper temp = this.right.add(ch, pos - this.rank - 1, this);
			this.right = temp.getNode();
			Boolean bool = temp.keepChanging;
			if (!bool) {
				return new Wrapper(this, false, temp.numberOfRotation);
			}
			if (this.balance == Code.SAME) {
				this.balance = Code.RIGHT;
				return new Wrapper(this, true, temp.numberOfRotation);
			} else if (this.balance == Code.LEFT) {
				this.balance = Code.SAME;
				return new Wrapper(this, false, temp.numberOfRotation);
			} else if (this.balance == Code.RIGHT) {
				if (this.right.balance == Code.RIGHT) {
					Node output = this.singleLeft(this);
					output.parent = parent;
					temp.numberOfRotation++;
					return new Wrapper(output, false, temp.numberOfRotation);
				} else if (this.right.balance == Code.LEFT) {
					Node output = this.doubleLeft(this);
					output.parent = parent;
					temp.numberOfRotation += 2;
					return new Wrapper(output, false, temp.numberOfRotation);
				} else {
					return new Wrapper(this, false, temp.numberOfRotation);
				}
			}
		}
		return new Wrapper(this, false, 0);
	}

	/*
	 * A easy way to getRank. Just used for debug. Same for many methods below.
	 */
	public int getRank() {
		return left.size();
	}

	public DisplayableNodeWrapper getDisplayableNodePart() {
		return wrap;
	}

	public boolean hasLeft() {
		if (this.left != EditTree.NULL_NODE) {
			return true;
		}
		return false;
	}

	public boolean hasRight() {
		if (this.right != EditTree.NULL_NODE) {
			return true;
		}
		return false;
	}

	public boolean hasParent() {
		if (this.parent != EditTree.NULL_NODE) {
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
		return this.balance;
	}

	/**
	 * The four classes below are rotation classes. We deal with the position,
	 * rank, balance code, rotation times and parent in the classes. We get the
	 * relationship by draw picture by hand and deduct.
	 */
	public Node singleLeft(Node parent) {
		Node child = parent.right;
		child.rank = child.rank + parent.rank + 1;
		parent.right = child.left;
		if (parent.right != EditTree.NULL_NODE) {
			parent.right.parent = parent;
		}
		child.left = parent;
		parent.parent = child;
		if (child.balance == Code.SAME) {
			child.balance = Code.LEFT;
			parent.balance = Code.RIGHT;
		} else {
			child.balance = Code.SAME;
			parent.balance = Code.SAME;
		}
		return child;
	}

	public Node singleRight(Node parent) {
		Node child = parent.left;
		parent.rank = parent.rank - child.rank - 1;
		parent.left = child.right;
		if (parent.left != EditTree.NULL_NODE) {
			parent.left.parent = parent;
		}
		child.right = parent;
		parent.parent = child;
		if (child.balance == Code.SAME) {
			child.balance = Code.RIGHT;
			parent.balance = Code.LEFT;
		} else {
			child.balance = Code.SAME;
			parent.balance = Code.SAME;
		}
		return child;
	}

	public Node doubleLeft(Node a) {
		Node c = a.right;
		Node b = c.left;
		if (b.balance == Code.LEFT) {
			a.balance = Code.SAME;
			c.balance = Code.RIGHT;
		} else if (b.balance == Code.RIGHT) {
			a.balance = Code.LEFT;
			c.balance = Code.SAME;
		} else {
			a.balance = Code.SAME;
			c.balance = Code.SAME;
		}
		a.right = b.left;
		a.right.parent = a;
		c.left = b.right;
		c.left.parent = c;
		b.left = a;
		a.parent = b;
		b.right = c;
		c.parent = b;
		c.rank = c.rank - b.rank - 1;
		b.rank += a.rank + 1;
		b.balance = Code.SAME;
		return b;
	}

	public Node doubleRight(Node a) {
		Node c = a.left;
		Node b = c.right;
		if (b.balance == Code.LEFT) {
			c.balance = Code.SAME;
			a.balance = Code.RIGHT;
		} else if (b.balance == Code.RIGHT) {
			c.balance = Code.LEFT;
			a.balance = Code.SAME;
		} else {
			a.balance = Code.SAME;
			c.balance = Code.SAME;
		}
		a.left = b.right;
		a.left.parent = a;
		c.right = b.left;
		c.right.parent = c;
		b.right = a;
		a.parent = b;
		b.left = c;
		c.parent = b;
		a.rank = a.rank - c.rank - b.rank - 2;
		b.rank += c.rank + 1;
		b.balance = Code.SAME;
		return b;
	}

	/*
	 * This is our delete method. By using delete itself when we facing root, we
	 * simplify lots of work. It is similar to adding method. Find the node we
	 * need to delete, and update its infomation, and Check if we need to do
	 * different rotations.
	 */
	public DeleteWrapper delete(int pos) {
		// update rank, keep tracking until we find the node we delete.
		if (pos < this.rank) {
			this.rank--;
			DeleteWrapper output = this.left.delete(pos);
			this.left = output.retrunNode;
			output.retrunNode.parent = this;
			// check if we need to keep changing balance code.
			// After rotation may not need to change.
			if (!output.keepChanging) {
				return new DeleteWrapper(this, output.deleteNode, false, output.numberOfRotation);
			}
			// If it is true, keep updating the balance code.
			if (this.balance == Code.LEFT) {
				this.balance = Code.SAME;
				return new DeleteWrapper(this, output.deleteNode, true, output.numberOfRotation);
			} else if (this.balance == Code.SAME) {
				this.balance = Code.RIGHT;
				return new DeleteWrapper(this, output.deleteNode, false, output.numberOfRotation);
			} else {
				Node thisParent = this.parent;
				// determine when we should roate after deletion.
				if (this.right.balance == Code.RIGHT) {
					Node rotateNode = singleLeft(this);
					rotateNode.parent = thisParent;
					output.numberOfRotation++;
					return new DeleteWrapper(rotateNode, output.deleteNode, true, output.numberOfRotation);
				} else if (this.right.balance == Code.LEFT) {
					Node rotateNode = doubleLeft(this);
					rotateNode.parent = thisParent;
					output.numberOfRotation += 2;
					return new DeleteWrapper(rotateNode, output.deleteNode, true, output.numberOfRotation);
				} else {
					Node rotateNode = singleLeft(this);
					rotateNode.parent = thisParent;
					output.numberOfRotation++;
					return new DeleteWrapper(rotateNode, output.deleteNode, false, output.numberOfRotation);
				}
			}
		} else if (pos > this.rank) {
			// find the node we need to delete.
			DeleteWrapper output = this.right.delete(pos - this.rank - 1);
			this.right = output.retrunNode;
			output.retrunNode.parent = this;
			// Update balance code when boolean is true.
			if (!output.keepChanging) {
				return new DeleteWrapper(this, output.deleteNode, false, output.numberOfRotation);
			}
			if (this.balance == Code.RIGHT) {
				this.balance = Code.SAME;
				return new DeleteWrapper(this, output.deleteNode, true, output.numberOfRotation);
			} else if (this.balance == Code.SAME) {
				this.balance = Code.LEFT;
				return new DeleteWrapper(this, output.deleteNode, false, output.numberOfRotation);
			} else {
				// determine when we should rotate after deletion by checking
				// balance codes above.
				Node thisParent = this.parent;
				if (this.left.balance == Code.LEFT) {
					Node rotateNode = singleRight(this);
					rotateNode.parent = thisParent;
					output.numberOfRotation++;
					return new DeleteWrapper(rotateNode, output.deleteNode, true, output.numberOfRotation);
				} else if (this.left.balance == Code.RIGHT) {
					Node rotateNode = doubleRight(this);
					rotateNode.parent = thisParent;
					output.numberOfRotation += 2;
					return new DeleteWrapper(rotateNode, output.deleteNode, true, output.numberOfRotation);
				} else {
					Node rotateNode = singleRight(this);
					rotateNode.parent = thisParent;
					output.numberOfRotation++;
					return new DeleteWrapper(rotateNode, output.deleteNode, false, output.numberOfRotation);
				}
			}
		} else {
			// speical case
			if (this.left == EditTree.NULL_NODE && this.right == EditTree.NULL_NODE) {
				return new DeleteWrapper(EditTree.NULL_NODE, this, true, 0);
			} else if (this.left == EditTree.NULL_NODE) {
				return new DeleteWrapper(this.right, this, true, 0);
			} else if (this.right == EditTree.NULL_NODE) {
				return new DeleteWrapper(this.left, this, true, 0);
			} else {
				return this.deleteWhenTwoChildren();
			}
		}
	}

	public DeleteWrapper deleteWhenTwoChildren() {
		// when we need to delete root, we need to get the node that has
		// pos 0 in the right subtree, so call delete()itself
		// would be far more easier than if we analyze different
		// situations again.
		DeleteWrapper temp = this.right.delete(0);
		this.right = temp.retrunNode;
		Node d = temp.deleteNode;
		d.parent = EditTree.NULL_NODE;
		// Update infomation after we call delete();
		d.left = this.left;
		if (d.equals(this.right)) {
			d.right = this.right.right;
		} else {
			d.right = this.right;
		}
		d.rank = this.rank;
		int l = d.left.height();
		int r = d.right.height();
		Node dParent = d.parent;
		int rotate = temp.numberOfRotation;
		if (l - r == 1) {
			d.balance = Code.LEFT;
			if (this.balance == Code.SAME) {
				d.parent = dParent;
				return new DeleteWrapper(d, this, false, rotate);
			}
		} else if (r - l == 1) {
			d.balance = Code.RIGHT;
			if (this.balance == Code.SAME) {
				d.parent = dParent;
				return new DeleteWrapper(d, this, false, rotate);
			}
		} else if (r == l) {
			d.balance = Code.SAME;
		} else if (l - r == 2) {
			if (d.left.balance == Code.LEFT) {
				d = singleRight(d);
				rotate++;
			} else if (d.left.balance == Code.RIGHT) {
				d = doubleRight(d);
				rotate += 2;
			}
		} else if (r - l == 2) {
			if (d.right.balance == Code.RIGHT) {
				d = singleLeft(d);
				rotate++;
			} else if (d.right.balance == Code.LEFT) {
				d = doubleLeft(d);
				rotate += 2;
			}
		}
		d.parent = dParent;
		return new DeleteWrapper(d, this, temp.keepChanging, rotate);
	}

	public Node copy() {
		// use recursion to copy nodes.
		if (this == EditTree.NULL_NODE) {

			return EditTree.NULL_NODE;
		}
		Node temp = new Node();
		temp.left = this.left.copy();
		temp.right = this.right.copy();
		temp.element = this.element;
		temp.rank = this.rank;
		temp.balance = this.getBalance();
		return temp;
	}

	public int find(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (this.element == s.charAt(i)) {
				return this.rank;
			}
		}
		return this.left.find(s.substring(1)) + this.right.find(s.substring(1));
	}

	public Node newAdd(String s, Node node) {
		if (s.length() <= 0) {
			return EditTree.NULL_NODE;
		}

		node = new Node(s.charAt(s.length() / 2));
		node.rank = s.length() / 2;
		node.left = newAdd(s.substring(0, s.length() / 2), node.left);
		node.right = newAdd(s.substring(s.length() / 2 + 1), node.right);
		node.left.parent = node;
		node.right.parent = node;

		return node;
	}

	public char get(int pos) {
		// Since we have the relationship between pos and rank, we don't need to
		// go thought each node in the tree.
		if (pos < this.rank) {
			return this.left.get(pos);
		} else if (pos > this.rank) {
			return this.right.get(pos - this.rank - 1);
		} else {
			return this.element;
		}
	}

	public Node refreshBalance() {
		// method for refreshing. Same as we did in add and deletion.
		int l = this.left.height();
		int r = this.right.height();
		Node thisParent = this.parent;
		if (l - r == 1) {
			this.balance = Code.LEFT;
			if (this.balance == Code.SAME) {
				this.parent = thisParent;
			}
		} else if (r - l == 1) {
			this.balance = Code.RIGHT;
			if (this.balance == Code.SAME) {
				this.parent = thisParent;
			}
		} else if (r == l) {
			this.balance = Code.SAME;
		} else if (l - r == 2) {
			if (this.left.balance == Code.RIGHT) {
				return doubleRight(this);
			} else {
				return singleRight(this);
			}
		} else if (r - l == 2) {
			if (this.right.balance == Code.LEFT) {
				return doubleLeft(this);
			} else {
				return singleLeft(this);
			}
		}
		this.parent = thisParent;
		return this;
	}

	public SplitWrapper split(int pos) {
		if (pos < this.rank) {
			EditTree tempRight = new EditTree(this.right);
			EditTree tempCenter = new EditTree(this.element);
			tempCenter.concatenate(tempRight);
			tempRight = tempCenter;
			this.balance = Code.RIGHT;
			SplitWrapper s = this.left.split(pos);
			s.rightTree.concatenate(tempRight);
			return new SplitWrapper(s.leftTree, s.rightTree);
		} else if (pos > this.rank) {
			EditTree tempLeft = new EditTree(this.left);
			EditTree tempCenter = new EditTree(this.element);
			tempLeft.concatenate(tempCenter);
			SplitWrapper s = this.right.split(pos - this.rank - 1);
			tempLeft.concatenate(s.leftTree);
			this.balance = Code.LEFT;
			return new SplitWrapper(tempLeft, s.rightTree);
		} else {
			EditTree tempLeft = new EditTree(this.left);
			EditTree tempRight = new EditTree(this.right);
			EditTree tempCenter = new EditTree(this.element);
			tempCenter.concatenate(tempRight);
			tempRight = tempCenter;
			this.balance = Code.RIGHT;
			return new SplitWrapper(tempLeft, tempRight);
		}
	}

	/**
	 * This is an inner class to wrap a node and a boolean together. Used for
	 * return in add.
	 */
	class Wrapper {
		private Node node;
		private boolean keepChanging;
		private int numberOfRotation = 0;

		public Wrapper(Node node, boolean heightChanged, int rotation) {
			this.node = node;
			this.keepChanging = heightChanged;
			this.numberOfRotation = rotation;
		}

		public Node getNode() {
			return node;
		}

		public int getNumberofRotation() {
			return this.numberOfRotation;
		}
	}

	class DeleteWrapper {
		private Node retrunNode;
		private Node deleteNode;
		private boolean keepChanging;
		private int numberOfRotation;

		public DeleteWrapper(Node returnNode, Node deleteNode, boolean keepChanging, int rotation) {
			this.retrunNode = returnNode;
			this.deleteNode = deleteNode;
			this.keepChanging = keepChanging;
			this.numberOfRotation = rotation;
		}

		public Node getReturnNode() {
			return this.retrunNode;
		}

		public Node getDeleteNode() {
			return this.deleteNode;
		}

		public int getNumberOfRotation() {
			return this.numberOfRotation;
		}
	}

	class SplitWrapper {
		private EditTree leftTree;
		private EditTree rightTree;

		public SplitWrapper(EditTree leftTree, EditTree rightTree) {
			this.leftTree = leftTree;
			this.rightTree = rightTree;
		}

		public EditTree getRightTree() {
			return this.rightTree;
		}

		public EditTree getLeftTree() {
			return this.leftTree;
		}
	}
}