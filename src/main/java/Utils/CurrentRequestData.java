package Utils;

import Entities.User;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class CurrentRequestData {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
