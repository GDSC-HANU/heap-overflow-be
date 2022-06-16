package hanu.gdsc.coderAuth_coderAuth.repositories.user;

import hanu.gdsc.coderAuth_coderAuth.domains.Email;
import hanu.gdsc.coderAuth_coderAuth.domains.User;
import hanu.gdsc.coderAuth_coderAuth.domains.Username;
import hanu.gdsc.share.domains.Id;

public interface UserRepository {
    public User getByUsername(Username username);
    public User getByEmail(Email email);
    public void save(User user);
    public User getByCoderId(Id coderId);
}
