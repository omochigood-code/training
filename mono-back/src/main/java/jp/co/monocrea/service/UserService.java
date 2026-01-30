package jp.co.monocrea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jp.co.monocrea.entity.User;
import jp.co.monocrea.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    /**
     * ユーザー一覧取得
     */
    public List<User> findAll() {
        return userRepository.listAll();
    }

    /**
     * ID 指定でユーザー取得
     */
    public Optional<User> findById(Long id) {
        return userRepository.findByIdOptional(id);
    }

    /**
     * ユーザー検索
     */
    public List<User> search(Long id, String name) {
        return userRepository.search(id, name);
    }

    /**
     * ユーザー新規作成
     */
    @Transactional
    public User create(String name) {
        User user = new User();
        user.setName(name);
        user.persist();
        return user;
    }

    /**
     * ユーザー更新
     */
    @Transactional
    public Optional<User> update(Long id, String name) {
        Optional<User> optionalUser = userRepository.findByIdOptional(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        user.setName(name);
        return Optional.of(user);
    }

    /**
     * ユーザー削除
     */
    @Transactional
    public boolean delete(Long id) {
        return userRepository.deleteById(id);
    }
}
