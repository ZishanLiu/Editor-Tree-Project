package editortrees;

import java.util.ArrayList;
import java.util.Stack;

import editortrees.Node.Code;

// A height-balanced binary tree with rank that could be the basis for a text editor.

public class EditTree {

	private DisplayableBinaryTree display;
	private Node root;
	private int numberOfRotation = 0;

	/**
	 * MILESTONE 1 Construct an empty tree
	 */
	public EditTree() {
		this.display = display;
		this.root = null;
	}

	/**
	 * MILESTONE 1 Construct a single-node tree whose element is cc
	 * 
	 * @param ch
	 */
	public EditTree(char ch) {
		this.display = display;
		this.root = new Node(ch);

	}

	/**
	 * MILESTONE 2 Make this tree be a copy of e, with all new nodes, but the
	 * same shape and contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {

	}

	/**
	 * MILESTONE 3 Create an EditTree whose toString is s. This can be done in
	 * O(N) time, where N is the length of the tree (repeatedly calling insert()
	 * would be O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {

	}

	/**
	 * MILESTONE 1 returns the total number of rotations done in this tree since
	 * it was created. A double rotation counts as two.
	 *
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		return numberOfRotation; // replace by a real calculation.
	}

	/**
	 * MILESTONE 1 return the string produced by an inorder traversal of this
	 * tree
	 */
	@Override
	public String toString() {

		String result = "";
		if (this.root == null) {
			return result;
		}
		ArrayList<Character> a = toInorderList(this.root);
		for (Character ch : a) {
			result += ch;
		}
		return result;
	}

	private ArrayList<Character> toInorderList(Node node) {
		Stack<Node> stack = new Stack<Node>();
		ArrayList<Character> result = new ArrayList<Character>();
		Node temp = root;
		while (temp != null || !stack.empty()) {
			while (temp != null) {
				stack.add(temp);
				temp = temp.left;
			}
			temp = stack.peek();
			stack.pop();
			result.add(temp.element);
			temp = temp.right;
		}
		return result;
	}

	/**
	 * MILESTONE 1 This one asks for more info from each node. You can write it
	 * like the arraylist-based toString() method from the BST assignment.
	 * However, the output isn't just the elements, but the elements, ranks, and
	 * balance codes. Former CSSE230 students recommended that this method,
	 * while making it harder to pass tests initially, saves them time later
	 * since it catches weird errors that occur when you don't update ranks and
	 * balance codes correctly. For the tree with node b and children a and c,
	 * it should return the string: [b1=, a0=, c0=] There are many more examples
	 * in the unit tests.
	 * 
	 * @return The string of elements, ranks, and balance codes, given in a
	 *         pre-order traversal of the tree.
	 */
	public String toDebugString() {
		String result = "";
		if (this.root == null) {
			return "[" + result + "]";
		}
		ArrayList<Node> a = toPreorderList(this.root);
		for (Node ch : a) {
			result += ch.element;
			result += ch.rank + ch.balance.toString() + "," + " ";
		}
		return "[" + result.substring(0, result.length() - 2) + "]";
	}

	private ArrayList<Node> toPreorderList(Node root) {
		ArrayList<Node> returnList = new ArrayList<Node>();

		if (root == null) {
			return returnList;
		}
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		while (!stack.empty()) {
			Node n = stack.pop();
			returnList.add(n);

			if (n.right != null) {
				stack.push(n.right);
			}
			if (n.left != null) {
				stack.push(n.left);
			}

		}
		return returnList;
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch
	 *            character to add to the end of this tree.
	 */
	public void add(char ch) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
		if (this.root == null) {
			this.root = new Node(ch);
		} else {
			add(ch, this.root);
		}

	}

	private void add(char ch, Node current) {
		if (current.right == null) {
			current.right = new Node(ch);
			current.right.parent = current;
			Node child = current.right;

			while (current != this.root) {
				if (current.balance.equals(Code.RIGHT)) {
					if (current.right.balance == Code.RIGHT) {
						if (current.parent != null) {
							if (current.parent.left.equals(current)) {
								current.parent.left = singleLeft(current);
								return;
							} else if (current.parent.right.equals(current)) {
								current.parent.right = singleLeft(current);
								return;
							}
							return;
						} else {
							this.root = singleLeft(current);
							return;
						}
					} else if (current.right.balance == Code.LEFT) {
						if (current.parent != null) {
							if (current.parent.left.equals(current)) {
								current.parent.left = doubleLeft(current);
							} else if (current.parent.right.equals(current)) {
								current.parent.right = doubleLeft(current);
							}
							return;
						} else {
							this.root = doubleLeft(current);
							return;
						}
					}
					return;

				} else if (current.balance.equals(Code.SAME)) {
					if (current.left != null && current.right != null) {
						if (current.left.balance != Code.SAME || current.right.balance != Code.SAME) {
							current.balance = Code.RIGHT;
						}
					} else {
						current.balance = Code.RIGHT;
					}
				} else {
					current.balance = Code.SAME;
				}
				child = current;
				current = current.parent;
			}

			if (current.balance.equals(Code.RIGHT)) {
				if (current.right.balance.equals(Code.RIGHT)) {
					this.root = singleLeft(current);
				} else if (current.right.balance.equals(Code.LEFT)) {
					this.root = doubleLeft(current);
				}
				return;
			} else if (current.balance.equals(Code.SAME)) {
				if (current.left != null && current.right != null) {
					if (current.left.balance != Code.SAME || current.right.balance != Code.SAME) {
						current.balance = Code.RIGHT;
					}
				} else {
					current.balance = Code.RIGHT;
				}
			} else if (current.balance.equals(Code.LEFT)) {
				current.balance = Code.SAME;
			}
			return;
		}
		add(ch, current.right);
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char ch, int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos > this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if (root == null) {
			root = new Node(ch);
		} else {
			add(ch, pos, this.root);
		}
	}

	public void add(char ch, int pos, Node current) throws IndexOutOfBoundsException {
		if (pos <= current.rank) {
			current.rank++;
			if (current.left == null) {
				Node a = new Node(ch);
				current.left = a;
				current.left.parent = current;
				Node child = current.left;
				if (current.equals(this.root)) {
					refreshRoot(current, child);
				} else {
					RefreshWrapper temp = refresh(current, child);
					if (temp.bool == true) {
						child = temp.child;
						current = temp.current;
						refreshRoot(current, child);
					}
				}
			} else {
				current = current.left;
				add(ch, pos, current);
			}
		} else {
			if (current.right == null) {
				Node a = new Node(ch);
				current.right = a;
				current.right.parent = current;
				Node child = current.right;
				if (current.equals(this.root)) {
					refreshRoot(current, child);
				} else {
					RefreshWrapper temp = refresh(current, child);
					if (temp.bool == true) {
						child = temp.child;
						current = temp.current;
						refreshRoot(current, child);
					}
				}
			} else {
				int rootRank = current.rank;
				current = current.right;
				add(ch, pos - rootRank - 1, current);
			}
		}
	}

	public RefreshWrapper refresh(Node current, Node child) {
		while (current != this.root) {
			if (current.left != null) {
				if (current.left.equals(child)) {
					if (current.balance.equals(Code.LEFT)) {
						if (current.left.balance == Code.LEFT) {
							if (current.parent != null) {
								if (current.parent.left.equals(current)) {
									current.parent.left = singleRight(current);
									return new RefreshWrapper(current, child, true);
								} else if (current.parent.right.equals(current)) {
									current.parent.right = singleRight(current);
									return new RefreshWrapper(current, child, true);
								}
								return new RefreshWrapper(current, child, false);
							} else {
								this.root = singleRight(current);
								return new RefreshWrapper(current, child, true);
							}
						} else if (current.left.balance == Code.RIGHT) {
							if (current.parent != null) {
								if (current.parent.left.equals(current)) {
									current.parent.left = doubleRight(current);
									return new RefreshWrapper(current, child, true);
								} else if (current.parent.right.equals(current)) {
									current.parent.right = doubleRight(current);
									return new RefreshWrapper(current, child, true);
								}
								return new RefreshWrapper(current, child, false);
							} else {
								this.root = doubleRight(current);
								return new RefreshWrapper(current, child, true);
							}
						}
						return new RefreshWrapper(current, child, false);
					} else if (current.balance.equals(Code.SAME)) {
						if (current.left != null && current.right != null) {
							if (current.left.balance != Code.SAME || current.right.balance != Code.SAME) {
								current.balance = Code.LEFT;
							}
						} else {
							current.balance = Code.LEFT;
						}
					} else {
						current.balance = Code.SAME;
					}
				}
			}
			if (current.right != null) {
				if (current.right.equals(child)) {
					if (current.balance.equals(Code.RIGHT)) {
						if (current.right.balance == Code.RIGHT) {
							if (current.parent != null) {
								if (current.parent.left.equals(current)) {
									current.parent.left = singleLeft(current);
									return new RefreshWrapper(current, child, true);
								} else if (current.parent.right.equals(current)) {
									current.parent.right = singleLeft(current);
									return new RefreshWrapper(current, child, true);
								}
								return new RefreshWrapper(current, child, false);
							} else {
								this.root = singleLeft(current);
								return new RefreshWrapper(current, child, true);
							}
						} else if (current.right.balance == Code.LEFT) {
							if (current.parent != null) {
								if (current.parent.left.equals(current)) {
									current.parent.left = doubleLeft(current);
								} else if (current.parent.right.equals(current)) {
									current.parent.right = doubleLeft(current);
								}
								return new RefreshWrapper(current, child, true);
							} else {
								this.root = doubleLeft(current);
								return new RefreshWrapper(current, child, true);
							}
						}
						return new RefreshWrapper(current, child, false);

					} else if (current.balance.equals(Code.SAME)) {
						if (current.left != null && current.right != null) {
							if (current.left.balance != Code.SAME || current.right.balance != Code.SAME) {
								current.balance = Code.RIGHT;
							}
						} else {
							current.balance = Code.RIGHT;
						}
					} else {
						current.balance = Code.SAME;
					}
				}
			}
			child = current;
			current = current.parent;
		}
		return new RefreshWrapper(current, child, true);
	}

	public void refreshRoot(Node current, Node child) {
		if (current == this.root) {
			if (current.left.equals(child)) {
				if (current.balance.equals(Code.LEFT)) {
					if (current.left.balance.equals(Code.LEFT)) {
						this.root = singleRight(current);
					} else if (current.left.balance.equals(Code.RIGHT)) {
						this.root = doubleRight(current);
					}
					return;
				} else if (current.balance.equals(Code.SAME)) {
					current.balance = Code.LEFT;
				} else if (current.balance.equals(Code.RIGHT)) {
					current.balance = Code.SAME;
				}
			} else if (current.right.equals(child)) {
				if (current.balance.equals(Code.RIGHT)) {
					if (current.right.balance.equals(Code.RIGHT)) {
						this.root = singleLeft(current);
					} else if (current.right.balance.equals(Code.LEFT)) {
						this.root = doubleLeft(current);
					}
					return;
				} else if (current.balance.equals(Code.SAME)) {
					if (current.left != null && current.right != null) {
						if (current.left.balance != Code.SAME || current.right.balance != Code.SAME) {
							current.balance = Code.RIGHT;
						}
					} else {
						current.balance = Code.RIGHT;
					}
				} else if (current.balance.equals(Code.LEFT)) {
					current.balance = Code.SAME;
				}
			}
		}
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if (pos >= this.size() || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		return this.toString().charAt(pos);
	}

	/**
	 * MILESTONE 1
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		if (this.root == null) {
			return -1;
		}
		return this.root.height();
	}

	/**
	 * MILESTONE 2
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		if (this.root == null) {
			return 0;
		}
		return this.root.size(); // replace by a real calculation.
	}

	/**
	 * MILESTONE 2
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		return '#'; // replace by a real calculation.
	}

	/**
	 * MILESTONE 3, EASY This method operates in O(length*log N), where N is the
	 * size of this tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		return "";
	}

	/**
	 * MILESTONE 3, MEDIUM - SEE PAPER REFERENCED IN SPEC FOR ALGORITHM! Append
	 * (in time proportional to the log of the size of the larger tree) the
	 * contents of the other tree to this one. Other should be made empty after
	 * this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {

	}

	/**
	 * MILESTONE 3: DIFFICULT This operation must be done in time proportional
	 * to the height of this tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		return null; // replace by a real calculation.
	}

	/**
	 * MILESTONE 3: JUST READ IT FOR USE OF SPLIT/CONCATENATE This method is
	 * provided for you, and should not need to be changed. If split() and
	 * concatenate() are O(log N) operations as required, delete should also be
	 * O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length) throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete" : "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * MILESTONE 3 Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		return -2;
	}

	/**
	 * MILESTONE 3
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		return -2;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}

	public Node singleLeft(Node parent) {
		Node child = parent.right;
		child.rank = child.rank + parent.rank + 1;
		parent.right = child.left;

		if (parent.right != null) {
			parent.right.parent = parent;
		}
		child.left = parent;

		parent.parent = child;

		numberOfRotation++;
		child.balance = Code.SAME;
		parent.balance = Code.SAME;
		return child;
	}

	public Node singleRight(Node parent) {
		Node child = parent.left;
		parent.rank = parent.rank - child.rank - 1;
		parent.left = child.right;

		if (parent.left != null) {
			parent.left.parent = parent;
		}
		child.right = parent;

		parent.parent = child;

		numberOfRotation++;
		child.balance = Code.SAME;
		parent.balance = Code.SAME;
		return child;
	}

	public Node doubleLeft(Node a) {
		Node c = a.right;
		Node b = c.left;
		a.right = b.left;

		if (a.right != null) {
			a.right.parent = a;
		}

		c.left = b.right;

		if (c.left != null) {
			c.left.parent = c;
		}

		b.left = a;

		a.parent = b;

		b.right = c;

		c.parent = b;

		c.rank = c.rank - b.rank - 1;
		b.rank += a.rank + 1;

		a.balance = Code.SAME;
		b.balance = Code.SAME;
		c.balance = Code.SAME;
		numberOfRotation += 2;
		return b;
	}

	public Node doubleRight(Node a) {
		Node c = a.left;
		Node b = c.right;
		a.left = b.right;

		if (a.left != null) {
			a.left.parent = a;
		}
		c.right = b.left;

		if (c.right != null) {
			c.right.parent = c;
		}
		b.right = a;

		a.parent = b;

		b.left = c;

		c.parent = b;

		a.rank = a.rank - c.rank - b.rank - 2;
		b.rank += c.rank + 1;

		a.balance = Code.SAME;
		b.balance = Code.SAME;
		c.balance = Code.SAME;
		numberOfRotation += 2;
		return b;
	}

	public int slowSize() {
		if (this.root == null) {
			return 0;
		}
		return this.root.size();
	}

	public int slowHeight() {
		if (this.root == null) {
			return -1;
		}
		return this.root.height();
	}

	public void show() {
		if (this.display == null) {
			this.display = new DisplayableBinaryTree(this, 960, 1080, true);
		} else {
			this.display.show(true);
		}
	}

	public void close() {
		if (this.display != null) {
			this.display.close();
		}
	}

	class RefreshWrapper {
		private Node current;
		private Node child;
		private Boolean bool;

		public RefreshWrapper(Node current, Node child, Boolean bool) {
			this.current = current;
			this.child = child;
			this.bool = bool;
		}
	}

}
