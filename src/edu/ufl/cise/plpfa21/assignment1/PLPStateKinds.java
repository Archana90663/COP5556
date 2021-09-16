package edu.ufl.cise.plpfa21.assignment1;


public interface PLPStateKinds {

	static enum State{
		start,
		integer_literal,
		not,
		less_than,
		greater_than,
		equal_sign,
		or,
		identity,
		minus,
		div,
		comment,
		close_comment
	}

}
