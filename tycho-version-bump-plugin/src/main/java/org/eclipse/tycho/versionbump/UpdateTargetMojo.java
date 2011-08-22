/*******************************************************************************
 * Copyright (c) 2010, 2011 Sonatype Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.tycho.versionbump;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tycho.p2.resolver.TargetDefinitionFile;
import org.eclipse.tycho.p2.resolver.TargetDefinitionFile.IULocation;
import org.eclipse.tycho.p2.resolver.TargetDefinitionFile.Unit;
import org.eclipse.tycho.p2.resolver.facade.P2ResolutionResult;
import org.eclipse.tycho.p2.target.facade.TargetDefinition;

/**
 * Quick&dirty way to update .target file to use latest versions of IUs available from specified
 * metadata repositories.
 * 
 * @goal update-target
 */
public class UpdateTargetMojo extends AbstractUpdateMojo {

    /**
     * @parameter expression="${target}"
     */
    private File targetFile;

    protected void doUpdate() throws IOException, URISyntaxException {

        TargetDefinitionFile target = TargetDefinitionFile.read(targetFile);

        for (TargetDefinition.Location location : target.getLocations()) {
            if (location instanceof IULocation) {
                IULocation locationImpl = (IULocation) location;

                for (TargetDefinition.Unit unit : locationImpl.getUnits()) {
                    Unit unitImpl = (Unit) unit;
                    unitImpl.setVersion("0.0.0");
                }
            }
        }
        resolutionContext.addTargetDefinition(target, getEnvironments());
        P2ResolutionResult result = p2.resolveMetadata(resolutionContext, getEnvironments().get(0));

        Map<String, String> ius = new HashMap<String, String>();
        for (P2ResolutionResult.Entry entry : result.getArtifacts()) {
            ius.put(entry.getId(), entry.getVersion());
        }

        for (TargetDefinition.Location location : target.getLocations()) {
            if (location instanceof IULocation) {
                IULocation locationImpl = (IULocation) location;

                for (TargetDefinition.Unit unit : locationImpl.getUnits()) {
                    Unit unitImpl = (Unit) unit;

                    String version = ius.get(unitImpl.getId());
                    if (version != null) {
                        unitImpl.setVersion(version);
                    } else {
                        getLog().error("Resolution result does not contain root installable unit " + unit.getId());
                    }
                }
            }
        }

        TargetDefinitionFile.write(target, targetFile);
    }

    @Override
    protected File getTargetFile() {
        return targetFile;
    }

}
