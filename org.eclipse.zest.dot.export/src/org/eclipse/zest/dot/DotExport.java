/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.dot;

import java.io.File;
import java.util.Scanner;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.internal.DotFileUtils;
import org.eclipse.zest.dot.internal.DotTemplate;

/**
 * Transformation of Zest Graph instances to DOT strings or files, or via DOT to an image generated by calling
 * the 'dot' executable.
 * @author Fabian Steeg (fsteeg)
 */
public final class DotExport {
    private Graph graph;

    /**
     * @param graph The Zest graph to export to DOT
     */
    public DotExport(final Graph graph) {
        this.graph = graph;
    }

    /**
     * Export a Zest Graph to a DOT string.
     * @return The DOT representation of the given Zest graph
     */
    public String toDotString() {
        return graphToDot(graph);
    }

    /**
     * Export a Zest Graph to a DOT file.
     * @param destination The file to store the DOT export
     * @return The given file
     */
    public File toDotFile(final File destination) {
        DotFileUtils.write(graphToDot(graph), destination);
        return destination;
    }

    /**
     * @param dotDir The directory containing the 'dot' executable of the local Graphviz installation
     * @param format The image format to export the graph to (e.g. 'pdf' or 'png')
     * @return The image file exported via DOT for the given Zest graph, or null
     */
    public File toImage(final File dotDir, final String format) {
        File dotFile = DotFileUtils.write(toDotString());
        File image = DotDrawer.renderImage(dotDir, dotFile, format);
        return image;
    }

    /**
     * @return A name for the given graph, that can be used as a filename and as a valid name for a DOT graph
     */
    String name() {
        String simpleClassName = graph.getClass().getSimpleName();
        /* The exact name 'Graph' is not valid for rendering with Graphviz: */
        return simpleClassName.equals("Graph") ? "Zest" + simpleClassName : simpleClassName;
    }

    private static String graphToDot(final Graph graph) {
        String raw = new DotTemplate().generate(graph);
        raw = removeBlankLines(raw);
        return raw;
    }

    private static String removeBlankLines(final String raw) {
        Scanner scanner = new Scanner(raw);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.trim().equals("")) {
                builder.append(line + "\n");
            }
        }
        return builder.toString();
    }

}
