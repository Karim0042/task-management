package az.iktlab.taskmanagment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(Object event) {
        eventPublisher.publishEvent(event);
    }
}
