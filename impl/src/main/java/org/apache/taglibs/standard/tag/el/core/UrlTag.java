/*
* Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Distribution License v. 1.0, which is available at
* http://www.eclipse.org/org/documents/edl-v10.php.
*
* SPDX-License-Identifier: BSD-3-Clause
*/

package org.apache.taglibs.standard.tag.el.core;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.UrlSupport;

/**
 * <p>A handler for &lt;urlEncode&gt; that accepts attributes as Strings
 * and evaluates them as expressions at runtime.</p>
 *
 * @author Shawn Bayern
 */

public class UrlTag extends UrlSupport {

    //*********************************************************************
    // 'Private' state (implementation details)

    private String value_;			// stores EL-based property
    private String context_;			// stores EL-based property


    //*********************************************************************
    // Constructor

    /**
     * Constructs a new URLEncodeTag.  As with TagSupport, subclasses
     * should not provide other constructors and are expected to call
     * the superclass constructor
     */
    public UrlTag() {
        super();
        init();
    }


    //*********************************************************************
    // Tag logic

    // evaluates expression and chains to parent
    public int doStartTag() throws JspException {

        // evaluate any expressions we were passed, once per invocation
        evaluateExpressions();

	// chain to the parent implementation
	return super.doStartTag();
    }


    // Releases any resources we may have (or inherit)
    public void release() {
        super.release();
        init();
    }


    //*********************************************************************
    // Accessor methods

    public void setValue(String value_) {
        this.value_ = value_;
    }

    public void setContext(String context_) {
        this.context_ = context_;
    }


    //*********************************************************************
    // Private (utility) methods

    // (re)initializes state (during release() or construction)
    private void init() {
        // null implies "no expression"
	value_ = null;
    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        /* 
         * Note: we don't check for type mismatches here; we assume
         * the expression evaluator will return the expected type
         * (by virtue of knowledge we give it about what that type is).
         * A ClassCastException here is truly unexpected, so we let it
         * propagate up.
         */

	value = (String) ExpressionUtil.evalNotNull(
	    "url", "value", value_, String.class, this, pageContext);
	context = (String) ExpressionUtil.evalNotNull(
	    "url", "context", context_, String.class, this, pageContext);
    }
}
