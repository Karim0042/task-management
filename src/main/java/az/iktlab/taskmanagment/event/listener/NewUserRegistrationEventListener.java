package az.iktlab.taskmanagment.event.listener;


import az.iktlab.taskmanagment.event.NewUserRegistrationEvent;
import az.iktlab.taskmanagment.service.CustomMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewUserRegistrationEventListener {
    private final CustomMailService mailService;

    @Async
    @EventListener
    public void onEvent(NewUserRegistrationEvent event) {
        mailService.sendMail(event.getEmail());
    }
}
