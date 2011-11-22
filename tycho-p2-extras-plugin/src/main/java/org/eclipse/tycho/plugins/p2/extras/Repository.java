/*******************************************************************************
 * Copyright (c) 2011 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.tycho.plugins.p2.extras;

import java.net.URI;

public final class Repository {

    public enum Layout {
        BOTH("p2"), METADATA("p2-metadata"), ARTIFACTS("p2-artifacts");

        private String literal;

        private Layout(String literal) {
            this.literal = literal;
        }

        boolean matches(String value) {
            return literal.equals(value);
        }

        public boolean hasMetadata() {
            return this != ARTIFACTS;
        }

        public boolean hasArtifacts() {
            return this != METADATA;
        }

        @Override
        public String toString() {
            return literal;
        }
    }

    @SuppressWarnings("unused")
    private String id;

    private URI url;

    private Layout layout = Layout.BOTH;

    public Repository() {
    }

    Repository(URI location) {
        this.url = location;
    }

    /**
     * @return never <code>null</code>
     */
    public URI getLocation() {
        if (url == null)
            throw new IllegalStateException("Attribute 'url' is required for source repositories");
        return url;
    }

    /**
     * @return never <code>null</code>
     */
    public Layout getLayout() {
        return layout;
    }

    public void setLayout(String value) {
        for (Layout layout : Layout.values()) {
            if (layout.matches(value)) {
                this.layout = layout;
                return;
            }
        }
        throw new IllegalArgumentException("Unrecognized value for attribute 'layout': \"" + value
                + "\". Valid values are: " + listValues(Layout.values()));
    }

    private String listValues(Enum<?>... values) {
        StringBuilder result = new StringBuilder();
        String separator = ", ";
        for (Enum<?> value : values) {
            result.append(value.toString());
            result.append(separator);
        }
        return result.substring(0, result.length() - separator.length());
    }
}
