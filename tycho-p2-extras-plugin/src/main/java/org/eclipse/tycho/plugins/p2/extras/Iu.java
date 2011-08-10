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

import org.eclipse.tycho.p2.tools.mirroring.facade.IUDescription;

/**
 * Installable Unit adapter class for {@link MirrorMojo} configuration purposes only.
 */
public class Iu {

    String id;
    String version;

    public IUDescription toIUDescription() {
        return new IUDescription(id, version);
    }
}
