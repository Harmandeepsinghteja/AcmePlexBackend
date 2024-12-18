package __project.server.repositories;

import __project.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByEmail(String email);

    public boolean existsByEmailAndPassword(String email, String password);

    public User findByEmailAndPassword(String email, String password);

    @Query(value = """
            SELECT *
            FROM users
            WHERE membershipExpiryDate IS NOT NULL AND membershipExpiryDate >= NOW();
            """
            , nativeQuery = true)
    public List<User> findPremiumUsers();

    @Query(value = """
            SELECT *
            FROM users
            WHERE membershipExpiryDate IS NULL OR membershipExpiryDate < NOW();
            """
            , nativeQuery = true)
    public List<User> findNonPremiumUsers();
}
