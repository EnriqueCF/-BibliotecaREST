package cloud.apps.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cloud.apps.model.User;
import cloud.apps.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User save(User user) {
		return this.userRepository.save(user);
	}

	public Optional<User> findUserByNickName(String nickname) {
		return this.userRepository.findByNick(nickname);
	}

	public void delete(User user) {
		this.userRepository.delete(user);
	}

	public Optional<User> findById(Integer id) {
		return this.userRepository.findById(id);
	}

	public List<User> findAll() {
		return this.userRepository.findAll();
	}

	public Optional<User> delete(Integer id) {
		Optional<User> user = this.findById(id);
		if (user.isPresent()) {
			if (user.get().getComments().isEmpty()) {
				this.userRepository.delete(user.get());
				return user;
			}
		}
		return Optional.empty();
	}

	public Optional<User> updateEmail(Integer id, String email) {
		Optional<User> user = this.findById(id);
		if (user.isPresent()) {
			User userUpdate = user.get();
			userUpdate.setEmail(email);
			this.userRepository.save(userUpdate);
			return Optional.of(userUpdate);
		}
		return Optional.empty();
	}
	
	
}
