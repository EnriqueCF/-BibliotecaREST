package cloud.apps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cloud.apps.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByNick(String nick);
}
