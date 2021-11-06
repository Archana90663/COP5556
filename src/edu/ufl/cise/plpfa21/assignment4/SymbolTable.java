package edu.ufl.cise.plpfa21.assignment4;

import java.util.HashMap;
import java.util.LinkedList;

import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;

/**
 * This class implements a slightly modified LeBlanc-Cook symbol table.
 * 
 * @author B. Sanders
 *
 */
public class SymbolTable {

	static class SymbolTableEntry {

		int scope; // scope number 
		boolean mutable;  // name declared with VAL or FUN keywords are not mutable.  Other variables, including formal parameters of functions are mutable.
		IDeclaration dec;  // the declaration where this name was introduced
		SymbolTableEntry next;  // next entry in chain of entries with same name.

		public SymbolTableEntry(int scope, IDeclaration dec, SymbolTableEntry next) {
			super();
			this.scope = scope;
			this.dec = dec;
			this.next = next;
		}

		@Override
		public String toString() {
			return "SymbolTableEntry [scope=" + scope + ",  mutable=" + mutable + ", dec="
					+ dec + ", next=" + next + "]";
		}



	}

	HashMap<String, SymbolTableEntry> entries;  //map from names to SymbolTableEntry
	LinkedList<Integer> scopeStack;  //stack containing the scope numbers of currently visible scopes
	int currentScope;  

	/**
	 * To be called when new scope is entered.  In our language, this is when 
	 * in a function declaration or a let statement.  
	 */
	public void enterScope() {
		scopeStack.addFirst(++currentScope);
	}

	/**
	 * To be called when a scope is exited.  
	 */
	public void leaveScope() {
		scopeStack.removeFirst();
		currentScope = scopeStack.peek();
	}

	
	/** 
	 * Insert an entry into the symbol table if the name is not already declared in this scope.
	 * The return value indicates whether or not the name was successfuly inserted.  
	 * 
	 * @param name      Name of variable or function 
	 * @param mutable   Whether mutable, which depends on context
	 * @param dec       The IDeclaration where this name is declared. 
	 * @return          
	 */
	public boolean insert(String name, IDeclaration dec) {
		// check for existing entry with same name
		SymbolTableEntry entry = entries.get(name);
		
		// if an entry is found, check that none of the chained entries with this name
		// were declared in the current scope.
		while (entry != null) {			
			if (entry.scope == currentScope) {
				return false;
			}
			entry = entry.next;
		}
		
		// Create a new SymbolTableEntry object for the given name and declaration and insert into the HashMap
		// If there was already an entry for this name, the new SymbolTableEntry becomes the head 
		// of a chain of entries for this name.  
		entries.put(name, new SymbolTableEntry(currentScope,  dec, entries.get(name)));
		return true;
	}
	

	/**
	 * Return the declaration corresponding to the indicated name in the current scope.
	 * If there is no in-scope declaration for this name, return null;
	 * 
	 * @param name
	 * @return
	 */
	public IDeclaration lookupDec(String name) {
		SymbolTableEntry entry = lookupEntry(name);
		return entry != null ? entry.dec : null;			
	}
	

	/**
	 * Obtains the SymbolTableEntry for the given name in the current scope.
	 * 
	 * @param name
	 * @return
	 */
	public SymbolTableEntry lookupEntry(String name) {
		SymbolTableEntry entry = entries.get(name);
		if (entry == null)
			return null;
		for (int i = 0; i < scopeStack.size(); ++i) {
			int scope = scopeStack.get(i);
			SymbolTableEntry scanner = entry;
			while (scanner != null && scanner.scope != scope) {
				scanner = scanner.next;
			}
			if (scanner != null)
				return scanner;
		}
		return entry;
	}
	
	public SymbolTable() {
		currentScope = 0;
		entries = new HashMap<String, SymbolTableEntry>();
		scopeStack = new LinkedList<Integer>();
		scopeStack.addFirst(currentScope); 
	}

	@Override
	public String toString() {
		return "SymbolTable [entries=" + entries + ", scopeStack=" + scopeStack + ", currentScope=" + currentScope
				+ "]";
	}




}
