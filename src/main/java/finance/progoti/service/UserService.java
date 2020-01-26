package finance.progoti.service;

import finance.progoti.user.model.Role;
import finance.progoti.user.model.User;

public interface UserService {
	
	public void saveUser(User user);
	public void saveHeadUser(User user,Role role);
	public void saveAdminUser(User user);
	public boolean isUserAlreadyPresent(User user);
}
