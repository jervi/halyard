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

import com.netflix.spinnaker.halyard.config.model.v1.node.Provider;
import com.netflix.spinnaker.halyard.config.model.v1.providers.aws.AwsAccount;
import com.netflix.spinnaker.halyard.core.error.v1.HalException;
import com.netflix.spinnaker.halyard.core.problem.v1.Problem;
import com.netflix.spinnaker.halyard.deploy.deployment.v1.AccountDeploymentDetails;
import com.netflix.spinnaker.halyard.deploy.services.v1.GenerateService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.RunningServiceDetails;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.SpinnakerRuntimeSettings;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.profile.Profile;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.ConfigSource;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.ServiceSettings;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.SpinnakerService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.distributed.DistributedService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.distributed.SidecarService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface  AwsDistributedService<T> extends DistributedService<T, AwsAccount> {
  default String getEnvFile() {
    return "/etc/default/spinnaker";
  }

  @Override
  default Provider.ProviderType getProviderType() {
    return Provider.ProviderType.AWS;
  }

  @Override
  default List<ConfigSource> stageProfiles(AccountDeploymentDetails<AwsAccount> details, GenerateService.ResolvedConfiguration resolvedConfiguration) {
    SpinnakerService thisService = getService();
    ServiceSettings thisServiceSettings = resolvedConfiguration.getServiceSettings(thisService);
    SpinnakerRuntimeSettings runtimeSettings = resolvedConfiguration.getRuntimeSettings();
    Integer version = getRunningServiceDetails(details, runtimeSettings).getLatestEnabledVersion();
    if (version == null) {
      version = 0;
    } else {
      version++;
    }

    Map<String, String> env = new HashMap<>();
    List<ConfigSource> configSources = new ArrayList<>();

    Map<String, Profile> serviceProfiles = resolvedConfiguration.getProfilesForService(thisService.getType());

    for (SidecarService sidecarService : getSidecars(runtimeSettings)) {
      for (Profile profile : sidecarService.getSidecarProfiles(resolvedConfiguration, thisService)) {
        if (profile == null) {
          throw new HalException(Problem.Severity.FATAL, "Service " + sidecarService.getService().getCanonicalName() + " is required but was not supplied for deployment.");
        }

        serviceProfiles.put(profile.getName(), profile);
      }
    }

    return configSources;
  }

  @Override
  default void ensureRunning(AccountDeploymentDetails<AwsAccount> details, GenerateService.ResolvedConfiguration resolvedConfiguration, List<ConfigSource> configSources, boolean recreate) {

  }

  @Override
  default Map<String, Object> getLoadBalancerDescription(AccountDeploymentDetails<AwsAccount> details, SpinnakerRuntimeSettings runtimeSettings) {
    return null;
  }

  @Override
  default Map<String, Object> getServerGroupDescription(AccountDeploymentDetails<AwsAccount> details, SpinnakerRuntimeSettings runtimeSettings, List<ConfigSource> configSources) {
    return null;
  }

  @Override
  default List<String> getHealthProviders() {
    return null;
  }

  @Override
  default Map<String, List<String>> getAvailabilityZones(ServiceSettings settings) {
    return null;
  }

  @Override
  default RunningServiceDetails getRunningServiceDetails(AccountDeploymentDetails<AwsAccount> details, SpinnakerRuntimeSettings runtimeSettings) {
    return null;
  }

  @Override
  default <S> S connectToService(AccountDeploymentDetails<AwsAccount> details, SpinnakerRuntimeSettings runtimeSettings, SpinnakerService<S> sidecar) {
    return null;
  }

  @Override
  default String connectCommand(AccountDeploymentDetails<AwsAccount> details, SpinnakerRuntimeSettings runtimeSettings) {
    return null;
  }

  @Override
  default void deleteVersion(AccountDeploymentDetails<AwsAccount> details, ServiceSettings settings, Integer version) {

  }
}
