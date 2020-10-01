package com.buzzardsview.angularspringgapi.data;

import com.buzzardsview.angularspringgapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> getByGoogleId(String id);

}