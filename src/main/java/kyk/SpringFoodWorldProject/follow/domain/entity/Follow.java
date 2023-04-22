package kyk.SpringFoodWorldProject.follow.domain.entity;

import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
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
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fromMember_Id")
	private Member fromMember;

	@ManyToOne
	@JoinColumn(name = "toMember_Id")
	private Member toMember;

	public Follow(Member fromMember, Member toMember) {
		this.fromMember = fromMember;
		this.toMember = toMember;
	}
}



