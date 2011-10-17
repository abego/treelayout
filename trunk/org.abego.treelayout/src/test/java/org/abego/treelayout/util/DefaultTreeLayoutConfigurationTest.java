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

import org.abego.treelayout.Configuration.AlignmentInLevel;
import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.util.DefaultConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class DefaultTreeLayoutConfigurationTest {
	@Test
	public void testConstructor() throws Exception {
		DefaultConfiguration config = new DefaultConfiguration(
				1, 2, Location.Right, AlignmentInLevel.AwayFromRoot);
		assertEquals(1.0, config.getGapBetweenLevels(1), 0.0);
		assertEquals(2.0, config.getGapBetweenNodes(null, null), 0.0);
		assertEquals(Location.Right, config.getRootLocation());
		assertEquals(AlignmentInLevel.AwayFromRoot,
				config.getAlignmentInLevel());
	}

	@Test
	public void testConstructor_defaults() throws Exception {
		DefaultConfiguration config = new DefaultConfiguration(
				1, 2, Location.Right);
		assertEquals(1.0, config.getGapBetweenLevels(1), 0.0);
		assertEquals(2.0, config.getGapBetweenNodes(null, null), 0.0);
		assertEquals(Location.Right, config.getRootLocation());
		assertEquals(AlignmentInLevel.Center, config.getAlignmentInLevel());
	}

	@Test
	public void testConstructor_defaults2() throws Exception {
		DefaultConfiguration config = new DefaultConfiguration(
				1, 2);
		assertEquals(1.0, config.getGapBetweenLevels(1), 0.0);
		assertEquals(2.0, config.getGapBetweenNodes(null, null), 0.0);
		assertEquals(Location.Top, config.getRootLocation());
		assertEquals(AlignmentInLevel.Center, config.getAlignmentInLevel());
	}

	@Test
	public void testConstructor_badGapBetweenLevels() throws Exception {
		try {
			new DefaultConfiguration(-1, 2);
			fail("exception expected");
		} catch (Exception ex) {
			assertEquals("gapBetweenLevels must be >= 0", ex.getMessage());
		}
	}

	@Test
	public void testConstructor_badGapBetweenNodes() throws Exception {
		try {
			new DefaultConfiguration(0, -1);
			fail("exception expected");
		} catch (Exception ex) {
			assertEquals("gapBetweenNodes must be >= 0", ex.getMessage());
		}
	}
}
