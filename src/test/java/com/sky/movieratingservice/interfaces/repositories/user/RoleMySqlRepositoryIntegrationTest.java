package com.sky.movieratingservice.interfaces.repositories.user;

import static org.junit.jupiter.api.Assertions.*;

import com.sky.movieratingservice.domain.Privilege;
import com.sky.movieratingservice.domain.Role;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoleMySqlRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoleMySqlRepository roleMySqlRepository;

    @BeforeEach
    void cleanup() {
        executeQuery("DELETE FROM users_roles;");
        executeQuery("DELETE FROM roles_privileges;");
        executeQuery("DELETE FROM roles;");
        executeQuery("DELETE FROM privileges;");
    }

    @Test
    @DisplayName("getByName should load role with its privileges")
    void getByName_loadsRoleWithPrivileges() {
        executeQuery("INSERT INTO privileges (name) VALUES ('RATE_MOVIE');");
        executeQuery("INSERT INTO privileges (name) VALUES ('DELETE_MOVIE_RATING');");
        executeQuery("INSERT INTO privileges (name) VALUES ('READ_ONLY');");

        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_USER');");

        Long roleId = getRowId("SELECT id AS id FROM roles WHERE name = 'ROLE_USER'");
        Long rateMovieId = getRowId("SELECT id AS id FROM privileges WHERE name = 'RATE_MOVIE'");
        Long deleteRatingId = getRowId("SELECT id AS id FROM privileges WHERE name = 'DELETE_MOVIE_RATING'");

        executeQuery("INSERT INTO roles_privileges (role_id, privilege_id) VALUES (%d, %d)".formatted(roleId, rateMovieId));
        executeQuery("INSERT INTO roles_privileges (role_id, privilege_id) VALUES (%d, %d)".formatted(roleId, deleteRatingId));

        Optional<Role> result = roleMySqlRepository.getByName("ROLE_USER");

        assertTrue(result.isPresent(), "Expected role to be present");
        Role role = result.get();
        assertEquals("ROLE_USER", role.name());

        assertNotNull(role.privileges(), "Privileges must not be null");
        assertEquals(2, role.privileges().size(), "Exactly two linked privileges expected");

        var names = role.privileges().stream().map(Privilege::name).collect(java.util.stream.Collectors.toSet());
        assertTrue(names.contains("RATE_MOVIE"));
        assertTrue(names.contains("DELETE_MOVIE_RATING"));
        assertFalse(names.contains("READ_ONLY"), "Unlinked privilege must not appear");
    }

    @Test
    @DisplayName("getByName should return empty when role does not exist")
    void getByName_whenNotExists_returnsEmpty() {
        Optional<Role> result = roleMySqlRepository.getByName("ROLE_ADMIN");

        assertTrue(result.isEmpty(), "Expected empty Optional for unknown role");
    }

    private Long getRowId(String sql) {
        var rows = fetchDbQueryResult(sql);
        assertEquals(1, rows.size(), "Expected exactly 1 row for: " + sql);
        Object id = rows.getFirst().get("id");
        assertNotNull(id, "id must not be null for: " + sql);
        return ((Number) id).longValue();
    }

}