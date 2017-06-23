/*
 * Copyright 2017 Schibsted ASA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.distributed.aws;

import com.netflix.spinnaker.halyard.config.model.v1.node.DeploymentConfiguration;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.ClouddriverBootstrapService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.ServiceSettings;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.SpinnakerMonitoringDaemonService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class AwsClouddriverBootstrapService extends ClouddriverBootstrapService implements AwsDistributedService<ClouddriverBootstrapService.Clouddriver> {
  @Override
  public Settings buildServiceSettings(DeploymentConfiguration deploymentConfiguration) {
    Settings settings = new Settings(Collections.singletonList("bootstrap"));
    settings.setArtifactId(getArtifactId(deploymentConfiguration.getName()))
      .setAddress(
    return null;
  }

  @Override
  public AwsVaultServerService getVaultServerService() {
    return null;
  }

  @Override
  public SpinnakerMonitoringDaemonService getMonitoringDaemonService() {
    return null;
  }

  final DeployPriority deployPriority = new DeployPriority(6);
  final boolean requiredToBootstrap = true;

}
