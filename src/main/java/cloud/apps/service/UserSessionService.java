package cloud.apps.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSessionService {

	private String user;
	private int numComments;

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public int getNumPosts() {
		return this.numComments;
	}

	public void incNumPosts() {
		this.numComments++;
	}
}

