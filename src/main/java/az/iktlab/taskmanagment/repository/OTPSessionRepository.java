package az.iktlab.taskmanagment.repository;

import az.iktlab.taskmanagment.entity.OTPSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface OTPSessionRepository extends JpaRepository<OTPSession,String> {
    Optional<OTPSession> findByOtpCodeAndUserIdAndIsUsedFalse(String otp,String userId);
}
