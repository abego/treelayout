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
package org.abego.treelayout;

import java.util.Random;

import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.FixedNodeExtentProvider;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class TreeLayoutPerformanceReport {

	private static StringTreeNode createRandomTree(int nodeCount,
			int maxChildrenCount, Random random) {
		StringTreeNode result = new StringTreeNode("");

		int remainingNodesCount = nodeCount - 1;
		if (remainingNodesCount <= 0) {
			return result;
		}

		// how many children should we create (at most)?
		int childrenCount = random.nextInt(maxChildrenCount) + 1;

		int i = 0;
		while (remainingNodesCount > 0) {
			int n = random.nextInt(remainingNodesCount) + 1;
			result.addChild(createRandomTree(n, maxChildrenCount, random));
			remainingNodesCount -= n;
			i++;
			if (i >= (childrenCount - 1) && remainingNodesCount > 0) {
				result.addChild(createRandomTree(remainingNodesCount,
						maxChildrenCount, random));
				remainingNodesCount = 0;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param n
	 * @return the number of milliseconds to create the tree layout (Time for
	 *         creating the tree not included)
	 */
	private static long layoutRandomTree(int n, int maxChildrenCount,
			Random random) {
		StringTreeNode root = createRandomTree(n, maxChildrenCount, random);

		StringTreeAsTreeForTreeLayout treeForTreeLayout = new StringTreeAsTreeForTreeLayout(
				root);

		long start = System.currentTimeMillis();
		new TreeLayout<StringTreeNode>(treeForTreeLayout,
				new FixedNodeExtentProvider<StringTreeNode>(60, 20),
				new DefaultConfiguration<StringTreeNode>(10, 10));
		long end = System.currentTimeMillis();
		long duration = end - start;

		return duration;
	}

	private static void writePerformanceReport(boolean randomOrder) {
		final long randomSeed = 4711;
		final int maxNodeCount = 100000;
		final int testCount = 1000;
		final int maxChildrenCount = 10;

		final Random random = new Random();
		random.setSeed(randomSeed);

		for (int i = 0; i < testCount; i++) {
			int n = randomOrder ? random.nextInt(maxNodeCount) + 1
					: (int) (((long) maxNodeCount) * (i + 1) / testCount);
			long duration = layoutRandomTree(n, maxChildrenCount, random);
			System.out.println(String.format("%d\t%d\t%d", i + 1, n, duration));
		}
	}

	// Runs the performance report
	public static void main(String[] args) {
		writePerformanceReport(true);
	}

}
