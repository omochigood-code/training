package jp.co.monocrea.repository;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jp.co.monocrea.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public List<User> search(Long id, String name) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (id != null) {
            query.append("id = :id");
            params.put("id", id);
        }

        if (name != null && !name.isBlank()) {
            if (!query.isEmpty()) {
                query.append(" and ");
            }
            query.append("name like :name");
            params.put("name", "%" + name + "%");
        }

        if (query.isEmpty()) {
            return listAll();
        }

        return find(query.toString(), params).list();
    }
}
