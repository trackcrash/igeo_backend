 package igeo.site.Repository;

 import org.springframework.data.jpa.repository.JpaRepository;
 import igeo.site.Model.User;
 import org.springframework.stereotype.Repository;

 //db에 접근하는 인터페이스
 @Repository
 public interface UserRepository extends JpaRepository<User, Long> {
     User findByEmail(String email);
     User findByPassword(String password);
     User findByName(String name);

     boolean existsByEmail(String email);
 }
