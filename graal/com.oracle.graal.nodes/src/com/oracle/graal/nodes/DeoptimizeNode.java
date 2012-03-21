/*
 * Copyright (c) 2009, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.graal.nodes;

import com.oracle.graal.graph.*;
import com.oracle.graal.nodes.spi.*;
import com.oracle.graal.nodes.type.*;

@NodeInfo(shortName = "Deopt")
public class DeoptimizeNode extends FixedNode implements Node.IterableNodeType, LIRLowerable {

    public static enum DeoptAction {
        None,                           // just interpret, do not invalidate nmethod
        Recompile,                      // recompile the nmethod; need not invalidate
        InvalidateReprofile,            // invalidate the nmethod, reset IC, maybe recompile
        InvalidateRecompile,            // invalidate the nmethod, recompile (probably)
        InvalidateStopCompiling,        // invalidate the nmethod and do not compile
    }

    @Data private String message;
    @Data private final DeoptAction action;
    private final long leafGraphId;

    public DeoptimizeNode() {
        this(DeoptAction.InvalidateReprofile, StructuredGraph.INVALID_GRAPH_ID);
    }

    public DeoptimizeNode(DeoptAction action, long leafGraphId) {
        super(StampFactory.illegal());
        this.action = action;
        this.leafGraphId = leafGraphId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public DeoptAction action() {
        return action;
    }

    public long leafGraphId() {
        return leafGraphId;
    }

    @Override
    public void generate(LIRGeneratorTool gen) {
        gen.emitDeoptimize(action, message, leafGraphId);
    }

    @NodeIntrinsic
    public static void deopt() {
        throw new UnsupportedOperationException();
    }
}
