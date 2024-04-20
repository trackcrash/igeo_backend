package igeo.site.Game;

import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LevelManager {
    private final UserRepository userRepository;
    //유저 존재여부 확인
    public User CheckUser(String userId){
        User user = userRepository.findByName(userId);
        if(user == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다." + userId);
        }
        return user;
    }
    //레벨업 시스템 다음 레벨에 필요한 경험치 수식 설정
    public int levelUpSystem(int level) {
        return (int)(30 + Math.pow(level, 1.77));
    }

    //레벨업
    @Transactional
    public void levelUp(String userId) {
        User user = CheckUser(userId);
        user.setLevel(user.getLevel() + 1);
        user.setNextExp(levelUpSystem(user.getLevel()));
        userRepository.save(user);
    }

    //exp 추가
    @Transactional
    public void addExp(String userId) {
        User user = CheckUser(userId);
        int exp = 1;
        user.setExp(user.getExp() + exp);
        if(user.getExp() >= user.getNextExp()) {
            user.setExp(user.getExp() - user.getNextExp());
            levelUp(userId);
        }
        userRepository.save(user);
    }

}
