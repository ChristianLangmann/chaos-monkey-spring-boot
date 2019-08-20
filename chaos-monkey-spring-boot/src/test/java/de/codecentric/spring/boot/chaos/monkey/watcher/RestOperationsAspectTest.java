package de.codecentric.spring.boot.chaos.monkey.watcher;

import de.codecentric.spring.boot.chaos.monkey.component.ChaosMonkeyRequestScope;
import de.codecentric.spring.boot.chaos.monkey.component.MetricEventPublisher;
import de.codecentric.spring.boot.chaos.monkey.component.MetricType;
import de.codecentric.spring.boot.chaos.monkey.configuration.WatcherProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.web.client.RestOperations;

import static org.mockito.Mockito.*;

/**
 * @author Christian Langmann
 */
@RunWith(MockitoJUnitRunner.class)
public class RestOperationsAspectTest {

    @Mock
    private RestOperations restOperations; //  = new RestTemplate();

    private WatcherProperties watcherProperties = new WatcherProperties();

    @Mock
    private ChaosMonkeyRequestScope chaosMonkeyRequestScopeMock;

    @Mock
    private MetricEventPublisher metricsMock;

    private AspectJProxyFactory factory;
    private String pointcutName = "execution.RestOperations.getForObject...";
    private String simpleName = "org.springframework.web.client.RestOperations.getForObject";

    @Test
    public void chaosMonkeyIsCalledWhenEnabledInConfig() {
        watcherProperties.setRestController(true);
        addRelevantAspect();

        callTargetMethod();

        verifyDependenciesCalledXTimes(1);
    }

    @Test
    public void chaosMonkeyIsNotCalledByAspectsWithUnrelatedPointcuts() {
        addNonRelevantAspects();

        callTargetMethod();

        verifyDependenciesCalledXTimes(0);
    }

    private void addNonRelevantAspects() {
        final RestTemplateAspect restTemplateAspect = new RestTemplateAspect(chaosMonkeyRequestScopeMock, metricsMock, watcherProperties);
        factory = new AspectJProxyFactory(restOperations);
        factory.setProxyTargetClass(true);
    }

    private void callTargetMethod() {
        RestOperations proxyRestOperations = factory.getProxy();
        final String result = proxyRestOperations.getForObject("http://localhost/", String.class);
    }

    private void addRelevantAspect() {
        final RestTemplateAspect restTemplateAspect = new RestTemplateAspect(chaosMonkeyRequestScopeMock, metricsMock, watcherProperties);
        factory = new AspectJProxyFactory(restOperations);
        factory.setProxyTargetClass(true);
        factory.addAspect(restTemplateAspect);
    }

    private void verifyDependenciesCalledXTimes(int i) {
        verify(chaosMonkeyRequestScopeMock, times(i)).callChaosMonkey(simpleName);
        verify(metricsMock, times(i)).publishMetricEvent(pointcutName, MetricType.RESTCONTROLLER);
        verifyNoMoreInteractions(chaosMonkeyRequestScopeMock, metricsMock);
    }

}
