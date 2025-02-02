/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

package de.codecentric.spring.boot.chaos.monkey.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Benjamin Wilms
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "chaos.monkey.watcher")
@EqualsAndHashCode
public class WatcherProperties {

    @Value("${controller:false}")
    private boolean controller;

    @Value("${restController:false}")
    private boolean restController;

    @Value("${service:true}")
    private boolean service;

    @Value("${repository:false}")
    private boolean repository;

    @Value("${component:false}")
    private boolean component;

    @Value("${restOperations:false}")
    private boolean restOperations;
}
