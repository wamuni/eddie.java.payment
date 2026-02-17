package eddie.payment.orders.auth;

import eddie.payment.authsdk.CurrentUser;
import eddie.payment.authsdk.CurrentUserProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    private final CurrentUserProvider currentUserProvider;

    public MeController(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/me")
    public CurrentUser me() {
        return currentUserProvider.currentUser();
    }
}
