package kyk.SpringFoodWorldProject.follow.domain.entity;

import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name="follow_uk",
						columnNames = {"fromMember_Id", "toMember_Id"}
				)
		}
)
public class Follow extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "fromMember_Id")
	private Member fromMember;

	@ManyToOne
	@JoinColumn(name = "toMember_Id")
	private Member toMember;
}



