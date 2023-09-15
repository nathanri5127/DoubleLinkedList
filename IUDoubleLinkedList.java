import java.util.Iterator;
import java.util.ListIterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> next;
	private Node<T> head;
	private Node<T> tail;
	private T element;
	private int size;
	private int modCount;
	
	public IUDoubleLinkedList() {
		head = null;
		tail = null;
		next = null;
		size = 0;
		modCount = 0;
	}
	
	/**
	 * Adds the inputed element into the front of the list
	 */
	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
		if(size == 0) {
			head = newNode;
			tail = newNode;
		} else {
			newNode.setNext(head);
			//head.setPrevious(newNode);
			head = newNode;
		}
		size++;
		modCount++;
	}
	/**
	 * Adds the inputed element to the back of the list
	 */
	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		if(size == 0) {
			head = tail = newNode;
		} else {
			tail.setNext(newNode);
		}
		tail = newNode;
		size++;
		modCount++;
	}
	/**
	 * Adds the inputed element to the back of the list
	 */
	@Override
	public void add(T element) {
		addToRear(element);
	}
	/**
	 * Adds an element to the position after a passed through target element
	 */
	@Override
	public void addAfter(T element, T target) {
		Node<T> targetNode = head;
		while(targetNode != null && targetNode.getElement() != target) {
			targetNode = targetNode.getNext();
		}
		if(targetNode == null) {
			throw new NoSuchElementException();
		}
		Node<T> newNode = new Node<T>(element);
		newNode.setNext(targetNode.getNext());
		targetNode.setNext(newNode);
		if(targetNode == tail) {
			tail = newNode;
		}
		size++;
		modCount++;
	}
	/**
	 * Adds an element after the passed through target index of the list
	 */
	@Override
	public void add(int index, T element) {
		if(index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> newNode = new Node<T>(element);
		if(index == 0) {
			newNode.setNext(head);
			head = newNode;
		} else {
			Node<T> current = head;
			for(int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			Node<T> next = current.getNext();
			current.setNext(newNode);
			newNode.setNext(next);
			//newNode.setPrevious(current);
		}
		if((size == 0) || (index == size)) {
			tail = newNode;
		}
		size++;
		modCount++;
	}
	/**
	 * Removes the first element in the list
	 */
	@Override
	public T removeFirst() {
		if(size == 1) {
			T retVal = head.getElement();
			head = tail = null;
			size--;
			modCount++;
			return retVal;
		} else if (size == 0) {
			throw new NoSuchElementException();
		} else {
			Node<T> nextNode = head.getNext();
			T retVal = head.getElement();
			head = null;
			head = nextNode;
			size--;
			modCount++;
			return retVal;
		}
		
	}
	/**
	 * Removes the last element in the list
	 */
	@Override
	public T removeLast() {
		if(size == 1){
			T retVal = head.getElement();
			head = tail = null;
			size--;
			modCount++;
			return retVal;
		} else if (size == 0) {
			throw new NoSuchElementException();
		} else {
			Node<T> temp = head;
			for(int i = 0; i < size - 2; i++) {
				temp = temp.getNext();
			}
			T retVal = tail.getElement();
			tail = temp;
			temp.setNext(null);
			size--;
			modCount++;
			return retVal;
		}
	}
	/**
	 * Removes a inputed element from the list
	 */
	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}
	/**
	 * Removes the element at the specified element in the list
	 */
	@Override
	public T remove(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> previous = null;
		Node<T> current = head;
		for(int i = 0; i < index; i++) {
			previous = current;
			current = current.getNext();
		}
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		size--;
		modCount++;
		
		return current.getElement();
	}
	/**
	 * Sets the passed through element at the passed through index of the list
	 */
	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> temp = head;
		for(int i = 0; i < index; i++) {
			temp = temp.getNext();
		}
		temp.setElement(element);
	}
	/**
	 * Returns the element in the list at the passed through index
	 */
	@Override
	public T get(int index) {
		Node<T> temp = head;
		if(size == 0) {
			throw new IndexOutOfBoundsException();
		}
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		} else {
			for(int i = 0; i < index; i++) {
				temp = temp.getNext();
			}
		}
		return temp.getElement();
	}
	/**
	 * Returns the index in the list where the target element is found
	 */
	@Override
	public int indexOf(T element) {
		Node<T> newNode = head;
		int count = 0;
		if(!contains(element)){
			return -1;
		} else {
			while(newNode != null && newNode.getElement() != null && count < count) {
				if(newNode.getElement().equals(element)) {
					return count;
				} else {
					newNode = newNode.getNext();
					count++;
				}
			}
		}
		return 0;
	}
	/**
	 * Returns the first element in the list
	 */
	@Override
	public T first() {
		if(size == 0) {
			throw new NoSuchElementException();
		} else {
			return head.getElement();
		}
	}
	/**
	 * Returns the last element of the list
	 */
	@Override
	public T last() {
		if(size == 0) {
			throw new NoSuchElementException();
		} else {
			return tail.getElement();
		}
	}
	/**
	 * Returns true or false if the passed through element is found in the lsit
	 */
	@Override
	public boolean contains(T target) {
		if(isEmpty()) {
			return false;
		}
		Node<T> node = head;
		while(node != null && node.getElement() != target) {
			return false;
		}
		return true;
	}
	/**
	 * Returns true if the list is empty and false is the list contains elements
	 */
	@Override
	public boolean isEmpty() {
		if(size == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Returns the amount of elements in the list
	 */
	@Override
	public int size() {
		return size;
	}
	/**
	 * Turns the list into a organized string
	 */
	public String toString() {
		String list = "[" ;
		Node<T> current = head;
		while(current != null) {
			list += current.getElement();
			if(current.getNext() != null) {
				list += ", ";
			}
			current = current.getNext();
		}
		
		return list + "]";
	}

	@Override
	public Iterator<T> iterator() {
		return new iterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		ListIterator<T> listIterator = new listIterator();
		return listIterator;
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		ListIterator<T> listIterator = new listIterator(startingIndex);
		return listIterator;
	}
	
	public class iterator implements Iterator<T> {
		Node<T> newNode = head;
		Node<T> previousNode = null;
		Node<T> secondPrevious = null;
		boolean canRemove;
		
		public iterator() {
			if(size ==0) {
				newNode = null;
			}
			canRemove = false;
		}
		/**
		 * Determines if the iterator has another element
		 */
		@Override
		public boolean hasNext() {
			return newNode != null;
		}
		/**
		 * Returns the next element in the iterator
		 */
		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = newNode.getElement();
			secondPrevious = previousNode;
			previousNode = newNode;
			newNode = newNode.getNext();
			canRemove = true;
			return retVal;
		}
		/**
		 * Removes the element at the iterator
		 */
		public void remove() {
			if(!canRemove) {
				throw new IllegalStateException();
			} else if (previousNode == tail && head != tail){
				tail = secondPrevious;
				secondPrevious.setNext(null);
			} else if(head == tail) {
				head = tail = null;
			} else if(secondPrevious!= null) {
				secondPrevious.setNext(previousNode);
				previousNode.setNext(null);
			} else {
				head = newNode;
				previousNode.setNext(null);
			}
			size--;
			canRemove = false;
		}
	}
	
	public class listIterator implements ListIterator<T>{
		Node<T> newNode = null;
		Node<T> currentNode = head;
		Node<T> add = null;
		Node<T> last = null;
		boolean canAdd, canRemove;
		int index =  0;
		
		public listIterator() {
			if(size == 0) {
				newNode = null;
			} else {
				newNode = head;
			}
			canRemove = false;
		}
		
		public listIterator(int index) {
			if(index < 0 || index > size) {
				throw new IndexOutOfBoundsException();
			}
			if(size == 0) {
				newNode = null;
			} else {
				newNode = head;
				for(int i = 0; i < index; i++) {
					newNode = newNode.getNext();
				}
			}
			canRemove = false;
		}
		/**
		 * Determines if the iterator has more elements
		 */
		@Override
		public boolean hasNext() {
			return index < size;
		}
		/**
		 * Returns the next element in the iterator
		 */
		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T object = newNode.getElement();
			last = newNode;
			if(newNode.getNext() != null) {
				newNode = newNode.getNext();
			}
			canRemove = true;
			index++;
			return object;
		}
		/**
		 * Determines if the iterator has previous elements to run through
		 */
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		/**
		 * Returns the previous element in the iterator
		 */
		@Override
		public T previous() {
			if(!hasPrevious()) {
				throw new NoSuchElementException();
			}
			last = newNode;
			if(size == 1 && newNode == null) {
				newNode = head;
			}
			if(!hasNext() && canAdd == true) {
				newNode = add;
			}
			index--;
			return newNode.getElement();
		}
		/**
		 * Returns the index of the next element in the iterator
		 */
		@Override
		public int nextIndex() {
			return index;
		}
		/**
		 * Returns the index of the previous element in the iterator
		 */
		@Override
		public int previousIndex() {
			return index - 1;
		}
		/**
		 * Removes the element at the iterator
		 */
		@Override
		public void remove() {
			Node<T> previousNode = null;
			Node<T> nextNode = null;
			if(canRemove == false) {
				throw new IllegalStateException();
			}
			if(size == 1) {
				newNode = null;
				head = tail = null;
			} else if(index == 0) {
				nextNode = newNode.getNext();
				previousNode = newNode.getNext();
			} else {
				if(last.getNext() != null) {
					nextNode = last.getNext();
				}
				if(size == 3 && newNode != head && newNode != tail) {
					head.setNext(tail);
					previousNode = tail;
				}
				if(size == 3) {
					currentNode.setNext(last);
				}
			}
			newNode = previousNode;
			canRemove = false;
			size--;
		}
		/**
		 * Sets the passed through element at the current state of the iterator
		 */
		@Override
		public void set(T element) {
			if(canRemove = false) {
				throw new IllegalStateException();
			}
			newNode.setElement(element);
		}
		/**
		 * Inserts the element at the current state of the iterator
		 */
		@Override
		public void add(T element) {
			canAdd = true;
			Node<T> addNode = new Node<T>(element);
			if(size == 0) {
				head = tail = addNode;
				newNode = addNode;
			} else if(size == 1 && index == 0) {
				head = addNode;
				tail = addNode;
				addNode.setNext(newNode);
				newNode = addNode.getNext();
			} else if(size == 1 && index == 0) {
				head = newNode;
				tail = newNode;
				newNode.setNext(addNode);
			} else {
				Node<T> currentNode = newNode;
				addNode.setNext(currentNode);
				newNode = addNode.getNext();
			}
			size++;
			index++;
			canRemove = false;
		}
		
	}
}
