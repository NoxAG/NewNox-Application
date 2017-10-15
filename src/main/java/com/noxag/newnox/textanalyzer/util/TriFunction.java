package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;

/**
 * This interface is used to represent a Function with three input parameters
 * and one result
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 * @param <A>
 *            the first parameter type of the function
 * @param <B>
 *            the second parameter type of the function
 * @param <C>
 *            the third parameter type of the function
 * @param <D>
 *            the result type of the function
 */
@FunctionalInterface
public interface TriFunction<A, B, C, D> {
    public D apply(A one, B two, C three) throws IOException;
}
