/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.abego.treelayout.internal.util.java.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class IterableUtilTest {
	@Test
	public void testHasNext_Next() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.add("C");

		Iterable<String> iterable = IterableUtil.createReverseIterable(list);
		Iterator<String> iter = iterable.iterator();
		assertTrue(iter.hasNext());
		assertEquals("C", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("B", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("A", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testRemove() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.add("C");

		Iterable<String> iterable = IterableUtil.createReverseIterable(list);
		Iterator<String> iter = iterable.iterator();

		assertTrue(iter.hasNext());
		assertEquals("C", iter.next());
		iter.remove();
		assertEquals(2, list.size());

		assertTrue(iter.hasNext());
		assertEquals("B", iter.next());
		iter.remove();
		assertEquals(1, list.size());

		assertTrue(iter.hasNext());
		assertEquals("A", iter.next());
		iter.remove();
		assertEquals(0, list.size());

		assertFalse(iter.hasNext());
	}

	@Test
	public void testEmptyIterator() throws Exception {
		List<String> list = new ArrayList<String>();

		Iterable<String> iterable = IterableUtil.createReverseIterable(list);
		Iterator<String> iter = iterable.iterator();
		assertFalse(iter.hasNext());
	}

}
