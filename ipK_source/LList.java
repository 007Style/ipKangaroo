/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

/** A Linked List Implementation (not thread-safe for simplicity) */
package collections.impl;

import collections.List;
import collections.Stack;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import collections.impl.LLCell;

/** A Simple Linked List Impl. (adds to the tail) (has an enumeration)
 * @author Terence Parr
 * <a href=http://www.MageLang.com>MageLang Institute</a>
 */
public class LList implements List, Stack {
    protected LLCell head=null, tail=null;
    protected int length=0;
    
    /** Add an object to the end of the list.
     * @param o the object to add
     */
    public void add(Object o) { append(o); }
    
    /** Append an object to the end of the list.
     * @param o the object to append
     */
    public void append(Object o) {
        LLCell n = new LLCell(o);
        if ( length==0 ) {
            head=tail=n;
            length=1;
        }
        else {
            tail.next = n;
            tail=n;
            length++;
        }
    }
    
    /** Answers whether or not an object is contained in the list
     * @param o the object to test for inclusion.
     * @return true if object is contained else false.
     */
    public boolean includes(Object o) {
        for (LLCell p = head; p!=null; p=p.next) {
            if ( p.data.equals(o) ) return true;
        }
        return false;
    }
    
    /**Get the ith element in the list.
     * @param i the index (from 0) of the requested element.
     * @return the object at index i
     * NoSuchElementException is thrown if i out of range
     */
    public Object elementAt(int i) throws NoSuchElementException {
        int j=0;
        for (LLCell p = head; p!=null; p=p.next) {
            if ( i==j ) return p.data;
            j++;
        }
        throw new NoSuchElementException();
    }
    
    /**Return the length of the list.*/
    public int length() { return length; }
    
    /**Return an enumeration of the list elements */
    public Enumeration elements() { return new LLEnumeration(this); }
    
    // The next two methods make LLQueues and LLStacks easier.
    
    /** Insert an object at the head of the list.
     * @param o the object to add
     */
    protected void insertHead(Object o) {
        LLCell c = head;
        head = new LLCell(o);
        head.next = c;
        length++;
    }
   
    /**Delete the object at the head of the list.
     * @return the object found at the head of the list.
     * @exception NoSuchElementException if the list is empty.
     */
    protected Object deleteHead() throws NoSuchElementException {
        if ( head==null ) throw new NoSuchElementException();
        Object o = head.data;
        head = head.next;
        length--;
        return o;
    }
    
    // Satisfy the Stack interface now.
    
    /** Push an object onto the stack.
     * @param o the object to push
     */
    public void push(Object o) { insertHead(o); }
    
    /** Pop the top element of the stack off.
     * @return the top of stack that was popped off.
     * @exception NoSuchElementException if the stack is empty.
     */
    public Object pop() throws NoSuchElementException {
        Object o = deleteHead();
        return o;
    }
    
    /** How high is the stack? */
    public int height() { return length; }
}

/**An enumeration of a LList.  Maintains a cursor through the list.
 * bad things would happen if the list changed via another thread
 * while we were walking this list.
 */
final class LLEnumeration implements Enumeration {
    LLCell cursor;
    LList list;
    
    /**Create an enumeration attached to a LList*/
    public LLEnumeration(LList l) {list = l; cursor=list.head;}
    
    /** Return true/false depending on whether there are more
     * elements to enumerate.
     */
    public boolean hasMoreElements() {
        if ( cursor!=null ) return true;
        else return false;
    }
    
    /**Get the next element in the enumeration.  Destructive in that
     * the returned element is removed from the enumeration.  This
     * does not affect the list itself.
     * @return the next object in the enumeration.
     */
    public Object nextElement() {
        if ( !hasMoreElements() ) throw new NoSuchElementException();
        LLCell p = cursor;
        cursor = cursor.next;
        return p.data;
    }
}
