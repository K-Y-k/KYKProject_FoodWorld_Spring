package kyk.SpringFoodWorldProject.follow.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class FollowJdbcTemplateRepository {

//    private final JdbcTemplate template;
//
//    public FollowJdbcTemplateRepository(DataSource dataSource) {
//        this.template = new JdbcTemplate(dataSource);
//    }
//
//
//    public List<Member> getRandomFollowersOfMember(Long currentMemberId) {
//        String sql = "SELECT m.* " +
//                "FROM Follow f " +
//                "JOIN Member m ON f.fromMember_id = m.member_id " +
//                "WHERE f.toMember_id = ? " +
//                "ORDER BY RAND() " +
//                "LIMIT 5";
//        return template.queryForObject(sql, memberRowMapper(), currentMemberId);
//    }
//
//    // 매핑 정보 메서드
//    private RowMapper<Member> memberRowMapper() {
//        // sql 결과인 rs를 던져서 결과 member를 반환한다.
//        return (rs, rowNum) -> { // rowNum은 몇번째 row 번호 들어오는지
//            Member member = new Member();
//            member.setId(rs.getLong("member_id"));
//            member.setName(rs.getString("name"));
//            member.setLoginId(rs.getString("loginId"));
//            member.setPassword(rs.getString("password"));
//            member.setIntroduce(rs.getString("introduce"));
//            member.setFollowingCount(rs.getInt("followCount"));
//            member.setFollowingCount(rs.getInt("followingCount"));
//
//            return member;
//        };
//    }


}
