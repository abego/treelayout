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
package org.abego.treelayout.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.abego.treelayout.StringTreeNode;
import org.abego.treelayout.util.FixedNodeExtentProvider;
import org.junit.Test;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class FixedNodeExtentProviderTest {
	@Test
	public void testConstructor() throws Exception {
		FixedNodeExtentProvider<StringTreeNode> provider = new FixedNodeExtentProvider<StringTreeNode>(
				1.0, 2.0);
		assertEquals(1.0, provider.getWidth(null), 0.0);
		assertEquals(2.0, provider.getHeight(null), 0.0);
	}

	@Test
	public void testConstructor_defaults() throws Exception {
		FixedNodeExtentProvider<StringTreeNode> provider = new FixedNodeExtentProvider<StringTreeNode>();
		assertEquals(0.0, provider.getWidth(null), 0.0);
		assertEquals(0.0, provider.getHeight(null), 0.0);
	}

	@Test
	public void testConstructor_invalidWidth() throws Exception {
		try {
			new FixedNodeExtentProvider<StringTreeNode>(-1.0, 2.0);
			fail("exception expected");
		} catch (Exception ex) {
			assertEquals("width must be >= 0", ex.getMessage());
		}
	}

	@Test
	public void testConstructor_invalidHeight() throws Exception {
		try {
			new FixedNodeExtentProvider<StringTreeNode>(1.0, -2.0);
			fail("exception expected");
		} catch (Exception ex) {
			assertEquals("height must be >= 0", ex.getMessage());
		}
	}
}
