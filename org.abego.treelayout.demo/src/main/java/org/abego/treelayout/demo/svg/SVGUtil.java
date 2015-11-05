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
package org.abego.treelayout.demo.svg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A set of methods to generate SVG content.
 * <p>
 * Mainly to keep the footprint small only a very limited set of SVG
 * functionality is supported. In case more features are required have a look at
 * <a href="http://xmlgraphics.apache.org/batik/">Batik - Java SVG Toolkit</a>.
 * <p>
 * <b>Example</b>
 * 
 * <pre>
 * String s = doc(svg(
 *         160,
 *         200,
 *         rect(0, 0, 160, 200, &quot;fill:red;&quot;)
 *         + svg(10, 10, 100, 100,
 *                 rect(0, 0, 100, 100, &quot;fill:orange; stroke:rgb(0,0,0);&quot;))
 *         + line(20, 20, 100, 100, &quot;stroke:black; stroke-width:2px;&quot;)
 *         + line(20, 100, 100, 20, &quot;stroke:black; stroke-width:2px;&quot;)
 *         + text(10, 140,
 *                 &quot;font-family:verdana; font-size:20px; font-weight:bold;&quot;,
 *                 &quot;Hello world&quot;)));
 * 
 * File file = new File(&quot;demo.svg&quot;);
 * FileWriter w = null;
 * try {
 *     w = new FileWriter(file);
 *     w.write(s);
 * } finally {
 *     if (w != null) {
 *         w.close();
 *     }
 * }
 * </pre>
 * 
 * (see {@link #main(String[])})
 * <p>
 * 
 * @author Udo Borkowski (ub@abego.org)
 */
public class SVGUtil {

	// ------------------------------------------------------------------------
	// svg

	public static String svg(String width, String height, String content) {
		return String
				.format("<svg width=\"%s\" height=\"%s\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
						+ "%s" + "</svg>\n", width, height, content);
	}

	public static String svg(Number width, Number height, String content) {
		return svg(Integer.toString(width.intValue()),
				Integer.toString(height.intValue()), content);
	}

	public static String svg(String x, String y, String width, String height,
			String content) {
		return String
				.format("<svg x=\"%s\" y=\"%s\" width=\"%s\" height=\"%s\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
						+ "%s" + "</svg>\n", x, y, width, height, content);
	}

	public static String svg(Number x, Number y, Number width, Number height,
			String content) {
		return svg(Integer.toString(x.intValue()),
				Integer.toString(y.intValue()),
				Integer.toString(width.intValue()),
				Integer.toString(height.intValue()), content);
	}

	// ------------------------------------------------------------------------
	// rect

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param style
	 * @param extraAttributes
	 *            [default:""]
	 */
	public static String rect(String x, String y, String width, String height,
			String style, String extraAttributes) {
		return String
				.format("<rect x=\"%s\" y=\"%s\" width=\"%s\" height=\"%s\" style=\"%s\" %s/>\n",
						x, y, width, height, style, extraAttributes);
	}

	public static String rect(String x, String y, String width, String height,
			String style) {
		return rect(x, y, width, height, style, "");
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param style
	 * @param extraAttributes
	 *            [default:""]
	 */
	public static String rect(Number x, Number y, Number width, Number height,
			String style, String extraAttributes) {
		return rect(Integer.toString(x.intValue()),
				Integer.toString(y.intValue()),
				Integer.toString(width.intValue()),
				Integer.toString(height.intValue()), style, extraAttributes);
	}

	public static String rect(Number x, Number y, Number width, Number height,
			String style) {
		return rect(x, y, width, height, style, "");
	}

	// ------------------------------------------------------------------------
	// line

	public static String line(String x1, String y1, String x2, String y2,
			String style) {
		return String
				.format("<line x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\" style=\"%s\" />\n",
						x1, y1, x2, y2, style);
	}

	public static String line(Number x1, Number y1, Number x2, Number y2,
			String style) {
		return line(Integer.toString(x1.intValue()),
				Integer.toString(y1.intValue()),
				Integer.toString(x2.intValue()),
				Integer.toString(y2.intValue()), style);
	}

	// ------------------------------------------------------------------------
	// text

	public static String text(String x, String y, String style, String text) {
		return String.format(
				"<text x=\"%s\" y=\"%s\" style=\"%s\">\n%s\n</text>\n", x, y,
				style, text);
	}

	public static String text(Number x, Number y, String style, String text) {
		return text(Integer.toString(x.intValue()),
				Integer.toString(y.intValue()), style, text);
	}

	// ------------------------------------------------------------------------
	// doc

	public static String doc(String content) {
		return String
				.format("<?xml version=\"1.0\" standalone=\"no\" ?>\n"
						+ "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n"
						+ "%s\n", content);
	}

	// ------------------------------------------------------------------------
	// main

	private static boolean viewSVG(File file) throws IOException {
		if ("Mac OS X".equals(System.getProperty("os.name"))) {
			Runtime.getRuntime().exec(
					String.format("open -a /Applications/Safari.app %s",
							file.getAbsoluteFile()));
			return true;
		}
		return false;
	}

	/**
	 * Creates a sample SVG file "demo.svg"
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String s = doc(svg(
				160,
				200,
				rect(0, 0, 160, 200, "fill:red;")
						+ svg(10,
								10,
								100,
								100,
								rect(0, 0, 100, 100,
										"fill:orange; stroke:rgb(0,0,0);"))
						+ line(20, 20, 100, 100,
								"stroke:black; stroke-width:2px;")
						+ line(20, 100, 100, 20,
								"stroke:black; stroke-width:2px;")
						+ text(10,
								140,
								"font-family:verdana; font-size:20px; font-weight:bold;",
								"Hello world")));

		File file = new File("demo.svg");
		FileWriter w = null;
		try {
		    w = new FileWriter(file);
	        w.write(s);
		} finally {
		    if (w != null) {
		        w.close();
		    }
		}
		System.out.println(String.format("File written: %s",
				file.getAbsolutePath()));

		// optionally view the just created file
		if (args.length > 0 && args[0].equals("-view")) {
			if (!viewSVG(file)) {
				System.err.println("'-view' not supported on this platform");
			}
		}
	}

}