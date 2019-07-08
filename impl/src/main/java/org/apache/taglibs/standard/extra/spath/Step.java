/*
* Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Distribution License v. 1.0, which is available at
* http://www.eclipse.org/org/documents/edl-v10.php.
*
* SPDX-License-Identifier: BSD-3-Clause
*/

package org.apache.taglibs.standard.extra.spath;

import java.util.List;

/**
 * <p>Represents a 'step' in an SPath expression.</p>
 *
 * @author Shawn Bayern
 */
public class Step {

    private boolean depthUnlimited;
    private String name;
    private List predicates;

    // record a few things for for efficiency...
    private String uri, localPart;

    /**
     * Constructs a new Step object, given a name and a (possibly null)
     * list of predicates.  A boolean is also passed, indicating
     * whether this particular Step is relative to the 'descendent-or-self'
     * axis of the node courrently under consideration.  If true, it is;
     * if false, then this Step is rooted as a direct child of the node
     * under consideration.
     */
    public Step(boolean depthUnlimited, String name, List predicates) {
	if (name == null)
	    throw new IllegalArgumentException("non-null name required");
	this.depthUnlimited = depthUnlimited;
	this.name = name;
	this.predicates = predicates;
    }

    /**
     * Returns true if the given name matches the Step object's
     * name, taking into account the Step object's wildcards; returns
     * false otherwise.
     */
    public boolean isMatchingName(String uri, String localPart) {
	// check and normalize arguments
	if (localPart == null)
	    throw new IllegalArgumentException("need non-null localPart");
	if (uri != null && uri.equals(""))
	    uri = null;

	// split name into uri/localPart if we haven't done so already
	if (this.localPart == null && this.uri == null)
	    parseStepName();

	// generic wildcard
	if (this.uri == null && this.localPart.equals("*"))
	    return true;

	// match will null namespace
	if (uri == null && this.uri == null
		&& localPart.equals(this.localPart))
	    return true;

	if (uri != null && this.uri != null && uri.equals(this.uri)) {
	    // exact match
	    if (localPart.equals(this.localPart))
		return true;

	    // namespace-specific wildcard
	    if (this.localPart.equals("*"))
		return true;
	}

	// no match
	return false;
    }

    /** Returns true if the Step's depth is unlimited, false otherwise. */
    public boolean isDepthUnlimited() {
	return depthUnlimited;
    }

    /** Returns the Step's node name. */
    public String getName() {
	return name;
    }

    /** Returns a list of this Step object's predicates. */
    public List getPredicates() {
	return predicates;
    }

    /** Lazily computes some information about our name. */
    private void parseStepName() {
	String prefix;
	int colonIndex = name.indexOf(":");

	if (colonIndex == -1) {
	    // no colon, so localpart is simply name (even if it's "*")
	    prefix = null;
	    localPart = name;
	} else {
	    prefix = name.substring(0, colonIndex);
	    localPart = name.substring(colonIndex + 1);
	}

	uri = mapPrefix(prefix);
    }

    /** Returns a URI for the given prefix, given our mappings. */
    private String mapPrefix(String prefix) {
	// ability to specify a mapping is, as of yet, unimplemented
	if (prefix == null)
	    return null;
	else
	    throw new IllegalArgumentException(
		"unknown prefix '" + prefix + "'");
    }
}
