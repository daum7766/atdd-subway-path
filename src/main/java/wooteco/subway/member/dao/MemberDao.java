package wooteco.subway.member.dao;

import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.domain.Member;

@Repository
public class MemberDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<Member> rowMapper = (rs, rowNum) ->
        new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("age")
        );


    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        jdbcTemplate
            .update(sql, member.getEmail(), member.getPassword(), member.getAge(), member.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Member findById(Long id) {
        String sql = "select * from MEMBER where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Member findByTokenRequest(TokenRequest tokenRequest) {
        String sql = "select * from MEMBER where email = ? and password = ?";
        return jdbcTemplate
            .queryForObject(sql, rowMapper, tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    public Member findByEmail(String email) {
        String sql = "select * from MEMBER where email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public boolean existMemberOtherThanMeByEmail(Member member) {
        String sql = "select exists (select * from MEMBER where email = ? and id <> ?)";

        Boolean isExistEmail =
            jdbcTemplate.queryForObject(sql, boolean.class, member.getEmail(), member.getId());
        return Objects.requireNonNull(isExistEmail);
    }

    public boolean existMemberByEmail(Member member) {
        String sql = "select exists (select * from MEMBER where email = ?)";

        Boolean isExistEmail =
            jdbcTemplate.queryForObject(sql, boolean.class, member.getEmail());
        return Objects.requireNonNull(isExistEmail);
    }
}
